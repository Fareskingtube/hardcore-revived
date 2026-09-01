package net.fareskingtube.networking.packet;

import com.mojang.authlib.GameProfile;
import net.fareskingtube.HardcoreRevived;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record PlayerSelectionPayloadC2S(GameProfile player) implements CustomPayload {
    public static final CustomPayload.Id<PlayerSelectionPayloadC2S> ID =
            new CustomPayload.Id<>(Identifier.of(HardcoreRevived.MOD_ID, "dead_players"));

    public static final PacketCodec<PacketByteBuf, PlayerSelectionPayloadC2S> STREAM_CODEC = PacketCodec.tuple(
            PacketCodecs.GAME_PROFILE,
            PlayerSelectionPayloadC2S::player,

            PlayerSelectionPayloadC2S::new
    );


    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
