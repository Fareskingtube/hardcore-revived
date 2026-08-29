package net.fareskingtube.client.networking;

import com.mojang.authlib.GameProfile;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fareskingtube.client.gui.screen.custom.PlayerSelectorScreen;
import net.fareskingtube.networking.packet.DeadPlayersPayloadS2C;
import net.minecraft.client.MinecraftClient;

// Runs ON CLIENT on receive
public class ClientboundPackets {
    public static void handleDeadPlayersPayload(DeadPlayersPayloadS2C deadPlayersPayloadS2C, ClientPlayNetworking.Context context) {
        MinecraftClient.getInstance().setScreen(
                new PlayerSelectorScreen(deadPlayersPayloadS2C.deadPlayers(),
                        new GameProfile(context.player().getUuid(), context.player().getNameForScoreboard()), chosen -> {
                    /* Output here */
                })
        );
    }
}
