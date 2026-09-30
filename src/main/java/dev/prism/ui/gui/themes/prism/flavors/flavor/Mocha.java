package dev.prism.ui.gui.themes.prism.flavors.flavor;

import dev.prism.ui.gui.themes.prism.colors.PrismColor;
import dev.prism.ui.gui.themes.prism.flavors.FlavorColorProvider;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;

public enum Mocha implements FlavorColorProvider {
    Rosewater(new SettingColor(245, 224, 220), PrismColor.Rosewater),
    Flamingo(new SettingColor(242, 205, 205), PrismColor.Flamingo),
    Pink(new SettingColor(245, 194, 231), PrismColor.Pink),
    Mauve(new SettingColor(203, 166, 247), PrismColor.Mauve),
    Red(new SettingColor(243, 139, 168), PrismColor.Red),
    Maroon(new SettingColor(235, 160, 172), PrismColor.Maroon),
    Peach(new SettingColor(250, 179, 135), PrismColor.Peach),
    Yellow(new SettingColor(249, 226, 175), PrismColor.Yellow),
    Green(new SettingColor(166, 227, 161), PrismColor.Green),
    Teal(new SettingColor(148, 226, 213), PrismColor.Teal),
    Sky(new SettingColor(137, 220, 235), PrismColor.Sky),
    Sapphire(new SettingColor(116, 199, 236), PrismColor.Sapphire),
    Blue(new SettingColor(137, 180, 250), PrismColor.Blue),
    Lavender(new SettingColor(180, 190, 254), PrismColor.Lavender),
    Text(new SettingColor(205, 214, 244), PrismColor.Text),
    Subtext1(new SettingColor(186, 194, 222), PrismColor.Subtext1),
    Subtext0(new SettingColor(166, 173, 200), PrismColor.Subtext0),
    Overlay2(new SettingColor(147, 153, 178), PrismColor.Overlay2),
    Overlay1(new SettingColor(127, 132, 156), PrismColor.Overlay1),
    Overlay0(new SettingColor(108, 112, 134), PrismColor.Overlay0),
    Surface2(new SettingColor(88, 91, 112), PrismColor.Surface2),
    Surface1(new SettingColor(69, 71, 90), PrismColor.Surface1),
    Surface0(new SettingColor(49, 50, 68), PrismColor.Surface0),
    Base(new SettingColor(30, 30, 46), PrismColor.Base),
    Mantle(new SettingColor(24, 24, 37), PrismColor.Mantle),
    Crust(new SettingColor(17, 17, 27), PrismColor.Crust);

    private final SettingColor color;
    private final PrismColor type;

    Mocha(SettingColor color, PrismColor type) {
        this.color = color;
        this.type = type;
    }

    public SettingColor getColor() {
        return color;
    }

    public PrismColor getType() {
        return type;
    }
}
