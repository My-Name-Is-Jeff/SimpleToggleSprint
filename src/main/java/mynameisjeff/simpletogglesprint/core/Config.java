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

package mynameisjeff.simpletogglesprint.core;

import gg.essential.vigilance.Vigilant;
import gg.essential.vigilance.data.*;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.File;
import java.util.Comparator;

public class Config extends Vigilant {

    @Property(
            type = PropertyType.SWITCH,
            name = "Show Config on Menu",
            category = "General", subcategory = "Options",
            description = "Adds the option to configure to the escape menu.\nYou can also access this menu with /simpletogglesprint."
    )
    public static boolean showConfigOnEscape = true;

    @Property(
            type = PropertyType.SWITCH,
            name = "Enabled",
            category = "General", subcategory = "Toggle Sprint",
            description = "Enables the toggle sprint functionality."
    )
    public static boolean enabledToggleSprint = true;

    @Property(
            type = PropertyType.SWITCH,
            name = "State",
            category = "General", subcategory = "Toggle Sprint",
            description = "Saves the sprint state to use on launch.",
            hidden = true
    )
    public static boolean toggleSprintState = false;

    @Property(
            type = PropertyType.SWITCH,
            name = "Enabled",
            category = "General", subcategory = "Toggle Sneak",
            description = "Enables the toggle sneak functionality.\nThis does not function while in a menu."
    )
    public static boolean enabledToggleSneak = false;

    @Property(
            type = PropertyType.SWITCH,
            name = "State",
            category = "General", subcategory = "Toggle Sneak",
            description = "Saves the sneak state to use on launch.",
            hidden = true
    )
    public static boolean toggleSneakState = false;

    @Property(
            type = PropertyType.SWITCH,
            name = "Seperate Keybind for Toggle Sprint",
            category = "General", subcategory = "Toggle Sprint",
            description = "Use a seperate keybind for Toggle Sprint.\nConfigure it in the In-Game Controls menu."
    )
    public static boolean keybindToggleSprint = false;

    @Property(
            type = PropertyType.SWITCH,
            name = "Seperate Keybind for Toggle Sneak",
            category = "General", subcategory = "Toggle Sneak",
            description = "Use a seperate keybind for Toggle Sneak.\nConfigure it in the In-Game Controls menu."
    )
    public static boolean keybindToggleSneak = false;

    @Property(
            type = PropertyType.SWITCH,
            name = "Enabled",
            category = "Display",
            description = "Displays the toggle states on your HUD."
    )
    public static boolean displayToggleState = true;

    @Property(
            type = PropertyType.SWITCH,
            name = "Show in GUIs",
            category = "Display"
    )
    public static boolean showInGui;

    @Property(
            type = PropertyType.SWITCH,
            name = "Display Background",
            category = "Display",
            description = "Display a background."
    )
    public static boolean displayBackground;
    @Property(
            type = PropertyType.COLOR,
            name = "Display Color",
            category = "Display", subcategory = "Color",
            description = "Changes the color of the display."
    )
    public static Color displayStateColor = Color.WHITE;

    @Property(
            type = PropertyType.DECIMAL_SLIDER,
            name = "X",
            category = "Display", subcategory = "Position",
            description = "Change the X value for the state display. Based on a percentage of your screen.",
            maxF = 100f,
            decimalPlaces = 2
    )
    public static float displayStateX = 2f;

    @Property(
            type = PropertyType.DECIMAL_SLIDER,
            name = "Y",
            category = "Display", subcategory = "Position",
            description = "Change the Y value for the state display. Based on a percentage of your screen.",
            maxF = 100f,
            decimalPlaces = 2
    )
    public static float displayStateY = 97.4f;

    @Property(
            type = PropertyType.DECIMAL_SLIDER,
            name = "Scale",
            category = "Display", subcategory = "Options",
            description = "Change the scale for the state display, this is a percentage.",
            maxF = 5f,
            decimalPlaces = 3
    )
    public static float displayStateScale = 1.085f;

    @Property(
            type = PropertyType.SWITCH,
            name = "Text Shadow",
            category = "Display", subcategory = "Options",
            description = "Change whether or not the display has text shadow."
    )
    public static boolean displayStateShadow = true;

    @Property(
            type = PropertyType.SELECTOR,
            name = "Text Alignment",
            category = "Display", subcategory = "Options",
            description = "Changes the text alignment settings for the state display.",
            options = {
                    "Left-Right",
                    "Right-Left",
                    "Center"
            }
    )
    public static int displayStateAlignment = 0;

    public Config() {
        super(new File("./config/simpletogglesprint.toml"), "ToggleSprint", new JVMAnnotationPropertyCollector(), new ConfigSorting());

        addDependency("keybindToggleSprint", "enabledToggleSprint");
        addDependency("keybindToggleSneak", "enabledToggleSneak");

        addDependency("showInGui", "displayToggleState");
        addDependency("displayBackground", "displayToggleState");
        addDependency("displayStateColor", "displayToggleState");
        addDependency("displayStateX", "displayToggleState");
        addDependency("displayStateY", "displayToggleState");
        addDependency("displayStateScale", "displayToggleState");
        addDependency("displayStateShadow", "displayToggleState");
        addDependency("displayStateAlignment", "displayToggleState");
        initialize();
    }

    private static class ConfigSorting extends SortingBehavior {
        @NotNull
        @Override
        public Comparator<Category> getCategoryComparator() {
            return (o1, o2) -> {
                if (o1.getName().equals("General")) return -1;
                if (o2.getName().equals("General")) return 1;
                return o1.getName().compareTo(o2.getName());
            };
        }
    }
}
