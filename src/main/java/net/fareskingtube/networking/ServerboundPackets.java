package net.fareskingtube.networking;

import com.mojang.authlib.GameProfile;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fareskingtube.HardcoreRevived;
import net.fareskingtube.component.ModDataComponentTypes;
import net.fareskingtube.item.custom.HardcoreHeartItem;
import net.fareskingtube.networking.packet.PlayerSelectionPayloadC2S;
import net.fareskingtube.persistent.DeadPlayersState;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;

// Runs ON SERVER on receive
public class ServerboundPackets {
    public static void handlePlayerSelectionPayload(PlayerSelectionPayloadC2S playerSelectionPayloadC2S, ServerPlayNetworking.Context context) {
        ServerPlayerEntity player = context.player();
        MinecraftServer server = context.server();

        GameProfile trustedProfile = DeadPlayersState.get(server).getDeadPlayer(playerSelectionPayloadC2S.player().getId());

        if (trustedProfile == null) {
            HardcoreRevived.LOGGER.warn("Player {} tried to select an invalid/non-dead profile: {}",
                    player.getName().getString(), player.getId());
            return;
        }

        Hand targetHand = null;
        for (Hand hand : Hand.values()) {
            if (player.getStackInHand(hand).getItem() instanceof HardcoreHeartItem) {
                targetHand = hand;
                break;
            }
        }
        if (targetHand == null) return;

        ItemStack stack = player.getStackInHand(targetHand);

        stack.set(ModDataComponentTypes.SELECTED_PLAYER, trustedProfile);
        player.getWorld().playSound(null, player.getBlockPos(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1f, 1f);
    }
}
