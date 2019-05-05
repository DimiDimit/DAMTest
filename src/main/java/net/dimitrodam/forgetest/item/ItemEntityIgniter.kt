package net.dimitrodam.forgetest.item

import net.dimitrodam.forgetest.util.DTItem
import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.init.Blocks
import net.minecraft.init.SoundEvents
import net.minecraft.item.Item
import net.minecraft.item.ItemFlintAndSteel
import net.minecraft.item.ItemStack
import net.minecraft.util.*
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class ItemEntityIgniter : DTItem("entity_igniter") {
	init {
		maxStackSize = 1
		maxDamage = 64
	}

	/**
	 * Code copied from [ItemFlintAndSteel.onItemUse].
	 */
	override fun onItemUse(player: EntityPlayer, worldIn: World, pos: BlockPos, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {
		/*if(player.isSneaking)
			return EnumActionResult.PASS*/
		val posOffs = pos.offset(facing)
		val itemStack = player.getHeldItem(hand)
		return if(!player.canPlayerEdit(posOffs, facing, itemStack)) {
			EnumActionResult.FAIL
		} else {
			if(worldIn.isAirBlock(posOffs)) {
				worldIn.playSound(player, posOffs, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0f, Item.itemRand.nextFloat() * 0.4f + 0.8f)
				if(!worldIn.isRemote) {
					worldIn.setBlockState(posOffs, Blocks.FIRE.defaultState, 11)
				}
			}

			if(player is EntityPlayerMP) {
				CriteriaTriggers.PLACED_BLOCK.trigger(player, posOffs, itemStack)
			}

			itemStack.damageItem(1, player)
			EnumActionResult.SUCCESS
		}
	}
	override fun itemInteractionForEntity(stack: ItemStack, playerIn: EntityPlayer, target: EntityLivingBase, hand: EnumHand): Boolean {
		/*if(playerIn.isSneaking)
			return false*/
		playerIn.world.playSound(playerIn, playerIn.position, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0f, Item.itemRand.nextFloat() * 0.4f + 0.8f)
		if(!playerIn.world.isRemote) {
			target.setFire(10)
			stack.damageItem(1, playerIn)
		}
		return true
		//return super.itemInteractionForEntity(stack, playerIn, target, hand)
	}

	override fun onItemRightClick(worldIn: World, playerIn: EntityPlayer, handIn: EnumHand): ActionResult<ItemStack> {
		val stack = playerIn.getHeldItem(handIn)
		if(!playerIn.isSneaking)
			return ActionResult(EnumActionResult.PASS, stack)
		playerIn.world.playSound(playerIn, playerIn.position, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0f, Item.itemRand.nextFloat() * 0.4f + 0.8f)
		if(!worldIn.isRemote) {
			playerIn.setFire(10)
			stack.damageItem(1, playerIn)
		}
		return ActionResult(EnumActionResult.SUCCESS, stack)
	}

	override fun isEnchantable(stack: ItemStack): Boolean = true
	override fun getItemEnchantability(): Int = Item.ToolMaterial.IRON.enchantability
}