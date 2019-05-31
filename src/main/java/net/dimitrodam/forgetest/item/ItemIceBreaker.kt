package net.dimitrodam.forgetest.item

import net.dimitrodam.forgetest.DTItems
import net.dimitrodam.forgetest.util.DTItem
import net.minecraft.block.state.IBlockState
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.stats.StatList
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import java.util.*

class ItemIceBreaker : DTItem("ice_breaker") {
	init {
		maxStackSize = 1
		maxDamage = 238
	}

	override fun onBlockDestroyed(stack: ItemStack, worldIn: World, state: IBlockState, pos: BlockPos, entityLiving: EntityLivingBase): Boolean {
		return if(state.block == Blocks.ICE) true else super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving)
	}

	override fun canHarvestBlock(blockIn: IBlockState): Boolean {
		return blockIn.block == Blocks.ICE
	}

	override fun onBlockStartBreak(itemstack: ItemStack, pos: BlockPos, player: EntityPlayer): Boolean {
		if(player.world.isRemote || player.capabilities.isCreativeMode)
			return super.onBlockStartBreak(itemstack, pos, player)
		val block = player.world.getBlockState(pos).block
		if(block == Blocks.ICE) {
			val drops = arrayOf(ItemStack(DTItems.iceShard, 3))
			val rand = Random()
			val var8 = drops.iterator()

			while(var8.hasNext()) {
				val stack = var8.next()
				val f = 0.7f
				val d = (rand.nextFloat() * f).toDouble() + (1.0f - f).toDouble() * 0.5
				val d1 = (rand.nextFloat() * f).toDouble() + (1.0f - f).toDouble() * 0.5
				val d2 = (rand.nextFloat() * f).toDouble() + (1.0f - f).toDouble() * 0.5
				val entityItem = EntityItem(player.world, pos.x.toDouble() + d, pos.y.toDouble() + d1, pos.z.toDouble() + d2, stack)
				entityItem.setDefaultPickupDelay()
				player.world.spawnEntity(entityItem)
			}

			itemstack.damageItem(1, player)
			player.addStat(StatList.getBlockStats(block)!!)
			player.world.setBlockState(pos, Blocks.AIR.defaultState, 11)
			return true
		}
		return super.onBlockStartBreak(itemstack, pos, player)
	}

	override fun getDestroySpeed(stack: ItemStack, state: IBlockState): Float {
		return if(state.block == Blocks.ICE) 15.0f else super.getDestroySpeed(stack, state)
	}

	override fun addInformation(stack: ItemStack, worldIn: World?, tooltip: MutableList<String>, flagIn: ITooltipFlag) {
		addInformation(stack, tooltip)
	}
}