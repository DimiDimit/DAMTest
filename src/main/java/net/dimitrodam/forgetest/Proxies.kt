@file:Suppress("UNUSED_PARAMETER")

package net.dimitrodam.forgetest

import net.dimitrodam.forgetest.biome.BiomeRainbowForest
import net.dimitrodam.forgetest.block.*
import net.dimitrodam.forgetest.container.ContainerExtractor
import net.dimitrodam.forgetest.container.ContainerFabricator
import net.dimitrodam.forgetest.entity.EntityRainbowPig
import net.dimitrodam.forgetest.entity.RenderRainbowPig
import net.dimitrodam.forgetest.guicontainer.GuiContainerExtractor
import net.dimitrodam.forgetest.guicontainer.GuiContainerFabricator
import net.dimitrodam.forgetest.item.*
import net.dimitrodam.forgetest.item.tool.*
import net.dimitrodam.forgetest.tile.TileExtractor
import net.dimitrodam.forgetest.tile.TileFabricator
import net.minecraft.block.Block
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.Item
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipe
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.biome.Biome
import net.minecraft.world.storage.loot.LootTableList
import net.minecraftforge.client.event.ModelRegistryEvent
import net.minecraftforge.common.BiomeManager
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.client.registry.RenderingRegistry
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.network.IGuiHandler
import net.minecraftforge.fml.common.network.NetworkRegistry
import net.minecraftforge.fml.common.registry.EntityRegistry
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.oredict.OreDictionary

@Mod.EventBusSubscriber
abstract class Proxy {
	open fun preInit(event: FMLPreInitializationEvent) {
		GameRegistry.registerWorldGenerator(DTWorldGen, 3)
		LootTableList.register(ResourceLocation(DAMTest.MODID, "entity/rainbow_pig"))
	}
	open fun init(event: FMLInitializationEvent) {
		NetworkRegistry.INSTANCE.registerGuiHandler(DAMTest.instance, GuiProxy)
		OreDictionary.registerOre("oreRainbow", DTBlocks.rainbowOre)
		OreDictionary.registerOre("ingotRainbow", DTItems.rainbowIngot)
		OreDictionary.registerOre("nuggetRainbow", DTItems.rainbowNugget)
		OreDictionary.registerOre("blockRainbow", DTBlocks.rainbowBlock)
		GameRegistry.addSmelting(DTBlocks.rainbowOre, ItemStack(DTItems.rainbowIngot), 20.0F)
		GameRegistry.addSmelting(DTBlocks.rainbowCobblestone, ItemStack(DTBlocks.rainbowStone), 0.0F)
	}
	open fun postInit(event: FMLPostInitializationEvent) {}

	companion object {
		lateinit var rainbowForest: BiomeRainbowForest

		@JvmStatic
		@SubscribeEvent
		fun registerBlocks(event: RegistryEvent.Register<Block>) {
			event.registry.registerAll(
					BlockFabricator(),
					BlockExtractor(),
					BlockMatter(),
					BlockRainbowOre(),
					BlockRainbow(),
					BlockRainbowGrass(),
					BlockRainbowDirt(),
					BlockRainbowStone(),
					BlockRainbowCobblestone(),
					BlockWhiteRod()
			)
			GameRegistry.registerTileEntity(TileFabricator::class.java, ResourceLocation(DAMTest.MODID, "fabricator"))
			GameRegistry.registerTileEntity(TileExtractor::class.java, ResourceLocation(DAMTest.MODID, "extractor"))
		}
		@JvmStatic
		@SubscribeEvent
		fun registerItems(event: RegistryEvent.Register<Item>) {
			event.registry.registerAll(
					ItemMatter(),
					ItemEntityIgniter(),
					ItemCreativeDestroyer(),
					ItemPack(),
					ItemRainbowIngot(),
					ItemRainbowNugget(),
					ItemRainbowSword(),
					ItemRainbowPickaxe(),
					ItemRainbowAxe(),
					ItemRainbowShovel(),
					ItemRainbowHoe(),
					ItemRainbowArmor("rainbow_helmet", 0, EntityEquipmentSlot.HEAD),
					ItemRainbowArmor("rainbow_chestplate", 0, EntityEquipmentSlot.CHEST),
					ItemRainbowArmor("rainbow_leggings", 1, EntityEquipmentSlot.LEGS),
					ItemRainbowArmor("rainbow_boots", 0, EntityEquipmentSlot.FEET),
					ItemWhiteRod(),
					ItemWhiteRodDust(),
					ItemWhiteRodSword(),
					ItemWhiteRodPickaxe(),
					ItemWhiteRodAxe(),
					ItemWhiteRodShovel(),
					ItemWhiteRodHoe(),
					ItemWhiteRodArmor("white_rod_helmet", 0, EntityEquipmentSlot.HEAD),
					ItemWhiteRodArmor("white_rod_chestplate", 0, EntityEquipmentSlot.CHEST),
					ItemWhiteRodArmor("white_rod_leggings", 1, EntityEquipmentSlot.LEGS),
					ItemWhiteRodArmor("white_rod_boots", 0, EntityEquipmentSlot.FEET),
					ItemBlazeSword(),
					ItemBlazePickaxe(),
					ItemBlazeAxe(),
					ItemBlazeShovel(),
					ItemBlazeHoe(),
					ItemBlazeArmor("blaze_helmet", 0, EntityEquipmentSlot.HEAD),
					ItemBlazeArmor("blaze_chestplate", 0, EntityEquipmentSlot.CHEST),
					ItemBlazeArmor("blaze_leggings", 1, EntityEquipmentSlot.LEGS),
					ItemBlazeArmor("blaze_boots", 0, EntityEquipmentSlot.FEET),
					ItemIceShard(),
					ItemIceBreaker(),

					ItemBlock(DTBlocks.fabricator).setRegistryName(DTBlocks.fabricator.registryName),
					ItemBlock(DTBlocks.extractor).setRegistryName(DTBlocks.extractor.registryName),
					ItemBlock(DTBlocks.matterBlock).setRegistryName(DTBlocks.matterBlock.registryName),
					ItemBlock(DTBlocks.rainbowOre).setRegistryName(DTBlocks.rainbowOre.registryName),
					ItemBlock(DTBlocks.rainbowBlock).setRegistryName(DTBlocks.rainbowBlock.registryName),
					ItemBlock(DTBlocks.rainbowGrass).setRegistryName(DTBlocks.rainbowGrass.registryName),
					ItemBlock(DTBlocks.rainbowDirt).setRegistryName(DTBlocks.rainbowDirt.registryName),
					ItemBlock(DTBlocks.rainbowStone).setRegistryName(DTBlocks.rainbowStone.registryName),
					ItemBlock(DTBlocks.rainbowCobblestone).setRegistryName(DTBlocks.rainbowCobblestone.registryName),
					ItemBlock(DTBlocks.whiteRodBlock).setRegistryName(DTBlocks.whiteRodBlock.registryName)
			)
		}
		@JvmStatic
		@SubscribeEvent
		fun registerRecipes(event: RegistryEvent.Register<IRecipe>) {
			event.registry.registerAll(
					ItemPack.CombinationRecipe(),
					ItemPack.TargetRecipe()
			)
		}
		@JvmStatic
		@SubscribeEvent
		fun registerBiomes(event: RegistryEvent.Register<Biome>) {
			rainbowForest = BiomeRainbowForest()
			event.registry.registerAll(
					rainbowForest.setRegistryName(ResourceLocation(DAMTest.MODID, "rainbow_forest"))
			)
			BiomeManager.addBiome(BiomeManager.BiomeType.WARM, BiomeManager.BiomeEntry(rainbowForest, 5))
		}
	}
}

@Mod.EventBusSubscriber(Side.CLIENT)
class ClientProxy : Proxy() {
	companion object {
		@JvmStatic
		@SubscribeEvent
		fun registerModels(event: ModelRegistryEvent) {
			DTBlocks.initModels()
			DTItems.initModels()
		}
	}

	override fun preInit(event: FMLPreInitializationEvent) {
		super.preInit(event)
		RenderingRegistry.registerEntityRenderingHandler(EntityRainbowPig::class.java) { renderManager -> RenderRainbowPig(renderManager) }
	}

	override fun init(event: FMLInitializationEvent) {
		super.init(event)
		EntityRegistry.registerModEntity(ResourceLocation(DAMTest.MODID, "rainbow_pig"), EntityRainbowPig::class.java, "rainbow_pig", 0, DAMTest.MODID, 64, 5, true, 0xC20000, 0xF80000)
	}
}

@Mod.EventBusSubscriber(Side.SERVER)
class ServerProxy : Proxy()

object GuiProxy : IGuiHandler {
	override fun getServerGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): Any? {
		val te = world.getTileEntity(BlockPos(x, y, z))
		if(te is TileFabricator)
			return ContainerFabricator(player.inventory, te)
		else if(te is TileExtractor)
			return ContainerExtractor(player.inventory, te)
		return null
	}
	override fun getClientGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): Any? {
		val te = world.getTileEntity(BlockPos(x, y, z))
		if(te is TileFabricator)
			return GuiContainerFabricator(ContainerFabricator(player.inventory, te), te, player.inventory)
		else if(te is TileExtractor)
			return GuiContainerExtractor(ContainerExtractor(player.inventory, te), te, player.inventory)
		return null
	}
}