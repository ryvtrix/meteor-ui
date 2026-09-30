package dev.prism.ui.mixin.meteorclient;

import dev.prism.ui.PrismAddon;
import dev.prism.ui.renderer.text.RichTextRenderer;
import dev.prism.ui.gui.themes.prism.PrismGuiTheme;
import meteordevelopment.meteorclient.gui.GuiThemes;
import meteordevelopment.meteorclient.renderer.Fonts;
import meteordevelopment.meteorclient.renderer.text.FontFace;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Fonts.class, remap = false)
public abstract class FontsMixin {

    @Inject(method = "load", at = @At("HEAD"))
    private static void prism$load(FontFace fontFace, CallbackInfo ci) {
        if (!(GuiThemes.get() instanceof PrismGuiTheme theme)) return;

        if (theme.textRenderer() instanceof RichTextRenderer currentRenderer)
             if (currentRenderer.fontFace.equals(fontFace)) return;

        try {
            theme.setTextRenderer(new RichTextRenderer(fontFace));
        } catch (Exception e) {
            PrismAddon.LOG.error("Failed to load font: {}", fontFace, e);
        }
    }
}
