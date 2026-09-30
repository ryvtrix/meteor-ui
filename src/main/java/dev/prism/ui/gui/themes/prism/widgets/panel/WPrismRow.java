package dev.prism.ui.gui.themes.prism.widgets.panel;

import dev.prism.ui.api.animation.Animation;
import dev.prism.ui.api.animation.Direction;
import dev.prism.ui.api.animation.Easing;
import dev.prism.ui.api.render.style.Corners;
import dev.prism.ui.api.text.RichText;
import dev.prism.ui.gui.themes.prism.PrismGuiTheme;
import dev.prism.ui.gui.themes.prism.PrismWidget;
import dev.prism.ui.gui.themes.prism.icons.PrismBuiltinIcons;
import dev.prism.ui.gui.themes.prism.widgets.settings.WPrismKeybind;
import dev.prism.ui.utils.ColorUtils;
import meteordevelopment.meteorclient.gui.renderer.GuiRenderer;
import meteordevelopment.meteorclient.gui.utils.Cell;
import meteordevelopment.meteorclient.gui.widgets.WWidget;
import meteordevelopment.meteorclient.gui.widgets.containers.WContainer;
import meteordevelopment.meteorclient.gui.widgets.containers.WHorizontalList;
import meteordevelopment.meteorclient.gui.widgets.containers.WVerticalList;
import meteordevelopment.meteorclient.gui.widgets.pressable.WCheckbox;
import meteordevelopment.meteorclient.gui.widgets.pressable.WPressable;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.utils.render.color.Color;

import java.util.Locale;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT;

/**
 * A module in the panel: a compact row (name, gear, animated switch) that expands in place to show
 * the module's keybind and settings.
 *
 * <ul>
 *   <li>Left click: toggle the module</li>
 *   <li>Right click, or click the gear: expand / collapse the settings</li>
 * </ul>
 */
public class WPrismRow extends WVerticalList implements PrismWidget {
    private final Module module;
    private final String title;

    private RichText label;
    private Header header;

    private WVerticalList body;
    private Cell<WVerticalList> bodyCell;
    private WVerticalList settingsBox;
    private WPrismKeybind keybind;

    private boolean expanded;
    private boolean wasBinding;
    private Animation expandAnimation;
    private double actualHeight;

    public WPrismRow(Module module, String title) {
        this.module = module;
        this.title = title;
    }

    @Override
    public void init() {
        super.init();

        PrismGuiTheme theme = theme();

        spacing = 0;
        label = RichText.of(theme.uppercaseText.get() ? title.toUpperCase(Locale.ROOT) : title);

        expandAnimation = new Animation(
                theme.guiAnimationEasing(),
                theme.guiAnimationDuration(),
                Direction.BACKWARDS
        );

        header = add(new Header()).expandX().widget();
    }

    // Expanding

    private void toggleExpanded() {
        expanded = !expanded;

        if (expanded && body == null) buildBody();

        if (expandAnimation.isRunning()) expandAnimation.reverse();
        else expandAnimation.start(expanded ? Direction.FORWARDS : Direction.BACKWARDS);

        invalidate();
    }

    private void buildBody() {
        PrismGuiTheme theme = theme();
        double pad = theme.scale(8);

        body = theme.verticalList();
        body.spacing = pad;

        // Keybind + hold mode
        WHorizontalList bind = body.add(theme.horizontalList())
                                   .expandX()
                                   .padHorizontal(pad)
                                   .padVertical(pad)
                                   .widget();
        bind.spacing = pad;

        keybind = bind.add(theme.prismKeybind(module.keybind)).widget();
        keybind.actionOnSet = () -> Modules.get().setModuleToBind(module);

        WCheckbox hold = bind.add(theme.checkbox(module.toggleOnBindRelease)).widget();
        hold.action = () -> module.toggleOnBindRelease = hold.checked;
        hold.tooltip = "Only keep the module enabled while the key is held.";
        bind.add(theme.label("Hold"));

        // Chat feedback
        WHorizontalList feedback = body.add(theme.horizontalList())
                                       .expandX()
                                       .padHorizontal(pad)
                                       .widget();
        feedback.spacing = pad;

        WCheckbox chat = feedback.add(theme.checkbox(module.chatFeedback)).widget();
        chat.action = () -> module.chatFeedback = chat.checked;
        chat.tooltip = "Displays a toggle message in chat when enabled.";
        feedback.add(theme.label("Chat feedback"));

        // Settings
        if (!module.settings.groups.isEmpty()) {
            settingsBox = body.add(theme.verticalList()).expandX().padHorizontal(pad).widget();
            settingsBox.add(theme.settings(module.settings)).expandX();
        }

        // Module specific widget
        WWidget custom = module.getWidget(theme);

        if (custom != null) {
            body.add(theme.horizontalSeparator()).expandX().padHorizontal(pad);

            WContainer container = body.add(theme.horizontalList())
                                       .expandX()
                                       .padHorizontal(pad)
                                       .widget();

            Cell<WWidget> cell = container.add(custom);
            if (custom instanceof WContainer) cell.expandX();
        }

        // Bottom breathing room
        body.add(theme.horizontalSeparator()).expandX().padHorizontal(pad);

        bodyCell = add(body).expandX();
    }

    private void removeBody() {
        if (bodyCell != null) remove(bodyCell);

        bodyCell = null;
        body = null;
        settingsBox = null;
        keybind = null;

        invalidate();
    }

    private void tickBody() {
        // Settings can change the visibility of other settings
        if (settingsBox != null) module.settings.tick(settingsBox, theme());

        // Refresh the keybind button once binding is over
        boolean binding = Modules.get().isBinding();
        if (keybind != null && wasBinding && !binding) keybind.reset();
        wasBinding = binding;
    }

    // Layout

    @Override
    protected void onCalculateSize() {
        super.onCalculateSize();
        actualHeight = height;

        if (header == null) return;

        double progress = Math.max(0, expandAnimation.getProgress());

        if (expandAnimation.isRunning() || progress < 1) {
            double contentHeight = Math.max(actualHeight - header.height, 0);
            height = header.height + contentHeight * progress;
        }
    }

    // Rendering

    @Override
    public boolean render(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
        if (!visible) return true;

        if (!expanded && body != null && !expandAnimation.isRunning()) removeBody();
        if (expanded && body != null) tickBody();

        boolean animating = expandAnimation.isRunning();

        if (animating) {
            invalidate();
            renderer.scissorStart(x, y, width, height);
        }

        boolean result = super.render(renderer, mouseX, mouseY, delta);

        if (animating) renderer.scissorEnd();

        return result;
    }

    @Override
    protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
        if (header == null || expandAnimation == null) return;

        double progress = clamp01(expandAnimation.getProgress());
        if (progress <= 0 && !expandAnimation.isRunning()) return;

        double top = y + header.height;
        double h = height - header.height;
        if (h <= 0) return;

        PrismGuiTheme theme = theme();

        // Inset panel behind the settings
        roundedRect().pos(x, top)
                     .size(width, h)
                     .radius(smallRadius(), Corners.BOTTOM)
                     .color(ColorUtils.withAlpha(theme.crustColor(), 0.38))
                     .outline(ColorUtils.withAlpha(theme.accentColor(), (int) (55 * progress)), outlineWidth())
                     .render();
    }

    private static double clamp01(double v) {
        return Math.max(0, Math.min(1, v));
    }

    // Header

    private class Header extends WPressable implements PrismWidget {
        private static final double GAP = 8;

        private double textWidth;
        private double lastMouseX;

        private Animation activeAnimation;
        private Animation hoverAnimation;
        private boolean wasActive;
        private boolean wasHovered;

        @Override
        public void init() {
            wasActive = module.isActive();

            activeAnimation = new Animation(
                    Easing.QUART_OUT,
                    260,
                    wasActive ? Direction.FORWARDS : Direction.BACKWARDS
            );

            hoverAnimation = new Animation(Easing.LINEAR, 160);

            tooltip = module.description;
        }

        private double padX() {
            return theme().scale(10);
        }

        private double gap() {
            return theme().scale(GAP);
        }

        private double switchHeight() {
            return theme().textHeight() * 1.05;
        }

        private double switchWidth() {
            return switchHeight() * 1.9;
        }

        private double gearSize() {
            return theme().textHeight() * 0.9;
        }

        private double gearX() {
            return x + width - padX() - switchWidth() - gap() - gearSize();
        }

        @Override
        protected void onCalculateSize() {
            PrismGuiTheme theme = theme();

            textWidth = theme.textWidth(label);

            width = padX() * 2 + textWidth + gap() * 2 + gearSize() + switchWidth();
            height = theme.textHeight() + theme.scale(theme.rowPadding.get()) * 2;
        }

        @Override
        protected void onPressed(int button) {
            if (button == GLFW_MOUSE_BUTTON_RIGHT) {
                toggleExpanded();
                return;
            }

            if (button != GLFW_MOUSE_BUTTON_LEFT) return;

            double gearX = gearX();
            boolean onGear = lastMouseX >= gearX - gap() / 2 && lastMouseX <= gearX + gearSize() + gap() / 2;

            if (onGear) toggleExpanded();
            else module.toggle();
        }

        @Override
        protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
            PrismGuiTheme theme = theme();
            lastMouseX = mouseX;

            boolean active = module.isActive();

            if (active != wasActive) {
                wasActive = active;
                activeAnimation.start(active ? Direction.FORWARDS : Direction.BACKWARDS);
            }

            if (mouseOver != wasHovered) {
                wasHovered = mouseOver;

                if (mouseOver) hoverAnimation.start();
                else hoverAnimation.finishedAt(Direction.BACKWARDS);
            }

            double activeProgress = clamp01(activeAnimation.getProgress());
            double hoverProgress = theme.hoverGlow.get() ? clamp01(hoverAnimation.getProgress()) : 0;
            double expandProgress = clamp01(expandAnimation.getProgress());

            Color accent = theme.accentColor();
            double padX = padX();

            // Hover / active background
            if (hoverProgress > 0 || activeProgress > 0) {
                int alpha = (int) Math.min(255, 255 * (0.11 * hoverProgress + 0.07 * activeProgress));

                roundedRect().bounds(this)
                             .radius(smallRadius(), expandProgress > 0.02 ? Corners.TOP : Corners.ALL)
                             .color(ColorUtils.withAlpha(accent, alpha))
                             .render();
            }

            // Active indicator
            if (theme.activeIndicator.get() && activeProgress > 0.01) {
                double barHeight = height * 0.52 * activeProgress;

                roundedRect().pos(x + theme.scale(2), y + (height - barHeight) / 2)
                             .size(theme.scale(3), barHeight)
                             .radius(theme.scale(1.5))
                             .color(accent)
                             .render();
            }

            // Title
            Color dim = ColorUtils.lerp(theme.textColor(), theme.textSecondaryColor(), 0.5);
            Color textColor = ColorUtils.lerp(dim, theme.textColor(), Math.max(activeProgress, hoverProgress * 0.8));

            renderer().text(label, x + padX + theme.scale(3), y + (height - theme.textHeight()) / 2, textColor);

            // Gear
            double gearSize = gearSize();
            double gearX = gearX();
            boolean overGear = mouseOver && mouseX >= gearX - gap() / 2 && mouseX <= gearX + gearSize + gap() / 2;

            Color gearColor = ColorUtils.lerp(theme.overlay2Color(), accent, expandProgress);
            if (overGear) gearColor = theme.textColor();

            renderer.quad(
                    gearX,
                    y + (height - gearSize) / 2,
                    gearSize,
                    gearSize,
                    PrismBuiltinIcons.SETTING.texture(),
                    gearColor
            );

            renderSwitch(activeProgress);
        }

        private void renderSwitch(double progress) {
            PrismGuiTheme theme = theme();

            double sh = switchHeight();
            double sw = switchWidth();
            double sx = x + width - padX() - sw;
            double sy = y + (height - sh) / 2;
            double inset = theme.scale(2);

            Color accent = theme.accentColor();

            Color track = ColorUtils.lerp(ColorUtils.withAlpha(theme.crustColor(), 0.7), accent, progress);
            Color outline = ColorUtils.withAlpha(
                    ColorUtils.lerp(theme.overlay0Color(), accent, progress),
                    mouseOver ? 170 : 110
            );

            var trackShape = roundedRect().pos(sx, sy)
                                          .size(sw, sh)
                                          .radius(sh / 2)
                                          .color(track)
                                          .outline(outline, outlineWidth());

            if (theme.toggleGlow.get() && progress > 0.02) {
                trackShape.shadow(
                        theme.scale(9) * progress,
                        theme.scale(0.5),
                        ColorUtils.withAlpha(accent, 0.55 * progress)
                );
            }

            trackShape.render();

            double knob = sh - inset * 2;
            double knobX = sx + inset + (sw - knob - inset * 2) * progress;

            roundedRect().pos(knobX, sy + inset)
                         .size(knob, knob)
                         .radius(knob / 2)
                         .color(ColorUtils.lerp(theme.textSecondaryColor(), theme.textColor(), progress))
                         .render();
        }
    }
}
