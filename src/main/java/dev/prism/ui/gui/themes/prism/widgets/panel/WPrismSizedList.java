package dev.prism.ui.gui.themes.prism.widgets.panel;

import meteordevelopment.meteorclient.gui.widgets.containers.WVerticalList;

/**
 * A vertical list that never gets narrower than {@link #minWidth}. Used for the sidebar and the
 * module columns, so that all columns end up with the same width.
 */
public class WPrismSizedList extends WVerticalList {
    public double minWidth;

    public WPrismSizedList(double minWidth) {
        this.minWidth = minWidth;
    }

    @Override
    protected void onCalculateSize() {
        super.onCalculateSize();

        if (width < minWidth) width = minWidth;
    }
}
