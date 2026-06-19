package net.fareskingtube.block.entity;

import net.fareskingtube.HardcoreRevived;
import net.fareskingtube.block.ModBlocks;
import net.fareskingtube.block.entity.custom.RevivalAltarBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {
    public static final BlockEntityType<RevivalAltarBlockEntity> REVIVAL_ALTAR_BE = Registry.register(Registries.BLOCK_ENTITY_TYPE,
            Identifier.of(HardcoreRevived.MOD_ID, "revival_be"), BlockEntityType.Builder.create(RevivalAltarBlockEntity::new, ModBlocks.REVIVAL_ALTAR).build(null));

    public static void registerBlockEntities() {
        HardcoreRevived.LOGGER.info("Registering Block Entities for " + HardcoreRevived.MOD_ID);
    }
}
