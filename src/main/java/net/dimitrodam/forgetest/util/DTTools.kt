package net.dimitrodam.forgetest.util

import net.dimitrodam.forgetest.util.DTItem.Companion.initWoTab
import net.minecraft.block.state.IBlockState
import net.minecraft.item.*

object DTItemTool {
	fun canHarvestBlock(toolClasses: Set<String>, toolMaterial: Item.ToolMaterial, blockIn: IBlockState): Boolean =
			(blockIn.block.getHarvestTool(blockIn) == null || toolClasses.contains(blockIn.block.getHarvestTool(blockIn)!!)) && toolMaterial.harvestLevel >= blockIn.block.getHarvestLevel(blockIn)
	fun getDestroySpeed(toolClasses: Set<String>, toolMaterial: Item.ToolMaterial, state: IBlockState): Float = if(canHarvestBlock(toolClasses, toolMaterial, state)) toolMaterial.efficiency else 1.0F
}

open class DTItemPickaxe(registryName: String, material: ToolMaterial) : ItemPickaxe(material) {
	init { initWoTab(registryName) }
	override fun canHarvestBlock(blockIn: IBlockState, stack: ItemStack): Boolean = DTItemTool.canHarvestBlock(getToolClasses(stack), toolMaterial, blockIn)
	override fun getDestroySpeed(stack: ItemStack, state: IBlockState): Float = DTItemTool.getDestroySpeed(getToolClasses(stack), toolMaterial, state)
}
open class DTItemAxe(registryName: String, material: ToolMaterial, damage: Float, speed: Float) : ItemAxe(material, damage, speed) {
	init { initWoTab(registryName) }
	override fun canHarvestBlock(blockIn: IBlockState, stack: ItemStack): Boolean = DTItemTool.canHarvestBlock(getToolClasses(stack), toolMaterial, blockIn)
	override fun getDestroySpeed(stack: ItemStack, state: IBlockState): Float = DTItemTool.getDestroySpeed(getToolClasses(stack), toolMaterial, state)
}
open class DTItemShovel(registryName: String, material: ToolMaterial) : ItemSpade(material) {
	init { initWoTab(registryName) }
	override fun canHarvestBlock(blockIn: IBlockState, stack: ItemStack): Boolean = DTItemTool.canHarvestBlock(getToolClasses(stack), toolMaterial, blockIn)
	override fun getDestroySpeed(stack: ItemStack, state: IBlockState): Float = DTItemTool.getDestroySpeed(getToolClasses(stack), toolMaterial, state)
}