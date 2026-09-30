package dev.prism.ui.gui.themes.prism.widgets.container;

import dev.prism.ui.gui.themes.prism.PrismWidget;
import meteordevelopment.meteorclient.gui.renderer.GuiRenderer;
import meteordevelopment.meteorclient.gui.widgets.containers.WView;
import meteordevelopment.meteorclient.utils.Utils;

//? if <=1.21.10
//import dev.prism.ui.gui.widgets.IWidgetBackport;

public class WPrismView extends WView implements PrismWidget {

    @Override
    public void init() {
        maxHeight = Utils.getWindowHeight() - theme.scale(200);
    }

    @Override
    protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
        if (canScroll && hasScrollBar) {
            roundedRect().pos(handleX(), handleY())
                         .size(handleWidth(), handleHeight())
                         .radius(smallRadius())
                         .color(theme().scrollbarColor.get(
                                 //? if >=1.21.11 {
                                 focused,
                                 //? } else
                                 //((IWidgetBackport)this).prism$isSelfFocused(),
                                 handleMouseOver
                         ))
                         .render();
        }
    }
}
