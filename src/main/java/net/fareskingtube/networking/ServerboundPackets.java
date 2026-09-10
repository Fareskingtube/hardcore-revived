package net.fareskingtube.networking;

import com.mojang.authlib.GameProfile;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fareskingtube.HardcoreRevived;
import net.fareskingtube.component.ModDataComponentTypes;
import net.fareskingtube.item.custom.HardcoreHeartItem;
import net.fareskingtube.networking.packet.PlayerSelectionPayloadC2S;
import net.fareskingtube.persistent.DeadPlayersState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

// Runs ON SERVER on receive
public class ServerboundPackets {
    // Handles when a player is selected from a hardcore heart by a client
    public static void handlePlayerSelectionPayload(PlayerSelectionPayloadC2S playerSelectionPayloadC2S, ServerPlayNetworking.Context context) {
        ServerPlayer player = context.player();
        MinecraftServer server = context.server();

        GameProfile trustedProfile = DeadPlayersState.get(server).getDeadPlayer(playerSelectionPayloadC2S.player().getId());

        if (trustedProfile == null) {
            HardcoreRevived.LOGGER.warn("Player {} tried to select an invalid/non-dead profile: {}",
                    player.getName().getString(), player.getId());
            return;
        }

        InteractionHand targetHand = null;
        for (InteractionHand hand : InteractionHand.values()) {
            if (player.getItemInHand(hand).getItem() instanceof HardcoreHeartItem) {
                targetHand = hand;
                break;
            }
        }
        if (targetHand == null) return;

        ItemStack stack = player.getItemInHand(targetHand);

        stack.set(ModDataComponentTypes.SELECTED_PLAYER, trustedProfile);
        player.level().playSound(null, player.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 1f, 1f);
    }
}
