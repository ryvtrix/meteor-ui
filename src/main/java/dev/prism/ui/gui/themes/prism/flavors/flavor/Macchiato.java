package dev.prism.ui.gui.themes.prism.flavors.flavor;

import dev.prism.ui.gui.themes.prism.colors.PrismColor;
import dev.prism.ui.gui.themes.prism.flavors.FlavorColorProvider;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;

public enum Macchiato implements FlavorColorProvider {
    Rosewater(new SettingColor(244, 219, 214), PrismColor.Rosewater),
    Flamingo(new SettingColor(240, 198, 198), PrismColor.Flamingo),
    Pink(new SettingColor(245, 189, 230), PrismColor.Pink),
    Mauve(new SettingColor(198, 160, 246), PrismColor.Mauve),
    Red(new SettingColor(237, 135, 150), PrismColor.Red),
    Maroon(new SettingColor(238, 153, 160), PrismColor.Maroon),
    Peach(new SettingColor(245, 169, 127), PrismColor.Peach),
    Yellow(new SettingColor(238, 212, 159), PrismColor.Yellow),
    Green(new SettingColor(166, 218, 149), PrismColor.Green),
    Teal(new SettingColor(139, 213, 202), PrismColor.Teal),
    Sky(new SettingColor(145, 215, 227), PrismColor.Sky),
    Sapphire(new SettingColor(125, 196, 228), PrismColor.Sapphire),
    Blue(new SettingColor(138, 173, 244), PrismColor.Blue),
    Lavender(new SettingColor(183, 189, 248), PrismColor.Lavender),
    Text(new SettingColor(202, 211, 245), PrismColor.Text),
    Subtext1(new SettingColor(184, 192, 224), PrismColor.Subtext1),
    Subtext0(new SettingColor(165, 173, 203), PrismColor.Subtext0),
    Overlay2(new SettingColor(147, 154, 183), PrismColor.Overlay2),
    Overlay1(new SettingColor(128, 135, 162), PrismColor.Overlay1),
    Overlay0(new SettingColor(110, 115, 141), PrismColor.Overlay0),
    Surface2(new SettingColor(91, 96, 120), PrismColor.Surface2),
    Surface1(new SettingColor(73, 77, 100), PrismColor.Surface1),
    Surface0(new SettingColor(54, 58, 79), PrismColor.Surface0),
    Base(new SettingColor(36, 39, 58), PrismColor.Base),
    Mantle(new SettingColor(30, 32, 48), PrismColor.Mantle),
    Crust(new SettingColor(24, 25, 38), PrismColor.Crust);

    private final SettingColor color;
    private final PrismColor type;

    Macchiato(SettingColor color, PrismColor type) {
        this.color = color;
        this.type = type;
    }

    @Override
    public SettingColor getColor() {
        return color;
    }

    @Override
    public PrismColor getType() {
        return type;
    }
}
