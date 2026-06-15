package net.fareskingtube.client.util;


import net.fareskingtube.HardcoreRevived;
import net.fareskingtube.component.ModDataComponentTypes;
import net.fareskingtube.item.ModItems;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;

public class ModModelPredicates {
    public static void registerModelPredicates() {
        ModelPredicateProviderRegistry.register(ModItems.HEART_INJECTOR, Identifier.of(HardcoreRevived.MOD_ID, "has_heart"),
                (stack, world, entity, seed) -> {
                    Boolean hasHeart = stack.get(ModDataComponentTypes.HAS_HEART);
                    return hasHeart != null && hasHeart ? 1f : 0f;
                }
        );
    }
}
