package net.fareskingtube.client.networking;

import com.mojang.authlib.GameProfile;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fareskingtube.client.gui.screen.custom.PlayerSelectorScreen;
import net.fareskingtube.networking.packet.DeadPlayersPayloadS2C;
import net.fareskingtube.networking.packet.PlayerSelectionPayloadC2S;
import net.minecraft.client.Minecraft;

// Runs ON CLIENT on receive
public class ClientboundPackets {
    public static void handleDeadPlayersPayload(DeadPlayersPayloadS2C deadPlayersPayloadS2C, ClientPlayNetworking.Context context) {
        Minecraft.getInstance().setScreen(
                new PlayerSelectorScreen(deadPlayersPayloadS2C.deadPlayers(),
                        new GameProfile(context.player().getUUID(), context.player().getScoreboardName()),
                        chosen -> ClientPlayNetworking.send(new PlayerSelectionPayloadC2S(chosen))
                )
        );
    }
}
