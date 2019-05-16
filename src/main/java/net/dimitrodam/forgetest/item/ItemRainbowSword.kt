package net.dimitrodam.forgetest.item

import net.dimitrodam.forgetest.DAMTest
import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemSword
import net.minecraft.potion.Potion
import net.minecraft.potion.PotionEffect

class ItemRainbowSword : ItemSword(DAMTest.TOOL_MATERIAL_RAINBOW) {
	init {
		unlocalizedName = "${DAMTest.MODID}.rainbow_sword"
		setRegistryName("rainbow_sword")
	}

	override fun hitEntity(stack: ItemStack, target: EntityLivingBase, attacker: EntityLivingBase): Boolean {
		target.addPotionEffect(PotionEffect(Potion.getPotionById(19)!!, 60, 1))
		return super.hitEntity(stack, target, attacker)
	}
}