package dev.prism.ui.gui.widgets.tabs;

import meteordevelopment.meteorclient.gui.renderer.packer.GuiTexture;
import meteordevelopment.meteorclient.gui.widgets.containers.WContainer;

import java.util.function.Consumer;

public class PrismTab {
    private final String name;
    private final GuiTexture icon;
    private final Consumer<WContainer> contentBuilder;

    public PrismTab(String name, Consumer<WContainer> contentBuilder) {
        this(name, null, contentBuilder);
    }

    public PrismTab(String name, GuiTexture icon, Consumer<WContainer> contentBuilder) {
        this.name = name;
        this.icon = icon;
        this.contentBuilder = contentBuilder;
    }

    public String name() {
        return name;
    }

    public GuiTexture icon() {
        return icon;
    }

    public void build(WContainer content) {
        contentBuilder.accept(content);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PrismTab other) {
            return name.equalsIgnoreCase(other.name);
        }
        return false;
    }
}
