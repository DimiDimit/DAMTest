package net.dimitrodam.forgetest

import net.dimitrodam.forgetest.block.*
import net.dimitrodam.forgetest.item.*
import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.block.model.ModelBakery
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.item.Item.getItemFromBlock
import net.minecraft.item.ItemSword
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.chunk.IChunkProvider
import net.minecraft.world.gen.IChunkGenerator
import net.minecraft.world.gen.feature.WorldGenMinable
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.fml.common.IWorldGenerator
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import java.util.*


object DTBlocks {
	@JvmStatic
	@GameRegistry.ObjectHolder("${DAMTest.MODID}:fabricator")
	lateinit var fabricator: BlockFabricator
	@JvmStatic
	@GameRegistry.ObjectHolder("${DAMTest.MODID}:extractor")
	lateinit var extractor: BlockExtractor
	@JvmStatic
	@GameRegistry.ObjectHolder("${DAMTest.MODID}:matter_block")
	lateinit var matterBlock: BlockMatter
	@JvmStatic
	@GameRegistry.ObjectHolder("${DAMTest.MODID}:rainbow_ore")
	lateinit var rainbowOre: BlockRainbowOre
	@JvmStatic
	@GameRegistry.ObjectHolder("${DAMTest.MODID}:rainbow_block")
	lateinit var rainbowBlock: BlockRainbow

	@JvmStatic
	@SideOnly(Side.CLIENT)
	fun initModels() {
		for(b in arrayOf(
				fabricator,
				extractor,
				matterBlock,
				rainbowOre,
				rainbowBlock
		)) {
			ModelLoader.setCustomModelResourceLocation(
					getItemFromBlock(b),
					0, ModelResourceLocation(b.registryName!!, "inventory")
			)
		}
	}
}

object DTItems {
	@JvmStatic
	@GameRegistry.ObjectHolder("${DAMTest.MODID}:matter")
	lateinit var matter: ItemMatter
	@JvmStatic
	@GameRegistry.ObjectHolder("${DAMTest.MODID}:entity_igniter")
	lateinit var entityIgniter: ItemEntityIgniter
	@JvmStatic
	@GameRegistry.ObjectHolder("${DAMTest.MODID}:creative_destroyer")
	lateinit var creativeDestroyer: ItemCreativeDestroyer
	@JvmStatic
	@GameRegistry.ObjectHolder("${DAMTest.MODID}:pack")
	lateinit var pack: ItemPack
	@JvmStatic
	@GameRegistry.ObjectHolder("${DAMTest.MODID}:rainbow_ingot")
	lateinit var rainbowIngot: ItemRainbowIngot
	@JvmStatic
	@GameRegistry.ObjectHolder("${DAMTest.MODID}:rainbow_sword")
	lateinit var rainbowSword: ItemSword

	@JvmStatic
	@SideOnly(Side.CLIENT)
	fun initModels() {
		for(i in arrayOf(
				matter,
				entityIgniter,
				creativeDestroyer,
				rainbowIngot,
				rainbowSword
		)) {
			ModelLoader.setCustomModelResourceLocation(
					i, 0, ModelResourceLocation(i.registryName!!, "inventory")
			)
		}

		ModelBakery.registerItemVariants(pack, *ItemPack.ItemMesh.getVariants())
		ModelLoader.setCustomMeshDefinition(pack, ItemPack.ItemMesh())
	}
}

class DTWorldGen : IWorldGenerator {
	override fun generate(random: Random, chunkX: Int, chunkZ: Int, world: World, chunkGenerator: IChunkGenerator, chunkProvider: IChunkProvider) {
		when(world.provider.dimension) {
			0 -> { // Overworld
				generateOre(DTBlocks.rainbowOre.defaultState, world, random, chunkX * 16, chunkZ * 16, 4, 15, 2, 1)
			}
		}
	}

	private fun generateOre(ore: IBlockState, world: World, random: Random, x: Int, z: Int, minY: Int, maxY: Int, size: Int, chances: Int) {
		val deltaY = maxY - minY

		for(i in 0 until chances) {
			val pos = BlockPos(x + random.nextInt(16), minY + random.nextInt(deltaY), z + random.nextInt(16))

			val generator = WorldGenMinable(ore, size)
			generator.generate(world, random, pos)
		}
	}
}