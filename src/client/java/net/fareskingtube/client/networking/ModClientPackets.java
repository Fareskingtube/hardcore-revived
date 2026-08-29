package net.fareskingtube.client.networking;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fareskingtube.HardcoreRevived;
import net.minecraft.network.RegistryByteBuf;

public class ModClientPackets {
    // Payload from server to client (C2S) -> Client to Server
    private static void registerClientbound(PayloadTypeRegistry<RegistryByteBuf> registry) {
//        registry.register(TestPayloadC2S.ID, TestPayloadC2S.STREAM_CODEC);
//
//        ClientPlayNetworking.registerGlobalReceiver(TestPayloadC2S.ID, ClientboundPackets::handleTestPayload);
    }

    public static void registerPackets() {
        HardcoreRevived.LOGGER.info("Registering Clientbound Packets for " + HardcoreRevived.MOD_ID);
        registerClientbound(PayloadTypeRegistry.playS2C());
    }
}
