package net.dimitrodam.forgetest.biome

import net.dimitrodam.forgetest.DTBlocks
import net.dimitrodam.forgetest.entity.EntityRainbowPig
import net.minecraft.block.BlockSand
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.init.Blocks
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.biome.BiomeForest
import net.minecraft.world.chunk.ChunkPrimer
import java.util.*

class BiomeRainbowForest : BiomeForest(Type.NORMAL, BiomeProperties("Rainbow Forest")) {
	val STONE: IBlockState = DTBlocks.rainbowStone.defaultState

	init {
		topBlock = DTBlocks.rainbowGrass.defaultState
		fillerBlock = DTBlocks.rainbowDirt.defaultState

		spawnableCreatureList.clear()
		spawnableCreatureList.add(SpawnListEntry(EntityRainbowPig::class.java, 10, 4, 4))
		spawnableMonsterList.clear()
		spawnableWaterCreatureList.clear()
		spawnableCaveCreatureList.clear()
	}

	override fun genTerrainBlocks(worldIn: World, rand: Random, chunkPrimerIn: ChunkPrimer, x: Int, z: Int, noiseVal: Double) {
		val i = worldIn.seaLevel
		var cTopBlock: IBlockState? = this.topBlock
		var cFillerBlock = this.fillerBlock
		var j = -1
		val k = (noiseVal / 3.0 + 3.0 + rand.nextDouble() * 0.25).toInt()
		val l = x and 15
		val i1 = z and 15
		val mutableBlockPos = BlockPos.MutableBlockPos()

		for(j1 in 255 downTo 0) {
			if(j1 <= rand.nextInt(5)) {
				chunkPrimerIn.setBlockState(i1, j1, l, BEDROCK)
			} else {
				val blockState = chunkPrimerIn.getBlockState(i1, j1, l)
				if(blockState.material == Material.AIR) {
					j = -1
				} else if(blockState.block == Blocks.STONE) {
					if(j == -1) {
						if(k <= 0) {
							cTopBlock = AIR
							cFillerBlock = STONE
						} else if(j1 >= i - 4 && j1 <= i + 1) {
							cTopBlock = this.topBlock
							cFillerBlock = this.fillerBlock
						}

						if(j1 < i && (cTopBlock == null || cTopBlock.material == Material.AIR)) {
							cTopBlock = if(this.getTemperature(mutableBlockPos.setPos(x, j1, z)) < 0.15f) {
								ICE
							} else {
								WATER
							}
						}

						j = k
						when {
							j1 >= i - 1 -> chunkPrimerIn.setBlockState(i1, j1, l, cTopBlock!!)
							j1 < i - 7 - k -> {
								cTopBlock = AIR
								cFillerBlock = STONE
								chunkPrimerIn.setBlockState(i1, j1, l, GRAVEL)
							}
							else -> chunkPrimerIn.setBlockState(i1, j1, l, cFillerBlock)
						}
					} else if(j > 0) {
						--j
						chunkPrimerIn.setBlockState(i1, j1, l, cFillerBlock)
						if(j == 0 && cFillerBlock.block == Blocks.SAND && k > 1) {
							j = rand.nextInt(4) + Math.max(0, j1 - 63)
							cFillerBlock = if(cFillerBlock.getValue(BlockSand.VARIANT) == BlockSand.EnumType.RED_SAND) RED_SANDSTONE else SANDSTONE
						}
					}
				}
			}
		}
	}
}