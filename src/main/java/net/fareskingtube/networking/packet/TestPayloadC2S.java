package net.fareskingtube.networking.packet;

import net.fareskingtube.HardcoreRevived;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record TestPayloadC2S(String name, int value) implements CustomPayload {
    public static final CustomPayload.Id<TestPayloadC2S> ID =
            new CustomPayload.Id<>(Identifier.of(HardcoreRevived.MOD_ID, "dead_players"));

    public static final PacketCodec<PacketByteBuf, TestPayloadC2S> STREAM_CODEC = PacketCodec.tuple(
            PacketCodecs.STRING,
            TestPayloadC2S::name,

            PacketCodecs.INTEGER,
            TestPayloadC2S::value,

            TestPayloadC2S::new
    );


    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
