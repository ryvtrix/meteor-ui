package dev.prism.ui.gui.themes.prism.flavors;

import dev.prism.ui.gui.themes.prism.colors.PrismColor;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;

public interface FlavorColorProvider {
    PrismColor getType();
    SettingColor getColor();
}
