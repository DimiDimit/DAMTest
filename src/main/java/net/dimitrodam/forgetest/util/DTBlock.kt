package net.dimitrodam.forgetest.util

import net.dimitrodam.forgetest.DAMTest
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.creativetab.CreativeTabs

abstract class DTBlock(material: Material, registryName: String, hardness: Float = 1.5f, tab: CreativeTabs = DAMTest.TAB_DT) : Block(material) {
	companion object {
		fun Block.initMin(registryName: String) {
			unlocalizedName = "${DAMTest.MODID}.$registryName"
			setRegistryName(registryName)
		}

		fun Block.initWoTab(registryName: String, hardness: Float = 1.5f) {
			initMin(registryName)
			setHardness(hardness)
		}

		fun Block.init(registryName: String, hardness: Float = 1.5f, tab: CreativeTabs = DAMTest.TAB_DT) {
			initWoTab(registryName, hardness)
			setCreativeTab(tab)
		}
	}

	init {
		init(registryName, hardness, tab)
	}
}