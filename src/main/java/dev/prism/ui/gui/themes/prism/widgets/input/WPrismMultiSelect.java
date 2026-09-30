package dev.prism.ui.gui.themes.prism.widgets.input;

import dev.prism.ui.api.render.style.Corners;
import dev.prism.ui.gui.themes.prism.PrismGuiTheme;
import dev.prism.ui.gui.themes.prism.PrismWidget;
import dev.prism.ui.gui.widgets.input.WMultiSelect;
import dev.prism.ui.utils.ColorUtils;
import meteordevelopment.meteorclient.gui.renderer.GuiRenderer;
import meteordevelopment.meteorclient.utils.render.color.Color;

import java.util.List;

public class WPrismMultiSelect<T> extends WMultiSelect<T> implements PrismWidget {
    public WPrismMultiSelect(String title, List<T> items) {
        super(title, items);
    }

    @Override
    protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
        if (expanded || animation.isRunning())
            roundedRect().pos(x, y + header.height)
                         .size(width, height - header.height)
                         .radius(radius(), Corners.BOTTOM)
                         .color(ColorUtils.withAlpha(theme().baseColor(), theme().backgroundOpacity()))
                         .render();
    }

    @Override
    protected WHeader createHeader() {
        return new WPrismHeader(title);
    }

    @Override
    protected WItem createItem(T item) {
        return new WPrismItem(item);
    }

    protected class WPrismHeader extends WHeader {

        public WPrismHeader(String title) {
            super(title);
        }

        @Override
        protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
            PrismGuiTheme theme = theme();
            Color bgColor = ColorUtils.withAlpha(
                    mouseOver ? theme.surface1Color() : theme.surface0Color(),
                    theme.backgroundOpacity()
            );

            // Background
            roundedRect().bounds(this)
                         .radius(radius(), expanded || animation.isRunning() ? Corners.TOP : Corners.ALL)
                         .color(bgColor)
                         .render();
        }
    }

    protected class WPrismItem extends WItem {

        public WPrismItem(T item) {
            super(item);
        }

        @Override
        protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
            if (!mouseOver || checkbox.mouseOver) return;

            roundedRect().bounds(this)
                         .radius(smallRadius())
                         .color(theme().surface0Color())
                         .render();
        }
    }
}
