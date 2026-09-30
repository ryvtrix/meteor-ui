# Prism UI — a customizable sidebar GUI theme for Meteor Client

A Meteor Client addon that replaces the modules screen with one rounded, translucent panel:
category sidebar, search bar, two columns of compact module rows with animated switches, and
settings that expand in place (right-click a row or click its gear).

Built from the source of the Catppuccin Addon by Pindour (GPL-3.0). This project is GPL-3.0 too.

## Easiest: build it on GitHub (no Java needed)

1. Create a new GitHub repo and upload everything in this folder (keep the `.github` folder).
2. Open the **Actions** tab, wait for "Build jar" to finish (a few minutes).
3. Open the finished run and download the **prism-ui-jar** artifact. That zip contains your jar.

## Build locally (Minecraft 1.21.11)

Requires JDK 21 and internet access for Gradle.

    Windows: double-click build.bat      Mac/Linux: ./build.sh

The jar ends up in `build/libs/1.0.0/`. Drop it in your `mods` folder next to Meteor Client, then in
the GUI open the **GUI** tab and pick the **Prism** theme.

## Customizing (GUI tab -> Prism)

* **Colors** – presets (Violet, Midnight, Ocean, Rose, Emerald, Sunset, Mono) or Custom: accent,
  second accent, background, text, secondary text, surface contrast.
* **Transparency** – panel, sidebar, window, widget backgrounds, screen dimming.
* **Shape** – corner radii, outline thickness and opacity.
* **Effects** – panel glow (size/strength), gradient accent bar, backdrop tint, hover glow,
  toggle glow, active-module indicator.
* **Panel Layout** – panel width/height %, sidebar width, 1-3 columns, row padding, brand text.
* **General** – Panel or classic Windows layout, UPPERCASE text, Switch or Checkbox toggles.

## Notes

* Only 1.21.11 is configured (`settings.gradle.kts`). Other versions would need re-adding and checking.
* Source uses Stonecutter's newer-version naming; the build converts it for 1.21.11 automatically.
