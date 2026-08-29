package net.fareskingtube.networking.packet;

import com.mojang.authlib.GameProfile;
import net.fareskingtube.HardcoreRevived;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.util.List;

public record DeadPlayersPayloadS2C(List<GameProfile> deadPlayers) implements CustomPayload {
    public static final CustomPayload.Id<DeadPlayersPayloadS2C> ID =
            new CustomPayload.Id<>(Identifier.of(HardcoreRevived.MOD_ID, "dead_players"));

    public static final PacketCodec<PacketByteBuf, DeadPlayersPayloadS2C> STREAM_CODEC = PacketCodec.tuple(
            PacketCodecs.GAME_PROFILE.collect(PacketCodecs.toList()),
            DeadPlayersPayloadS2C::deadPlayers,

            DeadPlayersPayloadS2C::new
    );


    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
