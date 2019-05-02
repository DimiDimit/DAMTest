package net.dimitrodam.forgetest.util

import net.dimitrodam.forgetest.DAMTest
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

abstract class BlockGuiTileEntity(material: Material, registryName: String, hardness: Float = 1.5f, tab: CreativeTabs = DAMTest.TAB_DT, tileEntity: Class<out TileEntity>, val gui: Int = 0) : BlockTileEntity(material, registryName, hardness, tab, tileEntity) {
	override fun onBlockActivated(worldIn: World, pos: BlockPos, state: IBlockState, playerIn: EntityPlayer, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean {
		if(worldIn.isRemote) return true
		if(!tileEntity.isInstance(worldIn.getTileEntity(pos))) return false
		playerIn.openGui(DAMTest.instance, gui, worldIn, pos.x, pos.y, pos.z)
		return true
	}
}