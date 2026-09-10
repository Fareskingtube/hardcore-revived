package net.fareskingtube.block.entity;

import net.fareskingtube.HardcoreRevived;
import net.fareskingtube.block.ModBlocks;
import net.fareskingtube.block.entity.custom.RevivalAltarBlockEntity;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockEntities {
    public static final BlockEntityType<RevivalAltarBlockEntity> REVIVAL_ALTAR_BE = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(HardcoreRevived.MOD_ID, "revival_be"), BlockEntityType.Builder.of(RevivalAltarBlockEntity::new, ModBlocks.REVIVAL_ALTAR).build(null));

    public static void registerBlockEntities() {
        HardcoreRevived.LOGGER.info("Registering Block Entities for " + HardcoreRevived.MOD_ID);
    }
}
