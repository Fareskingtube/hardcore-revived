package net.fareskingtube;

import com.mojang.authlib.GameProfile;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fareskingtube.block.entity.ModBlockEntities;
import net.fareskingtube.component.ModDataComponentTypes;
import net.fareskingtube.item.ModItemGroup;
import net.fareskingtube.item.ModItems;
import net.fareskingtube.multiblock.ModMultiblocks;
import net.fareskingtube.networking.ModPackets;
import net.fareskingtube.persistent.DeadPlayersState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
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

                    for (GameProfile deadPlayer : DeadPlayersState.get(server).getDeadPlayers()) {
                        LOGGER.info("players: " + deadPlayer.getName() + " Is on the dead list");
                    }
                }
            }
        });
    }
}