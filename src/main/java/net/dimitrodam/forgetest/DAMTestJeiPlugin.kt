package net.dimitrodam.forgetest

import mezz.jei.api.IGuiHelper
import mezz.jei.api.gui.IDrawable
import mezz.jei.api.gui.IRecipeLayout
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.ingredients.VanillaTypes
import mezz.jei.api.recipe.IRecipeCategory
import mezz.jei.api.recipe.IRecipeWrapper
import net.minecraft.inventory.InventoryCrafting
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipe
import net.minecraft.item.crafting.Ingredient
import net.minecraft.util.NonNullList
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World

class FabricatorRecipeCategory(guiHelper: IGuiHelper) : IRecipeCategory<FabricatorRecipeWrapper> {
	companion object {
		const val NAME = "${DAMTest.MODID}.fabricator"
	}

	val mTitle: String = DTBlocks.fabricator.localizedName
	val mBackground: IDrawable = guiHelper.createDrawable(ResourceLocation(DAMTest.MODID, "textures/gui/jei.png"), 0, 54, 82, 26) // 0, 0, 82, 54)

	override fun getUid(): String = NAME
	override fun getModName(): String = DAMTest.MODID
	override fun getTitle(): String = mTitle
	override fun getBackground(): IDrawable = mBackground

	override fun setRecipe(recipeLayout: IRecipeLayout, recipeWrapper: FabricatorRecipeWrapper, ingredients: IIngredients) {
		val inputs = ingredients.getInputs(VanillaTypes.ITEM)
		recipeLayout.itemStacks.init(0, true, 0, 3) // 0)
		recipeLayout.itemStacks.set(0, inputs[0])
//		recipeLayout.itemStacks.init(1, true, 0, 36)
//		recipeLayout.itemStacks.set(1, inputs[1])
		recipeLayout.itemStacks.init(1, false, 60, 4) // 18)
		recipeLayout.itemStacks.set(1, inputs[1])
//		recipeLayout.itemStacks.set(ingredients)
	}
}
class ExtractorRecipeCategory(guiHelper: IGuiHelper) : IRecipeCategory<ExtractorRecipeWrapper> {
	companion object {
		const val NAME = "${DAMTest.MODID}.extractor"
	}

	val mTitle: String = DTBlocks.extractor.localizedName
	val mBackground: IDrawable = guiHelper.createDrawable(ResourceLocation(DAMTest.MODID, "textures/gui/jei.png"), 0, 54, 82, 26)

	override fun getUid(): String = NAME
	override fun getModName(): String = DAMTest.MODID
	override fun getTitle(): String = mTitle
	override fun getBackground(): IDrawable = mBackground

	override fun setRecipe(recipeLayout: IRecipeLayout, recipeWrapper: ExtractorRecipeWrapper, ingredients: IIngredients) {
		val inputs = ingredients.getInputs(VanillaTypes.ITEM)
		recipeLayout.itemStacks.init(0, true, 0, 3)
		recipeLayout.itemStacks.set(0, inputs[0])
		recipeLayout.itemStacks.init(1, false, 60, 4)
		recipeLayout.itemStacks.set(1, inputs[1])
//		recipeLayout.itemStacks.set(ingredients)
	}
}
class FabricatorRecipeWrapper(val recipe: IFabricatorRecipe) : IRecipeWrapper {
	override fun getIngredients(ingredients: IIngredients) {
		//ingredients.setInputs(VanillaTypes.ITEM, arrayListOf(ItemStack(Items.IRON_INGOT), ItemStack(Items.DIAMOND)))
		//ingredients.setOutput(VanillaTypes.ITEM, ItemStack(Items.DIAMOND))
		ingredients.setInputLists(VanillaTypes.ITEM, recipe.ingredients.map { it.matchingStacks.toList() })
		ingredients.setOutput(VanillaTypes.ITEM, recipe.recipeOutput)
	}
}
class ExtractorRecipeWrapper(val recipe: IExtractorRecipe) : IRecipeWrapper {
	override fun getIngredients(ingredients: IIngredients) {
		//ingredients.setInputs(VanillaTypes.ITEM, arrayListOf(ItemStack(Items.IRON_INGOT), ItemStack(Items.DIAMOND)))
		//ingredients.setOutput(VanillaTypes.ITEM, ItemStack(Items.DIAMOND))
		ingredients.setInputLists(VanillaTypes.ITEM, recipe.ingredients.map { it.matchingStacks.toList() })
		ingredients.setOutput(VanillaTypes.ITEM, recipe.recipeOutput)
	}
}
interface IFabricatorRecipe : IRecipe
interface IExtractorRecipe : IRecipe
class FabricatorRecipe(registryName: String, mIngredients: NonNullList<Ingredient>) : GenericRecipe(registryName, mIngredients), IFabricatorRecipe {
	constructor(registryName: String, vararg mIngredients: ItemStack) : this(registryName, NonNullList.from<Ingredient>(Ingredient.EMPTY, *(mIngredients.map { Ingredient.fromStacks(it) }.toTypedArray())))
	constructor(registryName: String, vararg mIngredients: Item) : this(registryName, *(mIngredients.map { ItemStack(it) }.toTypedArray()))
	constructor(registryName: String, vararg mIngredients: Array<out ItemStack>) : this(registryName, NonNullList.from<Ingredient>(Ingredient.EMPTY, *(mIngredients.map { Ingredient.fromStacks(*it) }.toTypedArray())))
	constructor(registryName: String, vararg mIngredients: Ingredient) : this(registryName, NonNullList.from<Ingredient>(Ingredient.EMPTY, *mIngredients))
}
class ExtractorRecipe(registryName: String, mIngredients: NonNullList<Ingredient>) : GenericRecipe(registryName, mIngredients), IExtractorRecipe {
	constructor(registryName: String, vararg mIngredients: ItemStack) : this(registryName, NonNullList.from<Ingredient>(Ingredient.EMPTY, *(mIngredients.map { Ingredient.fromStacks(it) }.toTypedArray())))
	constructor(registryName: String, vararg mIngredients: Item) : this(registryName, *(mIngredients.map { ItemStack(it) }.toTypedArray()))
	constructor(registryName: String, vararg mIngredients: Array<out ItemStack>) : this(registryName, NonNullList.from<Ingredient>(Ingredient.EMPTY, *(mIngredients.map { Ingredient.fromStacks(*it) }.toTypedArray())))
	constructor(registryName: String, vararg mIngredients: Ingredient) : this(registryName, NonNullList.from<Ingredient>(Ingredient.EMPTY, *mIngredients))
}
open class GenericRecipe(registryName: String, val mIngredients: NonNullList<Ingredient>) : IRecipe {
	var mRegistryName: ResourceLocation? = ResourceLocation(DAMTest.MODID, registryName)

	constructor(registryName: String, vararg mIngredients: ItemStack) : this(registryName, NonNullList.from<Ingredient>(Ingredient.EMPTY, *(mIngredients.map { Ingredient.fromStacks(it) }.toTypedArray())))
	constructor(registryName: String, vararg mIngredients: Item) : this(registryName, *(mIngredients.map { ItemStack(it) }.toTypedArray()))
	constructor(registryName: String, vararg mIngredients: Array<out ItemStack>) : this(registryName, NonNullList.from<Ingredient>(Ingredient.EMPTY, *(mIngredients.map { Ingredient.fromStacks(*it) }.toTypedArray())))
	constructor(registryName: String, vararg mIngredients: Ingredient) : this(registryName, NonNullList.from<Ingredient>(Ingredient.EMPTY, *mIngredients))

	override fun canFit(p0: Int, p1: Int): Boolean = true
	override fun getRegistryType(): Class<IRecipe>? = null
	override fun getRegistryName(): ResourceLocation? = mRegistryName
	override fun getCraftingResult(p0: InventoryCrafting): ItemStack = ItemStack.EMPTY
	override fun setRegistryName(p0: ResourceLocation?): IRecipe {
		mRegistryName = p0
		return this
	}
	override fun matches(p0: InventoryCrafting, p1: World): Boolean = true

	override fun getRecipeOutput(): ItemStack = ItemStack.EMPTY // null
	override fun getIngredients(): NonNullList<Ingredient> = mIngredients
}