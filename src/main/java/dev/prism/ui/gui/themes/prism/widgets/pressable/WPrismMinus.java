package dev.prism.ui.gui.themes.prism.widgets.pressable;

import dev.prism.ui.gui.themes.prism.PrismWidget;
import dev.prism.ui.gui.themes.prism.icons.PrismBuiltinIcons;
import meteordevelopment.meteorclient.gui.renderer.GuiRenderer;
import meteordevelopment.meteorclient.gui.widgets.pressable.WMinus;

public class WPrismMinus extends WMinus implements PrismWidget {

    @Override
    protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
        double pad = pad();
        double s = theme.textHeight();

        background(pressed, mouseOver).render();

        renderer.quad(
                x + pad,
                y + pad,
                s,
                s,
                PrismBuiltinIcons.MINUS.texture(),
                theme().redColor()
        );
    }
}
