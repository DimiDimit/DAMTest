package net.dimitrodam.forgetest.util

import net.dimitrodam.forgetest.DAMTest
import net.minecraft.block.ITileEntityProvider
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.item.EntityItem
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.IItemHandler
import net.minecraftforge.items.ItemStackHandler


abstract class BlockTileEntity(material: Material, registryName: String, hardness: Float = 1.5f, tab: CreativeTabs = DAMTest.TAB_DT, val tileEntity: Class<out TileEntity>) : DTBlock(material, registryName, hardness, tab), ITileEntityProvider {
	override fun createNewTileEntity(worldIn: World, meta: Int): TileEntity = tileEntity.newInstance()
	override fun breakBlock(worldIn: World, pos: BlockPos, state: IBlockState) {
		val te = worldIn.getTileEntity(pos)!!
		if(te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
			val ish = te.getCapability<IItemHandler>(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null) as ItemStackHandler
			for(i in 0 until ish.slots) {
				worldIn.spawnEntity(EntityItem(worldIn, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), ish.getStackInSlot(i)))
			}
		}

		super.breakBlock(worldIn, pos, state)
	}
}