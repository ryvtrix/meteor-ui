package dev.prism.ui.gui.themes.prism.widgets.input;

import dev.prism.ui.api.render.style.Corners;
import dev.prism.ui.api.text.RichText;
import dev.prism.ui.api.text.TextScale;
import dev.prism.ui.gui.themes.prism.PrismGuiTheme;
import dev.prism.ui.gui.themes.prism.PrismWidget;
import dev.prism.ui.gui.themes.prism.icons.PrismBuiltinIcons;
import dev.prism.ui.gui.widgets.input.WSearch;
import dev.prism.ui.utils.ColorUtils;
import dev.prism.ui.utils.search.SearchResult;
import dev.prism.ui.utils.search.results.ModuleSearchResult;
import dev.prism.ui.utils.search.results.SettingSearchResult;
import meteordevelopment.meteorclient.gui.renderer.GuiRenderer;
import meteordevelopment.meteorclient.gui.renderer.packer.GuiTexture;
import meteordevelopment.meteorclient.gui.widgets.WLabel;
import meteordevelopment.meteorclient.gui.widgets.containers.WContainer;
import meteordevelopment.meteorclient.gui.widgets.containers.WHorizontalList;
import meteordevelopment.meteorclient.gui.widgets.containers.WVerticalList;
import meteordevelopment.meteorclient.utils.render.color.Color;

public class WPrismSearch extends WSearch implements PrismWidget {

    @Override
    protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
        roundedRect().pos(x, y)
                     .size(width, height)
                     .radius(radius())
                     .color(ColorUtils.withAlpha(theme().crustColor(), 0.4))
                     .shadow(shadow())
                     .render();
    }

    @Override
    protected WSearchHeader createHeader(WSearch search) {
        return new WPrismHeader(search);
    }

    @Override
    protected WResultsContainer createResultsContainer() {
        return new WPrismResultsContainer();
    }

    @Override
    protected WSearchResult createSearchResult(SearchResult result) {
        return new WPrismResult(result);
    }

    private static class WPrismHeader extends WSearchHeader implements PrismWidget {

        public WPrismHeader(WSearch search) {
            super(search);
        }

        @Override
        public void init() {
            PrismGuiTheme theme = theme();

            // Row container
            WHorizontalList row = add(theme.horizontalList()).expandX().pad(theme.scale(12)).widget();

            // Search texture
            row.add(theme.texture(PrismBuiltinIcons.SEARCH.texture(), theme.textHeight())).center();

            // Search textbox
            WPrismTextBox textBox = (WPrismTextBox) theme.textBox("", "Search for modules...");
            textBox.shouldRenderBackground(false);

            row.add(textBox).expandX();
            search.initTextBox(textBox);

            // Hint label
            row.add(theme.label("ESC to close"));
        }

        @Override
        protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
            roundedRect().bounds(this)
                         .color(theme().crustColor())
                         .radius(radius(), Corners.TOP)
                         .render();
        }
    }

    private static class WPrismResultsContainer extends WResultsContainer implements PrismWidget {
        @Override
        public void init() {
            super.init();

            addDirect(theme.label("Left click to toggle module; Right click to open the module's settings.").color(theme().textSecondaryColor())).pad(theme.pad()).centerX();
        }

        @Override
        protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
            PrismGuiTheme theme = theme();

            roundedRect().bounds(this)
                         .color(ColorUtils.withAlpha(theme.baseColor(), theme.windowOpacity()))
                         .radius(radius(), Corners.BOTTOM)
                         .render();
        }
    }

    private static class WPrismResult extends WSearchResult implements PrismWidget {
        private PrismGuiTheme theme;

        public WPrismResult(SearchResult result) {
            super(result);
        }

        @Override
        public void init() {
            theme = theme();

            // Row container
            WHorizontalList row = add(theme.horizontalList()).expandX().pad(6).widget();

            // Result type icon
            row.add(new WResultType(result)).pad(theme.pad()).center();

            // Result info container
            WVerticalList infoColumn = row.add(theme.verticalList()).expandX().widget();

            // Result title
            infoColumn.add(theme.label(RichText.of(result.title())));

            // Result description
            RichText desc = RichText.of(result.description()).scale(TextScale.SMALL.get());
            WLabel descLabel = infoColumn.add(theme.label(desc)).widget();
            descLabel.color = theme.textSecondaryColor();
        }

        @Override
        protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
            if (!mouseOver) return;

            Color outlineColor = ColorUtils.withAlpha(
                    theme.accentColor(),
                    theme.backgroundOpacity() * 0.5
            );

            background(getBackgroundColor(pressed, false), outlineColor).render();
        }

        public static class WResultType extends WContainer implements PrismWidget {
            private final SearchResult result;
            private Color color;

            public WResultType(SearchResult result) {
                this.result = result;
            }

            @Override
            public void init() {
                color = getColor();

                add(theme().texture(getIcon(), theme.textHeight()).color(color)).pad(theme.pad()).center();
            }

            @Override
            protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
                roundedRect().bounds(this)
                             .color(ColorUtils.withAlpha(color, 60))
                             .radius(smallRadius())
                             .render();
            }

            private Color getColor() {
                return switch (result) {
                    case ModuleSearchResult r -> r.hasAlias() ? theme().yellowColor() : theme().greenColor();
                    case SettingSearchResult ignored -> theme().blueColor();
                    default -> theme().textSecondaryColor();
                };
            }

            private GuiTexture getIcon() {
                return switch (result) {
                    case ModuleSearchResult ignored -> PrismBuiltinIcons.CUBE.texture();
                    case SettingSearchResult ignored -> PrismBuiltinIcons.SETTING.texture();
                    default -> PrismBuiltinIcons.QUESTION_MARK.texture();
                };
            }
        }
    }
}