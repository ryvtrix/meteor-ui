package dev.prism.ui.gui.themes.prism.widgets;

import dev.prism.ui.gui.themes.prism.PrismGuiTheme;
import dev.prism.ui.gui.themes.prism.PrismWidget;
import meteordevelopment.meteorclient.gui.renderer.GuiRenderer;
import meteordevelopment.meteorclient.gui.widgets.WVerticalSeparator;

public class WPrismVerticalSeparator extends WVerticalSeparator implements PrismWidget {
    public double size = 2;

    @Override
    protected void onCalculateSize() {
        width = theme.scale(size);
        height = 1;
    }

    @Override
    protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
        PrismGuiTheme theme = theme();

        roundedRect().bounds(this)
                    .radius(smallRadius())
                    .color(theme.surface0Color())
                    .render();
    }
}
