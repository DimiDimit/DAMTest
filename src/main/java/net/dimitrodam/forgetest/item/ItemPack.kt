package net.dimitrodam.forgetest.item

import net.dimitrodam.forgetest.DAMTest
import net.dimitrodam.forgetest.DTItems
import net.dimitrodam.forgetest.util.DTItem
import net.minecraft.client.renderer.ItemMeshDefinition
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.client.resources.I18n
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
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
			return setAction(ItemStack(DTItems.pack), strings.joinToString(separator = ","), stacks[0].tagCompound?.getString(TARGET) ?: "self")
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
	class TargetRecipe(private val group: String = "") : IForgeRegistryEntry.Impl<IRecipe>(), IRecipe {
		companion object {
			val TARGET_FROM_INGREDIENT: Map<Ingredient, String> = mapOf(
					Pair(Ingredient.fromItem(Items.FEATHER), "self"),
					Pair(Ingredient.fromItem(Items.FIRE_CHARGE), "rightclick")
			)
		}

		init {
			registryName = ResourceLocation(DAMTest.MODID, "pack_target")
		}

		override fun canFit(width: Int, height: Int): Boolean = width * height >= 2
		override fun getRecipeOutput(): ItemStack = ItemStack(DTItems.pack)
		override fun getGroup(): String = group
		override fun isDynamic(): Boolean = true

		override fun getCraftingResult(inv: InventoryCrafting): ItemStack {
			var pack: ItemStack? = null
			var target: String? = null
			for(i in 0 until inv.sizeInventory) {
				val s = inv.getStackInSlot(i)
				if(s.item == DTItems.pack)
					pack = s
				val t = TARGET_FROM_INGREDIENT.filterKeys { it.test(s) }.entries.iterator()
				if(t.hasNext())
					target = t.next().value
			}
			if(pack == null || target == null) return ItemStack(DTItems.pack)
			return setAction(ItemStack(DTItems.pack), pack.tagCompound?.getString(ACTIONS), target)
		}

		override fun matches(inv: InventoryCrafting, worldIn: World): Boolean {
			var foundPack = false
			var foundTarget = false
			for(i in 0 until inv.sizeInventory) {
				val s = inv.getStackInSlot(i)
				if(s.item == DTItems.pack) {
					if(!foundPack) foundPack = true
					else return false
				} else if(TARGET_FROM_INGREDIENT.keys.count { it.test(s) } >= 1) {
					if(!foundTarget) foundTarget = true
					else return false
				} else if(s.item != Items.AIR)
					return false
			}
			return foundPack && foundTarget
		}
	}

	companion object {
		const val REGISTRY_NAME = "pack"
		const val ACTIONS = "actions"
		const val TARGET = "target"

		@Suppress("UNUSED_ANONYMOUS_PARAMETER")
		val PACKS: MutableMap<String, Pair<(worldIn: World, target: Entity, stack: ItemStack) -> Boolean, (worldIn: World, target: Entity, stack: ItemStack) -> Any?>> =
				mutableMapOf(
						Pair("health", Pair(
								{ worldIn: World, target: Entity, stack: ItemStack -> target is EntityLivingBase && target.health < target.maxHealth },
								{ worldIn: World, target: Entity, stack: ItemStack -> if(target !is EntityLivingBase) return@Pair; target.heal(target.maxHealth - target.health) })
						),
						Pair("hunger", Pair(
								{ worldIn: World, target: Entity, stack: ItemStack -> if(target !is EntityPlayer) return@Pair false; val food = target.foodStats; food.foodLevel < 20 && food.saturationLevel < 20f },
								{ worldIn: World, target: Entity, stack: ItemStack ->
									if(target !is EntityPlayer) return@Pair
									val food = target.foodStats
									target.heal(target.maxHealth - target.health)
									food.foodLevel = 20
									food.setFoodSaturationLevel(20f)
								})
						),
						Pair("fire", Pair(
								{ worldIn: World, target: Entity, stack: ItemStack -> !target.isBurning },
								{ worldIn: World, target: Entity, stack: ItemStack -> target.setFire(10) })
						),
						Pair("extinguish", Pair(
								{ worldIn: World, target: Entity, stack: ItemStack -> target.isBurning },
								{ worldIn: World, target: Entity, stack: ItemStack -> target.extinguish() })
						),
						Pair("day", Pair(
								{ worldIn: World, target: Entity, stack: ItemStack -> true },
								{ worldIn: World, target: Entity, stack: ItemStack ->
									worldIn.worldTime = 6000 // time command: 1000
								})
						),
						Pair("night", Pair(
								{ worldIn: World, target: Entity, stack: ItemStack -> true },
								{ worldIn: World, target: Entity, stack: ItemStack ->
									worldIn.worldTime = 18000 // time command: 13000
								})
						)
				)

		fun setAction(stack: ItemStack, action: String? = null, target: String? = "self"): ItemStack {
			val nbt = if(stack.hasTagCompound()) stack.tagCompound!! else NBTTagCompound()
			if(action != null) nbt.setString(ACTIONS, action)
			if(target != null) nbt.setString(TARGET, target)
			stack.tagCompound = nbt
			return stack
		}
	}

	override fun getItemStackDisplayName(stack: ItemStack): String {
		val nbt = stack.tagCompound ?: return super.getItemStackDisplayName(stack)
		val actions = nbt.getString(ACTIONS).split(',')
		val target = nbt.getString(TARGET) ?: return super.getItemStackDisplayName(stack)
		val un = getUnlocalizedName(stack)
		return actions.joinToString(prefix = "${I18n.format("$un.target.$target")} ",
				postfix = " ${super.getItemStackDisplayName(stack)}"){ I18n.format("$un.$it") }.trim()
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
		val nbt = stack.tagCompound ?: return super.onItemRightClick(worldIn, playerIn, handIn)
		val targetS = nbt.getString(TARGET) ?: return super.onItemRightClick(worldIn, playerIn, handIn)
		if(targetS == "self") {
			val action = nbt.getString(ACTIONS) ?: return super.onItemRightClick(worldIn, playerIn, handIn)
			var metConditions = true
			val toExecute = mutableListOf<(worldIn: World, target: Entity, stack: ItemStack) -> Any?>()
			for(a in action.split(',')) {
				val p = PACKS[a] ?: return super.onItemRightClick(worldIn, playerIn, handIn)
				if(!p.first(worldIn, playerIn, stack)) {
					metConditions = false
					break
				}
				toExecute.add(p.second)
			}
			if(!metConditions) return super.onItemRightClick(worldIn, playerIn, handIn)
			for(a in toExecute) a(worldIn, playerIn, stack)
			stack.shrink(1)
			return ActionResult(EnumActionResult.SUCCESS, stack)
		}
		return super.onItemRightClick(worldIn, playerIn, handIn)
	}

	override fun itemInteractionForEntity(stack: ItemStack, playerIn: EntityPlayer, target: EntityLivingBase, hand: EnumHand): Boolean {
		val worldIn = playerIn.world
		val nbt = stack.tagCompound ?: return super.itemInteractionForEntity(stack, playerIn, target, hand)
		val targetS = nbt.getString(TARGET) ?: return super.itemInteractionForEntity(stack, playerIn, target, hand)
		if(targetS == "rightclick") {
			val action = nbt.getString(ACTIONS) ?: return super.itemInteractionForEntity(stack, playerIn, target, hand)
			var metConditions = true
			val toExecute = mutableListOf<(worldIn: World, target: Entity, stack: ItemStack) -> Any?>()
			for(a in action.split(',')) {
				val p = PACKS[a] ?: return super.itemInteractionForEntity(stack, playerIn, target, hand)
				if(!p.first(worldIn, target, stack)) {
					metConditions = false
					break
				}
				toExecute.add(p.second)
			}
			if(!metConditions) return super.itemInteractionForEntity(stack, playerIn, target, hand)
			for(a in toExecute) a(worldIn, target, stack)
			stack.shrink(1)
			return true
		}
		return super.itemInteractionForEntity(stack, playerIn, target, hand)
	}
}