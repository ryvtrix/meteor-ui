package dev.prism.ui.mixin.meteorclient;

import meteordevelopment.meteorclient.gui.widgets.WWidget;
import meteordevelopment.meteorclient.gui.widgets.input.WSlider;
import org.spongepowered.asm.mixin.Mixin;
//? if <=1.21.10 {
/*import dev.prism.ui.gui.widgets.IWidgetBackport;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
*///? }

//? if >=1.21.10 && <=1.21.11
//import net.minecraft.client.input.MouseButtonEvent;

@Mixin(value = WSlider.class, remap = false)
public abstract class WSliderMixin extends WWidget {
    //? if <=1.21.10 {

    /*@Inject(
        method = "onMouseClicked",
        at = @At(
            value = "FIELD",
            target = "Lmeteordevelopment/meteorclient/gui/widgets/input/WSlider;dragging:Z",
            opcode = Opcodes.PUTFIELD,
            shift = At.Shift.AFTER
        )
    )
    private void prism$onMouseClicked(MouseButtonEvent click, boolean doubled, CallbackInfoReturnable<Boolean> cir) {
        ((IWidgetBackport)this).prism$setFocused(true);
    }

    @Inject(
        method = "onMouseReleased",
        at = @At(
            value = "FIELD",
            target = "Lmeteordevelopment/meteorclient/gui/widgets/input/WSlider;dragging:Z",
            opcode = Opcodes.PUTFIELD,
            shift = At.Shift.AFTER
        )
    )
    private void prism$onMouseReleased(MouseButtonEvent click, CallbackInfoReturnable<Boolean> cir) {
        ((IWidgetBackport)this).prism$setFocused(false);
    }

    *///? }
}
