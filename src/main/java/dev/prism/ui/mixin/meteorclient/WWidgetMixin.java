package dev.prism.ui.mixin.meteorclient;

import dev.prism.ui.gui.themes.prism.PrismGuiTheme;
import dev.prism.ui.gui.widgets.IWidgetBackport;
import meteordevelopment.meteorclient.gui.GuiTheme;
import meteordevelopment.meteorclient.gui.GuiThemes;
import meteordevelopment.meteorclient.gui.widgets.WWidget;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

//? if <=1.21.10 {
/*import meteordevelopment.meteorclient.gui.renderer.GuiRenderer;
import meteordevelopment.meteorclient.gui.widgets.containers.WView;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
*///? }

@Mixin(value = WWidget.class, remap = false)
public abstract class WWidgetMixin implements IWidgetBackport {

    @Redirect(method = "calculateSize", at = @At(value = "INVOKE", target = "Lmeteordevelopment/meteorclient/gui/GuiTheme;scale(D)D"))
    private double prism$scale(GuiTheme theme, double value) {
        // Fixes https://github.com/MeteorDevelopment/meteor-client/issues/5632
        // But requires already scaled values
        if (GuiThemes.get() instanceof PrismGuiTheme) return value;
        return theme.scale(value);
    }

    //? if <=1.21.10 {

    /*@Shadow public WWidget parent;
    @Shadow public String tooltip;

    @Shadow protected double mouseOverTimer;
    @Shadow public boolean visible;

    @Shadow public abstract boolean isOver(double x, double y);
    @Shadow protected abstract void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta);

    @Unique
    public boolean focused;

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void prism$render(GuiRenderer renderer, double mouseX, double mouseY, double delta, CallbackInfoReturnable<Boolean> cir) {
        if (!visible) {
            cir.setReturnValue(true);
            return;
        }

        if (isOver(mouseX, mouseY)) {
            mouseOverTimer += delta;

            if ((prism$hasInstantTooltips() || mouseOverTimer >= 1) && tooltip != null) {
                WView view = prism$getView();
                if (view == null || view.mouseOver) renderer.tooltip(tooltip);
            }
        } else {
            mouseOverTimer = 0;
        }

        onRender(renderer, mouseX, mouseY, delta);
        cir.setReturnValue(false);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public WView prism$getView() {
        return (Object) this instanceof WView ? (WView) (Object) this : (parent != null ? ((IWidgetBackport) parent).prism$getView() : null);
    }

    @Override
    public boolean prism$isFocused() {
        return focused;
    }

    @Override
    public boolean prism$isSelfFocused() {
        return focused;
    }

    @Override
    public void prism$setFocused(boolean focused) {
        if (this.focused != focused) this.focused = focused;
    }

    *///? }

    //? if >=1.21.10 {
    
    @Shadow
    protected boolean instantTooltips;

    @Override
    public boolean prism$hasInstantTooltips() {
        return instantTooltips;
    }

    @Override
    public void prism$setInstantTooltips(boolean instant) {
        instantTooltips = instant;
    }

    //? } else {

    /*@Unique
    public boolean prism$instantTooltips;

    @Override
    public boolean prism$hasInstantTooltips() {
        return prism$instantTooltips;
    }

    @Override
    public void prism$setInstantTooltips(boolean instant) {
        this.prism$instantTooltips = instant;
    }

    *///? }
}
