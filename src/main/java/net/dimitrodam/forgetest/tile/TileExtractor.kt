package net.dimitrodam.forgetest.tile

import net.dimitrodam.forgetest.DTItems
import net.dimitrodam.forgetest.IExtractorRecipe
import net.dimitrodam.forgetest.util.RestrictedItemStackHandler
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.ItemStackHandler


class TileExtractor : TileEntity() {
	companion object {
		const val SIZE = 2

		/*val RECIPES: Array<IRecipe> = arrayOf(
				ExtractorRecipe("extractor_coal", ItemStack(DTItems.matter, 1), arrayOf(ItemStack(Items.COAL), ItemStack(Items.COAL, 1, 1))),
				ExtractorRecipe("extractor_coal_block", ItemStack(DTItems.matter, 1*9), ItemStack(Blocks.COAL_BLOCK)),
				ExtractorRecipe("extractor_redstone", ItemStack(DTItems.matter, 2), ItemStack(Items.REDSTONE)),
				ExtractorRecipe("extractor_redstone_block", ItemStack(DTItems.matter, 2*9), ItemStack(Blocks.REDSTONE_BLOCK)),
				ExtractorRecipe("extractor_lapis", ItemStack(DTItems.matter, 2), ItemStack(Items.DYE, 1, 4)),
				ExtractorRecipe("extractor_lapis_block", ItemStack(DTItems.matter, 2*9), ItemStack(Blocks.LAPIS_BLOCK)),
				ExtractorRecipe("extractor_iron", ItemStack(DTItems.matter, 3), ItemStack(Items.IRON_INGOT)),
				ExtractorRecipe("extractor_iron_block", ItemStack(DTItems.matter, 3*9), ItemStack(Blocks.IRON_BLOCK)),
				ExtractorRecipe("extractor_copper", ItemStack(DTItems.matter, 4), OreIngredient("ingotCopper")),
				ExtractorRecipe("extractor_gold", ItemStack(DTItems.matter, 5), ItemStack(Items.GOLD_INGOT)),
				ExtractorRecipe("extractor_gold_block", ItemStack(DTItems.matter, 5*9), ItemStack(Blocks.GOLD_BLOCK)),
				ExtractorRecipe("extractor_diamond", ItemStack(DTItems.matter, 15), ItemStack(Items.DIAMOND)),
				ExtractorRecipe("extractor_diamond_block", ItemStack(DTItems.matter, 15*9), ItemStack(Blocks.DIAMOND_BLOCK)),
				ExtractorRecipe("extractor_emerald", ItemStack(DTItems.matter, 20), ItemStack(Items.EMERALD)),
				ExtractorRecipe("extractor_emerald_block", ItemStack(DTItems.matter, 20*9), ItemStack(Blocks.EMERALD_BLOCK))
		)*/
		val RECIPES = mutableListOf<IExtractorRecipe>()
	}

	private val baseItemStackHandler = object : ItemStackHandler(SIZE) {
		override fun onContentsChanged(slot: Int) {
			if(!stacks[0].isEmpty) {
				var given: Int? = null
				for(recipe in RECIPES) {
					val index = recipe.ingredients[0].matchingStacks.find { stacks[0].item == it.item && stacks[0].metadata == it.metadata }
					if(index != null) {
						given = recipe.ingredients[1].matchingStacks[0].count
					}
				}
				if(given != null) {
					while(!stacks[0].isEmpty && (stacks[1].isEmpty || stacks[1].count <= stacks[1].maxStackSize - given)) {
						stacks[0].shrink(1)
						if(stacks[1].isEmpty) {
							val ns = ItemStack(DTItems.matter, given)
							stacks[1] = ns
						} else
							stacks[1].grow(given)
					}
				}
			}
			if(stacks[0] != ItemStack.EMPTY && stacks[0].isEmpty)
				stacks[0] = ItemStack.EMPTY
			if(stacks[1] != ItemStack.EMPTY && stacks[1].isEmpty)
				stacks[1] = ItemStack.EMPTY

			this@TileExtractor.markDirty()
		}

		override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
			if(slot == 0) {
				if(stack.item == DTItems.matter)
					return stack
				var matches = false
				for(recipe in RECIPES) {
					if(recipe.ingredients[0].matchingStacks.find { stack.item == it.item && stack.metadata == it.metadata } != null) {
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
	val playerItemStackHandler = RestrictedItemStackHandler(baseItemStackHandler, noInputSlots = intArrayOf(1))
	private val autoItemStackHandler = RestrictedItemStackHandler(baseItemStackHandler, noInputSlots = intArrayOf(1), noOutputSlots = intArrayOf(0))

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