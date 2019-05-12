package net.dimitrodam.forgetest.util

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.Container
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.Slot
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.items.IItemHandler
import net.minecraftforge.items.SlotItemHandler


abstract class DTContainer(@Suppress("UNUSED_PARAMETER") playerInventory: IInventory, val te: TileEntity, val size: Int) : Container() {
	override fun canInteractWith(playerIn: EntityPlayer): Boolean = true

	override fun transferStackInSlot(playerIn: EntityPlayer, index: Int): ItemStack {
		var itemstack = ItemStack.EMPTY
		val slot = inventorySlots[index]

		if(slot != null && slot.hasStack) {
			val itemstack1 = slot.stack
			itemstack = itemstack1.copy()

			if(index < size) {
				if(!this.mergeItemStack(itemstack1, size, this.inventorySlots.size, true)) return ItemStack.EMPTY
			} else if(!this.mergeItemStack(itemstack1, 0, size, false)) return ItemStack.EMPTY

			if(itemstack1.isEmpty) slot.putStack(ItemStack.EMPTY) else slot.onSlotChanged()
			te.markDirty()
		}

		return itemstack
	}

	fun addSlots(itemHandler: IItemHandler, width: Int, height: Int, offsetX: Int = 0, offsetY: Int = 0, offsetIndex: Int = 0, slotWidth: Int = 18, slotHeight: Int = 18) {
		for(row in 0 until height) {
			for(col in 0 until width) {
				addSlotToContainer(SlotItemHandler(itemHandler, col + row * width + offsetIndex, offsetX + col * slotWidth, row * slotHeight + offsetY))
			}
		}
	}
	fun addPlayerSlots(playerInventory: IInventory, offsetX: Int = 8, offsetY: Int = 0) {
		// Main inventory
		for(row in 0 until 3) {
			for(col in 0 until 9) {
				val x = offsetX + col * 18
				val y = row * 18 + offsetY
				addSlotToContainer(Slot(playerInventory, col + row * 9 + 9, x, y))
			}
		}

		// Hotbar
		for(row in 0 until 9) {
			val x = offsetX + row * 18
			val y = 58 + offsetY
			addSlotToContainer(Slot(playerInventory, row, x, y))
		}
	}
}