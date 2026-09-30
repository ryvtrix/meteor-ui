package dev.prism.ui.gui.themes.prism.widgets.panel;

import dev.prism.ui.api.text.RichText;
import dev.prism.ui.gui.themes.prism.PrismGuiTheme;
import dev.prism.ui.gui.themes.prism.PrismWidget;
import dev.prism.ui.utils.ColorUtils;
import meteordevelopment.meteorclient.gui.renderer.GuiRenderer;
import meteordevelopment.meteorclient.gui.widgets.WWidget;
import meteordevelopment.meteorclient.utils.render.color.Color;

/**
 * Glowing logo mark with a title, shown at the top of the sidebar.
 */
public class WPrismBrand extends WWidget implements PrismWidget {
    private final RichText text;
    private double textWidth;

    public WPrismBrand(String text) {
        this.text = RichText.of(text).bold();
    }

    private double logoSize() {
        return theme().textHeight() * 1.9;
    }

    @Override
    protected void onCalculateSize() {
        PrismGuiTheme theme = theme();

        textWidth = theme.textWidth(text);
        width = logoSize() + theme.scale(10) + textWidth;
        height = logoSize();
    }

    @Override
    protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
        PrismGuiTheme theme = theme();

        double size = logoSize();
        Color accent = theme.accentColor();

        // Logo tile
        roundedRect().pos(x, y)
                     .size(size, size)
                     .radius(size * 0.32)
                     .color(ColorUtils.withAlpha(accent, 0.20))
                     .outline(ColorUtils.withAlpha(accent, 0.80), outlineWidth())
                     .shadow(theme.scale(12), 0, ColorUtils.withAlpha(accent, 0.35))
                     .render();

        // Logo core
        double core = size * 0.36;

        roundedRect().pos(x + (size - core) / 2, y + (size - core) / 2)
                     .size(core, core)
                     .radius(core * 0.5)
                     .color(accent)
                     .render();

        // Title
        double textX = x + size + theme.scale(10);
        double textY = y + size / 2 - theme.textHeight() / 2;

        renderer().text(text, textX, textY, theme.textColor());
    }
}
