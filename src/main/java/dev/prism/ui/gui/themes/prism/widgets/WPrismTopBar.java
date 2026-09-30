package dev.prism.ui.gui.themes.prism.widgets;

import dev.prism.ui.api.icons.PrismIcons;
import dev.prism.ui.gui.themes.prism.PrismWidget;
import dev.prism.ui.gui.widgets.tabs.TabBridge;
import dev.prism.ui.gui.widgets.tabs.WTabView;
import meteordevelopment.meteorclient.gui.renderer.GuiRenderer;
import meteordevelopment.meteorclient.gui.tabs.Tab;
import meteordevelopment.meteorclient.gui.tabs.Tabs;
import meteordevelopment.meteorclient.gui.widgets.WTopBar;
import meteordevelopment.meteorclient.utils.render.color.Color;

import static meteordevelopment.meteorclient.MeteorClient.mc;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPos;

/**
 * Singleton-like top bar widget. Kept persistent across screen switches
 * to preserve tab indicator cool transition animations.
 */
public class WPrismTopBar extends WTopBar implements PrismWidget {
    private boolean invalid = true;
    private WTabView tabView;

    private TabBridge tabBridge;
    private Tab pendingTab;

    @Override
    public void init() {
        if (invalid) {
            tabBridge = new TabBridge(
                    Tabs.get(),
                    theme().tabIcons.get()
                            ? tab -> PrismIcons.getTabIcon(tab.getClass())
                            : null
            );
            clear();

            tabView = add(theme().tabView(
                    tabBridge.prismTabs(),
                    tabBridge.prismTab(pendingTab)
            )).widget();

            tabView.onTabChange = tab -> open(tabBridge.meteorTab(tab));

            invalid = false;
        }

        pendingTab = null;
    }

    @Override
    protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
        roundedRect().bounds(this)
                     .radius(radius())
                     .color(theme().baseColor())
                     .shadow(shadow())
                     .render();
    }

    public void onTabScreenOpen(Tab tab) {
        invalid = tabView == null || !tabView.isTabActive(tabBridge.prismTab(tab));
        pendingTab = tab;
    }

    private void open(Tab tab) {
        if (tab == null) return;

        double mouseX = mc.mouseHandler.xpos();
        double mouseY = mc.mouseHandler.ypos();

        tab.openScreen(theme());

        glfwSetCursorPos(
                //? if <=1.21.4 {
                /*mc.getWindow().getWindow(),
                *///? } else {
                mc.getWindow().handle(),
                //? }
                mouseX,
                mouseY
        );
    }

    @Override
    protected Color getButtonColor(boolean pressed, boolean hovered) {
        return theme().backgroundColor.get(pressed, hovered);
    }

    @Override
    protected Color getNameColor() {
        return theme().textColor();
    }
}
