package net.fareskingtube.item;

import net.fareskingtube.HardcoreRevived;
import net.fareskingtube.item.custom.HardcoreHeartItem;
import net.fareskingtube.item.custom.HeartExtractorItem;
import net.fareskingtube.item.custom.HeartInjectorItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item HARDCORE_HEART = registerItem("hardcore_heart", new HardcoreHeartItem(new Item.Settings().fireproof().maxCount(1)));
    public static final Item HEART_EXTRACTOR = registerItem("heart_extractor", new HeartExtractorItem(new Item.Settings().maxDamage(1).maxCount(1)));
    public static final Item HEART_INJECTOR = registerItem("heart_injector", new HeartInjectorItem(new Item.Settings().maxCount(1)));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(HardcoreRevived.MOD_ID, name), item);
    }

    public static void registerModItems() {
        HardcoreRevived.LOGGER.info("Registering Mod Items for " + HardcoreRevived.MOD_ID);
    }
}
