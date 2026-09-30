package dev.prism.ui;

import dev.prism.ui.api.render.shape.RoundedRect;
import dev.prism.ui.gui.themes.prism.PrismGuiTheme;
import com.mojang.logging.LogUtils;
import dev.prism.ui.renderer.PrismRenderer;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.gui.GuiThemes;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;

public class PrismAddon extends MeteorAddon {
    public static final Logger LOG = LogUtils.getLogger();

    public static final String MOD_ID = "prism-ui";

    @Override
    public void onInitialize() {
        LOG.info("Initializing Prism Addon");

        GuiThemes.add(new PrismGuiTheme());

        RoundedRect.get().registerRenderer(PrismRenderer.get());
    }

    public static Identifier identifier(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    @Override
    public String getPackage() {
        return "dev.prism.ui";
    }
}
