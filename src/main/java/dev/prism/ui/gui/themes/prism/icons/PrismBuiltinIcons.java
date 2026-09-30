package dev.prism.ui.gui.themes.prism.icons;

import dev.prism.ui.PrismAddon;
import dev.prism.ui.api.icons.PrismIcons;
import meteordevelopment.meteorclient.gui.renderer.GuiRenderer;
import meteordevelopment.meteorclient.gui.renderer.packer.GuiTexture;
import meteordevelopment.meteorclient.gui.tabs.Tab;
import meteordevelopment.meteorclient.gui.tabs.builtin.ConfigTab;
import meteordevelopment.meteorclient.gui.tabs.builtin.FriendsTab;
import meteordevelopment.meteorclient.gui.tabs.builtin.GuiTab;
import meteordevelopment.meteorclient.gui.tabs.builtin.HudTab;
import meteordevelopment.meteorclient.gui.tabs.builtin.MacrosTab;
import meteordevelopment.meteorclient.gui.tabs.builtin.ModulesTab;
import meteordevelopment.meteorclient.gui.tabs.builtin.ProfilesTab;
import meteordevelopment.meteorclient.systems.modules.Categories;

import java.util.Locale;

/*
 * Sources: https://icon-sets.iconify.design/solar/
 *          https://app.iconsax.io/?corner=Rounded
 */
public enum PrismBuiltinIcons {
    ARROW,
    BOOKMARK_NO,
    BOOKMARK_YES,
    BRUSH,
    COPY,
    CUBE,
    EDIT,
    EYE,
    GRID,
    IMPORT,
    LINK,
    MINUS,
    MOUSE,
    MOVEMENT,
    PEOPLE,
    PERSON,
    PLUS,
    QUESTION_MARK,
    RESET,
    SEARCH,
    SETTING,
    SWORD,
    TICK;

    private final String path;
    private GuiTexture texture;

    PrismBuiltinIcons() {
        this.path = "textures/icons/gui/" + name().toLowerCase(Locale.ROOT) + ".png";
    }

    public static void init() {
        for (PrismBuiltinIcons icon : values())
            icon.initIcon();

        // Init icons for Meteor
        PrismIcons.registerCategoryIcon(Categories.Combat.name, SWORD.texture());
        PrismIcons.registerCategoryIcon(Categories.Player.name, PERSON.texture());
        PrismIcons.registerCategoryIcon(Categories.Movement.name, MOVEMENT.texture());
        PrismIcons.registerCategoryIcon(Categories.Render.name, EYE.texture());
        PrismIcons.registerCategoryIcon(Categories.World.name, CUBE.texture());

        // Init icons for Meteor tabs
        PrismIcons.registerTabIcon(ModulesTab.class, CUBE.texture());
        PrismIcons.registerTabIcon(ConfigTab.class, SETTING.texture());
        PrismIcons.registerTabIcon(GuiTab.class, BRUSH.texture());
        PrismIcons.registerTabIcon(HudTab.class, GRID.texture());
        PrismIcons.registerTabIcon(FriendsTab.class, PEOPLE.texture());
        PrismIcons.registerTabIcon(MacrosTab.class, MOUSE.texture());
        PrismIcons.registerTabIcon(ProfilesTab.class, PERSON.texture());

        // Asteroid support :)
        try {
            Class<? extends Tab> pathManagerClass = Class
                    .forName("meteordevelopment.meteorclient.gui.tabs.builtin.PathManagerTab")
                    .asSubclass(Tab.class);

            PrismIcons.registerTabIcon(pathManagerClass, MOVEMENT.texture());
        } catch (ClassNotFoundException ignored) { }
    }

    public void initIcon() {
        try {
            this.texture = GuiRenderer.addTexture(PrismAddon.identifier(path));
        } catch (Exception e) {
            throw new RuntimeException("Icon '" + name() + "' could not be loaded.");
        }
    }

    public GuiTexture texture() {
        if (texture == null) throw new IllegalStateException("Icon " + name() + " not initialized.");
        return texture;
    }
}
