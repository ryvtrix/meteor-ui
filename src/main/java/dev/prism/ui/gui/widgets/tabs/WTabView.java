package dev.prism.ui.gui.widgets.tabs;

import meteordevelopment.meteorclient.gui.widgets.containers.WContainer;
import meteordevelopment.meteorclient.gui.widgets.containers.WHorizontalList;
import meteordevelopment.meteorclient.gui.widgets.containers.WVerticalList;
import meteordevelopment.meteorclient.gui.widgets.pressable.WPressable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class WTabView extends WVerticalList {
    protected final List<PrismTab> tabs;
    protected PrismTab activeTab;

    protected WHeader header;
    protected WContainer content;
    private boolean rebuilding;

    public Consumer<PrismTab> onTabChange;

    public WTabView(List<PrismTab> tabs, PrismTab initialTab) {
        this.tabs = new ArrayList<>(tabs);
        this.activeTab = initialTab;
        this.spacing = 0;
    }

    @Override
    public void init() {
        header = createHeader();
        add(header).centerX();

        content = createContent();
        add(content).expandX();
        rebuildContent();
    }

    protected WHeader createHeader() {
        return new WHeader();
    }

    protected WContainer createContent() {
        return null;
    }

    protected WTabButton createTabButton(PrismTab tab) {
        return new WTabButton(tab);
    }

    public boolean isTabActive(PrismTab tab) {
        return activeTab == tab;
    }

    private void rebuildContent() {
        if (rebuilding) return;

        rebuilding = true;

        content.clear();
        activeTab.build(content);

        rebuilding = false;
    }

    protected class WHeader extends WHorizontalList {
        @Override
        public void init() {
            for (PrismTab tab : tabs) {
                add(createTabButton(tab));
            }
        }

        protected void onTabChange(PrismTab tab) { }
    }

    protected class WTabButton extends WPressable {
        protected final PrismTab tab;

        public WTabButton(PrismTab tab) {
            this.tab = tab;
        }

        @Override
        protected void onPressed(int button) {
            if (activeTab == tab) return;

            activeTab = tab;
            rebuildContent();

            if (header != null) header.onTabChange(tab);
            if (onTabChange != null) onTabChange.accept(tab);
        }

        @Override
        protected void onCalculateSize() {
            double padH = theme.textHeight();
            double padV = theme.textHeight() / 2;

            width = padH + theme.textWidth(tab.name()) + padH;
            height = padV + theme.textHeight() + padV;
        }
    }
}
