package net.fareskingtube.client;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fareskingtube.client.datagen.ModBlockTagProvider;
import net.fareskingtube.client.datagen.ModLootTableProvider;
import net.fareskingtube.client.datagen.ModModelProvider;
import net.fareskingtube.client.datagen.ModRecipeProvider;

public class HardcoreRevivedDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(ModLootTableProvider::new);
        pack.addProvider(ModModelProvider::new);
        pack.addProvider(ModRecipeProvider::new);
        pack.addProvider(ModBlockTagProvider::new);
    }
}
