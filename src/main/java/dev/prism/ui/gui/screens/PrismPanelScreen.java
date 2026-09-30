package dev.prism.ui.gui.screens;

import dev.prism.ui.api.icons.PrismIcons;
import dev.prism.ui.gui.themes.prism.PrismGuiTheme;
import dev.prism.ui.gui.themes.prism.icons.PrismBuiltinIcons;
import dev.prism.ui.gui.themes.prism.widgets.panel.WPrismPanel;
import dev.prism.ui.gui.themes.prism.widgets.panel.WPrismSidebarItem;
import dev.prism.ui.gui.themes.prism.widgets.panel.WPrismSizedList;
import dev.prism.ui.utils.ColorUtils;
import dev.prism.ui.utils.search.SearchUtils;
import dev.prism.ui.utils.search.results.ModuleSearchResult;
import meteordevelopment.meteorclient.gui.GuiTheme;
import meteordevelopment.meteorclient.gui.renderer.packer.GuiTexture;
import meteordevelopment.meteorclient.gui.tabs.Tab;
import meteordevelopment.meteorclient.gui.tabs.TabScreen;
import meteordevelopment.meteorclient.gui.tabs.Tabs;
import meteordevelopment.meteorclient.gui.widgets.containers.WHorizontalList;
import meteordevelopment.meteorclient.gui.widgets.containers.WView;
import meteordevelopment.meteorclient.gui.widgets.input.WTextBox;
import meteordevelopment.meteorclient.systems.config.Config;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.utils.misc.NbtUtils;
import meteordevelopment.meteorclient.utils.render.color.Color;
import net.minecraft.client.gui.GuiGraphicsExtractor;

import java.util.ArrayList;
import java.util.List;

import static meteordevelopment.meteorclient.MeteorClient.mc;

//? if >=26.2
import org.jspecify.annotations.NonNull;

/**
 * Sidebar + two column modules screen. One big panel, categories on the left, search on top,
 * modules as compact rows that expand in place.
 */
public class PrismPanelScreen extends TabScreen {
    /** Remembered between openings so the GUI reopens where you left it. */
    private static String lastCategory;

    private final PrismGuiTheme theme;

    private WPrismPanel panel;
    private WPrismSizedList sidebar;
    private WTextBox search;
    private WHorizontalList columns;

    private final List<WPrismSidebarItem> items = new ArrayList<>();
    private final List<Category> itemCategories = new ArrayList<>();
    private final List<WPrismSizedList> lists = new ArrayList<>();

    private Category current;
    private double columnWidth;
    private int columnCount;
    private String lastSearchText = "";
    private boolean ignoreSearch;

    public PrismPanelScreen(GuiTheme theme) {
        super(theme, Tabs.get().getFirst());
        this.theme = (PrismGuiTheme) theme;
    }

    @Override
    public void initWidgets() {
        items.clear();
        itemCategories.clear();
        lists.clear();
        lastSearchText = "";
        ignoreSearch = false;

        double panelWidth = WPrismPanel.panelWidth(theme);
        double panelHeight = WPrismPanel.panelHeight(theme);

        double sidebarPad = theme.scale(10);
        double contentPad = theme.scale(14);
        double sidebarWidth = Math.min(theme.scale(theme.sidebarWidth.get()), panelWidth * 0.45);
        double contentWidth = panelWidth - sidebarWidth;

        panel = add(theme.panel()).center().widget();
        panel.sidebarWidth = sidebarWidth;

        WHorizontalList main = panel.add(theme.horizontalList()).expandX().widget();
        main.spacing = 0;

        // Sidebar
        sidebar = main.add(theme.sizedList(sidebarWidth - sidebarPad * 2)).pad(sidebarPad).widget();
        sidebar.spacing = theme.scale(4);

        if (theme.showBrand.get()) {
            sidebar.add(theme.brand(theme.brandName.get())).padBottom(theme.scale(14)).padLeft(theme.scale(4));
        }

        current = pickInitialCategory();

        for (Category category : Modules.loopCategories()) {
            if (visibleModules(category).isEmpty()) continue;

            WPrismSidebarItem item = theme.sidebarItem(category.name, iconFor(category), () -> select(category));
            item.selected = category == current;

            sidebar.add(item).expandX();

            items.add(item);
            itemCategories.add(category);
        }

        // Content
        WPrismSizedList content = main.add(theme.sizedList(contentWidth - contentPad * 2)).pad(contentPad).expandX().widget();
        content.spacing = theme.scale(10);

        WHorizontalList top = content.add(theme.horizontalList()).expandX().widget();
        top.spacing = theme.scale(8);

        search = top.add(theme.textBox("", "SEARCH MODULES")).expandX().widget();
        search.action = this::onSearchChanged;

        top.add(theme.button("EDIT HUD")).widget().action = () -> openTab("HUD");
        top.add(theme.button(PrismBuiltinIcons.SETTING.texture())).widget().action = () -> openTab("GUI");

        // Scrolling module area
        WView view = content.add(theme.view()).expandX().widget();
        view.scrollOnlyWhenMouseOver = true;
        view.hasScrollBar = false;
        view.spacing = 0;

        double headerHeight = theme.textHeight() * 2.6 + theme.scale(10);
        view.maxHeight = Math.max(theme.scale(120), panelHeight - contentPad * 2 - headerHeight - theme.scale(10));

        // Module columns
        columnCount = Math.max(1, Math.min(3, theme.columns.get()));
        double gap = theme.scale(10);
        columnWidth = (contentWidth - contentPad * 2 - gap * (columnCount - 1)) / columnCount;

        columns = view.add(theme.horizontalList()).expandX().widget();
        columns.spacing = gap;

        populate();
    }

    // Categories

    private Category pickInitialCategory() {
        Category first = null;

        for (Category category : Modules.loopCategories()) {
            if (visibleModules(category).isEmpty()) continue;
            if (first == null) first = category;
            if (category.name.equals(lastCategory)) return category;
        }

        return first;
    }

    private List<Module> visibleModules(Category category) {
        return Modules.get().getGroup(category).stream()
                      .filter(module -> !Config.get().hiddenModules.get().contains(module))
                      .toList();
    }

    private GuiTexture iconFor(Category category) {
        GuiTexture icon = PrismIcons.getCategoryIcon(category.name);
        return icon != null ? icon : PrismBuiltinIcons.QUESTION_MARK.texture();
    }

    private void select(Category category) {
        if (category == current && search.get().isEmpty()) return;

        current = category;
        lastCategory = category.name;
        ignoreSearch = !search.get().isEmpty();

        for (int i = 0; i < items.size(); i++) {
            items.get(i).selected = itemCategories.get(i) == category;
        }

        populate();
    }

    // Search

    private void onSearchChanged() {
        String text = search.get();

        if (text.equals(lastSearchText)) return;

        lastSearchText = text;
        ignoreSearch = false;

        populate();
    }

    // Modules

    private void populate() {
        columns.clear();
        lists.clear();

        for (int i = 0; i < columnCount; i++) {
            WPrismSizedList list = columns.add(theme.sizedList(columnWidth)).expandX().widget();
            list.spacing = theme.scale(2);
            lists.add(list);
        }

        int index = 0;
        String text = search == null ? "" : search.get();

        if (!text.isEmpty() && !ignoreSearch) {
            int limit = Config.get().moduleSearchCount.get();
            List<ModuleSearchResult> results = SearchUtils.searchModules(text, 50);

            for (int i = 0; i < Math.min(results.size(), limit); i++) {
                ModuleSearchResult result = results.get(i);
                lists.get(index++ % columnCount).add(theme.prismRow(result.module(), result.title())).expandX();
            }

            return;
        }

        if (current == null) return;

        for (Module module : visibleModules(current)) {
            lists.get(index++ % columnCount).add(theme.prismRow(module, module.title)).expandX();
        }
    }

    // Tabs

    private void openTab(String name) {
        for (Tab tab : Tabs.get()) {
            if (tab.name.equalsIgnoreCase(name)) {
                tab.openScreen(theme);
                return;
            }
        }
    }

    // Background

    @Override
    public void extractBackground(@NonNull GuiGraphicsExtractor context, int mouseX, int mouseY, float deltaTicks) {
        super.extractBackground(context, mouseX, mouseY, deltaTicks);

        int width = mc.getWindow().getGuiScaledWidth();
        int height = mc.getWindow().getGuiScaledHeight();

        double dim = theme.screenDim.get();
        double tint = theme.backdropTint.get() ? theme.backdropTintStrength.get() : 0;

        if (dim <= 0 && tint <= 0) return;

        // Top: plain dimming. Bottom: dimming mixed with the secondary accent.
        Color top = ColorUtils.withAlpha(Color.BLACK, dim * 0.7);
        Color bottom = ColorUtils.withAlpha(
                ColorUtils.lerp(Color.BLACK, theme.accentSecondaryColor(), Math.min(1, tint * 1.6)),
                Math.min(1, dim + tint)
        );

        context.fillGradient(0, 0, width, height, top.getPacked(), bottom.getPacked());
    }

    // Clipboard

    @Override
    public boolean toClipboard() {
        return NbtUtils.toClipboard(Modules.get());
    }

    @Override
    public boolean fromClipboard() {
        return NbtUtils.fromClipboard(Modules.get());
    }

    @Override
    public void reload() {}
}
