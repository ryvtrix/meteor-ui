package dev.prism.ui.mixin.meteorclient;

import dev.prism.ui.gui.themes.prism.colors.ColorLinkRegistry;
import dev.prism.ui.utils.SettingWatcher;
import meteordevelopment.meteorclient.settings.ColorSetting;
import meteordevelopment.meteorclient.settings.Setting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Setting.class)
public class SettingMixin {
    @Inject(method = "get", at = @At("HEAD"))
    private void prism$get(CallbackInfoReturnable<Object> cir) {
        SettingWatcher.touch((Setting<?>) (Object) this);
    }

    @Inject(method = "reset", at = @At("TAIL"))
    private void prism$reset(CallbackInfo ci) {
        if ((Object) this instanceof ColorSetting s)
            ColorLinkRegistry.unlink(s);
    }
}