package net.fareskingtube.item.custom;

import com.mojang.authlib.GameProfile;
import net.fareskingtube.bridge.ModMainClientBridges;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

// TODO: Make duration a config
public class HardcoreHeartItem extends HoldActivateItem {
    public HardcoreHeartItem(Settings settings) {
        super(settings, 20);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        List<GameProfile> deadPlayers = List.of();
        if (world.isClient && user instanceof PlayerEntity player) {
            // Code after activation here
            GameProfile profile = new GameProfile(player.getUuid(), player.getNameForScoreboard());
            ModMainClientBridges.OPEN_PLAYER_PICKER.accept(
                    deadPlayers, profile);

//            ClientPlayNetworking.send(player, new TestPayloadC2S(player.getName().toString(), 1));
        }
        return stack;
    }
}
