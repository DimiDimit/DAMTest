package net.dimitrodam.forgetest.item

import net.dimitrodam.forgetest.DAMTest
import net.dimitrodam.forgetest.DTItems
import net.dimitrodam.forgetest.util.DTItem
import net.minecraft.client.renderer.ItemMeshDefinition
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.client.resources.I18n
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.InventoryCrafting
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipe
import net.minecraft.item.crafting.Ingredient
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.*
import net.minecraft.world.World
import net.minecraftforge.registries.IForgeRegistryEntry

class ItemPack : DTItem(REGISTRY_NAME) {
	class ItemMesh : ItemMeshDefinition {
		companion object {
			fun getVariants(): Array<out ResourceLocation> {
				val rvs = mutableListOf<ResourceLocation>()
				val vs = mutableListOf(*PACKS.keys.toTypedArray(), "", "invalid", "multi")
				for(v in vs) {
					rvs.add(ResourceLocation(DAMTest.MODID, "$REGISTRY_NAME${if(v != "") "_" else ""}$v"))
				}
				return rvs.toTypedArray()
			}
		}

		override fun getModelLocation(stack: ItemStack): ModelResourceLocation? {
			var s = stack.tagCompound?.getString(ACTIONS) ?: "invalid"
			if(s.contains(','))
				s = "multi"
			return ModelResourceLocation(ResourceLocation(DAMTest.MODID, "${REGISTRY_NAME}_$s"), "inventory")
		}
	}
	class CombinationRecipe(private val group: String = "") : IForgeRegistryEntry.Impl<IRecipe>(), IRecipe {
		init {
			registryName = ResourceLocation(DAMTest.MODID, "pack_combine")
		}

		override fun canFit(width: Int, height: Int): Boolean = width * height >= 2
		override fun getRecipeOutput(): ItemStack = ItemStack(DTItems.pack)
		override fun getGroup(): String = group
		override fun isDynamic(): Boolean = true

		override fun getCraftingResult(inv: InventoryCrafting): ItemStack {
			val stacks = mutableListOf<ItemStack>()
			for(i in 0 until inv.sizeInventory) {
				val s = inv.getStackInSlot(i)
				if(s.item == DTItems.pack)
					stacks.add(s)
			}
			val strings = mutableListOf<String>()
			for(s in stacks) strings.add(s.tagCompound?.getString(ACTIONS) ?: return ItemStack(DTItems.pack))
			return setAction(ItemStack(DTItems.pack), strings.joinToString(separator = ","))
		}

		override fun matches(inv: InventoryCrafting, worldIn: World): Boolean {
			var found = 0
			for(i in 0 until inv.sizeInventory) {
				if(inv.getStackInSlot(i).item == DTItems.pack)
					found += 1
				if(found >= 2)
					return true
			}
			return false
		}

		override fun getIngredients(): NonNullList<Ingredient> = NonNullList.withSize(2, Ingredient.fromItem(DTItems.pack))
	}

	companion object {
		const val REGISTRY_NAME = "pack"
		const val ACTIONS = "actions"

		@Suppress("UNUSED_ANONYMOUS_PARAMETER")
		val PACKS: MutableMap<String, Pair<(worldIn: World, playerIn: EntityPlayer, handIn: EnumHand, stack: ItemStack) -> Boolean, (worldIn: World, playerIn: EntityPlayer, handIn: EnumHand, stack: ItemStack) -> Unit>> =
				mutableMapOf(
						Pair("health", Pair(
								{ worldIn: World, playerIn: EntityPlayer, handIn: EnumHand, stack: ItemStack -> playerIn.shouldHeal() },
								{ worldIn: World, playerIn: EntityPlayer, handIn: EnumHand, stack: ItemStack -> playerIn.heal(playerIn.maxHealth - playerIn.health) })
						), Pair("hunger", Pair(
								{ worldIn: World, playerIn: EntityPlayer, handIn: EnumHand, stack: ItemStack -> val food = playerIn.foodStats; food.foodLevel < 20 && food.saturationLevel < 20f },
								{ worldIn: World, playerIn: EntityPlayer, handIn: EnumHand, stack: ItemStack ->
									val food = playerIn.foodStats
									playerIn.heal(playerIn.maxHealth - playerIn.health)
									food.foodLevel = 20
									food.setFoodSaturationLevel(20f)
								})
						)
				)

		fun setAction(stack: ItemStack, action: String): ItemStack {
			val nbt = if(stack.hasTagCompound()) stack.tagCompound!! else NBTTagCompound()
			nbt.setString(ACTIONS, action)
			stack.tagCompound = nbt
			return stack
		}
	}

	override fun getItemStackDisplayName(stack: ItemStack): String {
		val actions = stack.tagCompound?.getString(ACTIONS)?.split(',') ?: return super.getItemStackDisplayName(stack)
		val un = getUnlocalizedName(stack)
		return actions.joinToString(postfix = " ${super.getItemStackDisplayName(stack)}"){
			I18n.format("$un.$it") }.trim()
	}

	override fun getSubItems(tab: CreativeTabs, items: NonNullList<ItemStack>) {
		if(isInCreativeTab(tab)) {
			for(p in PACKS.keys) {
				items.add(setAction(ItemStack(this), p))
			}
		}
	}

	override fun onItemRightClick(worldIn: World, playerIn: EntityPlayer, handIn: EnumHand): ActionResult<ItemStack> {
		val stack = playerIn.getHeldItem(handIn)
		val action = stack.tagCompound?.getString(ACTIONS) ?: return super.onItemRightClick(worldIn, playerIn, handIn)
		var metConditions = true
		val toExecute = mutableListOf<(worldIn: World, playerIn: EntityPlayer, handIn: EnumHand, stack: ItemStack) -> Unit>()
		for(a in action.split(',')) {
			val p = PACKS[a] ?: return super.onItemRightClick(worldIn, playerIn, handIn)
			if(!p.first(worldIn, playerIn, handIn, stack)) {
				metConditions = false
				break
			}
			toExecute.add(p.second)
		}
		if(!metConditions) return super.onItemRightClick(worldIn, playerIn, handIn)
		for(a in toExecute) a(worldIn, playerIn, handIn, stack)
		stack.shrink(1)
		return ActionResult(EnumActionResult.SUCCESS, stack)
	}
}