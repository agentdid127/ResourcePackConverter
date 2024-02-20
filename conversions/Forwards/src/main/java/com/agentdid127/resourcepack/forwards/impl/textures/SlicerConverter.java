package com.agentdid127.resourcepack.forwards.impl.textures;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;


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

    public static HashMap<Integer, String[]> ICONS = new HashMap<Integer, String[]>();

    public SlicerConverter(PackConverter converter, int from, int to) {
        super(converter);
        this.from = from;
        this.to = to;

        registerIcons();
    }

    private void registerIcons() {
        // Col #1
        // x: 0, y: 0, id: 0 - container
        ICONS.put(0, new String[] { "heart" + File.separator + "container.png" });
        // x: 1, y: 0, id: 1 - container_blinking
        ICONS.put(1, new String[] { "heart" + File.separator + "container_blinking.png" });
        
        // x: 2, y: 0, id: 2 - container_hardcore
        ICONS.put(2, new String[] { "heart" + File.separator + "container_hardcore.png" });
        // x: 3, y: 0, id: 3 - container_hardcore_blinking
        ICONS.put(3, new String[] { "heart" + File.separator + "container_hardcore_blinking.png" });

        // x: 4, y: 0, id: 4 - full
        ICONS.put(4, new String[] { "heart" + File.separator + "full.png" });
        // x: 5, y: 0, id: 5 - half
        ICONS.put(5, new String[] { "heart" + File.separator + "half.png" });

        // x: 6, y: 0, id: 6 - full_blinking
        ICONS.put(6, new String[] { "heart" + File.separator + "full_blinking.png" });
        // x: 7, y: 0, id: 7 - half
        ICONS.put(7, new String[] { "heart" + File.separator + "half_blinking.png" });

        // x: 8, y: 0, id: 8 - poisoned_full
        ICONS.put(8, new String[] { "heart" + File.separator + "poisoned_full.png" });
        // x: 9, y: 0, id: 9 - poisoned_half
        ICONS.put(9, new String[] { "heart" + File.separator + "poisoned_half.png" });

        // x: 10, y: 0, id: 10 - poisoned_full_blinking
        ICONS.put(10, new String[] { "heart" + File.separator + "poisoned_full_blinking.png" });
        // x: 11, y: 0, id: 11 - poisoned_half_blinking
        ICONS.put(11, new String[] { "heart" + File.separator + "poisoned_half_blinking.png" });

        // x: 12, y: 0, id: 12 - withered_full
        ICONS.put(12, new String[] { "heart" + File.separator + "withered_full.png" });
        // x: 13, y: 0, id: 13 - withered_half
        ICONS.put(13, new String[] { "heart" + File.separator + "withered_half.png" });
        
        // x: 14, y: 0, id: 14 - withered_full_blinking
        ICONS.put(14, new String[] { "heart" + File.separator + "withered_full_blinking.png" });
        // x: 15, y: 0, id: 15 - withered_half_blinking
        ICONS.put(15, new String[] { "heart" + File.separator + "withered_half_blinking.png" });

        // x: 16, y: 0, id: 16 - absorbing_full
        ICONS.put(16, new String[] { "heart" + File.separator + "absorbing_full.png" });
        // x: 17, y: 0, id: 17 - absorbing_half
        ICONS.put(17, new String[] { "heart" + File.separator + "absorbing_half.png" });

        Gson gson = packConverter.getGson();
        if (from >= Util.getVersionProtocol(gson, "1.17")) {
            // x: 18, y: 0, id: 18 - frozen_full/frozen_full_blinking
            ICONS.put(18, new String[] { 
                "heart" + File.separator + "frozen_full.png", 
                "heart" + File.separator + "frozen_full_blinking.png"  });
            // x: 19, y: 0, id: 19 - frozen_half/frozen_half_blinking
            ICONS.put(19, new String[] { 
                "heart" + File.separator + "frozen_half.png",
                "heart" + File.separator + "frozen_half_blinking.png" });
        }

        // Col #2
        // x: 0, y: 1, id: 256 - armor_empty
        ICONS.put(256, new String[] { "armor_empty.png" });

        // x: 1, y: 1, id: 257 - armor_half
        ICONS.put(257, new String[] { "armor_half.png" });

        // x: 2, y: 1, id: 258 - armor_full
        ICONS.put(258, new String[] { "armor_full.png" });

        // UNUSED: x: 3, y: 1 - duplicate of armor_full

        // x: 4, y: 1, id: 260 - vehicle_container
        ICONS.put(260, new String[] { "heart" + File.separator + "vehicle_container.png" });
        // UNUSED: x: 5, y: 1, id: 261 - _
        // UNUSED: x: 6, y: 1, id: 262 - _
        // UNUSED: x: 7, y: 1, id: 263 - _

        // x: 8, y: 1, id: 264 - vehicle_full
        ICONS.put(264, new String[] { "heart" + File.separator + "vehicle_full.png" });
        // x: 9, y: 1, id: 265 - vehicle_half
        ICONS.put(265, new String[] { "heart" + File.separator + "vehicle_half.png" });

        // UNUSED: x: 10, y: 1, id: 266 - vehicle_full_blinking
        // UNUSED: x: 11, y: 1, id: 267 - vehicle_half_blinking

        // Col #3
        // x: 0, y: 2, id: 512 - air
        ICONS.put(512, new String[] { "air.png" });
        // x: 1, y: 2, id: 513 - air_bursting
        ICONS.put(513, new String[] { "air_bursting.png" });
        // UNUSED: x: 2, y: 2, id: 514 - _
        // UNUSED: x: 3, y: 2, id: 515 - _

        // Col #4
        // x: 0, y: 3, id: 768 - food_empty
        ICONS.put(768, new String[] { "food_empty.png" });
        // UNUSED: x: 1, y: 3, id: 769 - _
        // UNUSED: x: 2, y: 3, id: 770 - _
        // UNUSED: x: 3, y: 3, id: 771 - _
        // x: 4, y: 3, id: 772 - food_full
        ICONS.put(772, new String[] { "food_full.png" });
        // x: 5, y: 3, id: 773 - food_half
        ICONS.put(773, new String[] { "food_half.png" });
        // UNUSED: x: 6, y: 3, id: 774 - _
        // UNUSED: x: 7, y: 3, id: 775 - _
        // x: 8, y: 3, id: 776 - food_full_hunger
        ICONS.put(776, new String[] { "food_full_hunger.png" });
        // x: 9, y: 3, id: 777 - food_half_hunger
        ICONS.put(777, new String[] { "food_half_hunger.png" });
        // UNUSED: x: 10, y: 3, id: 778 - _
        // UNUSED: x: 11, y: 3, id: 779 - _
        // UNUSED: x: 12, y: 3, id: 780 - _
        // x: 13, y: 3, id: 781 - food_empty_hunger
        ICONS.put(781, new String[] { "food_empty_hunger.png" });

        // (x=0, y=4) Could not find icon with RPID=1024
        // (x=0, y=5) Could not find icon with RPID=1280
        // (x=1, y=4) Could not find icon with RPID=1025
        // (x=1, y=5) Could not find icon with RPID=1281
        // (x=2, y=4) Could not find icon with RPID=1026
        // (x=2, y=5) Could not find icon with RPID=1282
        // (x=3, y=2) Could not find icon with RPID=515
        // (x=3, y=3) Could not find icon with RPID=771
        // (x=3, y=4) Could not find icon with RPID=1027
        // (x=3, y=5) Could not find icon with RPID=1283
        // (x=4, y=2) Could not find icon with RPID=516
        // (x=4, y=3) Could not find icon with RPID=772
        // (x=4, y=4) Could not find icon with RPID=1028
        // (x=4, y=5) Could not find icon with RPID=1284
        // (x=5, y=2) Could not find icon with RPID=517
        // (x=5, y=3) Could not find icon with RPID=773
        // (x=5, y=4) Could not find icon with RPID=1029
        // (x=5, y=5) Could not find icon with RPID=1285
        // (x=6, y=2) Could not find icon with RPID=518
        // (x=6, y=3) Could not find icon with RPID=774
        // (x=6, y=4) Could not find icon with RPID=1030
        // (x=6, y=5) Could not find icon with RPID=1286
        // (x=7, y=2) Could not find icon with RPID=519
        // (x=7, y=3) Could not find icon with RPID=775
        // (x=7, y=4) Could not find icon with RPID=1031
        // (x=7, y=5) Could not find icon with RPID=1287
        // (x=8, y=2) Could not find icon with RPID=520
        // (x=8, y=3) Could not find icon with RPID=776
        // (x=8, y=4) Could not find icon with RPID=1032
        // (x=8, y=5) Could not find icon with RPID=1288
        // (x=9, y=2) Could not find icon with RPID=521
        // (x=9, y=3) Could not find icon with RPID=777
        // (x=9, y=4) Could not find icon with RPID=1033
        // (x=9, y=5) Could not find icon with RPID=1289
        // (x=10, y=2) Could not find icon with RPID=522
        // (x=10, y=3) Could not find icon with RPID=778
        // (x=10, y=4) Could not find icon with RPID=1034
        // (x=10, y=5) Could not find icon with RPID=1290
        // (x=11, y=3) Could not find icon with RPID=779
        // (x=11, y=4) Could not find icon with RPID=1035
        // (x=11, y=5) Could not find icon with RPID=1291
        // (x=12, y=3) Could not find icon with RPID=780
        // (x=12, y=4) Could not find icon with RPID=1036
        // (x=12, y=5) Could not find icon with RPID=1292
        // (x=13, y=3) Could not find icon with RPID=781
        // (x=13, y=4) Could not find icon with RPID=1037
        // (x=13, y=5) Could not find icon with RPID=1293
        // (x=14, y=3) Could not find icon with RPID=782
        // (x=14, y=4) Could not find icon with RPID=1038
        // (x=14, y=5) Could not find icon with RPID=1294
        // (x=15, y=3) Could not find icon with RPID=783
        // (x=15, y=4) Could not find icon with RPID=1039
        // (x=15, y=5) Could not find icon with RPID=1295
        // (x=16, y=3) Could not find icon with RPID=784
        // (x=16, y=4) Could not find icon with RPID=1040
        // (x=16, y=5) Could not find icon with RPID=1296
        // (x=17, y=3) Could not find icon with RPID=785
        // (x=17, y=4) Could not find icon with RPID=1041
        // (x=17, y=5) Could not find icon with RPID=1297
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
                    int smaller_button_width = 20;
                    int smaller_button_height = 20;

                    int smaller_button_y_start = hotbar_height + hotbar_selector_height + (button_height * 3);

                    // // Language Button
                    // README: Determind if I should slice these or not because
                    // they now use the default button as the background and
                    // the icon is seperated into its own image
                    // I'll have to look into Mojangs Slicer & see what and if they do
                    // anything...

                    // Path iconFolder = spritesPath.resolve("icon");
                    // if (!iconFolder.toFile().exists())
                    // iconFolder.toFile().mkdir();

                    // // Language Button Default
                    // Path languageButtonPath = iconFolder.resolve("language.png");

                    // // Language Button Highlighted/Pressed
                    // Path languageButtonHighlightedPath = iconFolder.resolve("button.png");

                    if (from >= Util.getVersionProtocol(gson, "1.9")) {
                        // Locked (Background)
                        Path lockedButtonPath = widgetFolder.resolve("locked_button.png");
                        widgets.slice_and_save(0,
                                smaller_button_y_start + (smaller_button_height * 2),
                                smaller_button_width,
                                smaller_button_height, lockedButtonPath);

                        // Unlocked (Background)
                        Path unlockedButtonPath = widgetFolder.resolve("unlocked_button.png");
                        widgets.slice_and_save(smaller_button_width,
                                smaller_button_y_start + (smaller_button_height * 2),
                                smaller_button_width,
                                smaller_button_height, unlockedButtonPath);

                        // // Locked (Highlighted)
                        // Path lockedButtonHighlightedPath = widgetFolder.resolve("locked_button_highlighted.png");

                        // // Unlocked (Highlighted)
                        // Path unlockedButtonHighlightedPath = widgetFolder.resolve("unlocked_button_highlighted.png");

                        // // Locked (Disabled)
                        // Path lockedButtonDisabledPath = widgetFolder.resolve("locked_button_disabled.png");

                        // // Unlocked (Disabled)
                        // Path unlockedButtonDisabledPath = widgetFolder.resolve("unlocked_button_disabled.png");
                    }

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
                    int icon_rows = 20;
                    int icon_cols = 6;
                    int icon_width = 9;
                    int icon_height = 9;

                    for (int x = 0; x < icon_rows; ++x) {
                        for (int y = 0; y < icon_cols; ++y) {
                            int id = y * 256 + x; // 256 is a hack, idk what else to do
                            if (!ICONS.containsKey(id)) {
                                if (PackConverter.DEBUG)
                                    Logger.log("(x=" + x + ", y=" + y + ") Could not find icon with RPID=" + id);
                                continue;
                            }

                            String[] paths = ICONS.get(id);
                            for (String path : paths) {
                                Path iconPath = hudPath.resolve(path);
                                if (PackConverter.DEBUG)
                                    Logger.log("Icon: " + iconPath.getFileName());
    
                                if (!iconPath.getParent().toFile().exists()) 
                                    iconPath.getParent().toFile().mkdirs();
                    
                                int sx = crosshair_width + (x * icon_width);
                                int sy = y * icon_height;
                                
                                icons.newImage(icon_width, icon_height);
                                icons.subImage(sx, sy, sx + icon_width, sy + icon_height);
                                icons.store(iconPath);
                            }
                        }
                    }

                    if (from >= Util.getVersionProtocol(gson, "1.9")) {
                        // Cooldown Indicator (Hotbar)
                        int cooldown_indicator_hotbar_width = 18;
                        int cooldown_indicator_hotbar_height = 18;

                        // Cooldown Indicator Background
                        Path hotbarCooldownIndicatorPath = hudPath.resolve("hotbar_attack_indicator_background.png");
                        icons.slice_and_save(0, 64 + (xp_bar_height * 6), cooldown_indicator_hotbar_width,
                                cooldown_indicator_hotbar_height, hotbarCooldownIndicatorPath);

                        Path hotbarCooldownIndicatorProgressPath = hudPath
                                .resolve("hotbar_attack_indicator_progress.png");
                        icons.slice_and_save(cooldown_indicator_hotbar_width, 64 + (xp_bar_height * 6),
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