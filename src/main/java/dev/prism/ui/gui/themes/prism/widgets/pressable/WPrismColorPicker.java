package dev.prism.ui.gui.themes.prism.widgets.pressable;

import dev.prism.ui.gui.themes.prism.PrismWidget;
import dev.prism.ui.gui.widgets.pressable.WColorPicker;
import dev.prism.ui.utils.ColorUtils;
import meteordevelopment.meteorclient.gui.renderer.GuiRenderer;
import meteordevelopment.meteorclient.gui.renderer.packer.GuiTexture;
import meteordevelopment.meteorclient.utils.render.color.Color;

public class WPrismColorPicker extends WColorPicker implements PrismWidget {

    public WPrismColorPicker(Color color, GuiTexture overlayTexture) {
        super(color, overlayTexture);
    }

    @Override
    protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
        background(mouseOver ? ColorUtils.darker(color) : color, theme().surface2Color()).render();

        if (mouseOver) {
            double s = theme.textHeight();

            renderer.quad(
                    x + width / 2 - s / 2,
                    y + height / 2 - s / 2,
                    s,
                    s,
                    overlayTexture,
                    theme().textColor()
            );
        }
    }
}
