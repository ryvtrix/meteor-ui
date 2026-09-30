package dev.prism.ui.utils;

import dev.prism.ui.gui.themes.prism.icons.PrismBuiltinIcons;
import dev.prism.ui.gui.themes.prism.widgets.pressable.WPrismButton;
import dev.prism.ui.gui.widgets.IWidgetBackport;
import meteordevelopment.meteorclient.gui.utils.Cell;
import meteordevelopment.meteorclient.gui.widgets.WWidget;
import meteordevelopment.meteorclient.gui.widgets.containers.WContainer;
import meteordevelopment.meteorclient.settings.Setting;

import java.util.function.BooleanSupplier;

public class WidgetUtils {

    public static Cell<WPrismButton> reset(WContainer c, Setting<?> setting, Runnable action) {
        return reset(c, setting, action, null);
    }

    public static Cell<WPrismButton> reset(WContainer c, Setting<?> setting, Runnable action, BooleanSupplier visibilityCondition) {
        WPrismButton button = (WPrismButton) c.getTheme().button(PrismBuiltinIcons.RESET.texture());

        button.setVisibilityCondition(visibilityCondition);
        button.tooltip = "Reset";
        button.action = () -> {
            if (setting != null) setting.reset();
            if (action != null) action.run();
        };

        return c.add(button);
    }

    public static void enableInstantTooltips(WWidget widget) {
        ((IWidgetBackport) widget).prism$setInstantTooltips(true);
    }
}
