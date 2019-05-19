package net.dimitrodam.forgetest.util

import net.dimitrodam.forgetest.DAMTest
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item

abstract class DTItem(registryName: String, tab: CreativeTabs = DAMTest.TAB_DT) : Item() {
	companion object {
		fun Item.initWoTab(registryName: String) {
			unlocalizedName = "${DAMTest.MODID}.$registryName"
			setRegistryName(registryName)
		}

		fun Item.init(registryName: String, tab: CreativeTabs = DAMTest.TAB_DT) {
			initWoTab(registryName)
			creativeTab = tab
		}
	}

	init {
		init(registryName, tab)
	}
}