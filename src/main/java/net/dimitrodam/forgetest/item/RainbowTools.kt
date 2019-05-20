package net.dimitrodam.forgetest.item

import net.dimitrodam.forgetest.DAMTest
import net.dimitrodam.forgetest.util.DTItem.Companion.initWoTab
import net.dimitrodam.forgetest.util.DTItemAxe
import net.dimitrodam.forgetest.util.DTItemPickaxe
import net.dimitrodam.forgetest.util.DTItemShovel
import net.minecraft.entity.EntityLivingBase
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.ItemArmor
import net.minecraft.item.ItemHoe
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemSword
import net.minecraft.potion.Potion
import net.minecraft.potion.PotionEffect

class ItemRainbowSword : ItemSword(DAMTest.TOOL_MATERIAL_RAINBOW) {
	init {
		initWoTab("rainbow_sword")
	}

	override fun hitEntity(stack: ItemStack, target: EntityLivingBase, attacker: EntityLivingBase): Boolean {
		target.addPotionEffect(PotionEffect(Potion.getPotionById(19)!!, 60, 1))
		return super.hitEntity(stack, target, attacker)
	}
}

class ItemRainbowPickaxe : DTItemPickaxe("rainbow_pickaxe", DAMTest.TOOL_MATERIAL_RAINBOW)
class ItemRainbowAxe : DTItemAxe("rainbow_axe", DAMTest.TOOL_MATERIAL_RAINBOW, 12.0F, -3.0F)
class ItemRainbowShovel : DTItemShovel("rainbow_shovel", DAMTest.TOOL_MATERIAL_RAINBOW)
class ItemRainbowHoe : ItemHoe(DAMTest.TOOL_MATERIAL_RAINBOW) {
	init { initWoTab("rainbow_hoe") }
}

class ItemRainbowArmor(registryName: String, renderIndexIn: Int, equipmentSlotIn: EntityEquipmentSlot) : ItemArmor(DAMTest.ARMOR_MATERIAL_RAINBOW, renderIndexIn, equipmentSlotIn) {
	init { initWoTab(registryName) }
}