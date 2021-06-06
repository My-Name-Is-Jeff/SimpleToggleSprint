package mynameisjeff.simpletogglesprint.elements;

import co.uk.isxander.evergreenhud.elements.ElementData;
import co.uk.isxander.evergreenhud.elements.RenderOrigin;
import co.uk.isxander.evergreenhud.elements.type.SimpleTextElement;
import mynameisjeff.simpletogglesprint.forge.SimpleToggleSprint;

public class ToggleSprintElement extends SimpleTextElement {
    @Override
    protected String getValue() {
        return SimpleToggleSprint.DisplayState.getActiveDisplay();
    }

    @Override
    public String getDefaultDisplayTitle() {
        return null;
    }

    @Override
    protected ElementData metadata() {
        return new ElementData("SimpleToggleSprint", "Shows the status for SimpleToggleSprint.", "Combat");
    }

    @Override
    public void render(float partialTicks, RenderOrigin origin) {
        if (origin == RenderOrigin.HUD) {
            if (SimpleToggleSprint.DisplayState.getActiveDisplay().isEmpty())
                return;
        }

        super.render(partialTicks, origin);
    }
}
