package net.dimitrodam.forgetest.util

import net.dimitrodam.forgetest.DAMTest
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.creativetab.CreativeTabs

abstract class DTBlock(material: Material, registryName: String, hardness: Float = 1.5f, tab: CreativeTabs = DAMTest.TAB_DT) : Block(material) {
	init {
		unlocalizedName = "${DAMTest.MODID}.$registryName"
		setRegistryName(registryName)
		setHardness(hardness)
		setCreativeTab(tab)
	}
}