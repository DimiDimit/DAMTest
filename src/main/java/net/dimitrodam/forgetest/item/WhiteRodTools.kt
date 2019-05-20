package net.dimitrodam.forgetest.item

import net.dimitrodam.forgetest.DAMTest
import net.dimitrodam.forgetest.util.DTItem.Companion.initWoTab
import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemSword
import net.minecraft.util.EntityDamageSource

class ItemWhiteRodSword : ItemSword(DAMTest.TOOL_MATERIAL_WHITE_ROD) {
	init {
		initWoTab("white_rod_sword")
	}

	override fun hitEntity(stack: ItemStack, target: EntityLivingBase, attacker: EntityLivingBase): Boolean {
		target.attackEntityFrom(EntityDamageSource.causeMobDamage(attacker), 3.4028235E38F)
		return super.hitEntity(stack, target, attacker)
	}
}