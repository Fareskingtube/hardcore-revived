package net.fareskingtube.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fareskingtube.HardcoreRevived;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class ModItemGroup {
    public static final ItemGroup HARDCORE_REVIVED_GROUP = register("hardcore_revived_group",
            FabricItemGroup.builder()
                    .displayName(Text.translatable("itemGroup." + HardcoreRevived.MOD_ID + ".hardcore_revived_group"))
                    .icon(ModItems.HARDCORE_HEART::getDefaultStack)
                    .entries((displayContext, entries) -> Registries.ITEM.getIds()
                            .stream()
                            .filter(key -> key.getNamespace().equals(HardcoreRevived.MOD_ID))
                            .map(Registries.ITEM::getOrEmpty)
                            .map(Optional::orElseThrow)
                            .forEach(entries::add))
                    .build());

    public static <T extends ItemGroup> T register(String name, T itemGroup) {
        return Registry.register(Registries.ITEM_GROUP, Identifier.of(HardcoreRevived.MOD_ID, name), itemGroup);
    }

    public static void registerItemGroups() {
        HardcoreRevived.LOGGER.info("Registering Mod Item Groups for " + HardcoreRevived.MOD_ID);
    }
}
