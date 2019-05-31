package net.dimitrodam.forgetest.item

import baubles.api.BaubleType
import baubles.api.IBauble
import net.dimitrodam.forgetest.DAMTest
import net.dimitrodam.forgetest.DTDependantItems
import net.dimitrodam.forgetest.util.DTItem
import net.minecraft.client.resources.I18n
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.EntityLivingBase
import net.minecraft.init.Items
import net.minecraft.inventory.InventoryCrafting
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipe
import net.minecraft.item.crafting.Ingredient
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.potion.Potion
import net.minecraft.potion.PotionEffect
import net.minecraft.potion.PotionUtils
import net.minecraft.util.NonNullList
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World
import net.minecraftforge.registries.IForgeRegistryEntry

class ItemPotionRing : DTItem("potion_ring"), IBauble {
	class PotionRingRecipe(private val group: String = "") : IForgeRegistryEntry.Impl<IRecipe>(), IRecipe {
		init {
			registryName = ResourceLocation(DAMTest.MODID, "potion_ring")
		}

		override fun canFit(width: Int, height: Int): Boolean = width * height >= 2
		override fun getRecipeOutput(): ItemStack = ItemStack(DTDependantItems.potionRing)
		override fun getGroup(): String = group
		override fun isDynamic(): Boolean = true

		override fun getCraftingResult(inv: InventoryCrafting): ItemStack {
			val default = ItemStack.EMPTY
			var fr = false
			var potion: Map<Potion, Int>? = null
			for(i in 0 until inv.sizeInventory) {
				val s = inv.getStackInSlot(i)
				when(s.item) {
					DTDependantItems.emptyPotionRing -> {
						if(!fr)
							fr = true
						else
							return default
					}
					Items.POTIONITEM -> {
						if(potion == null)
							potion = PotionUtils.getPotionFromItem(s).effects.map { Pair(it.potion, it.amplifier) }.toMap()
						else
							return default
					}
					Items.AIR -> {}
					else -> return default
				}
			}
			if(!fr || potion == null)
				return default
			return setPotions(ItemStack(DTDependantItems.potionRing), potion)
		}

		override fun matches(inv: InventoryCrafting, worldIn: World?): Boolean {
			var fr = false
			var fp = false
			for(i in 0 until inv.sizeInventory) {
				val s = inv.getStackInSlot(i)
				when(s.item) {
					DTDependantItems.emptyPotionRing -> {
						if(!fr)
							fr = true
						else
							return false
					}
					Items.POTIONITEM -> {
						if(!fp)
							fp = true
						else
							return false
					}
					Items.AIR -> {}
					else -> return false
				}
			}
			return fr && fp
		}

		override fun getIngredients(): NonNullList<Ingredient> = NonNullList.from(Ingredient.EMPTY, Ingredient.fromItem(DTDependantItems.potionRing), Ingredient.fromItem(Items.POTIONITEM))
	}
	class CombinationRecipe(private val group: String = "") : IForgeRegistryEntry.Impl<IRecipe>(), IRecipe {
		init {
			registryName = ResourceLocation(DAMTest.MODID, "potion_ring_combine")
		}

		override fun canFit(width: Int, height: Int): Boolean = width * height >= 2
		override fun getRecipeOutput(): ItemStack = ItemStack(DTDependantItems.potionRing)
		override fun getGroup(): String = group
		override fun isDynamic(): Boolean = true

		override fun getCraftingResult(inv: InventoryCrafting): ItemStack {
			val stacks = mutableListOf<ItemStack>()
			for(i in 0 until inv.sizeInventory) {
				val s = inv.getStackInSlot(i)
				if(s.item == DTDependantItems.potionRing)
					stacks.add(s)
			}
			val cm = mutableMapOf<Potion, Int>()
			for(s in stacks) {
				val p = getPotions(s)
				if(p != null)
					cm.putAll(p)
			}
			return setPotions(ItemStack(DTDependantItems.potionRing), cm)
		}

		override fun matches(inv: InventoryCrafting, worldIn: World?): Boolean {
			var found = 0
			for(i in 0 until inv.sizeInventory) {
				val stack = inv.getStackInSlot(i)
				if(stack.item == DTDependantItems.potionRing)
					found += 1
				else if(!stack.isEmpty)
					return false
			}
			return found >= 2
		}

		override fun getIngredients(): NonNullList<Ingredient> = NonNullList.withSize(2, Ingredient.fromItem(DTDependantItems.potionRing))
	}

	companion object {
		const val POTIONS = "potions"

		fun getPotions(stack: ItemStack): Map<Potion, Int>? {
			return try {
				stack.tagCompound?.getString(POTIONS)?.split(',')?.map { val s = it.split(':', limit = 2); Pair(Potion.getPotionById(s[0].toInt())!!, s[1].toInt()) }?.toMap()
			} catch(e: NumberFormatException) {
				null
			}
		}
		fun setPotions(stack: ItemStack, potions: Map<Potion, Int>?): ItemStack {
			if(potions == null)
				return stack
			val nbt = if(stack.hasTagCompound()) stack.tagCompound!! else NBTTagCompound()
			nbt.setString(POTIONS, potions.entries.joinToString(separator = ",") { "${Potion.getIdFromPotion(it.key)}:${it.value}" })
			stack.tagCompound = nbt
			return stack
		}
	}

	init {
		maxStackSize = 1
	}

	override fun getBaubleType(stack: ItemStack?): BaubleType = BaubleType.RING

	override fun onWornTick(stack: ItemStack, player: EntityLivingBase) {
		for(p in getPotions(stack)?.entries ?: return) {
			player.addPotionEffect(PotionEffect(p.key, 100, p.value, true, true))
		}
	}

	override fun hasEffect(stack: ItemStack): Boolean = true

	override fun addInformation(stack: ItemStack, worldIn: World?, tooltip: MutableList<String>, flagIn: ITooltipFlag) {
		val ps = getPotions(stack)?.entries
		if(ps != null) {
			for(p in ps) {
				tooltip.add("${I18n.format(p.key.name)} ${I18n.format("potion.potency.${p.value}")}")
			}
			tooltip.add("")
		}
		addInformationWithRegistryName(tooltip, "jeimi")
	}
}