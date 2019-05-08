package net.dimitrodam.forgetest.tile

import net.dimitrodam.forgetest.*
import net.dimitrodam.forgetest.util.RestrictedItemStackHandler
import net.dimitrodam.forgetest.util.isWhole
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.ItemStackHandler
import net.minecraftforge.oredict.OreIngredient
import kotlin.math.roundToInt


class TileFabricator : TileEntity() {
	companion object {
		const val SIZE = 3

		/*val RECIPES: Array<IRecipe> = arrayOf(
				FabricatorRecipe("fabricator_coal", ItemStack(Items.COAL), ItemStack(DTItems.matter, 1)),
				FabricatorRecipe("fabricator_charcoal", ItemStack(Items.COAL, 1, 1), ItemStack(DTItems.matter, 1)),
				FabricatorRecipe("fabricator_coal_block", ItemStack(Blocks.COAL_BLOCK), ItemStack(DTItems.matter, 1*9)),
				FabricatorRecipe("fabricator_redstone", ItemStack(Items.REDSTONE), ItemStack(DTItems.matter, 2)),
				FabricatorRecipe("fabricator_redstone_block", ItemStack(Blocks.REDSTONE_BLOCK), ItemStack(DTItems.matter, 2*9)),
				FabricatorRecipe("fabricator_lapis", ItemStack(Items.DYE, 1, 4), ItemStack(DTItems.matter, 2)),
				FabricatorRecipe("fabricator_lapis_block", ItemStack(Blocks.LAPIS_BLOCK), ItemStack(DTItems.matter, 2*9)),
				FabricatorRecipe("fabricator_iron", ItemStack(Items.IRON_INGOT), ItemStack(DTItems.matter, 3)),
				FabricatorRecipe("fabricator_iron_block", ItemStack(Blocks.IRON_BLOCK), ItemStack(DTItems.matter, 3*9)),
				FabricatorRecipe("fabricator_gold", ItemStack(Items.GOLD_INGOT), ItemStack(DTItems.matter, 5)),
				FabricatorRecipe("fabricator_gold_block", ItemStack(Blocks.GOLD_BLOCK), ItemStack(DTItems.matter, 5*9)),
				FabricatorRecipe("fabricator_diamond", ItemStack(Items.DIAMOND), ItemStack(DTItems.matter, 15)),
				FabricatorRecipe("fabricator_diamond_block", ItemStack(Blocks.DIAMOND_BLOCK), ItemStack(DTItems.matter, 15*9)),
				FabricatorRecipe("fabricator_emerald", ItemStack(Items.EMERALD), ItemStack(DTItems.matter, 20)),
				FabricatorRecipe("fabricator_emerald_block", ItemStack(Blocks.EMERALD_BLOCK), ItemStack(DTItems.matter, 20*9))
		)*/
		val RECIPES = mutableListOf<IFabricatorRecipe>()
		fun addRecipes(name: String, value: Int, prefixes: Map<String, Float>) {
			val nameU = name.capitalize()
			val nameL = name.toLowerCase()
			for(p in prefixes) {
				val v = value * p.value
				if(!v.isWhole() || v < 1 || v > 64) continue
				val vi = v.roundToInt()
				val rn = p.key + (if(p.key != "") nameU else nameL)
				val oi = OreIngredient(rn)
				if(oi.matchingStacks.isEmpty())
					continue
				RECIPES.add(FabricatorRecipe("fabricator_$rn", Ingredient.fromStacks(ItemStack(DTItems.matter, vi)), oi))
				TileExtractor.RECIPES.add(ExtractorRecipe("extractor_$rn", oi, Ingredient.fromStacks(ItemStack(DTItems.matter, vi))))
			}
		}
		fun registerRecipeStrings(recipes: Array<String>, prefixes: Map<String, Float>) {
			for(r in recipes) {
				val l = r.split('=', limit = 2)
				if(l.size < 2)
					throw MalformedConfigOptionException(r, "Must be of format <name>=<value>!")
				val i = l[1].toInt()
				addRecipes(l[0], i, prefixes)
			}
		}
		var HAS_INIT_RECIPES: Boolean = false
		fun initRecipes() {
			if(HAS_INIT_RECIPES)
				return
			registerRecipeStrings(DTConfig.matterConfig.rawOredict, mapOf(Pair("", 1f)))
			registerRecipeStrings(DTConfig.matterConfig.rbOredict, mapOf(Pair("", 1f), Pair("block", 9f)))
			registerRecipeStrings(DTConfig.matterConfig.ndiggbOredict, mapOf(Pair("nugget", 1f/9f), Pair("dust", 1f), Pair("ingot", 1f), Pair("gem", 1f), Pair("gear", 4f), Pair("block", 9f)))
			HAS_INIT_RECIPES = true
		}
	}

	private val baseItemStackHandler = object : ItemStackHandler(SIZE) {
		override fun onContentsChanged(slot: Int) {
			if(!stacks[1].isEmpty) {
				var required: Int? = null
				for(recipe in RECIPES) {
					if(recipe.ingredients[1].matchingStacks.find { it.item == stacks[1].item && it.metadata == stacks[1].metadata } != null) {
						required = recipe.ingredients[0].matchingStacks.find { it.item == DTItems.matter }?.count
					}
				}
				if(required != null) {
					while((!stacks[0].isEmpty && stacks[0].count >= required) && !stacks[1].isEmpty && (stacks[2].isEmpty || (stacks[2].count < stacks[2].maxStackSize && stacks[2].item == stacks[1].item))) {
						stacks[0].shrink(required)
						if(stacks[2].isEmpty) {
							val ns = stacks[1].copy()
							ns.count = 1
							stacks[2] = ns
						} else
							stacks[2].grow(1)
					}
				}
			}
			if(stacks[0] != ItemStack.EMPTY && stacks[0].isEmpty)
				stacks[0] = ItemStack.EMPTY
			if(stacks[2] != ItemStack.EMPTY && stacks[2].isEmpty)
				stacks[2] = ItemStack.EMPTY

			this@TileFabricator.markDirty()
		}

		override fun getSlotLimit(slot: Int): Int {
			if(slot == 1)
				return 1
			return super.getSlotLimit(slot)
		}

		override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
			if((slot == 0 && stack.item != DTItems.matter))
				return stack
			if(slot == 1) {
				if(stack.item == DTItems.matter)
					return stack
				var matches = false
				for(recipe in RECIPES) {
					if(recipe.ingredients[1].matchingStacks.find { it.item == stack.item && it.metadata == stack.metadata } != null) {
						matches = true
						break
					}
				}
				if(!matches)
					return stack
			}
			return super.insertItem(slot, stack, simulate)
		}
	}
	val playerItemStackHandler = RestrictedItemStackHandler(baseItemStackHandler, noInputSlots = intArrayOf(2))
	private val autoItemStackHandler = object : RestrictedItemStackHandler(baseItemStackHandler, noInputSlots = intArrayOf(2)) {
		override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack {
			if(slot == 1 && !source.getStackInSlot(2).isEmpty)
				return ItemStack.EMPTY
			return super.extractItem(slot, amount, simulate)
		}
	}

	override fun readFromNBT(compound: NBTTagCompound) {
		super.readFromNBT(compound)
		if(compound.hasKey("items")) baseItemStackHandler.deserializeNBT(compound.getTag("items") as NBTTagCompound)
	}
	override fun writeToNBT(compound: NBTTagCompound): NBTTagCompound {
		super.writeToNBT(compound)
		compound.setTag("items", baseItemStackHandler.serializeNBT())
		return compound
	}

	override fun hasCapability(capability: Capability<*>, facing: EnumFacing?): Boolean {
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return true
		return super.hasCapability(capability, facing)
	}
	override fun <T : Any?> getCapability(capability: Capability<T>, facing: EnumFacing?): T? {
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(autoItemStackHandler)
		return super.getCapability(capability, facing)
	}
}