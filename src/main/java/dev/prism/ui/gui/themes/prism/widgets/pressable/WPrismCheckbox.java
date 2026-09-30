package dev.prism.ui.gui.themes.prism.widgets.pressable;

import dev.prism.ui.api.animation.Animation;
import dev.prism.ui.api.animation.Direction;
import dev.prism.ui.api.animation.Easing;
import dev.prism.ui.gui.themes.prism.PrismGuiTheme;
import dev.prism.ui.gui.themes.prism.PrismWidget;
import dev.prism.ui.gui.themes.prism.icons.PrismBuiltinIcons;
import dev.prism.ui.utils.ColorUtils;
import meteordevelopment.meteorclient.utils.render.color.Color;
import meteordevelopment.meteorclient.gui.renderer.GuiRenderer;
import meteordevelopment.meteorclient.gui.widgets.pressable.WCheckbox;

public class WPrismCheckbox extends WCheckbox implements PrismWidget {
    private Animation animation;

    public WPrismCheckbox(boolean checked) {
        super(checked);
    }

    @Override
    public void init() {
        super.init();

        animation = new Animation(
                Easing.BACK_IN_OUT,
                300,
                checked ? Direction.FORWARDS : Direction.BACKWARDS
        );
    }

    @Override
    protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
        if (theme().useSwitches()) {
            renderSwitch();
            return;
        }

        // Background is only visible when unchecked or animating
        if (!checked || animation.isRunning()) background(false, mouseOver).render();

        // Skip checkmark if unchecked and animation finished
        if (!checked && animation.isFinished()) return;

        renderCheckmark(renderer);
    }

    @Override
    protected void onCalculateSize() {
        super.onCalculateSize();

        if (theme().useSwitches()) {
            height *= 0.8;
            width = height * 1.9;
            return;
        }

        height *= 0.9;
        width *= 0.9;
    }

    @Override
    protected void onPressed(int button) {
        super.onPressed(button);
        animation.start(checked ? Direction.FORWARDS : Direction.BACKWARDS);
    }

    /**
     * Pill shaped switch: the track fades to the accent color and the knob slides across.
     */
    private void renderSwitch() {
        PrismGuiTheme theme = theme();

        double raw = animation.getProgress();
        double progress = Math.max(0, Math.min(1, raw));
        double inset = theme.scale(2);

        Color offTrack = ColorUtils.withAlpha(theme.crustColor(), 0.75);
        Color track = ColorUtils.lerp(offTrack, theme.accentColor(), progress);

        Color outline = ColorUtils.withAlpha(
                ColorUtils.lerp(theme.overlay0Color(), theme.accentColor(), progress),
                (int) (mouseOver ? 170 : 110)
        );

        var trackShape = roundedRect().bounds(this)
                                      .radius(height / 2)
                                      .color(track)
                                      .outline(outline, outlineWidth());

        if (theme.toggleGlow.get() && progress > 0.02) {
            trackShape.shadow(
                    theme.scale(9) * progress,
                    theme.scale(0.5),
                    ColorUtils.withAlpha(theme.accentColor(), 0.55 * progress)
            );
        }

        trackShape.render();

        double knob = height - inset * 2;
        double travel = width - knob - inset * 2;
        double knobX = x + inset + travel * raw;

        roundedRect().pos(knobX, y + inset)
                     .size(knob, knob)
                     .radius(knob / 2)
                     .color(ColorUtils.lerp(theme.textSecondaryColor(), theme.textColor(), progress))
                     .render();
    }

    private void renderCheckmark(GuiRenderer renderer) {
        PrismGuiTheme theme = theme();
        double progress = animation.getProgress();
        double size = width * progress;
        double tickSize = size * 0.6;
        double minSize = theme.scale(6);

        if (size <= minSize) return;

        double centerOffset = (width - size) / 2;

        roundedRect().pos(x + centerOffset, y + centerOffset)
                     .size(size, size)
                     .radius(smallRadius())
                     .color(theme.accentColor())
                     .outline(theme.accentColor().copy().a(mouseOver ? 140 : 80), 3f)
                     .render();

        if (tickSize <= minSize) return;

        centerOffset = (width - tickSize) / 2;

        renderer.rotatedQuad(
                x + centerOffset,
                y + centerOffset,
                tickSize,
                tickSize,
                0,
                PrismBuiltinIcons.TICK.texture(),
                theme.backgroundColor.get(160)
        );
    }

    public void setChecked(boolean checked) {
        if (this.checked == checked) return;
        this.checked = checked;
        animation.start(checked ? Direction.FORWARDS : Direction.BACKWARDS);
    }
}