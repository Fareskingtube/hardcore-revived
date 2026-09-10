package net.fareskingtube.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fareskingtube.HardcoreRevived;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import java.util.Optional;

public class ModItemGroup {
    public static final CreativeModeTab HARDCORE_REVIVED_GROUP = register("hardcore_revived_group",
            FabricItemGroup.builder()
                    .title(Component.translatable("itemGroup." + HardcoreRevived.MOD_ID + ".hardcore_revived_group"))
                    .icon(ModItems.HARDCORE_HEART::getDefaultInstance)
                    .displayItems((displayContext, entries) -> BuiltInRegistries.ITEM.keySet()
                            .stream()
                            .filter(key -> key.getNamespace().equals(HardcoreRevived.MOD_ID))
                            .map(BuiltInRegistries.ITEM::getOptional)
                            .map(Optional::orElseThrow)
                            .forEach(entries::accept))
                    .build());

    public static <T extends CreativeModeTab> T register(String name, T itemGroup) {
        return Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, ResourceLocation.fromNamespaceAndPath(HardcoreRevived.MOD_ID, name), itemGroup);
    }

    public static void registerItemGroups() {
        HardcoreRevived.LOGGER.info("Registering Mod Item Groups for " + HardcoreRevived.MOD_ID);
    }
}
