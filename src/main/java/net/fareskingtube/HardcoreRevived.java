package net.fareskingtube;

import net.fabricmc.api.ModInitializer;

import net.fareskingtube.block.entity.ModBlockEntities;
import net.fareskingtube.component.ModDataComponentTypes;
import net.fareskingtube.item.ModItemGroup;
import net.fareskingtube.item.ModItems;
import net.fareskingtube.multiblock.ModMultiblocks;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vazkii.patchouli.api.IMultiblock;
import vazkii.patchouli.api.PatchouliAPI;

public class HardcoreRevived implements ModInitializer {
    public static final String MOD_ID = "hardcore-revived";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Loading...");

        ModItems.registerModItems();
        ModItemGroup.registerItemGroups();
        ModDataComponentTypes.registerDataComponentTypes();
        ModBlockEntities.registerBlockEntities();
        ModMultiblocks.registerModMultiBlocks();

    }
}