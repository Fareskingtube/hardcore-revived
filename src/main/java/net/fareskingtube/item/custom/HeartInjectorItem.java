package net.fareskingtube.item.custom;

import net.fareskingtube.component.ModDataComponentTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

// TODO: Change Name and Sounds
public class HeartInjectorItem extends HoldActivateItem {
    public HeartInjectorItem(Settings settings) {
        super(settings, 60);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        Boolean hasHeart = stack.get(ModDataComponentTypes.HAS_HEART);
        EntityAttributeInstance maxHealth = user.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        if (!world.isClient() && hasHeart != null && hasHeart && maxHealth != null) {
            maxHealth.setBaseValue(maxHealth.getValue() + 2);
            stack.set(ModDataComponentTypes.HAS_HEART, false);
        }
        world.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_WITCH_DRINK, SoundCategory.PLAYERS);
        return stack;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        Boolean hasHeart = itemStack.get(ModDataComponentTypes.HAS_HEART);

        if (hasHeart != null && hasHeart) {
            if (getMaxUseTime(itemStack, user) <= 0) {
                finishUsing(itemStack, world, user);
            } else {
                user.setCurrentHand(hand);
            }
            return TypedActionResult.success(itemStack, world.isClient());
        }
        return TypedActionResult.fail(itemStack);
    }

    //    Changing the name of the Heart Importer based on the HAS_HEART Data Component
    @Override
    public Text getName(ItemStack stack) {
        Boolean hasHeart = stack.get(ModDataComponentTypes.HAS_HEART);
        if (hasHeart != null && hasHeart) {
            return Text.translatable("item.hardcore-revived.heart_injector.filled");
        }
        return Text.translatable("item.hardcore-revived.heart_injector.empty");
    }
}
