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
	@GameRegistry.ObjectHolder("${DAMTest.MODID}:rainbow_grass")
	lateinit var rainbowGrass: BlockRainbowGrass
	@JvmStatic
	@GameRegistry.ObjectHolder("${DAMTest.MODID}:rainbow_dirt")
	lateinit var rainbowDirt: BlockRainbowDirt
	@JvmStatic
	@GameRegistry.ObjectHolder("${DAMTest.MODID}:rainbow_stone")
	lateinit var rainbowStone: BlockRainbowStone
	@JvmStatic
	@GameRegistry.ObjectHolder("${DAMTest.MODID}:rainbow_cobblestone")
	lateinit var rainbowCobblestone: BlockRainbowCobblestone

	@JvmStatic
	@SideOnly(Side.CLIENT)
	fun initModels() {
		for(b in arrayOf(
				fabricator,
				extractor,
				matterBlock,
				rainbowOre,
				rainbowBlock,
				rainbowGrass,
				rainbowDirt,
				rainbowStone,
				rainbowCobblestone
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
	@GameRegistry.ObjectHolder("${DAMTest.MODID}:rainbow_nugget")
	lateinit var rainbowNugget: ItemRainbowNugget
	@JvmStatic
	@GameRegistry.ObjectHolder("${DAMTest.MODID}:rainbow_sword")
	lateinit var rainbowSword: ItemSword
	@JvmStatic
	@GameRegistry.ObjectHolder("${DAMTest.MODID}:rainbow_pickaxe")
	lateinit var rainbowPickaxe: ItemRainbowPickaxe
	@JvmStatic
	@GameRegistry.ObjectHolder("${DAMTest.MODID}:rainbow_axe")
	lateinit var rainbowAxe: ItemRainbowAxe
	@JvmStatic
	@GameRegistry.ObjectHolder("${DAMTest.MODID}:rainbow_shovel")
	lateinit var rainbowShovel: ItemRainbowShovel
	@JvmStatic
	@GameRegistry.ObjectHolder("${DAMTest.MODID}:rainbow_hoe")
	lateinit var rainbowHoe: ItemRainbowHoe
	@JvmStatic
	@GameRegistry.ObjectHolder("${DAMTest.MODID}:rainbow_helmet")
	lateinit var rainbowHelmet: ItemRainbowArmor
	@JvmStatic
	@GameRegistry.ObjectHolder("${DAMTest.MODID}:rainbow_chestplate")
	lateinit var rainbowChestplate: ItemRainbowArmor
	@JvmStatic
	@GameRegistry.ObjectHolder("${DAMTest.MODID}:rainbow_leggings")
	lateinit var rainbowLeggings: ItemRainbowArmor
	@JvmStatic
	@GameRegistry.ObjectHolder("${DAMTest.MODID}:rainbow_boots")
	lateinit var rainbowBoots: ItemRainbowArmor
	@JvmStatic
	@GameRegistry.ObjectHolder("${DAMTest.MODID}:white_rod")
	lateinit var whiteRod: ItemWhiteRod
	@JvmStatic
	@GameRegistry.ObjectHolder("${DAMTest.MODID}:white_rod_sword")
	lateinit var whiteRodSword: ItemWhiteRodSword

	@JvmStatic
	@SideOnly(Side.CLIENT)
	fun initModels() {
		for(i in arrayOf(
				matter,
				entityIgniter,
				creativeDestroyer,
				rainbowIngot,
				rainbowNugget,
				rainbowSword,
				rainbowPickaxe,
				rainbowAxe,
				rainbowShovel,
				rainbowHoe,
				rainbowHelmet,
				rainbowChestplate,
				rainbowLeggings,
				rainbowBoots,
				whiteRod,
				whiteRodSword
		)) {
			ModelLoader.setCustomModelResourceLocation(
					i, 0, ModelResourceLocation(i.registryName!!, "inventory")
			)
		}

		ModelBakery.registerItemVariants(pack, *ItemPack.ItemMesh.getVariants())
		ModelLoader.setCustomMeshDefinition(pack, ItemPack.ItemMesh())
	}
}

object DTWorldGen : IWorldGenerator {
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