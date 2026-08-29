package net.fareskingtube.client.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;import net.fareskingtube.networking.packet.TestPayloadC2S;

// Runs ON CLIENT on receive
public class ClientboundPackets {
    public static void handleTestPayload(TestPayloadC2S testPayloadC2S, ClientPlayNetworking.Context context) {
    }
}
