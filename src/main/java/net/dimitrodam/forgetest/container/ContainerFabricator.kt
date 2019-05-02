package net.dimitrodam.forgetest.container

import net.dimitrodam.forgetest.tile.TileFabricator
import net.dimitrodam.forgetest.util.DTContainer
import net.minecraft.inventory.IInventory
import net.minecraftforge.items.SlotItemHandler

class ContainerFabricator(playerInventory: IInventory, te: TileFabricator) : DTContainer(playerInventory, te, TileFabricator.SIZE) {
	init {
		addSlotToContainer(SlotItemHandler(te.playerItemStackHandler, 0, 56, 17))
		addSlotToContainer(SlotItemHandler(te.playerItemStackHandler, 1, 56, 53))
		addSlotToContainer(SlotItemHandler(te.playerItemStackHandler, 2, 116, 35))
		addPlayerSlots(playerInventory, 8, 84)
	}
}