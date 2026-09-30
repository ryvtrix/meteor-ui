package dev.prism.ui.gui.themes.prism.colors;

import dev.prism.ui.gui.themes.prism.PrismGuiTheme;
import dev.prism.ui.utils.ColorUtils;
import meteordevelopment.meteorclient.gui.GuiThemes;
import meteordevelopment.meteorclient.settings.ColorSetting;
import meteordevelopment.meteorclient.utils.misc.ISerializable;
import meteordevelopment.meteorclient.utils.render.color.Color;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.Range;

public class ColorLink implements ISerializable<ColorLink> {
    private static final String KEY_COLOR = "color";
    private static final String KEY_OPACITY = "opacity";

    private PrismColor color;
    private int opacity = 255;

    public ColorLink(PrismColor color) {
        this.color = color;
    }

    public ColorLink() { }

    public PrismColor color() {
        return color;
    }

    public int opacity() {
        return opacity;
    }

    public void setOpacity(@Range(from = 0, to = 255) int opacity) {
        this.opacity = opacity;
    }

    public void apply(ColorSetting setting) {
        if (GuiThemes.get() instanceof PrismGuiTheme theme) {
            if (color == null || setting == null) return;

            Color finalColor = ColorUtils.withAlpha(theme.getColor(color), opacity);
            setting.get().set(finalColor);
        }
    }

    @Override
    public CompoundTag toTag() {
        CompoundTag tag = new CompoundTag();

        if (color != null) {
            tag.putString(KEY_COLOR, color.name());
            tag.putInt(KEY_OPACITY, opacity);
        }

        return tag;
    }

    @Override
    public ColorLink fromTag(CompoundTag tag) {
        //? if <=1.21.4 {
        /*color = PrismColor.valueOf(tag.getString(KEY_COLOR));
        opacity = tag.contains(KEY_OPACITY) ? tag.getInt(KEY_OPACITY) : 255;
        *///? } else {
        color = PrismColor.valueOf(tag.getStringOr(KEY_COLOR, ""));
        opacity = tag.getIntOr(KEY_OPACITY, 255);
        //? }

        return this;
    }
}
