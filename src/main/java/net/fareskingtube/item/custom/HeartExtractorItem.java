package net.fareskingtube.item.custom;

import net.fareskingtube.HardcoreRevived;
import net.fareskingtube.item.ModItems;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class HeartExtractorItem extends HoldActivateItem {
    public HeartExtractorItem(Settings settings) {
        super(settings, 50);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (!world.isClient()) {
            EntityAttributeInstance maxHealth = user.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
            if (maxHealth != null && maxHealth.getValue() - 2 > 0) {
                maxHealth.setBaseValue(maxHealth.getValue() - 2);
                stack.damage(1, ((ServerWorld) world), ((ServerPlayerEntity) user),
                        item -> user.sendEquipmentBreakStatus(item, EquipmentSlot.MAINHAND));
                ((ServerPlayerEntity) user).giveItemStack(ModItems.HARDCORE_HEART.getDefaultStack());
            }
        }
        return stack;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        EntityAttributeInstance maxHealth = user.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        if (maxHealth != null && maxHealth.getValue() - 2 > 0) {
            if (getMaxUseTime(itemStack, user) <= 0) {
                finishUsing(itemStack, world, user);
            } else {
                user.setCurrentHand(hand);
            }
            return TypedActionResult.success(itemStack, world.isClient());
        }
        world.playSound(null, user.getBlockPos(), SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.PLAYERS);
        return TypedActionResult.fail(itemStack);
    }
}
