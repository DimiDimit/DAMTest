package net.dimitrodam.forgetest

import net.dimitrodam.forgetest.block.BlockExtractor
import net.dimitrodam.forgetest.block.BlockFabricator
import net.dimitrodam.forgetest.item.ItemCreativeDestroyer
import net.dimitrodam.forgetest.item.ItemEntityIgniter
import net.dimitrodam.forgetest.item.ItemMatter
import net.dimitrodam.forgetest.item.ItemPack
import net.minecraft.client.renderer.block.model.ModelBakery
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.item.Item.getItemFromBlock
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly


object DTBlocks {
	@JvmStatic
	@GameRegistry.ObjectHolder("${DAMTest.MODID}:fabricator")
	lateinit var fabricator: BlockFabricator
	@JvmStatic
	@GameRegistry.ObjectHolder("${DAMTest.MODID}:extractor")
	lateinit var extractor: BlockExtractor

	@JvmStatic
	@SideOnly(Side.CLIENT)
	fun initModels() {
		for(b in arrayOf(
				fabricator,
				extractor
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
	@SideOnly(Side.CLIENT)
	fun initModels() {
		for(i in arrayOf(
				matter,
				entityIgniter,
				creativeDestroyer
		)) {
			ModelLoader.setCustomModelResourceLocation(
					i, 0, ModelResourceLocation(i.registryName!!, "inventory")
			)
		}

		ModelBakery.registerItemVariants(pack, *ItemPack.ItemMesh.getVariants())
		ModelLoader.setCustomMeshDefinition(pack, ItemPack.ItemMesh())
	}
}