package mynameisjeff.simpletogglesprint;

import co.uk.isxander.evergreenhud.EvergreenHUD;
import co.uk.isxander.evergreenhud.addon.AddonMeta;
import co.uk.isxander.evergreenhud.addon.EvergreenAddon;
import mynameisjeff.simpletogglesprint.elements.ToggleSprintElement;


public class SimpleToggleSprint extends EvergreenAddon {
    public static final String MOD_VERSION = "1.0";
    public static final String MOD_NAME = "Simple Toggle Sprint (EvergreenHUD)";

    @Override
    public void init() {
        EvergreenHUD.getInstance().getElementManager().registerElement("TOGGLE_SPRINT", ToggleSprintElement.class);
    }

    @Override
    public AddonMeta metadata() {
        return new AddonMeta(MOD_NAME, "Displays the status for SimpleToggleSprint.", MOD_VERSION);
    }
}
