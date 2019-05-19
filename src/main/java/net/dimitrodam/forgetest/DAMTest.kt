package net.dimitrodam.forgetest

import net.dimitrodam.forgetest.tile.TileFabricator
import net.minecraft.block.material.MapColor
import net.minecraft.block.material.Material
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.init.Items
import net.minecraft.init.SoundEvents
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraftforge.common.util.EnumHelper
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.SidedProxy
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.apache.logging.log4j.Logger


@Suppress("unused")
@Mod(modid = DAMTest.MODID, name = DAMTest.NAME, version = DAMTest.VERSION, acceptedMinecraftVersions = DAMTest.ACCEPTED_MINECRAFT_VERSIONS, modLanguageAdapter = "net.shadowfacts.forgelin.KotlinAdapter")
@Mod.EventBusSubscriber
object DAMTest {
	const val MODID = "damtest"
	const val NAME = "DimitrodAM Test"
	const val VERSION = "1.12.2-1.0.1.0"
	const val ACCEPTED_MINECRAFT_VERSIONS = "[1.12.2]"

	@JvmStatic
	@SidedProxy(clientSide = "net.dimitrodam.forgetest.ClientProxy", serverSide = "net.dimitrodam.forgetest.ServerProxy")
	lateinit var proxy: Proxy
	@JvmStatic
	@Mod.Instance
	lateinit var instance: DAMTest
	lateinit var logger: Logger

	//region Creative Tabs
	val TAB_DT = object : CreativeTabs("damtest") {
		@SideOnly(Side.CLIENT)
		override fun getTabIconItem(): ItemStack = ItemStack(DTBlocks.fabricator)
	}
	//endregion

	//region Materials
	val MATERIAL_ROCK_HAND = Material(MapColor.STONE)

	val TOOL_MATERIAL_RAINBOW = EnumHelper.addToolMaterial("rainbow", 4, 6000, 14.0F, 6.0F, 30)!!

	val ARMOR_MATERIAL_RAINBOW = EnumHelper.addArmorMaterial("rainbow", ResourceLocation(MODID, "rainbow").toString(), 6000, intArrayOf(10, 10, 10, 10), 30, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 5.0F)!!
	//endregion

	//region Initialization Events
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
		TileFabricator.initRecipes()
	}
	@JvmStatic
	@Mod.EventHandler
	fun postInit(event: FMLPostInitializationEvent) {
		proxy.postInit(event)
	}
	//endregion

	//region Events
	@JvmStatic
	@SubscribeEvent
	fun finishUseItem(event: LivingEntityUseItemEvent.Finish) {
		val item = event.item
		val entity = event.entityLiving
		if(item.item == Items.POTIONITEM && item.tagCompound?.getString("Potion") == "minecraft:water" && entity.isBurning)
			entity.extinguish()
	}
	//endregion
}