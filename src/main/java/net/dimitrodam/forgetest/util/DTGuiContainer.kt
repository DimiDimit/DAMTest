package net.dimitrodam.forgetest.util

import net.dimitrodam.forgetest.DAMTest
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.inventory.Container
import net.minecraft.inventory.IInventory
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ResourceLocation

abstract class DTGuiContainer(container: Container, val tileEntity: TileEntity, val playerInventory: IInventory, val background: ResourceLocation, width: Int, height: Int) : GuiContainer(container) {
	init {
		xSize = width
		ySize = height
	}
	constructor(container: Container, tileEntity: TileEntity, playerInventory: IInventory, background: String, width: Int, height: Int) :
			this(container, tileEntity, playerInventory, ResourceLocation(DAMTest.MODID, "textures/gui/$background"), width, height)

	override fun drawGuiContainerBackgroundLayer(partialTicks: Float, mouseX: Int, mouseY: Int) {
		mc.textureManager.bindTexture(background)
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize)
	}
	override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
		super.drawScreen(mouseX, mouseY, partialTicks)
		renderHoveredToolTip(mouseX, mouseY)
	}
	override fun drawGuiContainerForegroundLayer(mouseX: Int, mouseY: Int) {
		fontRenderer.drawString(tileEntity.blockType.localizedName, 8, 6, 4210752)
		fontRenderer.drawString(playerInventory.displayName.unformattedText, 8, ySize - 96 + 2, 4210752)
	}
}