package net.dimitrodam.forgetest.block

import net.dimitrodam.forgetest.tile.TileFabricator
import net.dimitrodam.forgetest.util.BlockGuiTileEntity
import net.minecraft.block.BlockHorizontal
import net.minecraft.block.material.Material
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class BlockFabricator : BlockGuiTileEntity(Material.ROCK, "fabricator", tileEntity = TileFabricator::class.java) {
	override fun createBlockState(): BlockStateContainer = BlockStateContainer(this, BlockHorizontal.FACING)
	override fun onBlockPlacedBy(worldIn: World, pos: BlockPos, state: IBlockState, placer: EntityLivingBase, stack: ItemStack) {
		worldIn.setBlockState(pos, state.withProperty(BlockHorizontal.FACING, placer.horizontalFacing.opposite), 2)
	}
	override fun getStateFromMeta(meta: Int): IBlockState {
		var enumFacing = EnumFacing.getFront(meta)
		if(enumFacing.axis == EnumFacing.Axis.Y) enumFacing = EnumFacing.NORTH
		return defaultState.withProperty(BlockHorizontal.FACING, enumFacing)
	}
	override fun getMetaFromState(state: IBlockState): Int =
			(state.getValue<EnumFacing>(BlockHorizontal.FACING) as EnumFacing).index
}