package net.dimitrodam.forgetest.guicontainer

import net.dimitrodam.forgetest.container.ContainerExtractor
import net.dimitrodam.forgetest.tile.TileExtractor
import net.dimitrodam.forgetest.util.DTGuiContainer
import net.minecraft.inventory.IInventory

class GuiContainerExtractor(container: ContainerExtractor, tileEntity: TileExtractor, playerInventory: IInventory) :
		DTGuiContainer(container, tileEntity, playerInventory, "extractor.png", 176, 166)