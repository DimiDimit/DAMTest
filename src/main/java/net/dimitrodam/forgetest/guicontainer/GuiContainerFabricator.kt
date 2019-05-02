package net.dimitrodam.forgetest.guicontainer

import net.dimitrodam.forgetest.container.ContainerFabricator
import net.dimitrodam.forgetest.tile.TileFabricator
import net.dimitrodam.forgetest.util.DTGuiContainer
import net.minecraft.inventory.IInventory

class GuiContainerFabricator(container: ContainerFabricator, tileEntity: TileFabricator, playerInventory: IInventory) :
		DTGuiContainer(container, tileEntity, playerInventory, "fabricator.png", 176, 166)