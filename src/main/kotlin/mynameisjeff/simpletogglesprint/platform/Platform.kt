@file:JvmName("Platform")

package mynameisjeff.simpletogglesprint.platform

import mynameisjeff.simpletogglesprint.tweaker.FMLLoadingPlugin
import net.minecraft.entity.Entity

val is1_12_2 by lazy {
    FMLLoadingPlugin.mcVersion.startsWith("1.12")
}

val renderOrdinal by lazy {
    if (!is1_12_2) 9 else 10
}

fun isRiding(entity: Entity) = entity.isRiding