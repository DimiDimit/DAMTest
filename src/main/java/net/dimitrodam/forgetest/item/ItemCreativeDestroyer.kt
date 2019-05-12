package net.dimitrodam.forgetest.item

import net.dimitrodam.forgetest.util.DTItem
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.EntityList
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Enchantments
import net.minecraft.init.Items
import net.minecraft.item.ItemMonsterPlacer
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World


class ItemCreativeDestroyer : DTItem("creative_destroyer") {
	init {
		maxStackSize = 1
	}

	override fun onItemUse(player: EntityPlayer, worldIn: World, pos: BlockPos, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {
		val stack = player.getHeldItem(hand)
		/*if(playerIn.isSneaking)
			return EnumActionResult.PASS*/
		if(!worldIn.isRemote) {
			if(EnchantmentHelper.getEnchantments(stack)[Enchantments.SILK_TOUCH] != null) {
				val block = worldIn.getBlockState(pos).block
				worldIn.destroyBlock(pos, false)
				// FIXME: Handle metadata. For example, when you break tallgrass you get a shrub. This is important because mods like Thermal Expansion use metadata to distinguish between different materials, e.g. copper vs platinum.
				worldIn.spawnEntity(EntityItem(worldIn, pos.x.toDouble()+0.5, pos.y.toDouble()+0.5, pos.z.toDouble()+0.5, ItemStack(getItemFromBlock(block))))
			} else worldIn.destroyBlock(pos, true)
		}
		return EnumActionResult.SUCCESS
	}

	override fun itemInteractionForEntity(stack: ItemStack, playerIn: EntityPlayer, target: EntityLivingBase, hand: EnumHand): Boolean {
		//val stack = playerIn.getHeldItem(hand)
		val worldIn = playerIn.world
		val targetResourceLocation = EntityList.getKey(target)
		/*if(playerIn.isSneaking)
			return false*/
		if(!worldIn.isRemote) {
			if(EnchantmentHelper.getEnchantments(stack)[Enchantments.SILK_TOUCH] != null && targetResourceLocation != null) {
				target.setDead()
				val egg = ItemStack(Items.SPAWN_EGG)
				ItemMonsterPlacer.applyEntityIdToItemStack(egg, targetResourceLocation)
				worldIn.spawnEntity(EntityItem(worldIn, target.posX, target.posY, target.posZ, egg))
			} else target.onKillCommand()
		}
		return true
	}

	override fun onItemRightClick(worldIn: World, playerIn: EntityPlayer, handIn: EnumHand): ActionResult<ItemStack> {
		val stack = playerIn.getHeldItem(handIn)
		if(!playerIn.isSneaking)
			return ActionResult(EnumActionResult.PASS, stack)
		if(!worldIn.isRemote) {
			playerIn.onKillCommand()
		}
		return ActionResult(EnumActionResult.SUCCESS, stack)
	}

	override fun isEnchantable(stack: ItemStack): Boolean = true
	override fun getItemEnchantability(): Int = 15
	override fun canApplyAtEnchantingTable(stack: ItemStack, enchantment: Enchantment): Boolean {
		if(enchantment == Enchantments.SILK_TOUCH)
			return true
		return super.canApplyAtEnchantingTable(stack, enchantment)
	}
}