package net.fareskingtube.client;

import net.fabricmc.api.ClientModInitializer;
import net.fareskingtube.block.entity.ModBlockEntities;
import net.fareskingtube.client.block.entity.renderer.RevivalAltarBlockEntityRenderer;
import net.fareskingtube.client.config.ClientConfig;
import net.fareskingtube.client.networking.ModClientPackets;
import net.fareskingtube.client.util.ModModelPredicates;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

public class HardcoreRevivedClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Loading default client config to disk
        ClientConfig.load();

        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
        ModModelPredicates.registerModelPredicates();
        BlockEntityRenderers.register(ModBlockEntities.REVIVAL_ALTAR_BE, RevivalAltarBlockEntityRenderer::new);

        // ModClientBridges.init();

        ModClientPackets.registerPackets();

    }
}