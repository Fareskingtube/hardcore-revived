package net.fareskingtube;

import com.mojang.authlib.GameProfile;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fareskingtube.block.entity.ModBlockEntities;
import net.fareskingtube.block.entity.custom.RevivalAltarBlockEntity;
import net.fareskingtube.component.ModDataComponentTypes;
import net.fareskingtube.config.CommonConfig;
import net.fareskingtube.item.ModItemGroup;
import net.fareskingtube.item.ModItems;
import net.fareskingtube.multiblock.ModMultiblocks;
import net.fareskingtube.networking.ModPackets;
import net.fareskingtube.persistent.DeadPlayersState;
import net.fareskingtube.persistent.QueuedPlayer;
import net.fareskingtube.persistent.RevivalQueueState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.ItemScatterer;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HardcoreRevived implements ModInitializer {
    public static final String MOD_ID = "hardcore-revived";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Loading...");

        // Loading default common config to disk
        CommonConfig.load();

        ModItems.registerModItems();
        ModItemGroup.registerItemGroups();
        ModDataComponentTypes.registerDataComponentTypes();
        ModBlockEntities.registerBlockEntities();
        ModMultiblocks.registerModMultiBlocks();
        ModPackets.registerPackets();


        // On player death
        ServerLivingEntityEvents.AFTER_DEATH.register((livingEntity, damageSource) -> {
            if (livingEntity instanceof PlayerEntity player) {
                if (!player.getWorld().isClient()) {
                    MinecraftServer server = player.getServer();
                    if (server == null) return;
                    DeadPlayersState.get(server).addDeadPlayer(new GameProfile(player.getUuid(), player.getNameForScoreboard()));
                }
            }
        });

        // On player join
        ServerPlayerEvents.JOIN.register(serverPlayerEntity -> {
            MinecraftServer server = serverPlayerEntity.getServer();

            if (server == null) return;

            RevivalQueueState state = RevivalQueueState.get(server);

            QueuedPlayer queuedPlayer = state.getPlayer(serverPlayerEntity.getUuid());

            if (queuedPlayer == null) return;

            ServerWorld world = server.getWorld(queuedPlayer.world());

            if (world == null) return;

            if (world.getBlockEntity(queuedPlayer.pos()) instanceof RevivalAltarBlockEntity revivalAltarBlockEntity) {
                if (revivalAltarBlockEntity.isMultiblock(world, revivalAltarBlockEntity.getPos())) {
                    revivalAltarBlockEntity.revivePlayer();
                }
            }
        });

        // TODO: Add config to hearts taken from killer or respawn victim
        // On killing a player
        ServerLivingEntityEvents.AFTER_DEATH.register((livingEntity, damageSource) -> {
            if (damageSource.getAttacker() instanceof ServerPlayerEntity killer) {
                // TODO: Idea: Revive the victim instead of making the killer lose health
                if (livingEntity instanceof PlayerEntity) {
                    World world = livingEntity.getWorld();
                    EntityAttributeInstance maxHealth = killer.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
                    if (maxHealth != null && maxHealth.getValue() - 4 > 0) {
                        killer.sendMessage(Text.translatable("misc.hardcore-revived.player_kill").formatted(Formatting.RED));
                        EntityType.LIGHTNING_BOLT.spawn((ServerWorld) world, killer.getBlockPos(), SpawnReason.TRIGGERED);
                        maxHealth.setBaseValue(maxHealth.getValue() - 4);
                    }
                }
                //
                if (livingEntity instanceof PassiveEntity victim && killer.getMainHandStack().getItem() == ModItems.BUTCHER_KNIFE) {
                    int count = victim.getRandom().nextBetween(1, 3);
                    ItemScatterer.spawn(
                            victim.getWorld(),
                            victim.getX(), victim.getY(), victim.getZ(),
                            new ItemStack(ModItems.BLOOD, count)
                    );
                }
            }
        });

        // On damaging a player past half health
        ServerLivingEntityEvents.AFTER_DAMAGE.register((livingEntity, damageSource, v, v1, b) -> {
            if (livingEntity instanceof PlayerEntity victim) {
                if (damageSource.getAttacker() instanceof ServerPlayerEntity killer && victim.getHealth() < victim.getMaxHealth() / 2) {
                    if (killer.getMaxHealth() - 4 > 0) {
                        killer.sendMessage(Text.translatable("misc.hardcore-revived.player_kill_warn").formatted(Formatting.YELLOW));
                    }
                }
            }
        });
    }
}