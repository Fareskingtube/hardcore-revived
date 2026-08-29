package net.fareskingtube.networking;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fareskingtube.HardcoreRevived;
import net.fareskingtube.networking.packet.TestPayloadC2S;
import net.minecraft.network.RegistryByteBuf;

public class ModPackets {


    // Payload from client to server (S2C) -> Serve to Client
    private static void registerServerbound(PayloadTypeRegistry<RegistryByteBuf> registry) {
        registry.register(TestPayloadC2S.ID, TestPayloadC2S.STREAM_CODEC);

        ServerPlayNetworking.registerGlobalReceiver(TestPayloadC2S.ID, ServerboundPackets::handleTestPayload);
    }


    public static void registerPackets() {
        HardcoreRevived.LOGGER.info("Registering Serverbound Packets for " + HardcoreRevived.MOD_ID);
        registerServerbound(PayloadTypeRegistry.playC2S());
    }
}
