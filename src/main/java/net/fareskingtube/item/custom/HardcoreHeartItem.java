package net.fareskingtube.item.custom;

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
        if (world.isClient() && user instanceof PlayerEntity player) {
            // Code after activation here
            ModMainClientBridges.OPEN_PLAYER_PICKER.accept(List.of(player), player);
        }
        return stack;
    }
}
