package net.fareskingtube.networking.packet;

import com.mojang.authlib.GameProfile;
import net.fareskingtube.HardcoreRevived;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record PlayerSelectionPayloadC2S(GameProfile player) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<PlayerSelectionPayloadC2S> ID =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(HardcoreRevived.MOD_ID, "dead_players"));

    public static final StreamCodec<FriendlyByteBuf, PlayerSelectionPayloadC2S> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.GAME_PROFILE,
            PlayerSelectionPayloadC2S::player,

            PlayerSelectionPayloadC2S::new
    );


    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
