package dev.prism.ui.gui.screens;

import dev.prism.ui.gui.themes.prism.PrismGuiTheme;
import meteordevelopment.meteorclient.gui.GuiTheme;
import meteordevelopment.meteorclient.gui.WidgetScreen;
import meteordevelopment.meteorclient.utils.Utils;

public class PrismSearchScreen extends WidgetScreen {
    private final PrismGuiTheme theme;

    public PrismSearchScreen(GuiTheme theme) {
        super(theme, "Search");
        this.theme = (PrismGuiTheme) theme;
    }

    @Override
    public void initWidgets() {
        double margin = Utils.getWindowHeight() / 8.0;
        add(theme.search()).marginTop(margin).top().centerX();
    }
}
