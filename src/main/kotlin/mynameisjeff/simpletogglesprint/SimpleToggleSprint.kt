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
package mynameisjeff.simpletogglesprint

import gg.essential.universal.*
import mynameisjeff.simpletogglesprint.commands.SimpleToggleSprintCommand
import mynameisjeff.simpletogglesprint.core.*
import mynameisjeff.simpletogglesprint.platform.isRiding
import mynameisjeff.simpletogglesprint.platform.renderOrdinal
import net.minecraft.client.gui.Gui
import net.minecraft.client.settings.KeyBinding
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent
import java.awt.Color

@Mod(
    modid = SimpleToggleSprint.MODID,
    name = SimpleToggleSprint.MOD_NAME,
    version = SimpleToggleSprint.VERSION,
    acceptedMinecraftVersions = "[1.8.9,1.12.2]",
    clientSideOnly = true,
    modLanguage = "kotlin",
    modLanguageAdapter = "gg.essential.api.utils.KotlinAdapter"
)
object SimpleToggleSprint {

    const val MODID = Reference.MOD_ID
    const val MOD_NAME = Reference.MOD_NAME
    const val VERSION = Reference.VERSION
    val player
        get() = UMinecraft.getPlayer()
    val gameSettings
        get() = UMinecraft.getSettings()
    val keySprint = KeyBinding("Toggle Sprint", UKeyboard.KEY_NONE, "SimpleToggleSprint")
    val keySneak = KeyBinding("Toggle Sneak", UKeyboard.KEY_NONE, "SimpleToggleSprint")

    var sprintHeld = false
    var sneakHeld = false

    @Mod.EventHandler
    fun onInit(event: FMLInitializationEvent) {
        Config.preload()
        MinecraftForge.EVENT_BUS.register(this)
    }

    @Mod.EventHandler
    fun onPostInit(event: FMLPostInitializationEvent) {
        SimpleToggleSprintCommand.register()
        ClientRegistry.registerKeyBinding(keySprint)
        ClientRegistry.registerKeyBinding(keySneak)

        if (Config.lastLaunchedVersion != VERSION) differentVersion(Config.lastLaunchedVersion)
        Config.lastLaunchedVersion = VERSION
    }

    @Mod.EventHandler
    fun onLoad(event: FMLLoadCompleteEvent) {
        UpdateChecker.checkUpdate()
    }


    @SubscribeEvent
    fun onInput(event: InputEvent) {
        val sprint = gameSettings.keyBindSprint.keyCode
        val sneak = gameSettings.keyBindSneak.keyCode
        val sprintToggle = keySprint.keyCode
        val sneakToggle = keySneak.keyCode
        if ((Config.keybindToggleSprint && checkKeyCode(sprintToggle)) || (!Config.keybindToggleSprint && checkKeyCode(sprint))) {
            if (Config.enabledToggleSprint && !sprintHeld) {
                Config.toggleSprintState = !Config.toggleSprintState
                Config.markDirty()
            }
            sprintHeld = true
        } else {
            sprintHeld = false
        }
        if ((Config.keybindToggleSneak && checkKeyCode(sneakToggle)) || (!Config.keybindToggleSneak && checkKeyCode(sneak))) {
            if (Config.enabledToggleSneak && !sneakHeld) {
                Config.toggleSneakState = !Config.toggleSneakState
                Config.markDirty()
            }
            sneakHeld = true
        } else {
            sneakHeld = false
        }
    }

    @SubscribeEvent
    fun onRenderGameOverlay(event: RenderGameOverlayEvent.Post) {
        if (event.type.ordinal != renderOrdinal) return
        if (Config.displayToggleState && (UScreen.currentScreen == null || Config.showInGui) && player != null) {
            val sr = UResolution
            val fr = UMinecraft.getFontRenderer()
            val active = DisplayState.activeDisplay ?: return
            val x = sr.scaledWidth * (Config.displayStateX / 100.0)
            val y = sr.scaledHeight * (Config.displayStateY / 100.0)
            val scale = (Config.displayStateScale / 100f).toDouble()
            val xOffset =
                (if (Config.displayStateAlignment != 0) -fr.getStringWidth(active) / Config.displayStateAlignment.toFloat() else 0f) * scale
            UGraphics.enableBlend()
            UGraphics.tryBlendFuncSeparate(770, 771, 1, 0)
            UGraphics.GL.pushMatrix()
            UGraphics.GL.translate(x, y, 0.0)
            UGraphics.GL.scale(scale, scale, 1.0)
            fr.drawString(
                active, xOffset.toFloat(), 0f, if (Config.useChromaColor) Color.HSBtoRGB(
                    System.currentTimeMillis() % 2000L / 2000.0f, 0.8f, 0.8f
                ) else Config.displayColor.rgb, Config.displayStateShadow
            )
            if (Config.displayBackground) {
                UGraphics.GL.translate(0f, 0f, -1f)
                Gui.drawRect(
                    xOffset.toInt() - 2,
                    -2,
                    xOffset.toInt() + fr.getStringWidth(active) + 1,
                    fr.FONT_HEIGHT + 1,
                    Int.MIN_VALUE
                )
                UGraphics.GL.translate(0f, 0f, 1f)
            }
            UGraphics.GL.popMatrix()
            UGraphics.disableBlend()
        }
    }

    private fun differentVersion(old: String) {
        if (old.startsWith("0")) {
            if (Config.displayStateScale != 108.5f) Config.displayStateScale /= 10
            if (Config.displayStateX != 0.2f) Config.displayStateX /= 10
            if (Config.displayStateY != 97.4f) Config.displayStateY /= 10

            Config.markDirty()
        }
    }

    @Suppress("unused")
    private enum class DisplayState(val displayText: () -> String, val displayCheck: (EntityPlayer) -> Boolean) {
        DESCENDINGHELD({ Config.descendingHeld }, { it.capabilities.isFlying && it.isSneaking && sneakHeld }),
        DESCENDINGTOGGLED({ Config.descendingToggled }, { it.capabilities.isFlying && Config.enabledToggleSneak && Config.toggleSneakState }),
        DESCENDING({ Config.descending }, { it.capabilities.isFlying && it.isSneaking }),
        FLYING({ Config.flying }, { it.capabilities.isFlying }),
        RIDING({ Config.riding }, { isRiding(it) }),
        SNEAKHELD({ Config.sneakHeld }, { it.isSneaking && sneakHeld }),
        TOGGLESNEAK({ Config.sneakToggle }, { Config.enabledToggleSneak && Config.toggleSneakState }),
        SNEAKING({ Config.sneak }, { it.isSneaking }),
        SPRINTHELD({ Config.sprintHeld }, { it.isSprinting && sprintHeld }),
        TOGGLESPRINT({ Config.sprintToggle }, { Config.enabledToggleSprint && Config.toggleSprintState }),
        SPRINTING({ Config.sprint }, { it.isSprinting });

        val isActive: Boolean
            get() = displayCheck(player!!)

        companion object {
            private val items by lazy {
                return@lazy if (KotlinVersion.CURRENT.isAtLeast(1, 9)) entries else values().toList()
            }

            val activeDisplay: String?
                get() {
                    if (player == null) return null
                    return items.find { it.isActive }?.displayText?.invoke()
                }
        }
    }
}