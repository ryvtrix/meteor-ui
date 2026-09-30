package dev.prism.ui.gui.themes.prism;

import dev.prism.ui.PrismAddon;
import dev.prism.ui.api.animation.Easing;
import dev.prism.ui.api.render.style.Shadow;
import dev.prism.ui.api.text.RichText;
import dev.prism.ui.api.text.RichTextSegment;
import dev.prism.ui.gui.screens.PrismModuleScreen;
import dev.prism.ui.gui.screens.PrismPanelScreen;
import dev.prism.ui.gui.themes.prism.config.PrismPreset;
import dev.prism.ui.gui.themes.prism.widgets.panel.*;
import dev.prism.ui.utils.ColorUtils;
import dev.prism.ui.gui.screens.PrismWindowsModulesScreen;
import dev.prism.ui.gui.themes.prism.colors.PrismAccentColor;
import dev.prism.ui.gui.themes.prism.colors.PrismColor;
import dev.prism.ui.gui.themes.prism.colors.ColorLinkRegistry;
import dev.prism.ui.gui.themes.prism.flavors.PrismFlavors;
import dev.prism.ui.gui.themes.prism.widgets.*;
import dev.prism.ui.gui.themes.prism.widgets.container.WPrismSection;
import dev.prism.ui.gui.themes.prism.widgets.container.WPrismTabView;
import dev.prism.ui.gui.themes.prism.widgets.container.WPrismView;
import dev.prism.ui.gui.themes.prism.widgets.container.WPrismWindow;
import dev.prism.ui.gui.themes.prism.widgets.input.*;
import dev.prism.ui.gui.themes.prism.widgets.pressable.*;
import dev.prism.ui.gui.themes.prism.widgets.settings.WPrismDoubleEdit;
import dev.prism.ui.gui.themes.prism.widgets.settings.WPrismIntEdit;
import dev.prism.ui.gui.themes.prism.widgets.settings.WPrismKeybind;
import dev.prism.ui.gui.widgets.WGuiTexture;
import dev.prism.ui.gui.widgets.container.WTreeTable;
import dev.prism.ui.gui.widgets.tabs.PrismTab;
import dev.prism.ui.gui.widgets.tabs.WTabView;
import dev.prism.ui.gui.widgets.input.WMultiSelect;
import dev.prism.ui.gui.widgets.input.WSearch;
import dev.prism.ui.gui.widgets.pressable.WColorPicker;
import dev.prism.ui.renderer.PrismRenderer;
import dev.prism.ui.renderer.text.RichTextRenderer;
import meteordevelopment.meteorclient.gui.GuiTheme;
import meteordevelopment.meteorclient.gui.WidgetScreen;
import meteordevelopment.meteorclient.gui.renderer.packer.GuiTexture;
import meteordevelopment.meteorclient.gui.tabs.TabScreen;
import meteordevelopment.meteorclient.gui.utils.AlignmentX;
import meteordevelopment.meteorclient.gui.utils.CharFilter;
import meteordevelopment.meteorclient.gui.widgets.*;
import meteordevelopment.meteorclient.gui.widgets.containers.WSection;
import meteordevelopment.meteorclient.gui.widgets.containers.WTable;
import meteordevelopment.meteorclient.gui.widgets.containers.WView;
import meteordevelopment.meteorclient.gui.widgets.containers.WWindow;
import meteordevelopment.meteorclient.gui.widgets.input.WDropdown;
import meteordevelopment.meteorclient.gui.widgets.input.WSlider;
import meteordevelopment.meteorclient.gui.widgets.input.WTextBox;
import meteordevelopment.meteorclient.gui.widgets.pressable.*;
import meteordevelopment.meteorclient.renderer.text.TextRenderer;
import meteordevelopment.meteorclient.renderer.text.VanillaTextRenderer;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.accounts.Account;
import meteordevelopment.meteorclient.systems.config.Config;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.misc.Keybind;
import meteordevelopment.meteorclient.utils.render.color.Color;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import net.minecraft.client.gui.screens.Screen;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static meteordevelopment.meteorclient.MeteorClient.mc;
import net.minecraft.util.Util;

public class PrismGuiTheme extends GuiTheme {
    private final Map<PrismColor, Color> colorCache;

    private RichTextRenderer textRenderer;

    private WPrismTopBar topBar;

    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final SettingGroup sgLayout = settings.createGroup("Panel Layout");
    private final SettingGroup sgColors = settings.createGroup("Colors");
    private final SettingGroup sgTransparency = settings.createGroup("Transparency");
    private final SettingGroup sgShape = settings.createGroup("Shape");
    private final SettingGroup sgEffects = settings.createGroup("Effects");
    private final SettingGroup sgAnimations = settings.createGroup("Animations");
    private final SettingGroup sgShadows = settings.createGroup("Shadows");
    private final SettingGroup sgScreens = settings.createGroup("Screens");
    private final SettingGroup sgSnapping = settings.createGroup("Snapping");
    private final SettingGroup sgStarscript = settings.createGroup("Starscript");

    public enum Layout {
        Panel,
        Windows
    }

    public enum ToggleStyle {
        Switch,
        Checkbox
    }

    // General

    public final Setting<Layout> layout = sgGeneral.add(new EnumSetting.Builder<Layout>()
            .name("layout")
            .description("Panel: one sidebar panel with categories. Windows: classic draggable category windows.")
            .defaultValue(Layout.Panel)
            .build()
    );

    public final Setting<Double> scale = sgGeneral.add(new DoubleSetting.Builder()
            .name("scale")
            .description("Scale of the GUI.")
            .defaultValue(1)
            .min(0.75)
            .sliderRange(0.75, 4)
            .onSliderRelease()
            .onChanged(ignored -> invalidateScreen())
            .build()
    );

    public final Setting<Boolean> uppercaseText = sgGeneral.add(new BoolSetting.Builder()
            .name("uppercase-text")
            .description("Render module and category names in UPPERCASE. (Reopen the GUI to apply)")
            .defaultValue(true)
            .build()
    );

    public final Setting<ToggleStyle> toggleStyle = sgGeneral.add(new EnumSetting.Builder<ToggleStyle>()
            .name("toggle-style")
            .description("How on/off options are drawn.")
            .defaultValue(ToggleStyle.Switch)
            .build()
    );

    public final Setting<AlignmentX> moduleAlignment = sgGeneral.add(new EnumSetting.Builder<AlignmentX>()
            .name("module-alignment")
            .description("How module titles are aligned in the Windows layout.")
            .defaultValue(AlignmentX.Center)
            .visible(() -> layout.get() == Layout.Windows)
            .build()
    );

    public final Setting<Boolean> categoryIcons = sgGeneral.add(new BoolSetting.Builder()
            .name("category-icons")
            .description("Displays icons next to module categories.")
            .defaultValue(true)
            .build()
    );

    public final Setting<Boolean> tabIcons = sgGeneral.add(new BoolSetting.Builder()
            .name("tab-icons")
            .description("Displays icons next to tabs in the top bar. (Switch tabs to update the GUI)")
            .defaultValue(true)
            .onChanged(ignored -> topBar = null) // Rebuild the top bar on next tab switch
            .build()
    );

    public final Setting<Boolean> hideHUD = sgGeneral.add(new BoolSetting.Builder()
            .name("hide-HUD")
            .description("Hide HUD when in GUI.")
            .defaultValue(false)
            .onChanged(v -> {
                if (mc.gui.screen() instanceof WidgetScreen) mc.gameRenderer.gameRenderState().guiRenderState.isHudHidden = v;
            })
            .build()
    );

    public final Setting<Boolean> modulesHelpText = sgGeneral.add(new BoolSetting.Builder()
            .name("modules-help-text")
            .description("Toggle help text in the modules screen.")
            .defaultValue(true)
            .build()
    );

    public final Setting<Boolean> indentSettings = sgGeneral.add(new BoolSetting.Builder()
            .name("indent-settings")
            .description("Indents setting that have conditional visibility like in a Tree View.")
            .defaultValue(true)
            .build()
    );

    // Panel layout

    public final Setting<Integer> panelWidth = sgLayout.add(new IntSetting.Builder()
            .name("panel-width")
            .description("Width of the panel as a percentage of the screen.")
            .defaultValue(58)
            .sliderRange(35, 95)
            .build()
    );

    public final Setting<Integer> panelHeight = sgLayout.add(new IntSetting.Builder()
            .name("panel-height")
            .description("Height of the panel as a percentage of the screen.")
            .defaultValue(62)
            .sliderRange(35, 90)
            .build()
    );

    public final Setting<Integer> sidebarWidth = sgLayout.add(new IntSetting.Builder()
            .name("sidebar-width")
            .description("Width of the category sidebar.")
            .defaultValue(190)
            .sliderRange(120, 320)
            .build()
    );

    public final Setting<Integer> columns = sgLayout.add(new IntSetting.Builder()
            .name("module-columns")
            .description("How many columns of modules to show.")
            .defaultValue(2)
            .sliderRange(1, 3)
            .build()
    );

    public final Setting<Integer> rowPadding = sgLayout.add(new IntSetting.Builder()
            .name("row-padding")
            .description("Vertical padding of module rows. Lower is more compact.")
            .defaultValue(5)
            .sliderRange(1, 14)
            .build()
    );

    public final Setting<Boolean> showBrand = sgLayout.add(new BoolSetting.Builder()
            .name("show-brand")
            .description("Show the logo and name at the top of the sidebar.")
            .defaultValue(true)
            .build()
    );

    public final Setting<String> brandName = sgLayout.add(new StringSetting.Builder()
            .name("brand-name")
            .description("Text shown at the top of the sidebar.")
            .defaultValue("PRISM")
            .visible(showBrand::get)
            .build()
    );

    // Colors

    public final Setting<PrismPreset> preset = sgColors.add(new EnumSetting.Builder<PrismPreset>()
            .name("preset")
            .description("Selecting a preset overwrites the colors below. Choose Custom to keep your own.")
            .defaultValue(PrismPreset.Violet)
            .onChanged(this::applyPreset)
            .build()
    );

    public final Setting<SettingColor> accent = sgColors.add(new ColorSetting.Builder()
            .name("accent")
            .description("Main accent: toggles, sliders, selection, glow.")
            .defaultValue(PrismPreset.Violet.accent())
            .build()
    );

    public final Setting<SettingColor> accentSecondary = sgColors.add(new ColorSetting.Builder()
            .name("accent-secondary")
            .description("Second accent used for the gradient bar and backdrop tint.")
            .defaultValue(PrismPreset.Violet.accentSecondary())
            .build()
    );

    public final Setting<SettingColor> backgroundBase = sgColors.add(new ColorSetting.Builder()
            .name("background")
            .description("Base color of the panel. Sidebar, rows and outlines are derived from it.")
            .defaultValue(PrismPreset.Violet.background())
            .build()
    );

    public final Setting<SettingColor> textPrimary = sgColors.add(new ColorSetting.Builder()
            .name("text")
            .description("Main text color.")
            .defaultValue(PrismPreset.Violet.text())
            .build()
    );

    public final Setting<SettingColor> textMuted = sgColors.add(new ColorSetting.Builder()
            .name("text-secondary")
            .description("Color of secondary / inactive text.")
            .defaultValue(PrismPreset.Violet.textSecondary())
            .build()
    );

    public final Setting<Double> surfaceContrast = sgColors.add(new DoubleSetting.Builder()
            .name("surface-contrast")
            .description("How strongly buttons, rows and outlines stand out from the background.")
            .defaultValue(1)
            .sliderRange(0.4, 2)
            .decimalPlaces(2)
            .build()
    );

    // Transparency

    public final Setting<Double> panelOpacity = sgTransparency.add(new DoubleSetting.Builder()
            .name("panel-opacity")
            .description("Opacity of the main panel.")
            .defaultValue(0.86)
            .sliderRange(0, 1)
            .decimalPlaces(2)
            .build()
    );

    public final Setting<Double> sidebarOpacity = sgTransparency.add(new DoubleSetting.Builder()
            .name("sidebar-opacity")
            .description("Opacity of the sidebar (on top of the panel).")
            .defaultValue(0.55)
            .sliderRange(0, 1)
            .decimalPlaces(2)
            .build()
    );

    public final Setting<Double> windowOpacity = sgTransparency.add(new DoubleSetting.Builder()
            .name("window-opacity")
            .description("Controls the opacity of the windows (Windows layout, settings screens).")
            .defaultValue(1)
            .sliderRange(0, 1)
            .decimalPlaces(2)
            .build()
    );

    public final Setting<Double> backgroundOpacity = sgTransparency.add(new DoubleSetting.Builder()
            .name("background-opacity")
            .description("Controls the opacity of the backgrounds of UI elements.")
            .defaultValue(1)
            .sliderRange(0, 1)
            .decimalPlaces(2)
            .build()
    );

    public final Setting<Double> screenDim = sgTransparency.add(new DoubleSetting.Builder()
            .name("screen-dim")
            .description("How much the game view behind the GUI is darkened.")
            .defaultValue(0.25)
            .sliderRange(0, 1)
            .decimalPlaces(2)
            .build()
    );

    // Shape

    public final Setting<Integer> cornerRadius = sgShape.add(new IntSetting.Builder()
            .name("corner-radius")
            .description("The radius of corners for large UI elements.")
            .defaultValue(12)
            .sliderRange(0, 30)
            .build()
    );

    public final Setting<Integer> smallCornerRadius = sgShape.add(new IntSetting.Builder()
            .name("small-corner-radius")
            .description("The radius of corners for small UI elements.")
            .defaultValue(7)
            .sliderRange(0, 25)
            .build()
    );

    public final Setting<Double> outlineThickness = sgShape.add(new DoubleSetting.Builder()
            .name("outline-thickness")
            .description("Thickness of outlines around panels and widgets.")
            .defaultValue(1)
            .sliderRange(0, 4)
            .decimalPlaces(1)
            .build()
    );

    public final Setting<Double> outlineOpacity = sgShape.add(new DoubleSetting.Builder()
            .name("panel-outline-opacity")
            .description("Opacity of the accent outline around the main panel.")
            .defaultValue(0.35)
            .sliderRange(0, 1)
            .decimalPlaces(2)
            .build()
    );

    // Effects

    public final Setting<Boolean> panelGlow = sgEffects.add(new BoolSetting.Builder()
            .name("panel-glow")
            .description("Soft accent-colored glow around the panel.")
            .defaultValue(true)
            .build()
    );

    public final Setting<Integer> glowSize = sgEffects.add(new IntSetting.Builder()
            .name("glow-size")
            .description("How far the glow reaches.")
            .defaultValue(34)
            .sliderRange(4, 120)
            .visible(panelGlow::get)
            .build()
    );

    public final Setting<Double> glowStrength = sgEffects.add(new DoubleSetting.Builder()
            .name("glow-strength")
            .description("Opacity of the glow.")
            .defaultValue(0.32)
            .sliderRange(0, 1)
            .decimalPlaces(2)
            .visible(panelGlow::get)
            .build()
    );

    public final Setting<Boolean> accentBar = sgEffects.add(new BoolSetting.Builder()
            .name("accent-bar")
            .description("Gradient line (accent to secondary accent) along the top of the panel.")
            .defaultValue(true)
            .build()
    );

    public final Setting<Boolean> backdropTint = sgEffects.add(new BoolSetting.Builder()
            .name("backdrop-tint")
            .description("Tints the game view behind the GUI with a gradient of your accent colors.")
            .defaultValue(true)
            .build()
    );

    public final Setting<Double> backdropTintStrength = sgEffects.add(new DoubleSetting.Builder()
            .name("backdrop-tint-strength")
            .description("Opacity of the backdrop tint.")
            .defaultValue(0.28)
            .sliderRange(0, 1)
            .decimalPlaces(2)
            .visible(backdropTint::get)
            .build()
    );

    public final Setting<Boolean> hoverGlow = sgEffects.add(new BoolSetting.Builder()
            .name("hover-glow")
            .description("Rows and buttons light up softly under the cursor.")
            .defaultValue(true)
            .build()
    );

    public final Setting<Boolean> toggleGlow = sgEffects.add(new BoolSetting.Builder()
            .name("toggle-glow")
            .description("Enabled toggles glow in the accent color.")
            .defaultValue(true)
            .build()
    );

    public final Setting<Boolean> activeIndicator = sgEffects.add(new BoolSetting.Builder()
            .name("active-indicator")
            .description("Thin accent bar on the left of enabled modules.")
            .defaultValue(true)
            .build()
    );

    // Animations

    public final Setting<Easing> guiAnimation = sgAnimations.add(new EnumSetting.Builder<Easing>()
            .name("gui-animation-easing")
            .description("The easing function used for UI animations.")
            .defaultValue(Easing.QUART_OUT)
            .build()
    );

    public final Setting<Integer> guiAnimationDuration = sgAnimations.add(new IntSetting.Builder()
            .name("gui-animation-duration")
            .description("Duration of the animation in milliseconds.")
            .defaultValue(300)
            .sliderRange(1, 1000)
            .build()
    );

    // Shadows

    public final Setting<Boolean> windowShadow = sgShadows.add(new BoolSetting.Builder()
            .name("window-shadow")
            .description("Render a shadow under windows.")
            .defaultValue(true)
            .build()
    );

    public final Setting<Integer> shadowOffsetX = sgShadows.add(new IntSetting.Builder()
            .name("shadow-offset-x")
            .description("Offset of the shadow on horizontal axis.")
            .defaultValue(0)
            .sliderRange(-50, 50)
            .build()
    );

    public final Setting<Integer> shadowOffsetY = sgShadows.add(new IntSetting.Builder()
            .name("shadow-offset-y")
            .description("Offset of the shadow on vertical axis.")
            .defaultValue(0)
            .sliderRange(-50, 50)
            .build()
    );

    public final Setting<Integer> shadowBlur = sgShadows.add(new IntSetting.Builder()
            .name("shadow-blur")
            .description("Softness of the shadow edge.")
            .defaultValue(50)
            .sliderRange(0, 200)
            .build()
    );

    public final Setting<Integer> shadowSpread = sgShadows.add(new IntSetting.Builder()
            .name("shadow-spread")
            .description("Grows or shrinks the shadow size.")
            .defaultValue(-15)
            .sliderRange(-30, 30)
            .build()
    );

    private final Setting<SettingColor> shadowColor = sgShadows.add(new ColorSetting.Builder()
            .name("shadow-color")
            .description("Color of the shadow.")
            .defaultValue(Color.BLACK)
            .build()
    );

    // Snapping

    public final Setting<Boolean> snapModuleCategories = sgSnapping.add(new BoolSetting.Builder()
            .name("snap-module-categories")
            .description("Snaps category windows to the grid.")
            .defaultValue(true)
            .build()
    );

    public final Setting<Integer> snappingGridSize = sgSnapping.add(new IntSetting.Builder()
            .name("grid-size")
            .description("The size of the snapping grid.")
            .defaultValue(10)
            .sliderRange(5, 50)
            .visible(snapModuleCategories::get)
            .build()
    );

    // Screens

    public final Setting<Boolean> prismSearchScreen = sgScreens.add(new BoolSetting.Builder()
            .name("search-screen")
            .description("Replaces Meteor's search window with Prism's search screen.")
            .defaultValue(true)
            .build()
    );

    public final Setting<Boolean> prismEntityTypeListScreen = sgScreens.add(new BoolSetting.Builder()
            .name("entity-type-list-screen")
            .description("Replaces Meteor's entity selection screen with the Prism version.")
            .defaultValue(true)
            .build()
    );

    // Three state colors

    public final ThreeStateColor backgroundColor = new ThreeStateColor(
            this::surface0Color,
            this::surface1Color,
            this::surface2Color
    );

    public final ThreeStateColor outlineColor = new ThreeStateColor(
            this::overlay0Color,
            this::overlay1Color,
            this::overlay2Color
    );

    public final ThreeStateColor scrollbarColor = new ThreeStateColor(
            this::surface0Color,
            this::surface1Color,
            this::surface2Color
    );

    // Starscript

    private final Setting<SettingColor> starscriptText = color(sgStarscript, "starscript-text", "Color of text in Starscript code.", new SettingColor(169, 183, 198));
    private final Setting<SettingColor> starscriptBraces = color(sgStarscript, "starscript-braces", "Color of braces in Starscript code.", new SettingColor(150, 150, 150));
    private final Setting<SettingColor> starscriptParenthesis = color(sgStarscript, "starscript-parenthesis", "Color of parenthesis in Starscript code.", new SettingColor(169, 183, 198));
    private final Setting<SettingColor> starscriptDots = color(sgStarscript, "starscript-dots", "Color of dots in starscript code.", new SettingColor(169, 183, 198));
    private final Setting<SettingColor> starscriptCommas = color(sgStarscript, "starscript-commas", "Color of commas in starscript code.", new SettingColor(169, 183, 198));
    private final Setting<SettingColor> starscriptOperators = color(sgStarscript, "starscript-operators", "Color of operators in Starscript code.", new SettingColor(169, 183, 198));
    private final Setting<SettingColor> starscriptStrings = color(sgStarscript, "starscript-strings", "Color of strings in Starscript code.", new SettingColor(106, 135, 89));
    private final Setting<SettingColor> starscriptNumbers = color(sgStarscript, "starscript-numbers", "Color of numbers in Starscript code.", new SettingColor(104, 141, 187));
    private final Setting<SettingColor> starscriptKeywords = color(sgStarscript, "starscript-keywords", "Color of keywords in Starscript code.", new SettingColor(204, 120, 50));
    private final Setting<SettingColor> starscriptAccessedObjects = color(sgStarscript, "starscript-accessed-objects", "Color of accessed objects (before a dot) in Starscript code.", new SettingColor(152, 118, 170));

    public PrismGuiTheme() {
        super("Prism");

        settingsFactory = new PrismSettingsWidgetFactory(this);
        colorCache = new EnumMap<>(PrismColor.class);

        rebuildPalette();
    }

    private Setting<SettingColor> color(SettingGroup group, String name, String description, SettingColor color) {
        return group.add(new ColorSetting.Builder()
                .name(name + "-color")
                .description(description)
                .defaultValue(color)
                .build());
    }

    // Widgets

    @Override
    public WWindow window(WWidget icon, String title) {
        return w(new WPrismWindow(icon, title));
    }

    public WLabel label(RichText text, double maxWidth) {
        if (maxWidth == 0) return w(new WPrismLabel(text));
        return w(new WPrismMultiLabel(text, maxWidth));
    }

    public WLabel label(RichText text) {
        return label(text, 0);
    }

    @Override
    public WLabel label(String text, boolean title, double maxWidth) {
        if (maxWidth == 0) return w(new WPrismLabel(RichText.of(text).boldIf(title)));
        return w(new WPrismMultiLabel(RichText.of(text).boldIf(title), maxWidth));
    }

    @Override
    public WHorizontalSeparator horizontalSeparator(String text) {
        return w(new WPrismHorizontalSeparator(text));
    }

    @Override
    public WVerticalSeparator verticalSeparator() {
        return w(new WPrismVerticalSeparator());
    }

    public WPrismButton button(RichText text, GuiTexture texture) {
        return w(new WPrismButton(text, texture));
    }

    public WPrismButton button(RichText text) {
        return button(text, null);
    }

    @Override
    public WButton button(String text, GuiTexture texture) {
        return button(RichText.of(text), texture);
    }

    @Override
    public WButton button(GuiTexture texture) {
        return w(new WPrismButton(texture));
    }

    //? if >=1.21.10 {
    @Override
    protected WConfirmedButton confirmedButton(String text, String confirmText, GuiTexture texture) {
        return w(new WPrismConfirmedButton(text, confirmText, texture));
    }
    //?}

    @Override
    public WMinus minus() {
        return w(new WPrismMinus());
    }

    //? if >=1.21.10 {
    @Override
    public WConfirmedMinus confirmedMinus() {
        return w(new WPrismConfirmedMinus());
    }
    //?}

    @Override
    public WPlus plus() {
        return w(new WPrismPlus());
    }

    @Override
    public WCheckbox checkbox(boolean checked) {
        return w(new WPrismCheckbox(checked));
    }

    @Override
    public WSlider slider(double value, double min, double max) {
        return w(new WPrismSlider(value, min, max));
    }

    public WPrismColorSlider colorSlider(double value, double min, double max, Color[] colors) {
        return w(new WPrismColorSlider(value, min, max, colors));
    }

    public <T> WPrismColorGrid<T> colorGrid(String title, T[] values, Function<T, Color> colorOf, int columns, int maxVisibleRows) {
        return w(new WPrismColorGrid<>(title, values, colorOf, columns, maxVisibleRows));
    }

    public WTextBox textBox(String text, String placeholder, String title, double padding, CharFilter filter, Class<? extends WTextBox.Renderer> renderer) {
        return w(new WPrismTextBox(text, placeholder, title, padding, filter, renderer));
    }

    public WTextBox textBox(String text, String placeholder, String title, CharFilter filter, Class<? extends WTextBox.Renderer> renderer) {
        return textBox(text, placeholder, title, pad(), filter, renderer);
    }

    public WTextBox textBox(String text, CharFilter filter, double padding) {
        return textBox(text, null, "", padding, filter, null);
    }

    @Override
    public WTextBox textBox(String text, String placeholder, CharFilter filter, Class<? extends WTextBox.Renderer> renderer) {
        return textBox(text, placeholder, "", filter, renderer);
    }

    public <T> WDropdown<T> dropdown(String title, T[] values, T value) {
        return w(new WPrismDropdown<>(title, values, value));
    }

    @SuppressWarnings("unchecked")
    public <T extends Enum<?>> WDropdown<T> dropdown(String title, T value) {
        Class<?> klass = value.getDeclaringClass();
        T[] values = (T[]) klass.getEnumConstants();
        return dropdown(title, values, value);
    }

    @Override
    public <T> WDropdown<T> dropdown(T[] values, T value) {
        return dropdown(null, values, value);
    }

    @Override
    public WTriangle triangle() {
        return w(new WPrismTriangle());
    }

    @Override
    public WTooltip tooltip(String text) {
        return w(new WPrismTooltip(text));
    }

    @Override
    public WView view() {
        return w(new WPrismView());
    }

    @Override
    public WSection section(String title, boolean expanded, WWidget headerWidget) {
        return w(new WPrismSection(title, expanded, headerWidget));
    }

    public WTabView tabView(List<PrismTab> tabs, PrismTab initialTab) {
        return w(new WPrismTabView(tabs, initialTab));
    }

    public WTabView tabView(List<PrismTab> tabs) {
        return tabView(tabs, tabs.getFirst());
    }

    @Override
    public WAccount account(WidgetScreen screen, Account<?> account) {
        return w(new WPrismAccount(screen, account));
    }

    @Override
    public WWidget module(Module module) {
        return w(module(module, module.title));
    }

    //? if >=1.21.11
    @Override
    public WWidget module(Module module, String title) {
        return w(new WPrismModule(module, title));
    }

    @Override
    public WQuad quad(Color color) {
        return w(new WPrismQuad(color));
    }

    @Override
    public WTopBar topBar() {
        // Reuse the singleton TopBar to keep animations when switching screens
        if (topBar == null) topBar = w(new WPrismTopBar());
        return topBar;
    }

    @Override
    public WFavorite favorite(boolean checked) {
        return w(new WPrismFavorite(checked));
    }

    public WPrismKeybind prismKeybind(Keybind keybind) {
        return prismKeybind(keybind, Keybind.none());
    }

    public WPrismKeybind prismKeybind(Keybind keybind, Keybind defaultValue) {
        return prismKeybind(null, keybind, defaultValue);
    }

    public WPrismKeybind prismKeybind(String title, Keybind keybind, Keybind defaultValue) {
        return w(new WPrismKeybind(title, keybind, defaultValue));
    }

    public WGuiTexture texture(GuiTexture texture, double size) {
        return w(new WPrismGuiTexture(texture, size));
    }

    public WColorPicker colorPicker(Color color, GuiTexture overlayTexture) {
        return w(new WPrismColorPicker(color, overlayTexture));
    }

    public <T> WMultiSelect<T> multiSelect(String title, List<T> items) {
        return w(new WPrismMultiSelect<>(title, items));
    }

    public WSearch search() {
        return w(new WPrismSearch());
    }

    public <T> WTreeTable<T> treeTable(List<T> items, Function<T, Set<T>> dependencyResolver, Predicate<T> visibility, BiConsumer<WTable, T> factoryCreator) {
        return w(new WTreeTable<>(items, dependencyResolver, visibility, factoryCreator));
    }

    // Panel widgets

    public WPrismPanel panel() {
        return w(new WPrismPanel());
    }

    public WPrismSizedList sizedList(double minWidth) {
        return w(new WPrismSizedList(minWidth));
    }

    public WPrismBrand brand(String text) {
        return w(new WPrismBrand(text));
    }

    public WPrismSidebarItem sidebarItem(String title, GuiTexture icon, Runnable action) {
        return w(new WPrismSidebarItem(title, icon, action));
    }

    public WPrismRow prismRow(Module module, String title) {
        return w(new WPrismRow(module, title));
    }

    // Settings widgets

    public WPrismIntEdit prismIntEdit(IntSetting setting) {
        return w(new WPrismIntEdit(setting));
    }

    public WPrismDoubleEdit prismDoubleEdit(String title, String description, double value, double min, double max, int decimalPlaces, double sliderMin, double sliderMax, boolean noSlider) {
        return w(new WPrismDoubleEdit(title, description, null, value, min, max, decimalPlaces, sliderMin, sliderMax, noSlider));
    }

    public WPrismDoubleEdit prismDoubleEdit(DoubleSetting setting) {
        return w(new WPrismDoubleEdit(setting));
    }

    // Animations

    public Easing guiAnimationEasing() {
        return guiAnimation.get();
    }

    public int guiAnimationDuration() {
        return guiAnimationDuration.get();
    }

    // Colors

    /**
     * Passing {@link PrismColor#Accent} returns the theme's currently selected accent
     * color, any other value returns the corresponding color from the active flavor.
     */
    public Color getColor(PrismColor color) {
        return colorCache.get(color);
    }

    // Colors - Accent

    public Color accentColor() {
        return getColor(PrismColor.Accent);
    }

    // Colors - Main

    public Color greenColor() {
        return getColor(PrismColor.Green);
    }

    public Color yellowColor() {
        return getColor(PrismColor.Yellow);
    }

    public Color redColor() {
        return getColor(PrismColor.Red);
    }

    public Color blueColor() {
        return getColor(PrismColor.Blue);
    }

    // Colors - Overlay

    public Color overlay2Color() {
        return getColor(PrismColor.Overlay2);
    }

    public Color overlay1Color() {
        return getColor(PrismColor.Overlay1);
    }

    public Color overlay0Color() {
        return getColor(PrismColor.Overlay0);
    }

    // Colors - Surface

    public Color surface2Color() {
        return getColor(PrismColor.Surface2);
    }

    public Color surface1Color() {
        return getColor(PrismColor.Surface1);
    }

    public Color surface0Color() {
        return getColor(PrismColor.Surface0);
    }

    // Colors - Base

    public Color baseColor() {
        return getColor(PrismColor.Base);
    }

    public Color mantleColor() {
        return getColor(PrismColor.Mantle);
    }

    public Color crustColor() {
        return getColor(PrismColor.Crust);
    }

    // Colors - Text

    public Color textColor() {
        return getColor(PrismColor.Text);
    }

    public Color textSecondaryColor() {
        return getColor(PrismColor.Subtext0);
    }

    public Color textHighlightColor() {
        return getColor(PrismColor.Blue);
    }

    // Opacity

    public double windowOpacity() {
        return windowOpacity.get();
    }

    public double backgroundOpacity() {
        return backgroundOpacity.get();
    }

    public double panelOpacity() {
        return panelOpacity.get();
    }

    public double sidebarOpacity() {
        return sidebarOpacity.get();
    }

    public Color accentSecondaryColor() {
        return accentSecondary.get();
    }

    public boolean useSwitches() {
        return toggleStyle.get() == ToggleStyle.Switch;
    }

    // Shadow

    public Shadow windowShadow() {
        return windowShadow.get()
                ? Shadow.of(
                        shadowOffsetX.get(),
                        shadowOffsetY.get(),
                        shadowBlur.get(),
                        shadowSpread.get(),
                        shadowColor.get())
                : Shadow.none();
    }

    // Starscript

    @Override
    public Color starscriptTextColor() {
        return starscriptText.get();
    }

    @Override
    public Color starscriptBraceColor() {
        return starscriptBraces.get();
    }

    @Override
    public Color starscriptParenthesisColor() {
        return starscriptParenthesis.get();
    }

    @Override
    public Color starscriptDotColor() {
        return starscriptDots.get();
    }

    @Override
    public Color starscriptCommaColor() {
        return starscriptCommas.get();
    }

    @Override
    public Color starscriptOperatorColor() {
        return starscriptOperators.get();
    }

    @Override
    public Color starscriptStringColor() {
        return starscriptStrings.get();
    }

    @Override
    public Color starscriptNumberColor() {
        return starscriptNumbers.get();
    }

    @Override
    public Color starscriptKeywordColor() {
        return starscriptKeywords.get();
    }

    @Override
    public Color starscriptAccessedObjectColor() {
        return starscriptAccessedObjects.get();
    }

    // Colors - Other

    // Palette

    private int paletteSignature = 0;
    private boolean applyingPreset = false;

    /**
     * Copies a preset's colors into the color settings. Custom leaves everything untouched.
     */
    private void applyPreset(PrismPreset selected) {
        if (selected == null || selected.isCustom()) return;

        applyingPreset = true;

        accent.get().set(selected.accent());
        accentSecondary.get().set(selected.accentSecondary());
        backgroundBase.get().set(selected.background());
        textPrimary.get().set(selected.text());
        textMuted.get().set(selected.textSecondary());

        applyingPreset = false;
        rebuildPalette();
    }

    private int computeSignature() {
        int h = 17;
        h = h * 31 + accent.get().getPacked();
        h = h * 31 + accentSecondary.get().getPacked();
        h = h * 31 + backgroundBase.get().getPacked();
        h = h * 31 + textPrimary.get().getPacked();
        h = h * 31 + textMuted.get().getPacked();
        h = h * 31 + Double.hashCode(surfaceContrast.get());
        return h;
    }

    /**
     * Rebuilds every palette slot from the user's colors. The semantic slots (base, mantle,
     * surface0..2, overlay0..2, text...) are derived so that every widget of the theme follows
     * the chosen colors automatically. The named accents (red, green, ...) stay fixed.
     */
    private void rebuildPalette() {
        colorCache.clear();

        // Fixed named colors (used for status colors and linked color settings)
        for (PrismColor color : PrismColor.values()) {
            if (color == PrismColor.Accent) continue;
            colorCache.put(color, PrismFlavors.Mocha.getColor(color));
        }

        Color bg = backgroundBase.get();
        Color text = textPrimary.get();
        Color muted = textMuted.get();
        double k = surfaceContrast.get();

        colorCache.put(PrismColor.Accent, opaque(accent.get()));

        colorCache.put(PrismColor.Base, opaque(bg));
        colorCache.put(PrismColor.Mantle, opaque(ColorUtils.darker(bg, 0.82f)));
        colorCache.put(PrismColor.Crust, opaque(ColorUtils.darker(bg, 0.64f)));

        colorCache.put(PrismColor.Surface0, opaque(ColorUtils.lerp(bg, text, clamp01(0.08 * k))));
        colorCache.put(PrismColor.Surface1, opaque(ColorUtils.lerp(bg, text, clamp01(0.15 * k))));
        colorCache.put(PrismColor.Surface2, opaque(ColorUtils.lerp(bg, text, clamp01(0.23 * k))));

        colorCache.put(PrismColor.Overlay0, opaque(ColorUtils.lerp(bg, muted, clamp01(0.38 * k))));
        colorCache.put(PrismColor.Overlay1, opaque(ColorUtils.lerp(bg, muted, clamp01(0.52 * k))));
        colorCache.put(PrismColor.Overlay2, opaque(ColorUtils.lerp(bg, muted, clamp01(0.66 * k))));

        colorCache.put(PrismColor.Text, opaque(text));
        colorCache.put(PrismColor.Subtext1, opaque(ColorUtils.lerp(text, muted, 0.5)));
        colorCache.put(PrismColor.Subtext0, opaque(muted));

        paletteSignature = computeSignature();
        ColorLinkRegistry.applyAll();
    }

    private static Color opaque(Color color) {
        return new SettingColor(color.r, color.g, color.b, 255);
    }

    private static double clamp01(double v) {
        return Math.max(0, Math.min(1, v));
    }

    // Screens

    @Override
    public TabScreen modulesScreen() {
        return layout.get() == Layout.Panel
                ? new PrismPanelScreen(this)
                : new PrismWindowsModulesScreen(this);
    }

    @Override
    public boolean isModulesScreen(Screen screen) {
        return screen instanceof PrismPanelScreen || screen instanceof PrismWindowsModulesScreen;
    }

    @Override
    public WidgetScreen moduleScreen(Module module) {
        return new PrismModuleScreen(this, module);
    }

    // Text renderer

    @Override
    public TextRenderer textRenderer() {
        return Config.get().customFont.get() ? richTextRenderer() : VanillaTextRenderer.INSTANCE;
    }

    public RichTextRenderer richTextRenderer() {
        if (textRenderer == null) {
            try {
                setTextRenderer(new RichTextRenderer(Config.get().font.get()));
            } catch (Exception e) {
                PrismAddon.LOG.error("Failed to load TextRenderer: ", e);
            }
        }

        return textRenderer;
    }

    public void setTextRenderer(RichTextRenderer renderer) {
        if (textRenderer != null) textRenderer.destroy();
        this.textRenderer = renderer;
    }

    // Text

    public double textWidth(RichTextSegment segment) {
        return scale(Config.get().customFont.get()
                ? richTextRenderer().getWidth(segment, segment.getText().length())
                : textRenderer().getWidth(segment.getText()));
    }

    public double textWidth(RichText text) {
        return scale(Config.get().customFont.get()
                ? richTextRenderer().getWidth(text)
                : textRenderer().getWidth(text.getPlainText()));
    }

    @Override
    public double textWidth(String text, int length, boolean title) {
        return scale(Config.get().customFont.get()
                ? richTextRenderer().getWidth(RichText.of(text).boldIf(title), length)
                : textRenderer().getWidth(text, length, title));
    }

    @Override
    public double textWidth(String text) {
        return textWidth(RichText.of(text));
    }

    public double textHeight(RichText text) {
        return scale(Config.get().customFont.get()
                ? richTextRenderer().getHeight(text)
                : textRenderer().getHeight());
    }

    @Override
    public double textHeight(boolean title) {
        return scale(textRenderer().getHeight(title));
    }

    @Override
    public double textHeight() {
        return textHeight(false);
    }

    // Other

    @Override
    public void beforeRender() {
        super.beforeRender();

        // Colors can be edited at any time through the settings UI, so we watch for changes here
        if (!applyingPreset && computeSignature() != paletteSignature) rebuildPalette();

        PrismRenderer.get().setTheme(this);
    }

    @Override
    public double scale(double value) {
        double scaled = value * scale.get();

        if (Util.getPlatform() == Util.OS.OSX) {
            scaled /= (double) mc.getWindow().getWidth() / mc.getWindow().getWidth();
        }

        return scaled;
    }

    @Override
    public boolean categoryIcons() {
        return categoryIcons.get();
    }

    //? if >=26.2
    @Override
    public boolean modulesHelpText() {
        return modulesHelpText.get();
    }

    @Override
    public boolean hideHUD() {
        return hideHUD.get();
    }

    public void invalidateScreen() {
        if (mc.gui.screen() instanceof WidgetScreen s) s.invalidate();
    }

    public void reloadScreen() {
        if (mc.gui.screen() instanceof WidgetScreen s) s.reload();
    }

    public class ThreeStateColor {
        private final Supplier<Color> normal, hovered, pressed;

        public ThreeStateColor(Supplier<Color> normal, Supplier<Color> hovered, Supplier<Color> pressed) {
            this.normal = normal;
            this.hovered = hovered;
            this.pressed = pressed;
        }

        public Color get() {
            return normal.get();
        }

        public Color get(float alpha) {
            return withAlpha(normal.get(), alpha);
        }

        public Color get(boolean pressed, boolean hovered, boolean bypassDisableHoverColor) {
            if (pressed) return this.pressed.get();
            return (hovered && (bypassDisableHoverColor || !disableHoverColor)) ? this.hovered.get() : this.normal.get();
        }

        public Color get(boolean pressed, boolean hovered, boolean bypassDisableHoverColor, float alpha) {
            Color color = get(pressed, hovered, bypassDisableHoverColor);
            return withAlpha(color, alpha);
        }

        public Color get(boolean pressed, boolean hovered) {
            return get(pressed, hovered, false);
        }

        public Color get(boolean pressed, boolean hovered, float alpha) {
            return get(pressed, hovered, false, alpha);
        }

        public Color get(boolean hovered) {
            return get(false, hovered, false);
        }

        public Color get(boolean hovered, float alpha) {
            return get(false, hovered, false, alpha);
        }

        private Color withAlpha(Color color, float alpha) {
            Color result = color.copy().a((int) (255 * alpha));
            result.validate();
            return result;
        }
    }
}
