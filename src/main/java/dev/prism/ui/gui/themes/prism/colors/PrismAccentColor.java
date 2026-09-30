package dev.prism.ui.gui.themes.prism.colors;

public enum PrismAccentColor {
    Rosewater,
    Flamingo,
    Pink,
    Mauve,
    Maroon,
    Peach,
    Teal,
    Sky,
    Sapphire,
    Lavender,
    Red,
    Blue,
    Yellow,
    Green;

    public PrismColor toColor() {
        return PrismColor.valueOf(name());
    }
}
