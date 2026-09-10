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
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
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
            if (livingEntity instanceof Player player) {
                if (!player.level().isClientSide()) {
                    MinecraftServer server = player.getServer();
                    if (server == null) return;
                    DeadPlayersState.get(server).addDeadPlayer(new GameProfile(player.getUUID(), player.getScoreboardName()));
                }
            }
        });

        // On player join
        ServerPlayerEvents.JOIN.register(serverPlayerEntity -> {
            MinecraftServer server = serverPlayerEntity.getServer();

            if (server == null) return;

            RevivalQueueState state = RevivalQueueState.get(server);

            QueuedPlayer queuedPlayer = state.getPlayer(serverPlayerEntity.getUUID());

            if (queuedPlayer == null) return;

            ServerLevel world = server.getLevel(queuedPlayer.world());

            if (world == null) return;

            if (world.getBlockEntity(queuedPlayer.pos()) instanceof RevivalAltarBlockEntity revivalAltarBlockEntity) {
                if (revivalAltarBlockEntity.isMultiblock(world, revivalAltarBlockEntity.getBlockPos())) {
                    revivalAltarBlockEntity.revivePlayer();
                }
            }
        });

        // On killing a player
        ServerLivingEntityEvents.AFTER_DEATH.register((livingEntity, damageSource) -> {
            CommonConfig config = CommonConfig.HANDLER.instance();
            if (damageSource.getEntity() instanceof ServerPlayer killer) {
                // TODO: Idea: Revive the victim instead of making the killer lose health
                if (livingEntity instanceof ServerPlayer) {
                    Level world = livingEntity.level();
                    AttributeInstance maxHealth = killer.getAttribute(Attributes.MAX_HEALTH);
                    if (maxHealth != null && maxHealth.getValue() - 4 > 0) {
                        killer.sendSystemMessage(Component.translatable("misc.hardcore-revived.player_kill").withStyle(ChatFormatting.RED));
                        EntityType.LIGHTNING_BOLT.spawn((ServerLevel) world, killer.blockPosition(), MobSpawnType.TRIGGERED);
                        maxHealth.setBaseValue(maxHealth.getValue() - config.killPenalty);
                    }
                }
                if (livingEntity instanceof AgeableMob victim && killer.getMainHandItem().getItem() == ModItems.BUTCHER_KNIFE) {
                    int count = victim.getRandom().nextIntBetweenInclusive(1, 3);
                    Containers.dropItemStack(
                            victim.level(),
                            victim.getX(), victim.getY(), victim.getZ(),
                            new ItemStack(ModItems.BLOOD, count)
                    );
                }
            }
        });

        // On damaging a player past half health
        ServerLivingEntityEvents.AFTER_DAMAGE.register((livingEntity, damageSource, v, v1, b) -> {
            if (livingEntity instanceof Player victim) {
                if (damageSource.getEntity() instanceof ServerPlayer killer && victim.getHealth() < victim.getMaxHealth() / 2) {
                    if (killer.getMaxHealth() - 4 > 0) {
                        killer.sendSystemMessage(Component.translatable("misc.hardcore-revived.player_kill_warn").withStyle(ChatFormatting.YELLOW));
                    }
                }
            }
        });
    }
}