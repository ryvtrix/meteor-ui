package dev.prism.ui.gui.themes.prism.widgets.pressable;
//? if >=1.21.10 {
import dev.prism.ui.gui.themes.prism.PrismGuiTheme;
import dev.prism.ui.gui.themes.prism.PrismWidget;
import meteordevelopment.meteorclient.gui.renderer.GuiRenderer;
import meteordevelopment.meteorclient.gui.renderer.packer.GuiTexture;
import meteordevelopment.meteorclient.gui.widgets.pressable.WConfirmedButton;
import meteordevelopment.meteorclient.utils.render.color.Color;

public class WPrismConfirmedButton extends WConfirmedButton implements PrismWidget {

    public WPrismConfirmedButton(String text, String confirmText, GuiTexture texture) {
        super(text, confirmText, texture);
    }

    @Override
    protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
        PrismGuiTheme theme = theme();
        double pad = pad();

        Color outline = theme.outlineColor.get(pressed, mouseOver);
        Color fg = pressedOnce ? theme.backgroundColor.get(pressed, mouseOver) : theme.textColor();
        Color bg = pressedOnce ? theme.textColor() : theme.backgroundColor.get(pressed, mouseOver);

        background(bg, outline).render();

        String text = getText();

        if (text != null) {
            renderer.text(text, x + width / 2 - textWidth / 2, y + pad, fg, false);
        }
        else {
            double ts = theme.textHeight();
            renderer.quad(x + width / 2 - ts / 2, y + pad, ts, ts, texture, fg);
        }
    }
}
//?}