package dev.prism.ui.gui.widgets.tabs;

import meteordevelopment.meteorclient.gui.widgets.containers.WContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TabBuilder {
    private final List<PrismTab> tabs = new ArrayList<>();

    public TabBuilder tab(String name, Consumer<WContainer> contentBuilder) {
        tabs.add(new PrismTab(name, contentBuilder));
        return this;
    }

    public List<PrismTab> build() {
        return List.copyOf(tabs);
    }
}
