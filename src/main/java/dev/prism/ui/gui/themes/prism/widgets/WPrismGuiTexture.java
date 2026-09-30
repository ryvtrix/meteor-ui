package dev.prism.ui.gui.themes.prism.widgets;

import dev.prism.ui.gui.themes.prism.PrismWidget;
import dev.prism.ui.gui.widgets.WGuiTexture;
import meteordevelopment.meteorclient.gui.renderer.GuiRenderer;
import meteordevelopment.meteorclient.gui.renderer.packer.GuiTexture;

public class WPrismGuiTexture extends WGuiTexture implements PrismWidget {

    public WPrismGuiTexture(GuiTexture texture, double size) {
        super(texture, size);
    }

    @Override
    protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
        renderer.quad(x, y, size, size, texture, color);
    }
}
