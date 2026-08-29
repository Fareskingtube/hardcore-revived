package net.fareskingtube.item.custom;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fareskingtube.networking.packet.DeadPlayersPayloadS2C;
import net.fareskingtube.persistent.DeadPlayersState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

// TODO: Make duration a config
public class HardcoreHeartItem extends HoldActivateItem {
    public HardcoreHeartItem(Settings settings) {
        super(settings, 20);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        MinecraftServer server = world.getServer();
        if (server != null && !world.isClient && user instanceof ServerPlayerEntity player) {
            /* Gets the list of Players from Persistent Data and sends a Packet to the Client with the list of the dead players */
            ServerPlayNetworking.send(player, new DeadPlayersPayloadS2C(DeadPlayersState.get(server).getDeadPlayers()));
            // TODO: Delete this after testing
            // GameProfile profile = new GameProfile(player.getUuid(), player.getNameForScoreboard());
            // ServerPlayNetworking.send(player, new DeadPlayersPayloadS2C(List.of(
            //         profile,
            //         profile,
            //         profile,
            //         profile,
            //         profile,
            //         profile,
            //         profile,
            //         profile,
            //         profile,
            //         profile,
            //         profile,
            //         profile,
            //         profile,
            //         profile,
            //         profile,
            //         profile,
            //         profile)));
        }
        return stack;
    }
}
