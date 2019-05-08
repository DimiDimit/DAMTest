package net.dimitrodam.forgetest.util

fun Double.isWhole() = Math.floor(this) == this
fun Float.isWhole() = this.toDouble().isWhole()