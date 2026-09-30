package dev.prism.ui.gui.themes.prism.flavors;

import dev.prism.ui.PrismAddon;
import dev.prism.ui.gui.themes.prism.colors.PrismColor;
import dev.prism.ui.gui.themes.prism.flavors.flavor.Frappe;
import dev.prism.ui.gui.themes.prism.flavors.flavor.Latte;
import dev.prism.ui.gui.themes.prism.flavors.flavor.Macchiato;
import dev.prism.ui.gui.themes.prism.flavors.flavor.Mocha;
import dev.prism.ui.gui.themes.prism.flavors.flavor.*;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;

public enum PrismFlavors {
    Latte(Latte.class),
    Frappe(Frappe.class),
    Macchiato(Macchiato.class),
    Mocha(Mocha.class);


    private final Class<? extends FlavorColorProvider> colorProviderClass;

    PrismFlavors(Class<? extends FlavorColorProvider> providerClass) {
        this.colorProviderClass = providerClass;
    }

    public SettingColor getColor(PrismColor color) {
        try {
            for (FlavorColorProvider provider : colorProviderClass.getEnumConstants()) {
                if (provider.getType() == color) {
                    return provider.getColor();
                }
            }
            return new SettingColor(255, 255, 255); // fallback
        } catch (Exception e) {
            PrismAddon.LOG.error(e.getMessage());
            return new SettingColor(255, 255, 255);
        }
    }
}
