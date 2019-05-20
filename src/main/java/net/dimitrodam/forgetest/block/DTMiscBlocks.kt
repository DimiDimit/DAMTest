package net.dimitrodam.forgetest.block

import net.dimitrodam.forgetest.DTBlocks
import net.dimitrodam.forgetest.DTItems
import net.dimitrodam.forgetest.util.DTBlock
import net.dimitrodam.forgetest.util.DTBlock.Companion.initMin
import net.minecraft.block.BlockGrass
import net.minecraft.block.BlockTallGrass
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.init.Blocks
import net.minecraft.item.Item
import net.minecraft.util.BlockRenderLayer
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import java.util.*

class BlockMatter : DTBlock(Material.ROCK, "matter_block")

class BlockRainbowOre : DTBlock(Material.ROCK, "rainbow_ore") {
	init { setHarvestLevel("pickaxe", 3) }
	override fun getItemDropped(state: IBlockState, rand: Random, fortune: Int): Item = DTItems.rainbowIngot
	override fun getExpDrop(state: IBlockState, world: IBlockAccess, pos: BlockPos, fortune: Int): Int = 20
}
class BlockRainbow : DTBlock(Material.ROCK, "rainbow_block") { init { setHarvestLevel("pickaxe", 4) } }

class BlockRainbowGrass : BlockGrass() {
	init {
		initMin("rainbow_grass")
		setHardness(0.6f)
		soundType = SoundType.PLANT
	}

	override fun getItemDropped(state: IBlockState, rand: Random, fortune: Int): Item = Item.getItemFromBlock(DTBlocks.rainbowDirt)

	override fun updateTick(worldIn: World, pos: BlockPos, state: IBlockState, rand: Random) {
		if(!worldIn.isRemote) {
			if(!worldIn.isAreaLoaded(pos, 3)) {
				return
			}

			if(worldIn.getLightFromNeighbors(pos.up()) < 4 && worldIn.getBlockState(pos.up()).getLightOpacity(worldIn, pos.up()) > 2) {
				worldIn.setBlockState(pos, DTBlocks.rainbowDirt.defaultState)
			} else if(worldIn.getLightFromNeighbors(pos.up()) >= 9) {
				for(i in 0..3) {
					val blockPos = pos.add(rand.nextInt(3) - 1, rand.nextInt(5) - 3, rand.nextInt(3) - 1)
					if(blockPos.y in 0..255 && !worldIn.isBlockLoaded(blockPos)) {
						return
					}

					val blockState = worldIn.getBlockState(blockPos.up())
					val blockState1 = worldIn.getBlockState(blockPos)
					if(blockState1.block == DTBlocks.rainbowDirt && worldIn.getLightFromNeighbors(blockPos.up()) >= 4 && blockState.getLightOpacity(worldIn, pos.up()) <= 2) {
						worldIn.setBlockState(blockPos, DTBlocks.rainbowGrass.defaultState)
					}
				}
			}
		}
	}

	override fun grow(worldIn: World, rand: Random, pos: BlockPos, state: IBlockState) {
		val blockPos = pos.up()

		label35@ for(i in 0..127) {
			var blockPos1 = blockPos

			for(j in 0 until i / 16) {
				blockPos1 = blockPos1.add(rand.nextInt(3) - 1, (rand.nextInt(3) - 1) * rand.nextInt(3) / 2, rand.nextInt(3) - 1)
				if(worldIn.getBlockState(blockPos1.down()).block != DTBlocks.rainbowGrass || worldIn.getBlockState(blockPos1).isNormalCube) {
					continue@label35
				}
			}

			if(worldIn.isAirBlock(blockPos1)) {
				if(rand.nextInt(8) == 0) {
					worldIn.getBiome(blockPos1).plantFlower(worldIn, rand, blockPos1)
				} else {
					val blockState1 = Blocks.TALLGRASS.defaultState.withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.GRASS)
					if(Blocks.TALLGRASS.canBlockStay(worldIn, blockPos1, blockState1)) {
						worldIn.setBlockState(blockPos1, blockState1, 3)
					}
				}
			}
		}
	}

	@SideOnly(Side.CLIENT)
	override fun getBlockLayer(): BlockRenderLayer = BlockRenderLayer.SOLID
}
class BlockRainbowDirt : DTBlock(Material.GROUND, "rainbow_dirt", 0.5f) {
	init {
		soundType = SoundType.GROUND
		setCreativeTab(CreativeTabs.BUILDING_BLOCKS)
	}
}
class BlockRainbowStone : DTBlock(Material.ROCK, "rainbow_stone", 1.5f) {
	init {
		setResistance(10.0f)
		soundType = SoundType.STONE
		setCreativeTab(CreativeTabs.BUILDING_BLOCKS)
	}

	override fun getItemDropped(state: IBlockState, rand: Random, fortune: Int): Item = Item.getItemFromBlock(DTBlocks.rainbowCobblestone)
}
class BlockRainbowCobblestone : DTBlock(Material.ROCK, "rainbow_cobblestone", 2.0f) {
	init {
		setResistance(10.0f)
		soundType = SoundType.STONE
		setCreativeTab(CreativeTabs.BUILDING_BLOCKS)
	}
}