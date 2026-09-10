package net.fareskingtube.client.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fareskingtube.block.ModBlocks;
import net.fareskingtube.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void buildRecipes(RecipeOutput exporter) {
        // Revival Altar Recipe
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.REVIVAL_ALTAR)
                .pattern("BGB")
                .pattern("BDB")
                .pattern("DDD")
                .input('B', ModBlocks.BLOOD_BLOCK)
                .input('G', Blocks.GOLD_BLOCK)
                .input('D', Blocks.DEEPSLATE_TILES)
                .criterion(getHasName(ModBlocks.REVIVAL_ALTAR), has(ModBlocks.BLOOD_BLOCK))
                .offerTo(exporter);
        // Heart Injector recipe (temporary)
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.HEART_INJECTOR)
                .pattern("I ")
                .pattern(" G")
                .input('I', Items.IRON_INGOT)
                .input('G', Items.GLASS_BOTTLE)
                .criterion(getHasName(Items.GLASS_BOTTLE), has(ModItems.HEART_INJECTOR))
                .offerTo(exporter);
        // Heart Extractor recipe (temporary)
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.HEART_EXTRACTOR)
                .pattern("S S")
                .pattern("SSS")
                .pattern(" S ")
                .input('S', Items.STICK)
                .criterion(getHasName(Items.STICK), has(ModItems.HEART_EXTRACTOR))
                .offerTo(exporter);

        // Butcher Knife Recipe
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.BUTCHER_KNIFE)
                .pattern("I ")
                .pattern(" S")
                .input('I', Items.IRON_INGOT)
                .input('S', Items.STICK)
                .criterion(getHasName(Items.IRON_INGOT), has(ModItems.BUTCHER_KNIFE))
                .offerTo(exporter);

        // Blood Recipe
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.BLOOD_BLOCK)
                .input(ModItems.BLOOD, 4)
                .criterion(getHasName(ModItems.BLOOD), has(ModBlocks.BLOOD_BLOCK))
                .offerTo(exporter);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ModItems.BLOOD, 4)
                .input(ModBlocks.BLOOD_BLOCK)
                .criterion(getHasName(ModBlocks.BLOOD_BLOCK), has(ModItems.BLOOD))
                .offerTo(exporter);

    }
}
