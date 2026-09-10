package net.fareskingtube.client.util;


import net.fareskingtube.HardcoreRevived;
import net.fareskingtube.component.ModDataComponentTypes;
import net.fareskingtube.item.ModItems;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;

public class ModModelPredicates {
    public static void registerModelPredicates() {
        ItemProperties.register(ModItems.HEART_INJECTOR, ResourceLocation.fromNamespaceAndPath(HardcoreRevived.MOD_ID, "has_heart"),
                (stack, world, entity, seed) -> {
                    Boolean hasHeart = stack.get(ModDataComponentTypes.HAS_HEART);
                    return hasHeart != null && hasHeart ? 1f : 0f;
                }
        );
    }
}
