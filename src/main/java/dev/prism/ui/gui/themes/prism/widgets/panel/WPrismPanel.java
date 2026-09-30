package dev.prism.ui.gui.themes.prism.widgets.panel;

import dev.prism.ui.gui.themes.prism.PrismGuiTheme;
import dev.prism.ui.gui.themes.prism.PrismWidget;
import dev.prism.ui.utils.ColorUtils;
import meteordevelopment.meteorclient.gui.renderer.GuiRenderer;
import meteordevelopment.meteorclient.gui.utils.Cell;
import meteordevelopment.meteorclient.gui.widgets.WWidget;
import meteordevelopment.meteorclient.gui.widgets.containers.WContainer;
import meteordevelopment.meteorclient.utils.render.color.Color;

import static meteordevelopment.meteorclient.utils.Utils.getWindowHeight;
import static meteordevelopment.meteorclient.utils.Utils.getWindowWidth;

/**
 * The big rounded panel of the modules screen. It has a fixed size (a percentage of the screen),
 * paints the glow, background, sidebar backdrop, separator and gradient bar, and holds a single
 * child that fills it.
 */
public class WPrismPanel extends WContainer implements PrismWidget {
    /** Width of the sidebar area, in scaled pixels. Set by the owning screen. */
    public double sidebarWidth;

    // Sizing (shared with the screen so it can size the scrolling view)

    public static double panelWidth(PrismGuiTheme theme) {
        double windowWidth = getWindowWidth();
        double max = windowWidth - theme.scale(24);
        double min = Math.min(theme.textHeight() * 42, max);

        return Math.clamp(windowWidth * theme.panelWidth.get() / 100.0, min, max);
    }

    public static double panelHeight(PrismGuiTheme theme) {
        double windowHeight = getWindowHeight();
        // Leave room for the tab bar at the top
        double max = Math.max(windowHeight - theme.scale(130), theme.scale(200));
        double min = Math.min(theme.textHeight() * 24, max);

        return Math.clamp(windowHeight * theme.panelHeight.get() / 100.0, min, max);
    }

    @Override
    protected void onCalculateSize() {
        PrismGuiTheme theme = theme();

        width = panelWidth(theme);
        height = panelHeight(theme);
    }

    @Override
    protected void onCalculateWidgetPositions() {
        // Every child fills the whole panel
        for (Cell<?> cell : cells) {
            cell.x = x;
            cell.y = y;
            cell.width = width;
            cell.height = height;
            cell.alignWidget();

            WWidget child = cell.widget();
            child.x = x;
            child.y = y;
            child.width = width;
        }
    }

    @Override
    protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
        PrismGuiTheme theme = theme();

        float radius = radius();
        Color accent = theme.accentColor();
        double panelAlpha = theme.panelOpacity() * theme.windowOpacity();

        // Main body, with glow and outline
        var body = roundedRect().bounds(this)
                                .radius(radius)
                                .color(ColorUtils.withAlpha(theme.baseColor(), panelAlpha))
                                .outline(ColorUtils.withAlpha(accent, theme.outlineOpacity.get()), outlineWidth());

        if (theme.panelGlow.get()) {
            double size = theme.scale(theme.glowSize.get());

            body.shadow(
                    size,
                    -size * 0.25,
                    ColorUtils.withAlpha(accent, theme.glowStrength.get())
            );
        } else if (theme.windowShadow.get()) {
            body.shadow(theme.windowShadow());
        }

        body.render();

        // Sidebar backdrop
        double sidebar = Math.min(sidebarWidth, width * 0.45);

        roundedRect().pos(x, y)
                     .size(sidebar, height)
                     .radii(radius, 0, radius, 0)
                     .color(ColorUtils.withAlpha(theme.crustColor(), theme.sidebarOpacity() * theme.windowOpacity()))
                     .render();

        // Separator
        double inset = theme.scale(14);

        roundedRect().pos(x + sidebar, y + inset)
                     .size(Math.max(1, theme.scale(1)), height - inset * 2)
                     .color(ColorUtils.withAlpha(theme.overlay0Color(), 70))
                     .render();

        // Gradient accent bar along the top edge
        if (theme.accentBar.get()) renderAccentBar(radius);
    }

    private void renderAccentBar(float radius) {
        PrismGuiTheme theme = theme();

        int slices = 48;
        double barHeight = Math.max(2, theme.scale(2.5));
        double from = x + radius;
        double barWidth = width - radius * 2;
        double slice = barWidth / slices;

        Color start = theme.accentColor();
        Color end = theme.accentSecondaryColor();

        for (int i = 0; i < slices; i++) {
            double t = i / (double) (slices - 1);

            // Fade towards both ends so the bar melts into the rounded corners
            double edge = Math.min(t, 1 - t) * 6;
            double alpha = 0.95 * Math.min(1, edge);

            roundedRect().pos(from + slice * i, y)
                         .size(slice + 0.75, barHeight)
                         .color(ColorUtils.withAlpha(ColorUtils.lerp(start, end, t), alpha))
                         .render();
        }
    }
}
