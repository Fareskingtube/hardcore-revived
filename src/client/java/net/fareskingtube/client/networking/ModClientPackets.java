package net.fareskingtube.client.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fareskingtube.HardcoreRevived;
import net.fareskingtube.networking.packet.DeadPlayersPayloadS2C;
import net.minecraft.network.RegistryByteBuf;

public class ModClientPackets {
    // Payload from server to client (C2S) -> Client to Server
    private static void registerClientbound(PayloadTypeRegistry<RegistryByteBuf> registry) {
        registry.register(DeadPlayersPayloadS2C.ID, DeadPlayersPayloadS2C.STREAM_CODEC);

        ClientPlayNetworking.registerGlobalReceiver(DeadPlayersPayloadS2C.ID, ClientboundPackets::handleDeadPlayersPayload);
    }

    public static void registerPackets() {
        HardcoreRevived.LOGGER.info("Registering Clientbound Packets for " + HardcoreRevived.MOD_ID);
        registerClientbound(PayloadTypeRegistry.playS2C());
    }
}
