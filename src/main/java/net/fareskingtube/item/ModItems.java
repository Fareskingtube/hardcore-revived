package net.fareskingtube.item;

import net.fareskingtube.HardcoreRevived;
import net.fareskingtube.item.custom.HardcoreHeartItem;
import net.fareskingtube.item.custom.HeartExtractorItem;
import net.fareskingtube.item.custom.HeartInjectorItem;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;

public class ModItems {
    public static final Item HARDCORE_HEART = registerItem("hardcore_heart", new HardcoreHeartItem(new Item.Properties().fireResistant().stacksTo(1)));
    public static final Item HEART_EXTRACTOR = registerItem("heart_extractor", new HeartExtractorItem(new Item.Properties().durability(1).stacksTo(1)));
    public static final Item HEART_INJECTOR = registerItem("heart_injector", new HeartInjectorItem(new Item.Properties().stacksTo(1)));
    public static final Item BUTCHER_KNIFE = registerItem("butcher_knife", new SwordItem(ModToolMaterials.BUTCHER_KNIFE_MATERIAL, new Item.Properties()
            .attributes(SwordItem.createAttributes(ModToolMaterials.BUTCHER_KNIFE_MATERIAL, 3, -1.8f))));
    // TODO: Add tooltip for how to get
    public static final Item BLOOD = registerItem("blood", new Item(new Item.Properties()));

    private static Item registerItem(String name, Item item) {
        return Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(HardcoreRevived.MOD_ID, name), item);
    }

    public static void registerModItems() {
        HardcoreRevived.LOGGER.info("Registering Mod Items for " + HardcoreRevived.MOD_ID);
    }
}
