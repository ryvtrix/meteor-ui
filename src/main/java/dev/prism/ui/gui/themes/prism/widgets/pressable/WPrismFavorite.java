package dev.prism.ui.gui.themes.prism.widgets.pressable;

import dev.prism.ui.gui.themes.prism.PrismWidget;
import dev.prism.ui.gui.themes.prism.icons.PrismBuiltinIcons;
import meteordevelopment.meteorclient.gui.renderer.GuiRenderer;
import meteordevelopment.meteorclient.gui.widgets.pressable.WFavorite;
import meteordevelopment.meteorclient.utils.render.color.Color;

public class WPrismFavorite extends WFavorite implements PrismWidget {
    double size;

    public WPrismFavorite(boolean checked) {
        super(checked);
    }

    @Override
    public void init() {
        size = theme.textHeight();
    }

    @Override
    protected void onCalculateSize() {
        width = size;
        height = size;
    }

    @Override
    protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
        renderer.quad(
                x,
                y,
                size,
                size,
                checked ? PrismBuiltinIcons.BOOKMARK_YES.texture() : PrismBuiltinIcons.BOOKMARK_NO.texture(),
                getColor()
        );
    }

    @Override
    protected Color getColor() {
        return checked
                ? theme().accentColor()
                : mouseOver
                    ? theme().textSecondaryColor()
                    : theme().textColor();
    }
}
