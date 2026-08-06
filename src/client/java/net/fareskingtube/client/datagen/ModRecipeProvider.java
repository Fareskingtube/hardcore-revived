package net.fareskingtube.client.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fareskingtube.block.ModBlocks;
import net.fareskingtube.component.ModDataComponentTypes;
import net.fareskingtube.item.ModItems;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        // Revival Altar Recipe
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModBlocks.REVIVAL_ALTAR)
                .pattern("BGB")
                .pattern("BDB")
                .pattern("DDD")
                .input('B', ModBlocks.BLOOD_BLOCK)
                .input('G', Blocks.GOLD_BLOCK)
                .input('D', Blocks.DEEPSLATE_TILES)
                .criterion(hasItem(ModBlocks.REVIVAL_ALTAR), conditionsFromItem(ModBlocks.BLOOD_BLOCK))
                .offerTo(exporter);
        // Heart Injector recipe (temporary)
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.HEART_INJECTOR)
                .pattern("I ")
                .pattern(" G")
                .input('I', Items.IRON_INGOT)
                .input('G', Items.GLASS_BOTTLE)
                .criterion(hasItem(Items.GLASS_BOTTLE), conditionsFromItem(ModItems.HEART_INJECTOR))
                .offerTo(exporter);
        // Heart Extractor recipe (temporary)
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.HEART_EXTRACTOR)
                .pattern("S S")
                .pattern("SSS")
                .pattern(" S ")
                .input('S', Items.STICK)
                .criterion(hasItem(Items.STICK), conditionsFromItem(ModItems.HEART_EXTRACTOR))
                .offerTo(exporter);
        //  TODO: Change this recipe to an item that drops blood from damaging mobs or from extracting your own blood
        offer2x2CompactingRecipe(exporter, RecipeCategory.MISC, ModBlocks.BLOOD_BLOCK, Blocks.REDSTONE_BLOCK);
    }
}
