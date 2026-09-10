package net.fareskingtube.block;

import net.fareskingtube.HardcoreRevived;
import net.fareskingtube.block.custom.RevivalAltarBlock;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class ModBlocks {
    public static final Block REVIVAL_ALTAR = registerBlock("revival_altar", new RevivalAltarBlock(BlockBehaviour.Properties.of()
            .noOcclusion()
            .requiresCorrectToolForDrops()
            .strength(3.5F, 6.0F)));

    public static final Block BLOOD_BLOCK = registerBlock("blood_block", new Block(BlockBehaviour.Properties.of()
            .strength(0.6F, 2F)
            .sound(SoundType.STEM)
    ));

    private static void registerBlockItem(String name, Block block) {
        Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(HardcoreRevived.MOD_ID, name), new BlockItem(block, new Item.Properties()));
    }

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath(HardcoreRevived.MOD_ID, name), block);
    }

    public static void registerModBlocks() {
        HardcoreRevived.LOGGER.info("Registering Mod Blocks for " + HardcoreRevived.MOD_ID);
    }
}
