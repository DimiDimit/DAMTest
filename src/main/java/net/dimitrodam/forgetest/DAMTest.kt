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
	const val VERSION = "1.12.2-1.0.5.0"
	const val ACCEPTED_MINECRAFT_VERSIONS = "[1.12.2]"

	@JvmStatic
	@SidedProxy(clientSide = "net.dimitrodam.forgetest.ClientProxy", serverSide = "net.dimitrodam.forgetest.ServerProxy")
	lateinit var proxy: Proxy
	@JvmStatic
	@Mod.Instance
	lateinit var instance: DAMTest
	lateinit var logger: Logger

	//region Definitions
	val TAB_DT = object : CreativeTabs("damtest") {
		@SideOnly(Side.CLIENT)
		override fun getTabIconItem(): ItemStack = ItemStack(DTBlocks.fabricator)
	}

	val MATERIAL_ROCK_HAND = Material(MapColor.STONE)
	val TOOL_MATERIAL_RAINBOW = EnumHelper.addToolMaterial("rainbow", 4, 6000, 14.0F, 6.0F, 30)!!
	val ARMOR_MATERIAL_RAINBOW = EnumHelper.addArmorMaterial("rainbow", ResourceLocation(MODID, "rainbow").toString(), 400, intArrayOf(10, 11, 12, 10), 30, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 5.0F)!!
	val TOOL_MATERIAL_WHITE_ROD = EnumHelper.addToolMaterial("white_rod", 5, 8000, 30.0F, 3.0F, 4)!!
	val ARMOR_MATERIAL_WHITE_ROD = EnumHelper.addArmorMaterial("white_rod", ResourceLocation(MODID, "white_rod").toString(), 533, intArrayOf(14, 15, 16, 14), 4, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 4.0F)!!
	val TOOL_MATERIAL_BLAZE = EnumHelper.addToolMaterial("blaze", 3, 1561, 8.0F, 3.0F, 10)!!
	val ARMOR_MATERIAL_BLAZE = EnumHelper.addArmorMaterial("blaze", ResourceLocation(MODID, "blaze").toString(), 33, intArrayOf(3, 6, 8, 3), 10, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 2.0f)!!
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