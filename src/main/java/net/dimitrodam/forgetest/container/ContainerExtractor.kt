package net.dimitrodam.forgetest.container

import net.dimitrodam.forgetest.tile.TileExtractor
import net.dimitrodam.forgetest.util.DTContainer
import net.minecraft.inventory.IInventory
import net.minecraftforge.items.SlotItemHandler

class ContainerExtractor(playerInventory: IInventory, te: TileExtractor) : DTContainer(playerInventory, te, TileExtractor.SIZE) {
	init {
		addSlotToContainer(SlotItemHandler(te.playerItemStackHandler, 0, 56, 34))
		addSlotToContainer(SlotItemHandler(te.playerItemStackHandler, 1, 116, 35))
		addPlayerSlots(playerInventory, 8, 84)
	}
}