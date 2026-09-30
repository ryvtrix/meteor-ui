package dev.prism.ui.gui.themes.prism.widgets;

import dev.prism.ui.api.text.RichText;
import dev.prism.ui.gui.themes.prism.PrismWidget;
import meteordevelopment.meteorclient.gui.renderer.GuiRenderer;
import meteordevelopment.meteorclient.gui.utils.AlignmentX;
import meteordevelopment.meteorclient.gui.widgets.WLabel;

public class WPrismLabel extends WLabel implements PrismWidget {
    protected RichText richText;
    private AlignmentX alignX = AlignmentX.Left;

    public WPrismLabel(RichText text) {
        super(text.getPlainText(), false);
        richText = text;
    }

    @Override
    protected void onCalculateSize() {
        width = theme().textWidth(richText);
        height = theme().textHeight(richText);
    }

    @Override
    protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
        if (text.isEmpty()) return;

        double textWidth = theme().textWidth(text);
        double offsetX = 0;

        switch (alignX) {
            case Center -> offsetX = width / 2 - textWidth / 2;
            case Right -> offsetX = width - textWidth;
        }

        renderer().text(
                richText,
                x + offsetX,
                y,
                color != null ? color : theme().textColor()
        );
    }

    public void textLeft() {
        alignX = AlignmentX.Left;
    }

    public void textCenter() {
        alignX = AlignmentX.Center;
    }

    public void textRight() {
        alignX = AlignmentX.Right;
    }

    public void set(RichText text) {
        if (Math.round(theme().textWidth(text)) != width) invalidate();

        this.text = text.getPlainText();
        richText = text;
    }

    @Override
    public void set(String text) {
        set(RichText.of(text));
    }
}
