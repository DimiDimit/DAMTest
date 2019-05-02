package net.dimitrodam.forgetest.util

import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.items.ItemStackHandler

open class RestrictedItemStackHandler(val source: ItemStackHandler,
                                      val noInputSlots: IntArray = intArrayOf(),
                                      val noOutputSlots: IntArray = intArrayOf()) : ItemStackHandler(source.slots) {
	override fun getStackInSlot(slot: Int): ItemStack {
		return source.getStackInSlot(slot)
	}

	override fun deserializeNBT(nbt: NBTTagCompound?) {
		return source.deserializeNBT(nbt)
	}

	override fun getSlotLimit(slot: Int): Int {
		return source.getSlotLimit(slot)
	}

	override fun setStackInSlot(slot: Int, stack: ItemStack) {
		source.setStackInSlot(slot, stack)
	}

	override fun serializeNBT(): NBTTagCompound {
		return source.serializeNBT()
	}

	override fun setSize(size: Int) {
		source.setSize(size)
	}

	override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
		if(slot in noInputSlots) return stack
		return source.insertItem(slot, stack, simulate)
	}

	override fun isItemValid(slot: Int, stack: ItemStack): Boolean {
		return source.isItemValid(slot, stack)
	}

	override fun getSlots(): Int {
		return source.slots
	}

	override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack {
		if(slot in noOutputSlots) return ItemStack.EMPTY
		return source.extractItem(slot, amount, simulate)
	}
}