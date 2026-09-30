package dev.prism.ui.gui.themes.prism.widgets.pressable;

import dev.prism.ui.gui.themes.prism.PrismWidget;
import dev.prism.ui.gui.themes.prism.icons.PrismBuiltinIcons;
import meteordevelopment.meteorclient.gui.renderer.GuiRenderer;
import meteordevelopment.meteorclient.gui.widgets.pressable.WTriangle;

public class WPrismTriangle extends WTriangle implements PrismWidget {
    @Override
    protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
        double s = theme.textHeight();

        renderer.rotatedQuad(
                x,
                y,
                s,
                s,
                rotation,
                PrismBuiltinIcons.ARROW.texture(),
                theme.textColor()
        );
    }
}
