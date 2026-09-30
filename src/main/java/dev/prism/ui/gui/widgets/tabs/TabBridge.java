package dev.prism.ui.gui.widgets.tabs;

import meteordevelopment.meteorclient.gui.renderer.packer.GuiTexture;
import meteordevelopment.meteorclient.gui.tabs.Tab;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class TabBridge {
    private final List<TabPair> tabPairs = new ArrayList<>();

    public TabBridge(List<Tab> meteorTabs, Function<Tab, GuiTexture> iconResolver) {
        for (Tab meteorTab : meteorTabs) {
            GuiTexture icon = iconResolver != null ? iconResolver.apply(meteorTab) : null;
            PrismTab prismTab = new PrismTab(meteorTab.name, icon, ignored -> {});

            tabPairs.add(new TabPair(meteorTab, prismTab));
        }
    }

    public List<PrismTab> prismTabs() {
        return tabPairs.stream()
                       .map(TabPair::prism)
                       .toList();
    }

    public PrismTab prismTab(Tab meteorTab) {
        for (TabPair pair : tabPairs) {
            if (pair.meteor() == meteorTab) return pair.prism();
        }
        return null;
    }

    public Tab meteorTab(PrismTab prismTab) {
        for (TabPair pair : tabPairs) {
            if (pair.prism() == prismTab) return pair.meteor();
        }
        return null;
    }

    public record TabPair(Tab meteor, PrismTab prism) { }
}