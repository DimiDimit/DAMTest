package net.dimitrodam.forgetest.util

import net.minecraftforge.common.DimensionManager

object DTUtil {
	private val registeredDimensions = mutableListOf<Int>()

	fun findFreeDimensionID(): Int {
		for(i in 2 until Integer.MAX_VALUE) {
			if(!registeredDimensions.contains(i) && !DimensionManager.isDimensionRegistered(i)) {
				registeredDimensions.add(i)
				return i
			}
		}
		throw RuntimeException("Cannot find free dimension ID!")
	}
}