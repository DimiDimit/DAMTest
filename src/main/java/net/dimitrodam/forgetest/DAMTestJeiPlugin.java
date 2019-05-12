package net.dimitrodam.forgetest;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.dimitrodam.forgetest.guicontainer.GuiContainerExtractor;
import net.dimitrodam.forgetest.guicontainer.GuiContainerFabricator;
import net.dimitrodam.forgetest.tile.TileExtractor;
import net.dimitrodam.forgetest.tile.TileFabricator;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class DAMTestJeiPlugin implements IModPlugin {
    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
        registry.addRecipeCategories(new ExtractorRecipeCategory(guiHelper), new FabricatorRecipeCategory(guiHelper));
    }
    @Override
    public void register(IModRegistry registry) {
        TileFabricator.Companion.initRecipes();

        registry.handleRecipes(IExtractorRecipe.class, ExtractorRecipeWrapper::new, ExtractorRecipeCategory.NAME);
        registry.handleRecipes(IFabricatorRecipe.class, FabricatorRecipeWrapper::new, FabricatorRecipeCategory.NAME);
        registry.addRecipeCatalyst(new ItemStack(DTBlocks.extractor), ExtractorRecipeCategory.NAME);
        registry.addRecipeCatalyst(new ItemStack(DTBlocks.fabricator), FabricatorRecipeCategory.NAME);

        registry.addRecipes(TileExtractor.Companion.getRECIPES(), ExtractorRecipeCategory.NAME);
        registry.addRecipes(TileFabricator.Companion.getRECIPES(), FabricatorRecipeCategory.NAME);

        registry.addRecipeClickArea(GuiContainerExtractor.class, 78, 32, 28, 23, ExtractorRecipeCategory.NAME);
        registry.addRecipeClickArea(GuiContainerFabricator.class, 78, 32, 28, 23, FabricatorRecipeCategory.NAME);

        registry.addIngredientInfo(new ItemStack(DTItems.pack), VanillaTypes.ITEM, "item.damtest.pack.info");
    }
}