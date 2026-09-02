package net.fareskingtube;

import com.mojang.authlib.GameProfile;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fareskingtube.block.entity.ModBlockEntities;
import net.fareskingtube.block.entity.custom.RevivalAltarBlockEntity;
import net.fareskingtube.component.ModDataComponentTypes;
import net.fareskingtube.item.ModItemGroup;
import net.fareskingtube.item.ModItems;
import net.fareskingtube.multiblock.ModMultiblocks;
import net.fareskingtube.networking.ModPackets;
import net.fareskingtube.persistent.DeadPlayersState;
import net.fareskingtube.persistent.QueuedPlayer;
import net.fareskingtube.persistent.RevivalQueueState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HardcoreRevived implements ModInitializer {
    public static final String MOD_ID = "hardcore-revived";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Loading...");

        ModItems.registerModItems();
        ModItemGroup.registerItemGroups();
        ModDataComponentTypes.registerDataComponentTypes();
        ModBlockEntities.registerBlockEntities();
        ModMultiblocks.registerModMultiBlocks();
        ModPackets.registerPackets();

        ServerLivingEntityEvents.AFTER_DEATH.register((livingEntity, damageSource) -> {
            if (livingEntity instanceof PlayerEntity player) {
                if (!player.getWorld().isClient()) {
                    MinecraftServer server = player.getServer();
                    if (server == null) return;
                    DeadPlayersState.get(server).addDeadPlayer(new GameProfile(player.getUuid(), player.getNameForScoreboard()));
                }
            }
        });

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
    }
}