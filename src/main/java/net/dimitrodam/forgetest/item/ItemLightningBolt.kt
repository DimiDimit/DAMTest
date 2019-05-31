package net.dimitrodam.forgetest.item

import net.dimitrodam.forgetest.util.DTItem
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.effect.EntityLightningBolt
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class ItemLightningBolt : DTItem("lightning_bolt") {
	override fun onItemUse(player: EntityPlayer, worldIn: World, pos: BlockPos, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {
		val itemStack = player.getHeldItem(hand)
		if(!player.capabilities.isCreativeMode) {
			itemStack.shrink(1)
		}
		player.cooldownTracker.setCooldown(this, 20)
		worldIn.spawnEntity(EntityLightningBolt(worldIn, pos.x.toDouble(), pos.y.toDouble()+1.0, pos.z.toDouble(), false))
		return EnumActionResult.SUCCESS
	}

	override fun itemInteractionForEntity(stack: ItemStack, playerIn: EntityPlayer, target: EntityLivingBase, hand: EnumHand): Boolean {
		val itemStack = playerIn.getHeldItem(hand)
		val world = playerIn.world
		if(!playerIn.capabilities.isCreativeMode) {
			itemStack.shrink(1)
		}
		playerIn.cooldownTracker.setCooldown(this, 20)
		if(!world.isRemote) {
			world.spawnEntity(EntityLightningBolt(world, target.posX, target.posY, target.posZ, false))
		}
		return true
	}

	override fun onItemRightClick(worldIn: World, playerIn: EntityPlayer, handIn: EnumHand): ActionResult<ItemStack> {
		val itemStack = playerIn.getHeldItem(handIn)
		if(!playerIn.isSneaking)
			return ActionResult(EnumActionResult.PASS, itemStack)
		if(!playerIn.capabilities.isCreativeMode) {
			itemStack.shrink(1)
		}
		playerIn.cooldownTracker.setCooldown(this, 20)
		if(!worldIn.isRemote) {
			worldIn.spawnEntity(EntityLightningBolt(worldIn, playerIn.posX, playerIn.posY, playerIn.posZ, false))
		}
		return ActionResult(EnumActionResult.SUCCESS, itemStack)
	}

	override fun addInformation(stack: ItemStack, worldIn: World?, tooltip: MutableList<String>, flagIn: ITooltipFlag) {
		addInformation(stack, tooltip)
	}
}