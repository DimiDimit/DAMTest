package net.dimitrodam.forgetest.block

import net.dimitrodam.forgetest.DTItems
import net.dimitrodam.forgetest.util.DTBlock
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.item.Item
import java.util.*

class BlockMatter : DTBlock(Material.ROCK, "matter_block")

class BlockRainbowOre : DTBlock(Material.ROCK, "rainbow_ore") {
	init { setHarvestLevel("pickaxe", 3) }
	override fun getItemDropped(state: IBlockState, rand: Random, fortune: Int): Item = DTItems.rainbowIngot
}
class BlockRainbow : DTBlock(Material.ROCK, "rainbow_block") { init { setHarvestLevel("pickaxe", 4) } }