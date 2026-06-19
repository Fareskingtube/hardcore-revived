package net.fareskingtube.client;

import net.fabricmc.api.ClientModInitializer;
import net.fareskingtube.block.entity.ModBlockEntities;
import net.fareskingtube.client.block.entity.renderer.RevivalAltarBlockEntityRenderer;
import net.fareskingtube.client.util.ModModelPredicates;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class HardcoreRevivedClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
        ModModelPredicates.registerModelPredicates();
        BlockEntityRendererFactories.register(ModBlockEntities.REVIVAL_ALTAR_BE, RevivalAltarBlockEntityRenderer::new);
    }
}