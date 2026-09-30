package dev.prism.ui.gui.themes.prism.widgets.panel;

import dev.prism.ui.api.animation.Animation;
import dev.prism.ui.api.animation.Direction;
import dev.prism.ui.api.animation.Easing;
import dev.prism.ui.api.text.RichText;
import dev.prism.ui.gui.themes.prism.PrismGuiTheme;
import dev.prism.ui.gui.themes.prism.PrismWidget;
import dev.prism.ui.utils.ColorUtils;
import meteordevelopment.meteorclient.gui.renderer.GuiRenderer;
import meteordevelopment.meteorclient.gui.renderer.packer.GuiTexture;
import meteordevelopment.meteorclient.gui.widgets.pressable.WPressable;
import meteordevelopment.meteorclient.utils.render.color.Color;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

/**
 * One category entry in the sidebar: icon + name, with an animated selection highlight.
 */
public class WPrismSidebarItem extends WPressable implements PrismWidget {
    private final RichText title;
    private final GuiTexture icon;
    private final Runnable action;

    /** Set by the owning screen. The highlight animates whenever this changes. */
    public boolean selected;

    private Animation selectAnimation;
    private Animation hoverAnimation;
    private boolean wasSelected;
    private boolean wasHovered;

    public WPrismSidebarItem(String title, GuiTexture icon, Runnable action) {
        this.title = RichText.of(title);
        this.icon = icon;
        this.action = action;
    }

    @Override
    public void init() {
        wasSelected = selected;

        selectAnimation = new Animation(
                Easing.QUART_OUT,
                280,
                selected ? Direction.FORWARDS : Direction.BACKWARDS
        );

        hoverAnimation = new Animation(Easing.LINEAR, 160);
    }

    private double iconSize() {
        PrismGuiTheme theme = theme();
        return theme.categoryIcons() && icon != null ? theme.textHeight() * 1.05 : 0;
    }

    @Override
    protected void onCalculateSize() {
        PrismGuiTheme theme = theme();
        double pad = theme.scale(10);
        double iconSize = iconSize();

        width = pad * 2 + theme.textWidth(title) + (iconSize > 0 ? iconSize + pad : 0);
        height = theme.textHeight() + pad * 1.3;
    }

    @Override
    protected void onPressed(int button) {
        if (button == GLFW_MOUSE_BUTTON_LEFT && action != null) action.run();
    }

    @Override
    protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
        PrismGuiTheme theme = theme();
        double pad = theme.scale(10);

        if (selected != wasSelected) {
            wasSelected = selected;
            selectAnimation.start(selected ? Direction.FORWARDS : Direction.BACKWARDS);
        }

        if (mouseOver != wasHovered) {
            wasHovered = mouseOver;

            if (mouseOver) hoverAnimation.start();
            else hoverAnimation.finishedAt(Direction.BACKWARDS);
        }

        double select = clamp01(selectAnimation.getProgress());
        double hover = theme.hoverGlow.get() ? clamp01(hoverAnimation.getProgress()) : 0;

        Color accent = theme.accentColor();

        // Highlight pill
        if (select > 0 || hover > 0) {
            int fillAlpha = (int) Math.min(255, 255 * (0.24 * select + 0.09 * hover));
            int outlineAlpha = (int) (150 * select);

            roundedRect().bounds(this)
                         .radius(smallRadius())
                         .color(ColorUtils.withAlpha(accent, fillAlpha))
                         .outline(ColorUtils.withAlpha(accent, outlineAlpha), outlineWidth())
                         .render();
        }

        // Selection notch
        if (select > 0.01) {
            double notchHeight = height * 0.55 * select;

            roundedRect().pos(x + theme.scale(3), y + (height - notchHeight) / 2)
                         .size(theme.scale(3), notchHeight)
                         .radius(theme.scale(1.5))
                         .color(accent)
                         .render();
        }

        double cursor = x + pad;
        double iconSize = iconSize();

        if (iconSize > 0) {
            Color iconColor = ColorUtils.lerp(theme.textSecondaryColor(), accent, select);
            renderer.quad(cursor, y + (height - iconSize) / 2, iconSize, iconSize, icon, iconColor);
            cursor += iconSize + pad;
        }

        Color textColor = ColorUtils.lerp(
                theme.textSecondaryColor(),
                theme.textColor(),
                Math.max(select, hover * 0.7)
        );

        renderer().text(title, cursor, y + height / 2 - theme.textHeight() / 2, textColor);
    }

    private static double clamp01(double v) {
        return Math.max(0, Math.min(1, v));
    }
}
