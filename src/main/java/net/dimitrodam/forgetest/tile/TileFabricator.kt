package net.dimitrodam.forgetest.tile

import net.dimitrodam.forgetest.DTItems
import net.dimitrodam.forgetest.FabricatorRecipe
import net.dimitrodam.forgetest.util.RestrictedItemStackHandler
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipe
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.ItemStackHandler


class TileFabricator : TileEntity() {
	companion object {
		const val SIZE = 3

		val RECIPES: Array<IRecipe> = arrayOf(
				FabricatorRecipe("fabricator_coal", ItemStack(Items.COAL), ItemStack(DTItems.matter, 1)),
				FabricatorRecipe("fabricator_charcoal", ItemStack(Items.COAL, 1, 1), ItemStack(DTItems.matter, 1)),
				FabricatorRecipe("fabricator_redstone", ItemStack(Items.REDSTONE), ItemStack(DTItems.matter, 2)),
				FabricatorRecipe("fabricator_lapis", ItemStack(Items.DYE, 1, 4), ItemStack(DTItems.matter, 2)),
				FabricatorRecipe("fabricator_iron", ItemStack(Items.IRON_INGOT), ItemStack(DTItems.matter, 3)),
				FabricatorRecipe("fabricator_gold", ItemStack(Items.GOLD_INGOT), ItemStack(DTItems.matter, 5)),
				FabricatorRecipe("fabricator_diamond", ItemStack(Items.DIAMOND), ItemStack(DTItems.matter, 15)),
				FabricatorRecipe("fabricator_emerald", ItemStack(Items.EMERALD), ItemStack(DTItems.matter, 20))
		)
	}

	private val baseItemStackHandler = object : ItemStackHandler(SIZE) {
		override fun onContentsChanged(slot: Int) {
			if(!stacks[1].isEmpty) {
				var required: Int? = null
				for(recipe in RECIPES) {
					if(stacks[1].item == recipe.recipeOutput.item && stacks[1].metadata == recipe.recipeOutput.metadata) {
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
					if(stack.item == recipe.recipeOutput.item && stack.metadata == recipe.recipeOutput.metadata) {
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