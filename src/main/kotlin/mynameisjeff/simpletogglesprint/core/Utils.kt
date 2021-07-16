package mynameisjeff.simpletogglesprint.core

import mynameisjeff.simpletogglesprint.SimpleToggleSprint
import net.minecraft.client.settings.KeyBinding

fun shouldSetSprint(keyBinding: KeyBinding): Boolean {
    return keyBinding.isKeyDown || Config.enabledToggleSprint && Config.toggleSprintState && keyBinding === SimpleToggleSprint.mc.gameSettings.keyBindSprint && SimpleToggleSprint.mc.currentScreen == null
}