package dev.prism.ui.gui.themes.prism.widgets.container;

import dev.prism.ui.api.animation.Animation;
import dev.prism.ui.api.animation.Easing;
import dev.prism.ui.api.text.RichText;
import dev.prism.ui.gui.themes.prism.PrismWidget;
import dev.prism.ui.gui.widgets.tabs.PrismTab;
import dev.prism.ui.gui.widgets.tabs.WTabView;
import meteordevelopment.meteorclient.gui.renderer.GuiRenderer;
import meteordevelopment.meteorclient.gui.renderer.packer.GuiTexture;
import meteordevelopment.meteorclient.gui.utils.Cell;
import meteordevelopment.meteorclient.gui.widgets.containers.WContainer;
import meteordevelopment.meteorclient.utils.render.color.Color;
import net.minecraft.util.Mth;

import java.util.List;

public class WPrismTabView extends WTabView implements PrismWidget {

    public WPrismTabView(List<PrismTab> tabs, PrismTab initialTab) {
        super(tabs, initialTab);
    }

    @Override
    protected WHeader createHeader() {
        return new WPrismHeader();
    }

    @Override
    protected WContainer createContent() {
        return new WPrismContent();
    }

    @Override
    protected WTabButton createTabButton(PrismTab tab) {
        return new WPrismTabButton(tab);
    }

    protected class WPrismHeader extends WHeader {
        private static final double INDICATOR_HEIGHT = 3;

        private double[] indicatorWidths;

        private int targetIndex;
        private double prevX, prevWidth;

        private Animation indicatorAnimation;

        @Override
        public void init() {
            super.init();
            spacing = 0;

            targetIndex = tabs.indexOf(activeTab);

            indicatorAnimation = new Animation(Easing.QUART_OUT, 300);
        }

        @Override
        protected void onTabChange(PrismTab tab) {
            // Save where the indicator currently is (interpolated) as the new "from"
            // so it can continue from this position (very epic)
            double progress = indicatorAnimation.getProgress();
            prevX = Mth.lerp(progress, prevX, indicatorX(targetIndex));
            prevWidth = Mth.lerp(progress, prevWidth, indicatorWidths[targetIndex]);

            targetIndex = tabs.indexOf(tab);
            indicatorAnimation.start();
        }

        @Override
        protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
            if (!indicatorAnimation.isRunning()) {
                calculateWidths();
                prevX = indicatorX(targetIndex);
                prevWidth = indicatorWidths[targetIndex];
            }

            double progress = indicatorAnimation.getProgress();
            double ix = Mth.lerp(progress, prevX, indicatorX(targetIndex));
            double iWidth = Mth.lerp(progress, prevWidth, indicatorWidths[targetIndex]);
            double iHeight = theme.scale(INDICATOR_HEIGHT);
            double iy = y + height - iHeight;

            // Active tab indicator
            roundedRect().pos(ix, iy)
                         .size(iWidth, iHeight)
                         .radius(iHeight / 2)
                         .color(theme().accentColor())
                         .render();
        }

        // Indicator is centered under the active tab button
        private double indicatorX(int index) {
            WTabButton button = (WTabButton) cells.get(index).widget();
            return button.x + (button.width - indicatorWidths[index]) / 2;
        }

        private void calculateWidths() {
            indicatorWidths = new double[tabs.size()];
            int i = 0;

            for (Cell<?> cell : cells) {
                if (cell.widget() instanceof WPrismTabButton button) {
                    indicatorWidths[i] = button.contentWidth();
                    i++;
                }
            }
        }
    }

    protected static class WPrismContent extends WContainer { }

    protected class WPrismTabButton extends WTabButton implements PrismWidget {
        private static final double ICON_GAP = 4;

        private final RichText text;
        private double textWidth;

        public WPrismTabButton(PrismTab tab) {
            super(tab);
            this.text = RichText.of(tab.name());
        }

        @Override
        protected void onCalculateSize() {
            double padH = theme.scale(16);
            double padV = theme().textHeight() / 2;

            textWidth = theme().textWidth(text);

            width = padH + contentWidth() + padH;
            height = padV + theme().textHeight() + padV;
        }

        @Override
        protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
            boolean active = isTabActive(tab);

            Color color = active
                    ? theme().accentColor()
                    : mouseOver
                        ? theme().textColor()
                        : theme().textSecondaryColor();

            double contentX = x + (width - contentWidth()) / 2;
            double contentY = y + (height - iconSize()) / 2;
            Content content = content();

            if (content.hasIcon()) {
                double size = iconSize();
                renderer.quad(contentX, contentY, size, size, tab.icon(), color);
                contentX += size + iconGap();
            }

            if (content.hasText()) renderer().text(text, contentX, contentY, color);
        }

        protected Content content() {
            return Content.of(text, tab.icon());
        }

        protected double iconSize() {
            return theme.textHeight();
        }

        protected double iconGap() {
            return theme.scale(ICON_GAP);
        }

        public double contentWidth() {
            Content content = content();

            double width = content.hasIcon() ? iconSize() : 0;

            if (content.hasText()) {
                width += content.hasIcon() ? iconGap() : 0;
                width += textWidth;
            }

            return width;
        }

        protected enum Content {
            TEXT, ICON, ICON_AND_TEXT;

            static Content of(RichText text, GuiTexture texture) {
                if (text == null) return ICON;
                return texture == null ? TEXT : ICON_AND_TEXT;
            }

            public boolean hasIcon() {
                return this != TEXT;
            }

            public boolean hasText() {
                return this != ICON;
            }
        }
    }
}
