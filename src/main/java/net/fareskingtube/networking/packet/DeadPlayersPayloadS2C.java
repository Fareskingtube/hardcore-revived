package net.fareskingtube.networking.packet;

import com.mojang.authlib.GameProfile;
import net.fareskingtube.HardcoreRevived;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import java.util.List;

public record DeadPlayersPayloadS2C(List<GameProfile> deadPlayers) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<DeadPlayersPayloadS2C> ID =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(HardcoreRevived.MOD_ID, "dead_players"));

    public static final StreamCodec<FriendlyByteBuf, DeadPlayersPayloadS2C> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.GAME_PROFILE.apply(ByteBufCodecs.list()),
            DeadPlayersPayloadS2C::deadPlayers,

            DeadPlayersPayloadS2C::new
    );


    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
