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

import gg.essential.vigilance.Vigilant
import gg.essential.vigilance.data.Category
import gg.essential.vigilance.data.Property
import gg.essential.vigilance.data.PropertyType
import gg.essential.vigilance.data.SortingBehavior
import java.awt.Color
import java.io.File


object Config : Vigilant(File("./config/simpletogglesprint.toml"), "ToggleSprint", sortingBehavior = ConfigSorting) {
    @Property(
        type = PropertyType.TEXT,
        name = "Last Launched Version",
        category = "General",
        subcategory = "Hidden",
        hidden = true
    )
    var lastLaunchedVersion = "0"

    @JvmField
    @Property(
        type = PropertyType.SWITCH,
        name = "Show Config on Menu",
        category = "General",
        subcategory = "Options",
        description = "Adds the option to configure to the escape menu.\nYou can also access this menu with /simpletogglesprint."
    )
    var showConfigOnEscape = true

    @Property(
        type = PropertyType.SWITCH,
        name = "Enabled",
        category = "General",
        subcategory = "Toggle Sprint",
        description = "Enables the toggle sprint functionality."
    )
    var enabledToggleSprint = true

    @Property(
        type = PropertyType.SWITCH,
        name = "State",
        category = "General",
        subcategory = "Toggle Sprint",
        description = "Saves the sprint state to use on launch.",
        hidden = true
    )
    var toggleSprintState = false

    @JvmField
    @Property(
        type = PropertyType.SWITCH,
        name = "Enabled",
        category = "General",
        subcategory = "Toggle Sneak",
        description = "Enables the toggle sneak functionality.\nThis does not function while in a menu."
    )
    var enabledToggleSneak = false

    @JvmField
    @Property(
        type = PropertyType.SWITCH,
        name = "State",
        category = "General",
        subcategory = "Toggle Sneak",
        description = "Saves the sneak state to use on launch.",
        hidden = true
    )
    var toggleSneakState = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Seperate Keybind for Toggle Sprint",
        category = "General",
        subcategory = "Toggle Sprint",
        description = "Use a seperate keybind for Toggle Sprint.\nConfigure it in the In-Game Controls menu."
    )
    var keybindToggleSprint = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Seperate Keybind for Toggle Sneak",
        category = "General",
        subcategory = "Toggle Sneak",
        description = "Use a seperate keybind for Toggle Sneak.\nConfigure it in the In-Game Controls menu."
    )
    var keybindToggleSneak = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Enabled",
        category = "Display",
        description = "Displays the toggle states on your HUD."
    )
    var displayToggleState = true

    @Property(
        type = PropertyType.SWITCH,
        name = "Show in GUIs",
        category = "Display"
    )
    var showInGui = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Display Background",
        category = "Display",
        description = "Display a background."
    )
    var displayBackground = false

    @Property(
        type = PropertyType.COLOR,
        name = "Display Color",
        category = "Display",
        subcategory = "Color",
        description = "Select the color to show the display text in."
    )
    var displayColor = Color.WHITE

    @Property(
        type = PropertyType.SWITCH,
        name = "Use Chroma Color",
        category = "Display",
        subcategory = "Color",
        description = "Use a Chroma color instead."
    )
    var useChromaColor = false

    @Property(
        type = PropertyType.DECIMAL_SLIDER,
        name = "X",
        category = "Display",
        subcategory = "Position",
        description = "Change the X value for the state display. Based on a percentage of your screen.",
        maxF = 100f,
        decimalPlaces = 1
    )
    var displayStateX = 0.2f

    @Property(
        type = PropertyType.DECIMAL_SLIDER,
        name = "Y",
        category = "Display",
        subcategory = "Position",
        description = "Change the Y value for the state display. Based on a percentage of your screen.",
        maxF = 100f,
        decimalPlaces = 1
    )
    var displayStateY = 97.4f

    @Property(
        type = PropertyType.DECIMAL_SLIDER,
        name = "Scale",
        category = "Display",
        subcategory = "Options",
        description = "Change the scale for the state display, this is a percentage.",
        maxF = 500f,
        decimalPlaces = 1
    )
    var displayStateScale = 108.5f

    @Property(
        type = PropertyType.SWITCH,
        name = "Text Shadow",
        category = "Display",
        subcategory = "Options",
        description = "Change whether or not the display has text shadow."
    )
    var displayStateShadow = true

    @Property(
        type = PropertyType.SELECTOR,
        name = "Text Alignment",
        category = "Display",
        subcategory = "Options",
        description = "Changes the text alignment settings for the state display.",
        options = ["Left-Right", "Right-Left", "Center"]
    )
    var displayStateAlignment = 0

    @Property(
        type = PropertyType.TEXT,
        name = "Descending Held Text",
        category = "Display",
        subcategory = "Text",
        description = "Change the Descending Held text."
    )
    var descendingHeld = "[Descending (key held)]"

    @Property(
        type = PropertyType.TEXT,
        name = "Descending Toggled Text",
        category = "Display",
        subcategory = "Text",
        description = "Change the Descending Toggled text."
    )
    var descendingToggled = "[Descending (toggled)]"

    @Property(
        type = PropertyType.TEXT,
        name = "Descending Text",
        category = "Display",
        subcategory = "Text",
        description = "Change the Descending text."
    )
    var descending = "[Descending (vanilla)]"

    @Property(
        type = PropertyType.TEXT,
        name = "Flying Text",
        category = "Display",
        subcategory = "Text",
        description = "Change the Flying text."
    )
    var flying = "[Flying]"

    @Property(
        type = PropertyType.TEXT,
        name = "Riding Text",
        category = "Display",
        subcategory = "Text",
        description = "Change the Riding text."
    )
    var riding = "[Riding]"

    @Property(
        type = PropertyType.TEXT,
        name = "Sneak Held Text",
        category = "Display",
        subcategory = "Text",
        description = "Change the Sneak Held text."
    )
    var sneakHeld = "[Sneaking (key held)]"

    @Property(
        type = PropertyType.TEXT,
        name = "Sneak Toggle Text",
        category = "Display",
        subcategory = "Text",
        description = "Change the Sneak Toggle text."
    )
    var sneakToggle = "[Sneaking (toggled)]"

    @Property(
        type = PropertyType.TEXT,
        name = "Sneaking Text",
        category = "Display",
        subcategory = "Text",
        description = "Change the Sneaking text."
    )
    var sneak = "[Sneaking (vanilla)]"

    @Property(
        type = PropertyType.TEXT,
        name = "Sprint Held Text",
        category = "Display",
        subcategory = "Text",
        description = "Change the Sprint Held text."
    )
    var sprintHeld = "[Sprinting (key held)]"

    @Property(
        type = PropertyType.TEXT,
        name = "Sprint Toggle Text",
        category = "Display",
        subcategory = "Text",
        description = "Change the Sprint Toggle text."
    )
    var sprintToggle = "[Sprinting (toggled)]"

    @Property(
        type = PropertyType.TEXT,
        name = "Sprinting Text",
        category = "Display",
        subcategory = "Text",
        description = "Change the Sprinting text."
    )
    var sprint = "[Sprinting (vanilla)]"

    init {
        addDependency("keybindToggleSprint", "enabledToggleSprint")
        addDependency("keybindToggleSneak", "enabledToggleSneak")

        addDependency("showInGui", "displayToggleState")
        addDependency("displayBackground", "displayToggleState")
        addDependency("displayColor", "displayToggleState")
        addDependency("displayStateX", "displayToggleState")
        addDependency("displayStateY", "displayToggleState")
        addDependency("displayStateScale", "displayToggleState")
        addDependency("displayStateShadow", "displayToggleState")
        addDependency("displayStateAlignment", "displayToggleState")
        initialize()
    }

    private object ConfigSorting : SortingBehavior() {
        override fun getCategoryComparator(): Comparator<in Category> = Comparator { o1, o2 ->
            if (o1.name == "General") return@Comparator -1
            if (o2.name == "General") return@Comparator 1
            else compareValuesBy(o1, o2) {
                it.name
            }
        }
    }
}