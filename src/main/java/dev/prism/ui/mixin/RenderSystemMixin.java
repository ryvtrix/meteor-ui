package dev.prism.ui.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import org.spongepowered.asm.mixin.Mixin;

//? if <=26.1.2 {
/*import dev.prism.ui.renderer.PrismRenderer;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
*///? }

@Mixin(RenderSystem.class)
public abstract class RenderSystemMixin {
    //? if <=26.1.2 {
    /*@Inject(method = "flipFrame", at = @At("TAIL"))
    private static void prism$flipFrame(CallbackInfo info) {
        PrismRenderer.get().flipFrame();
    }
    *///? }
}