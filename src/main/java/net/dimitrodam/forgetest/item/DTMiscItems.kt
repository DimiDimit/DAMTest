package net.dimitrodam.forgetest.item

import net.dimitrodam.forgetest.util.DTItem
import net.dimitrodam.forgetest.util.DTItem.Companion.addInformation
import net.dimitrodam.forgetest.util.DTItem.Companion.initWoTab
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.init.MobEffects
import net.minecraft.item.ItemFood
import net.minecraft.item.ItemStack
import net.minecraft.potion.PotionEffect
import net.minecraft.world.World

class ItemMatter : DTItem("matter")

class ItemRainbowIngot : DTItem("rainbow_ingot")
class ItemRainbowNugget : DTItem("rainbow_nugget")

class ItemWhiteRod : DTItem("white_rod")
class ItemWhiteRodDust : DTItem("white_rod_dust")

class ItemIceShard : DTItem("ice_shard") {
	override fun addInformation(stack: ItemStack, worldIn: World?, tooltip: MutableList<String>, flagIn: ITooltipFlag) {
		addInformation(stack, tooltip)
	}
}

class ItemRainbowPorkchop : ItemFood(3, 0.3f, true) {
	init {
		initWoTab("rainbow_porkchop")
		setPotionEffect(PotionEffect(MobEffects.POISON, 60), 1.0f)
	}

	override fun addInformation(stack: ItemStack, worldIn: World?, tooltip: MutableList<String>, flagIn: ITooltipFlag) {
		addInformation(stack, tooltip)
	}
}
class ItemCookedRainbowPorkchop : ItemFood(8, 0.8f, true) {
	init { initWoTab("cooked_rainbow_porkchop") }
}

class ItemBacon : ItemFood(10, 1.0f, true) {
	init { initWoTab("bacon") }
}
class ItemRainbowBacon : ItemFood(16, 1.6f, true) {
	init { initWoTab("rainbow_bacon") }
}

class ItemEmptyPotionRing : DTItem("empty_potion_ring") {
	override fun addInformation(stack: ItemStack, worldIn: World?, tooltip: MutableList<String>, flagIn: ITooltipFlag) {
		addInformation(stack, tooltip)
	}
}