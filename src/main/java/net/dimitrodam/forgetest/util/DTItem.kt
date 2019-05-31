package net.dimitrodam.forgetest.util

import net.dimitrodam.forgetest.DAMTest
import net.minecraft.client.resources.I18n
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraft.item.ItemStack

abstract class DTItem(registryName: String, tab: CreativeTabs = DAMTest.TAB_DT) : Item() {
	companion object {
		fun getUnlocalizedFromRegistryName(registryName: String): String {
			return "${DAMTest.MODID}.$registryName"
		}

		fun Item.initWoTab(registryName: String) {
			unlocalizedName = getUnlocalizedFromRegistryName(registryName)
			setRegistryName(registryName)
		}

		fun Item.init(registryName: String, tab: CreativeTabs = DAMTest.TAB_DT) {
			initWoTab(registryName)
			creativeTab = tab
		}

		fun addInformation(tooltip: MutableList<String>, unlocalizedName: String) {
			tooltip.add(I18n.format("$unlocalizedName.lore"))
		}
		fun addInformationWithRegistryName(tooltip: MutableList<String>, registryName: String) {
			addInformation(tooltip, getUnlocalizedFromRegistryName(registryName))
		}
		fun Item.addInformation(stack: ItemStack, tooltip: MutableList<String>) {
			val rn = registryName
			if(rn != null)
				addInformation(tooltip, getUnlocalizedName(stack))
		}
	}

	init {
		init(registryName, tab)
	}
}