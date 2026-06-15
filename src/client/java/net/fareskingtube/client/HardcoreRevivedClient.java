package net.fareskingtube.client;

import net.fabricmc.api.ClientModInitializer;
import net.fareskingtube.client.util.ModModelPredicates;

public class HardcoreRevivedClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
        ModModelPredicates.registerModelPredicates();
    }
}