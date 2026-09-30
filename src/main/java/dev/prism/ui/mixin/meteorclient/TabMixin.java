package dev.prism.ui.mixin.meteorclient;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.prism.ui.gui.themes.prism.PrismGuiTheme;
import dev.prism.ui.gui.themes.prism.widgets.WPrismTopBar;
import meteordevelopment.meteorclient.gui.GuiThemes;
import meteordevelopment.meteorclient.gui.tabs.Tab;
import meteordevelopment.meteorclient.gui.tabs.TabScreen;
import meteordevelopment.meteorclient.gui.utils.Cell;
import meteordevelopment.meteorclient.gui.widgets.WWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = Tab.class, remap = false)
public abstract class TabMixin {

    @WrapOperation(
        method = "openScreen",
        at = @At(
            value = "INVOKE",
            target = "Lmeteordevelopment/meteorclient/gui/tabs/TabScreen;addDirect(Lmeteordevelopment/meteorclient/gui/widgets/WWidget;)Lmeteordevelopment/meteorclient/gui/utils/Cell;"
        )
    )
    private Cell<?> prism$addTopBarMargin(TabScreen screen, WWidget widget, Operation<Cell<?>> original) {
        if (widget instanceof WPrismTopBar topBar) {
            topBar.onTabScreenOpen(screen.tab);
        }

        Cell<?> cell = original.call(screen, widget);

        // Add a small margin at the top, but only when the theme is active
        if (GuiThemes.get() instanceof PrismGuiTheme)
            cell.marginTop(10);

        return cell;
    }
}
