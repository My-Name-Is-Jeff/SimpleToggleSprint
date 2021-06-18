/*
      SimpleToggleSprint
      Copyright (C) 2021  My-Name-Is-Jeff

      This program is free software: you can redistribute it and/or modify
      it under the terms of the GNU Affero General Public License as published by
      the Free Software Foundation, either version 3 of the License, or
      (at your option) any later version.

      This program is distributed in the hope that it will be useful,
      but WITHOUT ANY WARRANTY; without even the implied warranty of
      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
      GNU Affero General Public License for more details.

      You should have received a copy of the GNU Affero General Public License
      along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package mynameisjeff.simpletogglesprint;


import gg.essential.universal.UKeyboard;
import gg.essential.universal.UMinecraft;
import gg.essential.universal.UScreen;
import mynameisjeff.simpletogglesprint.commands.SimpleToggleSprintCommand;
import mynameisjeff.simpletogglesprint.core.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Mouse;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

@Mod(modid = SimpleToggleSprint.MODID, name = SimpleToggleSprint.MOD_NAME, version = SimpleToggleSprint.VERSION, acceptedMinecraftVersions = "[1.12,1.12.2]", clientSideOnly = true)
public class SimpleToggleSprint {

    public static final String MODID = "simpletogglesprint";
    public static final String MOD_NAME = "SimpleToggleSprint";
    public static final String VERSION = "1.0";
    public static final Minecraft mc = UMinecraft.getMinecraft();
    public static final KeyBinding keySprint = new KeyBinding("Toggle Sprint", 0, "SimpleToggleSprint");
    public static final KeyBinding keySneak = new KeyBinding("Toggle Sneak", 0, "SimpleToggleSprint");
    public static Config config = new Config();
    public static boolean sprintHeld = false;
    public static boolean sneakHeld = false;

    public static UScreen displayScreen = null;

    public static ExecutorService thread = Executors.newSingleThreadExecutor();

    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        config.preload();
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Mod.EventHandler
    public void onPostInit(FMLPostInitializationEvent event) {
        ClientCommandHandler.instance.registerCommand(new SimpleToggleSprintCommand());
        ClientRegistry.registerKeyBinding(keySprint);
        ClientRegistry.registerKeyBinding(keySneak);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        if (displayScreen != null) {
            mc.displayGuiScreen(displayScreen);
            displayScreen = null;
        }
    }

    @SubscribeEvent
    public void onInput(InputEvent event) {

        int sprint = UMinecraft.getSettings().keyBindSprint.getKeyCode();
        int sneak = UMinecraft.getSettings().keyBindSneak.getKeyCode();

        int sprintToggle = keySprint.getKeyCode();
        int sneakToggle = keySneak.getKeyCode();

        if (Config.keybindToggleSprint && sprintToggle > 0 ? UKeyboard.isKeyDown(sprintToggle) : Mouse.isButtonDown(sprintToggle + 100)) {
            if (Config.enabledToggleSprint && !sprintHeld) {
                Config.toggleSprintState = !Config.toggleSprintState;
                config.markDirty();
            }
            sprintHeld = true;
        } else if (!Config.keybindToggleSprint && sprint > 0 ? UKeyboard.isKeyDown(sprint) : Mouse.isButtonDown(sprint + 100)) {
            if (Config.enabledToggleSprint && !sprintHeld) {
                Config.toggleSprintState = !Config.toggleSprintState;
                config.markDirty();
            }
            sprintHeld = true;
        } else {
            sprintHeld = false;
        }

        if (Config.keybindToggleSneak && sneakToggle > 0 ? UKeyboard.isKeyDown(sneakToggle) : Mouse.isButtonDown(sneakToggle + 100)) {
            if (Config.enabledToggleSneak && !sneakHeld) {
                Config.toggleSneakState = !Config.toggleSneakState;
                config.markDirty();
            }
            sneakHeld = true;
        } else if (!Config.keybindToggleSprint && sneak > 0 ? UKeyboard.isKeyDown(sneak) : Mouse.isButtonDown(sneak + 100)) {
            if (Config.enabledToggleSneak && !sneakHeld) {
                Config.toggleSneakState = !Config.toggleSneakState;
                config.markDirty();
            }
            sneakHeld = true;
        } else {
            sneakHeld = false;
        }
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.HOTBAR) return;
        if (Config.displayToggleState && (mc.currentScreen == null || Config.showInGui) && UMinecraft.getPlayer() != null) {
            ScaledResolution sr = new ScaledResolution(mc);
            String active = DisplayState.getActiveDisplay();
            if (active == null) return;
            double x = sr.getScaledWidth_double() * (Config.displayStateX / 100f);
            double y = sr.getScaledHeight_double() * (Config.displayStateY / 100f);
            double scale = Config.displayStateScale;

            double xOffset = (Config.displayStateAlignment != 0 ? (-UMinecraft.getFontRenderer().getStringWidth(active) / (float) Config.displayStateAlignment) : 0) * scale;

            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, 0);
            GlStateManager.scale(scale, scale, 1d);

            UMinecraft.getFontRenderer().drawString(active, (float) xOffset, 0, Config.displayStateColor.getRGB(), Config.displayStateShadow);
            if (Config.displayBackground) {
                GlStateManager.translate(0, 0, -1);
                Gui.drawRect((int) xOffset - 2, -2, (int) xOffset + UMinecraft.getFontRenderer().getStringWidth(active) + 1, UMinecraft.getFontRenderer().FONT_HEIGHT + 1, 0);
                GlStateManager.translate(0, 0, 1);
            }

            GlStateManager.popMatrix();
        }
    }

    private enum DisplayState {
        DESCENDINGHELD("[Descending (key held)]", () -> UMinecraft.getPlayer().capabilities.isFlying && UMinecraft.getPlayer().isSneaking() && sneakHeld),
        DESCENDINGTOGGLED("[Descending (toggled)]", () -> UMinecraft.getPlayer().capabilities.isFlying && Config.enabledToggleSneak && Config.toggleSneakState),
        DESCENDING("[Descending (vanilla)]", () -> UMinecraft.getPlayer().capabilities.isFlying && UMinecraft.getPlayer().isSneaking()),
        FLYING("[Flying]", () -> UMinecraft.getPlayer().capabilities.isFlying),
        RIDING("[Riding]", () -> UMinecraft.getPlayer().isRiding()),
        SNEAKHELD("[Sneaking (key held)]", () -> UMinecraft.getPlayer().isSneaking() && sneakHeld),
        TOGGLESNEAK("[Sneaking (toggled)]", () -> Config.enabledToggleSneak && Config.toggleSneakState),
        SNEAKING("[Sneaking (vanilla)]", () -> UMinecraft.getPlayer().isSneaking()),
        SPRINTHELD("[Sprinting (key held)]", () -> UMinecraft.getPlayer().isSprinting() && sprintHeld),
        TOGGLESPRINT("[Sprinting (toggled)]", () -> Config.enabledToggleSprint && Config.toggleSprintState),
        SPRINTING("[Sprinting (vanilla)]", () -> UMinecraft.getPlayer().isSprinting());

        public final String displayText;
        public final Supplier<Boolean> displayCheck;

        DisplayState(String display, Supplier<Boolean> displayCheck) {
            this.displayText = display;
            this.displayCheck = displayCheck;
        }

        public static String getActiveDisplay() {
            if (UMinecraft.getPlayer() == null) return null;
            for (DisplayState state : DisplayState.values()) {
                if (state.isActive()) return state.displayText;
            }
            return null;
        }

        public boolean isActive() {
            return this.displayCheck.get();
        }
    }

}