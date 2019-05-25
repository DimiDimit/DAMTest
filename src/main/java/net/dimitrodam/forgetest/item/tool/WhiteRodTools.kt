package net.dimitrodam.forgetest.item.tool

import net.dimitrodam.forgetest.DAMTest
import net.dimitrodam.forgetest.util.DTItem.Companion.initWoTab
import net.dimitrodam.forgetest.util.DTItemAxe
import net.dimitrodam.forgetest.util.DTItemPickaxe
import net.dimitrodam.forgetest.util.DTItemShovel
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.ItemArmor
import net.minecraft.item.ItemHoe
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemSword
import net.minecraft.util.EntityDamageSource

class ItemWhiteRodSword : ItemSword(DAMTest.TOOL_MATERIAL_WHITE_ROD) {
	init {
		initWoTab("white_rod_sword")
	}

	override fun hitEntity(stack: ItemStack, target: EntityLivingBase, attacker: EntityLivingBase): Boolean {
		target.attackEntityFrom(if(attacker is EntityPlayer) EntityDamageSource.causePlayerDamage(attacker) else EntityDamageSource.causeMobDamage(attacker), 3.4028235E38F)
		return super.hitEntity(stack, target, attacker)
	}
}

class ItemWhiteRodPickaxe : DTItemPickaxe("white_rod_pickaxe", DAMTest.TOOL_MATERIAL_WHITE_ROD)
class ItemWhiteRodAxe : DTItemAxe("white_rod_axe", DAMTest.TOOL_MATERIAL_WHITE_ROD, 12.0F, -3.0F)
class ItemWhiteRodShovel : DTItemShovel("white_rod_shovel", DAMTest.TOOL_MATERIAL_WHITE_ROD)
class ItemWhiteRodHoe : ItemHoe(DAMTest.TOOL_MATERIAL_WHITE_ROD) {
	init { initWoTab("white_rod_hoe") }
}

class ItemWhiteRodArmor(registryName: String, renderIndexIn: Int, equipmentSlotIn: EntityEquipmentSlot) : ItemArmor(DAMTest.ARMOR_MATERIAL_WHITE_ROD, renderIndexIn, equipmentSlotIn) {
	init { initWoTab(registryName) }
}