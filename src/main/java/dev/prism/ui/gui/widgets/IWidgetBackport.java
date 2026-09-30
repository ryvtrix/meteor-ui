package dev.prism.ui.gui.widgets;

import meteordevelopment.meteorclient.gui.widgets.WWidget;
import meteordevelopment.meteorclient.gui.widgets.containers.WView;

@SuppressWarnings("unused")
public interface IWidgetBackport {
    boolean prism$isFocused();
    boolean prism$isSelfFocused();

    void prism$setFocused(boolean focused);

    boolean prism$hasInstantTooltips();
    void prism$setInstantTooltips(boolean instant);

    WView prism$getView();
    boolean prism$isWidgetInView(WWidget widget);
}
