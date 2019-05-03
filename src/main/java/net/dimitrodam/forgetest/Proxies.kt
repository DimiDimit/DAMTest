package net.dimitrodam.forgetest

import net.dimitrodam.forgetest.block.BlockExtractor
import net.dimitrodam.forgetest.block.BlockFabricator
import net.dimitrodam.forgetest.container.ContainerExtractor
import net.dimitrodam.forgetest.container.ContainerFabricator
import net.dimitrodam.forgetest.guicontainer.GuiContainerExtractor
import net.dimitrodam.forgetest.guicontainer.GuiContainerFabricator
import net.dimitrodam.forgetest.item.ItemEntityIgniter
import net.dimitrodam.forgetest.item.ItemMatter
import net.dimitrodam.forgetest.tile.TileExtractor
import net.dimitrodam.forgetest.tile.TileFabricator
import net.minecraft.block.Block
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemBlock
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.client.event.ModelRegistryEvent
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.network.IGuiHandler
import net.minecraftforge.fml.common.network.NetworkRegistry
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.fml.relauncher.Side

@Mod.EventBusSubscriber
abstract class Proxy {
	fun preInit(event: FMLPreInitializationEvent) {}
	fun init(event: FMLInitializationEvent) {
		NetworkRegistry.INSTANCE.registerGuiHandler(DAMTest.instance, GuiProxy)
	}
	fun postInit(event: FMLPostInitializationEvent) {}

	companion object {
		@JvmStatic
		@SubscribeEvent
		fun registerBlocks(event: RegistryEvent.Register<Block>) {
			event.registry.registerAll(
					BlockFabricator(),
					BlockExtractor()
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

					ItemBlock(DTBlocks.fabricator).setRegistryName(DTBlocks.fabricator.registryName),
					ItemBlock(DTBlocks.extractor).setRegistryName(DTBlocks.extractor.registryName)
			)
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