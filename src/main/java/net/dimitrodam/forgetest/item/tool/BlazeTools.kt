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
import net.minecraft.potion.Potion
import net.minecraft.potion.PotionEffect
import net.minecraft.world.World

class ItemBlazeSword : ItemSword(DAMTest.TOOL_MATERIAL_BLAZE) {
	companion object {
		fun hitEntity(target: EntityLivingBase) {
			target.setFire(10)
		}
	}

	init {
		initWoTab("blaze_sword")
	}

	override fun hitEntity(stack: ItemStack, target: EntityLivingBase, attacker: EntityLivingBase): Boolean {
		hitEntity(target)
		return super.hitEntity(stack, target, attacker)
	}
}

class ItemBlazePickaxe : DTItemPickaxe("blaze_pickaxe", DAMTest.TOOL_MATERIAL_BLAZE) {
	override fun hitEntity(stack: ItemStack, target: EntityLivingBase, attacker: EntityLivingBase): Boolean {
		ItemBlazeSword.hitEntity(target)
		return super.hitEntity(stack, target, attacker)
	}
}
class ItemBlazeAxe : DTItemAxe("blaze_axe", DAMTest.TOOL_MATERIAL_BLAZE, 12.0F, -3.0F) {
	override fun hitEntity(stack: ItemStack, target: EntityLivingBase, attacker: EntityLivingBase): Boolean {
		ItemBlazeSword.hitEntity(target)
		return super.hitEntity(stack, target, attacker)
	}
}
class ItemBlazeShovel : DTItemShovel("blaze_shovel", DAMTest.TOOL_MATERIAL_BLAZE) {
	override fun hitEntity(stack: ItemStack, target: EntityLivingBase, attacker: EntityLivingBase): Boolean {
		ItemBlazeSword.hitEntity(target)
		return super.hitEntity(stack, target, attacker)
	}
}
class ItemBlazeHoe : ItemHoe(DAMTest.TOOL_MATERIAL_BLAZE) {
	init { initWoTab("blaze_hoe") }
	override fun hitEntity(stack: ItemStack, target: EntityLivingBase, attacker: EntityLivingBase): Boolean {
		ItemBlazeSword.hitEntity(target)
		return super.hitEntity(stack, target, attacker)
	}
}

class ItemBlazeArmor(registryName: String, renderIndexIn: Int, equipmentSlotIn: EntityEquipmentSlot) : ItemArmor(DAMTest.ARMOR_MATERIAL_BLAZE, renderIndexIn, equipmentSlotIn) {
	init { initWoTab(registryName) }

	override fun onArmorTick(world: World, player: EntityPlayer, itemStack: ItemStack) {
		player.addPotionEffect(PotionEffect(Potion.getPotionById(12)!!, 100, 0, true, true))
	}
}