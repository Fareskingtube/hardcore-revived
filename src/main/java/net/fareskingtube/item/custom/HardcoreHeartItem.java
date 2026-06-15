package net.fareskingtube.item.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;

// TODO: Make duration a config
public class HardcoreHeartItem extends HoldActivateItem {
    public HardcoreHeartItem(Settings settings) {
        super(settings, 50);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (world.isClient()) {
            // Code after activation here
            user.sendMessage(Text.literal("Test Werked"));
        }
        return stack;
    }
}
