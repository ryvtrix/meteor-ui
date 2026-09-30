package dev.prism.ui.gui.themes.prism.flavors.flavor;

import dev.prism.ui.gui.themes.prism.colors.PrismColor;
import dev.prism.ui.gui.themes.prism.flavors.FlavorColorProvider;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;

public enum Latte implements FlavorColorProvider {
    Rosewater(new SettingColor(220, 138, 120), PrismColor.Rosewater),
    Flamingo(new SettingColor(221, 120, 120), PrismColor.Flamingo),
    Pink(new SettingColor(234, 118, 203), PrismColor.Pink),
    Mauve(new SettingColor(136, 57, 239), PrismColor.Mauve),
    Red(new SettingColor(210, 15, 57), PrismColor.Red),
    Maroon(new SettingColor(230, 69, 83), PrismColor.Maroon),
    Peach(new SettingColor(254, 100, 11), PrismColor.Peach),
    Yellow(new SettingColor(223, 142, 29), PrismColor.Yellow),
    Green(new SettingColor(64, 160, 43), PrismColor.Green),
    Teal(new SettingColor(23, 146, 153), PrismColor.Teal),
    Sky(new SettingColor(4, 165, 229), PrismColor.Sky),
    Sapphire(new SettingColor(32, 159, 181), PrismColor.Sapphire),
    Blue(new SettingColor(30, 102, 245), PrismColor.Blue),
    Lavender(new SettingColor(114, 135, 253), PrismColor.Lavender),
    Text(new SettingColor(76, 79, 105), PrismColor.Text),
    Subtext1(new SettingColor(92, 95, 119), PrismColor.Subtext1),
    Subtext0(new SettingColor(108, 111, 133), PrismColor.Subtext0),
    Overlay2(new SettingColor(124, 127, 147), PrismColor.Overlay2),
    Overlay1(new SettingColor(140, 143, 161), PrismColor.Overlay1),
    Overlay0(new SettingColor(156, 160, 176), PrismColor.Overlay0),
    Surface2(new SettingColor(172, 176, 190), PrismColor.Surface2),
    Surface1(new SettingColor(188, 192, 204), PrismColor.Surface1),
    Surface0(new SettingColor(204, 208, 218), PrismColor.Surface0),
    Base(new SettingColor(239, 241, 245), PrismColor.Base),
    Mantle(new SettingColor(230, 233, 239), PrismColor.Mantle),
    Crust(new SettingColor(220, 224, 232), PrismColor.Crust);

    private final SettingColor color;
    private final PrismColor type;

    Latte(SettingColor color, PrismColor type) {
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
