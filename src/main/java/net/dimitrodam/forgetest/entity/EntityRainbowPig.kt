package net.dimitrodam.forgetest.entity

import net.dimitrodam.forgetest.DAMTest
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.client.renderer.entity.RenderPig
import net.minecraft.entity.EntityAgeable
import net.minecraft.entity.passive.EntityPig
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World

class EntityRainbowPig(worldIn: World) : EntityPig(worldIn) {
	override fun createChild(ageable: EntityAgeable): EntityRainbowPig = EntityRainbowPig(world)
}

class RenderRainbowPig(renderManager: RenderManager) : RenderPig(renderManager) {
	companion object {
		private val RAINBOW_PIG_TEXTURES = ResourceLocation(DAMTest.MODID, "textures/entity/rainbow_pig/rainbow_pig.png")
	}

	override fun getEntityTexture(entity: EntityPig): ResourceLocation = RAINBOW_PIG_TEXTURES
}