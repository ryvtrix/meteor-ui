package dev.prism.ui.gui.themes.prism.flavors.flavor;

import dev.prism.ui.gui.themes.prism.colors.PrismColor;
import dev.prism.ui.gui.themes.prism.flavors.FlavorColorProvider;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;

public enum Frappe implements FlavorColorProvider {
    Rosewater(new SettingColor(242, 213, 207), PrismColor.Rosewater),
    Flamingo(new SettingColor(238, 190, 190), PrismColor.Flamingo),
    Pink(new SettingColor(244, 184, 228), PrismColor.Pink),
    Mauve(new SettingColor(202, 158, 230), PrismColor.Mauve),
    Red(new SettingColor(231, 130, 132), PrismColor.Red),
    Maroon(new SettingColor(234, 153, 156), PrismColor.Maroon),
    Peach(new SettingColor(239, 159, 118), PrismColor.Peach),
    Yellow(new SettingColor(229, 200, 144), PrismColor.Yellow),
    Green(new SettingColor(166, 209, 137), PrismColor.Green),
    Teal(new SettingColor(129, 200, 190), PrismColor.Teal),
    Sky(new SettingColor(153, 209, 219), PrismColor.Sky),
    Sapphire(new SettingColor(133, 193, 220), PrismColor.Sapphire),
    Blue(new SettingColor(140, 170, 238), PrismColor.Blue),
    Lavender(new SettingColor(186, 187, 241), PrismColor.Lavender),
    Text(new SettingColor(198, 208, 245), PrismColor.Text),
    Subtext1(new SettingColor(181, 191, 226), PrismColor.Subtext1),
    Subtext0(new SettingColor(165, 173, 206), PrismColor.Subtext0),
    Overlay2(new SettingColor(148, 156, 187), PrismColor.Overlay2),
    Overlay1(new SettingColor(131, 139, 167), PrismColor.Overlay1),
    Overlay0(new SettingColor(115, 121, 148), PrismColor.Overlay0),
    Surface2(new SettingColor(98, 104, 128), PrismColor.Surface2),
    Surface1(new SettingColor(81, 87, 109), PrismColor.Surface1),
    Surface0(new SettingColor(65, 69, 89), PrismColor.Surface0),
    Base(new SettingColor(48, 52, 70), PrismColor.Base),
    Mantle(new SettingColor(41, 44, 60), PrismColor.Mantle),
    Crust(new SettingColor(35, 38, 52), PrismColor.Crust);

    private final SettingColor color;
    private final PrismColor type;

    Frappe(SettingColor color, PrismColor type) {
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
