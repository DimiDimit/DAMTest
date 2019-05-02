package net.dimitrodam.forgetest

import net.minecraft.block.material.MapColor
import net.minecraft.block.material.Material
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.SidedProxy
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.apache.logging.log4j.Logger


@Suppress("unused")
@Mod(modid = DAMTest.MODID, name = DAMTest.NAME, version = DAMTest.VERSION, acceptedMinecraftVersions = DAMTest.ACCEPTED_MINECRAFT_VERSIONS, modLanguageAdapter = "net.shadowfacts.forgelin.KotlinAdapter")
object DAMTest {
	const val MODID = "damtest"
	const val NAME = "DimitrodAM Test"
	const val VERSION = "1.0.0"
	const val ACCEPTED_MINECRAFT_VERSIONS = "[1.12.2]"

	@JvmStatic
	@SidedProxy(clientSide = "net.dimitrodam.forgetest.ClientProxy", serverSide = "net.dimitrodam.forgetest.ServerProxy")
	lateinit var proxy: Proxy
	@JvmStatic
	@Mod.Instance
	lateinit var instance: DAMTest
	lateinit var logger: Logger

	val TAB_DT = object : CreativeTabs("damtest") {
		@SideOnly(Side.CLIENT)
		override fun getTabIconItem(): ItemStack = ItemStack(DTBlocks.fabricator)
	}

	val MATERIAL_ROCK_HAND = Material(MapColor.STONE)

	@JvmStatic
	@Mod.EventHandler
	fun preInit(event: FMLPreInitializationEvent) {
		logger = event.modLog
		proxy.preInit(event)
	}
	@JvmStatic
	@Mod.EventHandler
	fun init(event: FMLInitializationEvent) {
		proxy.init(event)
	}
	@JvmStatic
	@Mod.EventHandler
	fun postInit(event: FMLPostInitializationEvent) {
		proxy.postInit(event)
	}
}