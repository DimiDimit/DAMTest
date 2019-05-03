package net.dimitrodam.forgetest.item

import net.dimitrodam.forgetest.util.DTItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand
import net.minecraft.world.World

class ItemHealthPack : DTItem("health_pack") {
	override fun onItemRightClick(worldIn: World, playerIn: EntityPlayer, handIn: EnumHand): ActionResult<ItemStack> {
		val stack = playerIn.getHeldItem(handIn)
		if(!playerIn.shouldHeal())
			return ActionResult(EnumActionResult.FAIL, stack)
		playerIn.heal(playerIn.maxHealth - playerIn.health)
		stack.shrink(1)
		return ActionResult(EnumActionResult.SUCCESS, stack)
	}
}

class ItemHungerPack : DTItem("hunger_pack") {
	override fun onItemRightClick(worldIn: World, playerIn: EntityPlayer, handIn: EnumHand): ActionResult<ItemStack> {
		val stack = playerIn.getHeldItem(handIn)
		val food = playerIn.foodStats
		if(food.foodLevel >= 20 && food.saturationLevel >= 20f)
			return ActionResult(EnumActionResult.FAIL, stack)
		food.foodLevel = 20
		food.setFoodSaturationLevel(20f)
		stack.shrink(1)
		return ActionResult(EnumActionResult.SUCCESS, stack)
	}
}

class ItemMultiPack : DTItem("multi_pack") {
	override fun onItemRightClick(worldIn: World, playerIn: EntityPlayer, handIn: EnumHand): ActionResult<ItemStack> {
		val stack = playerIn.getHeldItem(handIn)
		val food = playerIn.foodStats
		if(!playerIn.shouldHeal() && food.foodLevel >= 20 && food.saturationLevel >= 20f)
			return ActionResult(EnumActionResult.FAIL, stack)
		playerIn.heal(playerIn.maxHealth - playerIn.health)
		food.foodLevel = 20
		food.setFoodSaturationLevel(20f)
		stack.shrink(1)
		return ActionResult(EnumActionResult.SUCCESS, stack)
	}
}