package net.fareskingtube.item.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;


public abstract class HoldActivateItem extends Item {
    private final int HOLD_DURATION;

    public HoldActivateItem(Settings settings, int duration) {
        super(settings);
        HOLD_DURATION = duration;
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return HOLD_DURATION;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public abstract ItemStack finishUsing(ItemStack stack, World world, LivingEntity user);

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (getMaxUseTime(itemStack, user) <= 0) {
            finishUsing(itemStack, world, user);
        } else {
            user.setCurrentHand(hand);
        }

        return TypedActionResult.success(itemStack, world.isClient());
    }
}
