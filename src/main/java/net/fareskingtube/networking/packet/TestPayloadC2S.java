package net.fareskingtube.networking.packet;

import net.fareskingtube.HardcoreRevived;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

// Not used just exists for future reference
public record TestPayloadC2S(String name, int value) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<TestPayloadC2S> ID =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(HardcoreRevived.MOD_ID, "test_payload"));

    public static final StreamCodec<FriendlyByteBuf, TestPayloadC2S> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            TestPayloadC2S::name,

            ByteBufCodecs.INT,
            TestPayloadC2S::value,

            TestPayloadC2S::new
    );


    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
