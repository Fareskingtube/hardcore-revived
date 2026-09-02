package net.fareskingtube.block.entity.custom;

import com.mojang.authlib.GameProfile;
import net.fareskingtube.block.entity.ImplementedInventory;
import net.fareskingtube.block.entity.ModBlockEntities;
import net.fareskingtube.block.entity.TickableBlockEntity;
import net.fareskingtube.component.ModDataComponentTypes;
import net.fareskingtube.item.ModItems;
import net.fareskingtube.multiblock.ModMultiblocks;
import net.fareskingtube.persistent.DeadPlayersState;
import net.fareskingtube.persistent.QueuedPlayer;
import net.fareskingtube.persistent.RevivalQueueState;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;

public class RevivalAltarBlockEntity extends BlockEntity implements ImplementedInventory, TickableBlockEntity {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(1, ItemStack.EMPTY);
    private boolean isMultiblock = false;
    private int ticks = 0;
    private float rotation = 0;

    public RevivalAltarBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.REVIVAL_ALTAR_BE, pos, state);
    }


    @Override
    public void tick() {
        if (this.getWorld() == null) return;

        World world = this.getWorld();

        if (this.ticks++ % 20 == 0) {
            boolean currentIsMultiblock = isMultiblock(world, this.getPos());
            if (this.isMultiblock && !currentIsMultiblock) {
                world.playSound(null, this.pos, SoundEvents.BLOCK_BEACON_DEACTIVATE, SoundCategory.BLOCKS);
                this.removeQueuedPlayer();
            }
            this.isMultiblock = currentIsMultiblock;
            if (this.isMultiblock && this.getStack(0) != null) {
                this.queuePlayerRevival();
            }
        }

        if (this.isMultiblock) {
            if (world.isClient()) {
                spawnParticles(world);
                spawnCherryBlossomParticle(world);
            } else {
                applyEffectsToNearbyPlayers(world);
            }
        }
    }

    //    Claude made most of both of those particle spawning methods
    private void spawnParticles(World world) {
        if (this.ticks % 4 != 0) return; // every 2 ticks for density

        Random random = world.getRandom();
        BlockPos pos = this.getPos();

        double centerX = pos.getX() + 0.5;
        double centerY = pos.getY() + 0.5;
        double centerZ = pos.getZ() + 0.5;

        for (int i = 0; i < 8; i++) {
            double x = centerX + (random.nextDouble() * 11 - 5.5);
            double y = centerY + 0.5 + random.nextDouble() * 6;
            double z = centerZ + 0.5 + (random.nextDouble() * 11 - 5.5);

            double dx = centerX - x;
            double dy = centerY - y;
            double dz = centerZ - z;

            double length = Math.sqrt(dx * dx + dy * dy + dz * dz);

            double speed = 0.05;
            double vx = (dx / length) * speed;
            double vy = (dy / length) * speed;
            double vz = (dz / length) * speed;


            world.addParticle(
                    ParticleTypes.ASH,
                    x, y, z,
                    vx,
                    vy,
                    vz
            );

        }
    }

    private void spawnCherryBlossomParticle(World world) {
        // Only runs every few ticks to avoid overwhelming the client
        if (this.ticks % 20 != 0) return;

        Random random = world.getRandom();
        BlockPos pos = this.getPos();

        // Spawns particles in a radius around the center block
        for (int i = 0; i < 3; i++) {
            double x = pos.getX() + 0.5 + (random.nextDouble() * 11 - 5.5);
            double y = pos.getY() + 5 + random.nextDouble() * 2;
            double z = pos.getZ() + 0.5 + (random.nextDouble() * 11 - 5.5);

            world.addParticle(
                    ParticleTypes.CHERRY_LEAVES, // swap for any ParticleTypes constant you like
                    x, y, z,
                    (random.nextDouble() - 0.5) * 0.05, -0.05, (random.nextDouble() - 0.5) * 0.05
            );
        }
    }

    private void applyEffectsToNearbyPlayers(World world) {
        // Only apply effects once per second (every 20 ticks)
        if (this.ticks % 80 != 0) return;

        BlockPos pos = this.getPos();
        double radius = 5.5;


        // Get all players within radius
        List<PlayerEntity> players = world.getEntitiesByClass(
                PlayerEntity.class,
                new Box(pos).expand(radius),
                player -> true
        );

        players.sort(Comparator.comparingDouble(p ->
                p.squaredDistanceTo(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5)
        ));

        for (PlayerEntity player : players) {
            player.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.REGENERATION, // swap for any StatusEffects constant
                    120,   // duration in ticks
                    0,    // amplifier (0 = level I, 1 = level II, etc.)
                    true, // ambient (true makes particles more transparent, like beacons)
                    true,  // show particles
                    true   // show icon in HUD
            ));
        }
    }

    // TODO: Make revive player start a craft
    public void revivePlayer() {
        World world = this.getWorld();

        if (world == null) return;

        if (!this.isMultiblock(world, this.getPos())) return;

        MinecraftServer server = world.getServer();

        if (server == null) return;

        RevivalQueueState state = RevivalQueueState.get(server);

        QueuedPlayer queuedPlayer = state.getPlayer(this.pos, world.getRegistryKey());

        GameProfile profile = this.getStack(0).get(ModDataComponentTypes.SELECTED_PLAYER);

        if (profile == null) return;

        if (!queuedPlayer.player().getId().equals(profile.getId())) return;

        ServerPlayerEntity player = server.getPlayerManager().getPlayer(queuedPlayer.player().getId());

        if (player == null) return;

        boolean isHeart = this.inventory.getFirst().getItem() == ModItems.HARDCORE_HEART;

        if (isHeart && player instanceof ServerPlayerEntity serverPlayer && serverPlayer.interactionManager.getGameMode() == GameMode.SPECTATOR) {
            setStack(0, ItemStack.EMPTY);
            markDirty();
            serverPlayer.teleport(
                    (ServerWorld) world,
                    pos.getX() + 0.5,
                    pos.getY() + 1,
                    pos.getZ() + 0.5,
                    180,
                    0
            );
            serverPlayer.changeGameMode(GameMode.SURVIVAL);
            world.playSound(null, pos, SoundEvents.ITEM_TOTEM_USE, SoundCategory.PLAYERS);
            if (world instanceof ServerWorld serverWorld) {
                serverWorld.spawnParticles(
                        ParticleTypes.TOTEM_OF_UNDYING,
                        serverPlayer.getX(), serverPlayer.getY() + 1, serverPlayer.getZ(),
                        30,
                        0.5, 0.5, 0.5,
                        0.1
                );
            }
            DeadPlayersState.get(server).removeDeadPlayer(player.getUuid());
            state.removeQueuedPlayer(player.getUuid(), this.getPos(), world.getRegistryKey());
        }
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    public boolean isMultiblock(World world, BlockPos pos) {
        boolean validateMultiblock = ModMultiblocks.REVIVAL_ALTAR_MULTIBLOCK.validate(world, pos.down(), BlockRotation.NONE);

        if (!this.isMultiblock && validateMultiblock) {
            world.playSound(null, pos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS);
        }

        return validateMultiblock;
    }

    public boolean getIsMultiblock() {
        return isMultiblock;
    }

    public void queuePlayerRevival() {
        if (!this.isMultiblock) return;

        if (this.world == null) return;
        MinecraftServer server = this.world.getServer();
        if (server == null) return;
        ItemStack stack = this.getStack(0);
        GameProfile player = stack.get(ModDataComponentTypes.SELECTED_PLAYER);
        if (player == null) return;

        RevivalQueueState.get(server).addQueuedPlayer(new QueuedPlayer(player, this.getPos(), this.world.getRegistryKey()));

        revivePlayer();
    }

    public void removeQueuedPlayer() {
        if (this.world == null) return;
        MinecraftServer server = this.world.getServer();
        if (server == null) return;

        RevivalQueueState.get(server).removeQueuedPlayer(this.getPos(), world.getRegistryKey());
    }

    public float getRenderingRotation(float rotationSpeedMultiplier) {
        rotation += 0.5f * rotationSpeedMultiplier;
        if (rotation >= 360) {
            rotation = 0;
        }
        return rotation;
    }

    @Override
    public void markDirty() {
        super.markDirty();

        if (world != null) {
            world.markDirty(pos);

            if (!world.isClient) {
                world.updateListeners(
                        pos,
                        getCachedState(),
                        getCachedState(),
                        Block.NOTIFY_ALL
                );
            }
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);

        nbt.putInt("ticks", this.ticks);
        nbt.putBoolean("isMultiBlock", this.isMultiblock);
        Inventories.writeNbt(nbt, inventory, registryLookup);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);

        this.ticks = nbt.getInt("ticks");
        this.isMultiblock = nbt.getBoolean("isMultiblock");
        inventory.set(0, ItemStack.EMPTY);
        Inventories.readNbt(nbt, inventory, registryLookup);
    }


    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return createNbt(registryLookup);
    }
}
