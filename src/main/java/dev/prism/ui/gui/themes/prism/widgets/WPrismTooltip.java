package dev.prism.ui.gui.themes.prism.widgets;

import dev.prism.ui.gui.themes.prism.PrismWidget;
import meteordevelopment.meteorclient.gui.renderer.GuiRenderer;
import meteordevelopment.meteorclient.gui.widgets.WTooltip;

public class WPrismTooltip extends WTooltip implements PrismWidget {

    public WPrismTooltip(String text) {
        super(text);
    }

    @Override
    public void init() {
        add(theme.label(text)).padVertical(4).padHorizontal(6);
    }

    @Override
    protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
        background(theme().baseColor(), theme().surface0Color()).render();
    }
}
