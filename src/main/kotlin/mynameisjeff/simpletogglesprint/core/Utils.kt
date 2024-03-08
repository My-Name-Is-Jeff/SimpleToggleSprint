/*
 *   SimpleToggleSprint
 *   Copyright (C) 2021  My-Name-Is-Jeff
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Affero General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public License
 *   along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package mynameisjeff.simpletogglesprint.core

import gg.essential.universal.UKeyboard
import gg.essential.universal.UScreen
import mynameisjeff.simpletogglesprint.SimpleToggleSprint.gameSettings
import net.minecraft.client.gui.Gui
import net.minecraft.client.settings.KeyBinding
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.common.ForgeVersion
import org.lwjgl.input.Mouse
import java.lang.invoke.MethodHandle
import java.lang.invoke.MethodHandles
import java.lang.invoke.MethodType
import kotlin.reflect.full.staticProperties

val is1_12_2 by lazy { 
    (ForgeVersion::class.staticProperties.find { 
        it.name == "mcVersion"
    }!!.get() as String).startsWith("1.12")
}

val drawRect1_12: MethodHandle by lazy {
    MethodHandles.publicLookup().findStatic(Gui::class.java,"drawRect", MethodType.methodType(Void.TYPE, Int::class.javaPrimitiveType, Int::class.javaPrimitiveType, Int::class.javaPrimitiveType, Int::class.javaPrimitiveType, Int::class.javaPrimitiveType))
}

val isRiding1_12: MethodHandle by lazy {
    MethodHandles.publicLookup().findVirtual(EntityPlayer::class.java, "func_184218_aH", MethodType.methodType(Boolean::class.javaPrimitiveType))
}

fun shouldSetSprint(keyBinding: KeyBinding): Boolean {
    return keyBinding.isKeyDown || UScreen.currentScreen == null && Config.enabledToggleSprint && Config.toggleSprintState && keyBinding === gameSettings.keyBindSprint
}

fun shouldSetSneak(keyBinding: KeyBinding): Boolean {
    return keyBinding.isKeyDown || UScreen.currentScreen == null && Config.enabledToggleSneak && Config.toggleSneakState && keyBinding === gameSettings.keyBindSneak
}

fun checkKeyCode(keyCode: Int) = if (keyCode > 0) UKeyboard.isKeyDown(keyCode) else Mouse.isButtonDown(
    keyCode + 100
)

fun drawRect(left: Int, top: Int, right: Int, bottom: Int, color: Int) = if (!is1_12_2) Gui.drawRect(left, top, right, bottom, color) else drawRect1_12.invokeExact(left, top, right, bottom, color)

fun isRiding(entity: EntityPlayer): Boolean = if (!is1_12_2) entity.isRiding else isRiding1_12.invokeExact(entity) as Boolean