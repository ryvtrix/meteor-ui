package dev.prism.ui.gui.themes.prism;

import dev.prism.ui.api.render.shape.RoundedRect;
import dev.prism.ui.api.render.style.Shadow;
import dev.prism.ui.renderer.PrismRenderer;
import dev.prism.ui.api.render.style.Corners;
import dev.prism.ui.utils.ColorUtils;
import meteordevelopment.meteorclient.gui.utils.BaseWidget;
import meteordevelopment.meteorclient.gui.widgets.WWidget;
import meteordevelopment.meteorclient.utils.render.color.Color;

public interface PrismWidget extends BaseWidget {

    // Getters

    default PrismGuiTheme theme() {
        return (PrismGuiTheme) getTheme();
    }

    default PrismRenderer renderer() {
        return PrismRenderer.get();
    }

    default RoundedRect roundedRect() {
        return RoundedRect.get();
    }

    // Styling

    default float radius() {
        return (float) (theme().scale(theme().cornerRadius.get()));
    }

    default float smallRadius() {
        return (float) (theme().scale(theme().smallCornerRadius.get()));
    }

    default Corners corners() {
        return Corners.ALL;
    }

    default float outlineWidth() {
        return (float) theme().scale(theme().outlineThickness.get());
    }

    default Shadow shadow() {
        PrismGuiTheme theme = theme();
        Color shadowColor = ColorUtils.withAlpha(Color.BLACK, theme.windowOpacity());

        return Shadow.of(
                0, 0,
                theme.shadowBlur.get(),
                theme.shadowSpread.get(),
                shadowColor
        );
    }

    // Rendering

    default RoundedRect background(Color backgroundColor, Color outlineColor) {
        return roundedRect().bounds((WWidget) this)
                            .radius(smallRadius(), corners())
                            .color(backgroundColor)
                            .outline(outlineColor, outlineWidth());
    }

    default RoundedRect background(boolean pressed, boolean mouseOver) {
        return background(getBackgroundColor(pressed, mouseOver), getOutlineColor(pressed, mouseOver));
    }

    // Colors

    default Color getBackgroundColor(boolean pressed, boolean mouseOver) {
        PrismGuiTheme theme = theme();

        return ColorUtils.withAlpha(
                theme.backgroundColor.get(pressed, mouseOver),
                theme.backgroundOpacity()
        );
    }

    default Color getOutlineColor(boolean pressed, boolean mouseOver) {
        PrismGuiTheme theme = theme();

        return ColorUtils.withAlpha(
                theme.outlineColor.get(pressed, mouseOver),
                theme.backgroundOpacity() * 0.5
        );
    }
}