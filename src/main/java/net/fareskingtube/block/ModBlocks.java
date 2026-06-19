package net.fareskingtube.block;

import net.fareskingtube.HardcoreRevived;
import net.fareskingtube.block.custom.RevivalAltarBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class ModBlocks {
    public static final Block REVIVAL_ALTAR = registerBlock("revival_altar", new RevivalAltarBlock(AbstractBlock.Settings.create()
            .nonOpaque()
            .requiresTool()
            .strength(3.5F, 6.0F)));

    public static final Block BLOOD_BLOCK = registerBlock("blood_block", new Block(AbstractBlock.Settings.create()
            .strength(0.6F, 2F)
            .sounds(BlockSoundGroup.NETHER_STEM)
    ));

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(HardcoreRevived.MOD_ID, name), new BlockItem(block, new Item.Settings()));
    }

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(HardcoreRevived.MOD_ID, name), block);
    }

    public static void registerModBlocks() {
        HardcoreRevived.LOGGER.info("Registering Mod Blocks for " + HardcoreRevived.MOD_ID);
    }
}
