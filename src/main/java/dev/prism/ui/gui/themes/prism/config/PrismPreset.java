package dev.prism.ui.gui.themes.prism.config;

import meteordevelopment.meteorclient.utils.render.color.SettingColor;

/**
 * Ready-made colour schemes. Selecting one copies its colours into the theme's colour settings,
 * after which every colour can still be tweaked by hand.
 */
public enum PrismPreset {
    Violet(   new int[]{152, 118, 240}, new int[]{198, 150, 255}, new int[]{72, 54, 104},  new int[]{234, 226, 248}, new int[]{172, 160, 200}),
    Midnight( new int[]{ 96, 165, 250}, new int[]{129, 140, 248}, new int[]{20, 24, 38},   new int[]{226, 232, 240}, new int[]{148, 163, 184}),
    Ocean(    new int[]{ 45, 212, 191}, new int[]{ 56, 189, 248}, new int[]{14, 44, 60},   new int[]{224, 242, 241}, new int[]{148, 190, 196}),
    Rose(     new int[]{244, 114, 182}, new int[]{251, 146, 160}, new int[]{58, 32, 48},   new int[]{253, 232, 240}, new int[]{198, 152, 174}),
    Emerald(  new int[]{ 74, 222, 128}, new int[]{163, 230,  53}, new int[]{18, 46, 38},   new int[]{226, 246, 232}, new int[]{150, 190, 166}),
    Sunset(   new int[]{251, 146,  60}, new int[]{244,  63,  94}, new int[]{58, 34, 38},   new int[]{255, 237, 226}, new int[]{204, 162, 150}),
    Mono(     new int[]{236, 236, 242}, new int[]{160, 160, 172}, new int[]{30, 30, 34},   new int[]{240, 240, 244}, new int[]{150, 150, 158}),
    /** Leaves the colour settings untouched. */
    Custom(null, null, null, null, null);

    private final int[] accent, accentSecondary, background, text, textSecondary;

    PrismPreset(int[] accent, int[] accentSecondary, int[] background, int[] text, int[] textSecondary) {
        this.accent = accent;
        this.accentSecondary = accentSecondary;
        this.background = background;
        this.text = text;
        this.textSecondary = textSecondary;
    }

    public boolean isCustom() {
        return accent == null;
    }

    public SettingColor accent() { return of(accent); }
    public SettingColor accentSecondary() { return of(accentSecondary); }
    public SettingColor background() { return of(background); }
    public SettingColor text() { return of(text); }
    public SettingColor textSecondary() { return of(textSecondary); }

    private static SettingColor of(int[] rgb) {
        return new SettingColor(rgb[0], rgb[1], rgb[2]);
    }
}
