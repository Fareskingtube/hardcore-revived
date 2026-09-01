package net.fareskingtube.networking;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fareskingtube.HardcoreRevived;
import net.fareskingtube.networking.packet.PlayerSelectionPayloadC2S;
import net.minecraft.network.RegistryByteBuf;

public class ModPackets {


    // Payload from client to server (S2C) -> Serve to Client
    private static void registerServerbound(PayloadTypeRegistry<RegistryByteBuf> registry) {
        registry.register(PlayerSelectionPayloadC2S.ID, PlayerSelectionPayloadC2S.STREAM_CODEC);

        ServerPlayNetworking.registerGlobalReceiver(PlayerSelectionPayloadC2S.ID, ServerboundPackets::handlePlayerSelectionPayload);
    }


    public static void registerPackets() {
        HardcoreRevived.LOGGER.info("Registering Serverbound Packets for " + HardcoreRevived.MOD_ID);
        registerServerbound(PayloadTypeRegistry.playC2S());
    }
}
