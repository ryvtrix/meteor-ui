package dev.prism.ui.gui.themes.prism.widgets;

import dev.prism.ui.api.text.RichText;
import dev.prism.ui.gui.themes.prism.PrismWidget;
import meteordevelopment.meteorclient.gui.renderer.GuiRenderer;
import meteordevelopment.meteorclient.gui.widgets.WMultiLabel;
import meteordevelopment.meteorclient.utils.render.color.Color;

public class WPrismMultiLabel extends WMultiLabel implements PrismWidget {

    public WPrismMultiLabel(RichText text, double maxWidth) {
        super(text.getPlainText(), false, maxWidth);
    }

    @Override
    protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
        double h = theme.textHeight(title);
        Color defaultColor = theme().textColor();

        for (int i = 0; i < lines.size(); i++) {
            renderer().text(
                    RichText.of(lines.get(i)).boldIf(title),
                    x,
                    y + h * i,
                    color != null ? color : defaultColor
            );
        }
    }
}
