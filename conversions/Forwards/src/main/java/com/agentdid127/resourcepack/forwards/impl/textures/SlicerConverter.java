package com.agentdid127.resourcepack.forwards.impl.textures;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.Util;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.ImageConverter;
import com.agentdid127.resourcepack.library.utilities.Logger;
import com.google.gson.Gson;

public class SlicerConverter extends Converter {
    private int from;
    private int to;

    public SlicerConverter(PackConverter converter, int from, int to) {
        super(converter);
        this.from = from;
        this.to = to;
    }

    @Override
    public void convert(Pack pack) throws IOException {
        Path texturesPath = pack.getWorkingPath().resolve("assets" + File.separator + "minecraft" + File.separator
                + "textures");

        Gson gson = packConverter.getGson();

        Path guiPath = texturesPath.resolve("gui");
        if (!guiPath.toFile().exists())
            return;

        Path spritesPath = guiPath.resolve("sprites");
        if (!spritesPath.toFile().exists())
            spritesPath.toFile().mkdirs();

        // widgets.png & icons.png
        {
            Path hudPath = spritesPath.resolve("hud");

            Path widgetsPath = guiPath.resolve("widgets.png");
            Path iconsPath = guiPath.resolve("icons.png");
            if ((widgetsPath.toFile().exists() || iconsPath.toFile().exists()) && !hudPath.toFile().exists())
                hudPath.toFile().mkdirs();

            if (widgetsPath.toFile().exists()) {
                int default_dimensions = 256;
                ImageConverter widgets = new ImageConverter(default_dimensions, default_dimensions, widgetsPath);
                if (widgets.isSquare()) {
                    // Hotbar
                    int hotbar_width = 182;
                    int hotbar_height = 22;
                    Path hotbarPath = hudPath.resolve("hotbar.png");
                    widgets.slice_and_save(0, 0, hotbar_width, hotbar_height, hotbarPath);

                    // Hotbar Selector
                    int hotbar_selector_width = 24;
                    int hotbar_selector_height = 24;
                    Path hotbarSelectionPath = hudPath.resolve("hotbar_selection.png");
                    widgets.slice_and_save(0, hotbar_height, hotbar_selector_width, hotbar_selector_height,
                            hotbarSelectionPath);

                    if (from >= Util.getVersionProtocol(gson, "1.9")) {
                        // Offhands
                        int offhand_width = 29;
                        int offhand_height = 24;

                        // Offhand // Left
                        Path offhandLeft = hudPath.resolve("hotbar_offhand_left.png");
                        widgets.slice_and_save(hotbar_selector_width + offhand_width, hotbar_height, offhand_width,
                                offhand_height, offhandLeft);

                        // Offhand Right
                        Path offhandRight = hudPath.resolve("hotbar_offhand_right.png");
                        widgets.slice_and_save(hotbar_selector_width + (offhand_width * 2), hotbar_height,
                                offhand_width,
                                offhand_height, offhandRight);
                    }

                    Path widgetFolder = spritesPath.resolve("widget");
                    if (!widgetFolder.toFile().exists())
                        widgetFolder.toFile().mkdir();

                    // Buttons
                    int button_width = 200;
                    int button_height = 20;

                    // Button Disabled
                    Path buttonDisabledPath = widgetFolder.resolve("button_disabled.png");
                    widgets.slice_and_save(0, hotbar_height + hotbar_selector_height, button_width, button_height,
                            buttonDisabledPath);

                    // Button Highlighted/Pressed
                    Path buttonHighlightedPath = widgetFolder.resolve("button_highlighted.png");
                    widgets.slice_and_save(0, hotbar_height + hotbar_selector_height + button_height, button_width,
                            button_height,
                            buttonHighlightedPath);

                    // Button Default
                    Path buttonPath = widgetFolder.resolve("button.png");
                    widgets.slice_and_save(0, hotbar_height + hotbar_selector_height + (button_height * 2),
                            button_width,
                            button_height,
                            buttonPath);

                    // Smaller Square Buttons
                    // README: Determind if I should slice these or not because
                    // they now use the default button as the background and
                    // the icon is seperated into its own image
                    // I'll have to look into Mojangs Slicer & see what and if they do
                    // anything...

                    // int smaller_button_width = 20;
                    // int smaller_button_height = 20;

                    // // Language Button
                    // Path iconFolder = spritesPath.resolve("icon");
                    // if (!iconFolder.toFile().exists())
                    // iconFolder.toFile().mkdir();

                    // // Language Button Default
                    // Path languageButtonPath = iconFolder.resolve("language.png");

                    // // Language Button Highlighted/Pressed
                    // Path languageButtonHighlightedPath = iconFolder.resolve("button.png");

                    widgetsPath.toFile().delete();
                } else
                    Logger.log("Failed to slice widgets.png, image is not square.");
            }

            if (iconsPath.toFile().exists()) {
                int dw = 256;
                int dh = 256;
                ImageConverter icons = new ImageConverter(dw, dh, iconsPath);
                if (icons.isSquare()) {
                    // Crosshair
                    int crosshair_width = 16;
                    int crosshair_height = 16;
                    Path crosshairPath = hudPath.resolve("crosshair.png");
                    icons.slice_and_save(0, 0, crosshair_width, crosshair_height, crosshairPath);

                    // XP Bars
                    int xp_bar_width = 182;
                    int xp_bar_height = 5;

                    // XP Bar Background
                    Path experienceBackgroundPath = hudPath.resolve("experience_bar_background.png");
                    icons.slice_and_save(0, 64, xp_bar_width, xp_bar_height, experienceBackgroundPath);

                    // XP Bar Progress
                    Path experienceProgressPath = hudPath.resolve("experience_bar_progress.png");
                    icons.slice_and_save(0, 64 + xp_bar_height, xp_bar_width, xp_bar_height, experienceProgressPath);

                    // Jump Bar Cooldown
                    Path jumpBarCooldownPath = hudPath.resolve("jump_bar_cooldown.png");
                    icons.slice_and_save(0, 64 + (xp_bar_height * 2), xp_bar_width, xp_bar_height, jumpBarCooldownPath);

                    // XP Bar Purple Background
                    // 0, 64 + (xp_bar_height * 3)

                    // Jump Bar Background
                    Path jumpBarBackgroundPath = hudPath.resolve("jump_bar_background.png");
                    icons.slice_and_save(0, 64 + (xp_bar_height * 4), xp_bar_width, xp_bar_height,
                            jumpBarBackgroundPath);

                    // Jump Bar Progress
                    Path jumpBarProgressPath = hudPath.resolve("jump_bar_progress.png");
                    icons.slice_and_save(0, 64 + (xp_bar_height * 5), xp_bar_width, xp_bar_height, jumpBarProgressPath);

                    // Mapped Icons
                    // int rows = 20;
                    // int cols = 6;

                    if (from >= Util.getVersionProtocol(gson, "1.9")) {
                        // Cooldown Indicator (Hotbar)
                        int cooldown_indicator_hotbar_width = 18;
                        int cooldown_indicator_hotbar_height = 18;

                        // Cooldown Indicator Background
                        Path hotbarCooldownIndicatorPath = hudPath.resolve("hotbar_attack_indicator_background.png");
                        icons.slice_and_save(0, 64 + (xp_bar_height * 5), cooldown_indicator_hotbar_width,
                                cooldown_indicator_hotbar_height, hotbarCooldownIndicatorPath);

                        Path hotbarCooldownIndicatorProgressPath = hudPath
                                .resolve("hotbar_attack_indicator_progress.png");
                        icons.slice_and_save(cooldown_indicator_hotbar_width, 64 + (xp_bar_height * 5),
                                cooldown_indicator_hotbar_width, cooldown_indicator_hotbar_height,
                                hotbarCooldownIndicatorProgressPath);

                        // Cooldown Indicator (Crosshair)
                        // ((cooldown_indicator_hotbar_width * 2), 64 + (xp_bar_height * 5),
                        // cooldown_indicator_hotbar_width,
                        // cooldown_indicator_hotbar_height)
                    }

                    // Ping bars

                    iconsPath.toFile().delete();
                } else
                    Logger.log("Failed to slice icons.png, image is not square.");
            }
        }
    }
}