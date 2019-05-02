package net.dimitrodam.forgetest.util

import net.dimitrodam.forgetest.DAMTest
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item

abstract class DTItem(registryName: String, tab: CreativeTabs = DAMTest.TAB_DT) : Item() {
	init {
		unlocalizedName = "${DAMTest.MODID}.$registryName"
		setRegistryName(registryName)
		setCreativeTab(tab)
	}
}