package dev.prism.ui.gui.themes.prism.widgets;

import dev.prism.ui.api.render.style.Corners;
import dev.prism.ui.gui.themes.prism.PrismWidget;
import meteordevelopment.meteorclient.gui.renderer.GuiRenderer;
import meteordevelopment.meteorclient.gui.widgets.WQuad;
import meteordevelopment.meteorclient.utils.render.color.Color;

public class WPrismQuad extends WQuad implements PrismWidget {
    public Corners corners;
    public float radius;
    public double minHeight;

    public WPrismQuad(Color color) {
        super(color);
    }

    @Override
    protected void onCalculateSize() {
        double s = theme.scale(32);

        width = s;
        height = Math.max(minHeight, s);
    }

    @Override
    protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
        if (corners != null && radius > 0)
            roundedRect().bounds(this)
                         .radius(radius, corners)
                         .color(color)
                         .render();

        else renderer.quad(x, y, width, height, color);
    }


}
