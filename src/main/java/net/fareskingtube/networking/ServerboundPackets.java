package net.fareskingtube.networking;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fareskingtube.networking.packet.TestPayloadC2S;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;

// Runs ON SERVER on receive
public class ServerboundPackets {
    public static void handleTestPayload(TestPayloadC2S testPayloadC2S, ServerPlayNetworking.Context context) {
        EntityType.COW.spawn(context.player().getServerWorld(), context.player().getBlockPos(), SpawnReason.TRIGGERED);
    }
}
