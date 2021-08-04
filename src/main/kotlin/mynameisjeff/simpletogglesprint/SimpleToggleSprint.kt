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

import gg.essential.universal.UGraphics
import gg.essential.universal.UMinecraft
import mynameisjeff.simpletogglesprint.commands.SimpleToggleSprintCommand
import mynameisjeff.simpletogglesprint.core.Config
import mynameisjeff.simpletogglesprint.core.UpdateChecker
import net.minecraft.client.gui.Gui
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.settings.KeyBinding
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent
import org.lwjgl.input.Keyboard
import org.lwjgl.input.Mouse
import java.awt.Color
import java.util.function.Supplier

@Mod(
    modid = SimpleToggleSprint.MODID,
    name = SimpleToggleSprint.MOD_NAME,
    version = SimpleToggleSprint.VERSION,
    acceptedMinecraftVersions = "[1.8.9]",
    clientSideOnly = true,
    modLanguage = "kotlin",
    modLanguageAdapter = "gg.essential.api.utils.KotlinAdapter"
)
object SimpleToggleSprint {

    const val MODID = "simpletogglesprint"
    const val MOD_NAME = "SimpleToggleSprint"
    const val VERSION = "2.0"
    val mc
        get() =  UMinecraft.getMinecraft()
    val keySprint = KeyBinding("Toggle Sprint", Keyboard.KEY_NONE, "SimpleToggleSprint")
    val keySneak = KeyBinding("Toggle Sneak", Keyboard.KEY_NONE, "SimpleToggleSprint")

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
        val sprint = mc.gameSettings.keyBindSprint.keyCode
        val sneak = mc.gameSettings.keyBindSneak.keyCode
        val sprintToggle = keySprint.keyCode
        val sneakToggle = keySneak.keyCode
        if (if (Config.keybindToggleSprint && sprintToggle > 0) Keyboard.isKeyDown(sprintToggle) else Mouse.isButtonDown(
                sprintToggle + 100
            )
        ) {
            if (Config.enabledToggleSprint && !sprintHeld) {
                Config.toggleSprintState = !Config.toggleSprintState
                Config.markDirty()
            }
            sprintHeld = true
        } else if (if (!Config.keybindToggleSprint && sprint > 0) Keyboard.isKeyDown(sprint) else Mouse.isButtonDown(
                sprint + 100
            )
        ) {
            if (Config.enabledToggleSprint && !sprintHeld) {
                Config.toggleSprintState = !Config.toggleSprintState
                Config.markDirty()
            }
            sprintHeld = true
        } else {
            sprintHeld = false
        }
        if (if (Config.keybindToggleSneak && sneakToggle > 0) Keyboard.isKeyDown(sneakToggle) else Mouse.isButtonDown(
                sneakToggle + 100
            )
        ) {
            if (Config.enabledToggleSneak && !sneakHeld) {
                Config.toggleSneakState = !Config.toggleSneakState
                Config.markDirty()
            }
            sneakHeld = true
        } else if (if (!Config.keybindToggleSprint && sneak > 0) Keyboard.isKeyDown(sneak) else Mouse.isButtonDown(
                sneak + 100
            )
        ) {
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
        if (event.type != RenderGameOverlayEvent.ElementType.HOTBAR) return
        if (Config.displayToggleState && (mc.currentScreen == null || Config.showInGui) && mc.thePlayer != null) {
            val sr = ScaledResolution(mc)
            val fr = UMinecraft.getFontRenderer()
            val active = DisplayState.activeDisplay ?: return
            val x = sr.scaledWidth_double * (Config.displayStateX / 100.0)
            val y = sr.scaledHeight_double * (Config.displayStateY / 100.0)
            val scale = (Config.displayStateScale / 100f).toDouble()
            val xOffset =
                (if (Config.displayStateAlignment != 0) -fr.getStringWidth(active) / Config.displayStateAlignment.toFloat() else 0f) * scale
            UGraphics.pushMatrix()
            UGraphics.translate(x, y, 0.0)
            UGraphics.scale(scale, scale, 1.0)
            fr.drawString(
                active, xOffset.toFloat(), 0f, if (Config.useChromaColor) Color.HSBtoRGB(
                    System.currentTimeMillis() % 2000L / 2000.0f, 0.8f, 0.8f
                ) else Config.displayColor.rgb, Config.displayStateShadow
            )
            if (Config.displayBackground) {
                UGraphics.translate(0f, 0f, -1f)
                Gui.drawRect(
                    xOffset.toInt() - 2,
                    -2,
                    xOffset.toInt() + fr.getStringWidth(active) + 1,
                    fr.FONT_HEIGHT + 1,
                    0
                )
                UGraphics.translate(0f, 0f, 1f)
            }
            UGraphics.popMatrix()
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
    private enum class DisplayState(val displayText: String, val displayCheck: () -> Boolean) {
        DESCENDINGHELD(
            "[Descending (key held)]",
            { mc.thePlayer.capabilities.isFlying && mc.thePlayer.isSneaking && sneakHeld }),
        DESCENDINGTOGGLED(
            "[Descending (toggled)]",
            { mc.thePlayer.capabilities.isFlying && Config.enabledToggleSneak && Config.toggleSneakState }),
        DESCENDING(
            "[Descending (vanilla)]",
            { mc.thePlayer.capabilities.isFlying && mc.thePlayer.isSneaking }),
        FLYING("[Flying]", { mc.thePlayer.capabilities.isFlying }), RIDING(
            "[Riding]",
            { mc.thePlayer.isRiding }),
        SNEAKHELD(
            "[Sneaking (key held)]",
            { mc.thePlayer.isSneaking && sneakHeld }),
        TOGGLESNEAK(
            "[Sneaking (toggled)]",
            { Config.enabledToggleSneak && Config.toggleSneakState }),
        SNEAKING("[Sneaking (vanilla)]", { mc.thePlayer.isSneaking }), SPRINTHELD(
            "[Sprinting (key held)]",
            { mc.thePlayer.isSprinting && sprintHeld }),
        TOGGLESPRINT(
            "[Sprinting (toggled)]",
            { Config.enabledToggleSprint && Config.toggleSprintState }),
        SPRINTING("[Sprinting (vanilla)]", { mc.thePlayer.isSprinting });

        val isActive: Boolean
            get() = displayCheck()

        companion object {
            val activeDisplay: String?
                get() {
                    if (mc.thePlayer == null) return null
                    return values().find { it.isActive }?.displayText
                }
        }
    }
}