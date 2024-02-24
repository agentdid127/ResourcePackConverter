package com.agentdid127.resourcepack.forwards.impl.textures;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.Util;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.ImageConverter;
import com.agentdid127.resourcepack.library.utilities.Logger;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class SlicerConverter extends Converter {
    private int from;

    public static HashMap<Integer, String[]> ICONS = new HashMap<Integer, String[]>();

    public SlicerConverter(PackConverter converter, int from) {
        super(converter);
        this.from = from;
        registerIcons();
    }

    private void registerIcons() {
        // (Col #1)
        // x: 0, y: 0, id: 0 - container
        ICONS.put(0, new String[] { "heart" + File.separator + "container.png" });
        // x: 1, y: 0, id: 1 - container_blinking
        ICONS.put(1, new String[] { "heart" + File.separator + "container_blinking.png" });
        
        // UNUSED: x: 2, y: 0, id: 2 - unknown
        // UNUSED: x: 3, y: 0, id: 3 - unknown

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

        // (Col #2)
        // x: 0, y: 1, id: 256 - armor_empty
        ICONS.put(256, new String[] { "armor_empty.png" });

        // x: 1, y: 1, id: 257 - armor_half
        ICONS.put(257, new String[] { "armor_half.png" });

        // x: 2, y: 1, id: 258 - armor_full
        ICONS.put(258, new String[] { "armor_full.png" });

        // UNUSED: x: 3, y: 1 - unknown (presumably duplicate of armor_full)

        // x: 4, y: 1, id: 260 - vehicle_container
        ICONS.put(260, new String[] { "heart" + File.separator + "vehicle_container.png" });
        
        // UNUSED: x: 5, y: 1, id: 261 - unknown
        // UNUSED: x: 6, y: 1, id: 262 - unknown
        // UNUSED: x: 7, y: 1, id: 263 - unknown
        
        // x: 8, y: 1, id: 264 - vehicle_full
        ICONS.put(264, new String[] { "heart" + File.separator + "vehicle_full.png" });
        // x: 9, y: 1, id: 265 - vehicle_half
        ICONS.put(265, new String[] { "heart" + File.separator + "vehicle_half.png" });
        
        // UNUSED: x: 10, y: 1, id: 266 - unknown (presumably vehicle_full_blinking)
        // UNUSED: x: 11, y: 1, id: 267 - unknown (presumably vehicle_half_blinking)

        // (Col #3)
        // x: 0, y: 2, id: 512 - air
        ICONS.put(512, new String[] { "air.png" });
        // x: 1, y: 2, id: 513 - air_bursting
        ICONS.put(513, new String[] { "air_bursting.png" });
        
        // UNUSED: x: 2, y: 2, id: 514 - unknown
        // UNUSED: x: 3, y: 2, id: 515 - unknown

        // (Col #4)
        // x: 0, y: 3, id: 768 - food_empty
        ICONS.put(768, new String[] { "food_empty.png" });
        
        // UNUSED: x: 1, y: 3, id: 769 - unknown
        // UNUSED: x: 2, y: 3, id: 770 - unknown
        // UNUSED: x: 3, y: 3, id: 771 - unknown

        // x: 4, y: 3, id: 772 - food_full
        ICONS.put(772, new String[] { "food_full.png" });
        // x: 5, y: 3, id: 773 - food_half
        ICONS.put(773, new String[] { "food_half.png" });
        
        // UNUSED: x: 6, y: 3, id: 774 - unknown
        // UNUSED: x: 7, y: 3, id: 775 - unknown
        
        // x: 8, y: 3, id: 776 - food_full_hunger
        ICONS.put(776, new String[] { "food_full_hunger.png" });
        // x: 9, y: 3, id: 777 - food_half_hunger
        ICONS.put(777, new String[] { "food_half_hunger.png" });
        
        // UNUSED: x: 10, y: 3, id: 778 - unknown
        // UNUSED: x: 11, y: 3, id: 779 - unknown
        // UNUSED: x: 12, y: 3, id: 780 - unknown
        
        // x: 13, y: 3, id: 781 - food_empty_hunger
        ICONS.put(781, new String[] { "food_empty_hunger.png" });

        // (Col #5)
        // UNUSED: x: 0, y: 4, id: 1024 - unknown

        // (Col #6)
        // x: 0, y: 5, id: 1280 - container_hardcore
        ICONS.put(1280, new String[] { "heart" + File.separator + "container_hardcore.png" });
        // x: 1, y: 5, id: 1281 - container_hardcore_blinking
        ICONS.put(1281, new String[] { "heart" + File.separator + "container_hardcore_blinking.png" });
        
        // UNUSED: x: 2, y: 5, id: 1282 - unknown
        // UNUSED: x: 3, y: 5, id: 1283 - unknown
        
        // x: 4, y: 5, id: 1284 - hardcore_full
        ICONS.put(1284, new String[] { "heart" + File.separator + "hardcore_full.png" });
        // x: 5, y: 5, id: 1285 - hardcore_half
        ICONS.put(1285, new String[] { "heart" + File.separator + "hardcore_half.png" });
        
        // x: 6, y: 5, id: 1286 - hardcore_full_blinking
        ICONS.put(1286, new String[] { "heart" + File.separator + "hardcore_full_blinking.png" });
        // x: 7, y: 5, id: 1287 - hardcore_half_blinking
        ICONS.put(1287, new String[] { "heart" + File.separator + "hardcore_half_blinking.png" });
        
        // x: 8, y: 5, id: 1288 - poisoned_hardcore_full
        ICONS.put(1288, new String[] { "heart" + File.separator + "poisoned_hardcore_full.png" });
        // x: 9, y: 5, id: 1289 - poisoned_hardcore_half
        ICONS.put(1289, new String[] { "heart" + File.separator + "poisoned_hardcore_half.png" });
        
        // x: 10, y: 5, id: 1290 - poisoned_hardcore_full_blinking
        ICONS.put(1290, new String[] { "heart" + File.separator + "poisoned_hardcore_full_blinking.png" });
        // x: 11, y: 5, id: 1291 - poisoned_hardcore_half_blinking
        ICONS.put(1291, new String[] { "heart" + File.separator + "poisoned_hardcore_half_blinking.png" });

        // x: 12, y: 5, id: 1292 - withered_hardcore_full
        ICONS.put(1292, new String[] { "heart" + File.separator + "withered_hardcore_full.png" });
        // x: 13, y: 5, id: 1293 - withered_hardcore_half
        ICONS.put(1293, new String[] { "heart" + File.separator + "withered_hardcore_half.png" });

        // x: 14, y: 5, id: 1294 - withered_hardcore_full_blinking
        ICONS.put(1294, new String[] { "heart" + File.separator + "withered_hardcore_full_blinking.png" });
        // x: 15, y: 5, id: 1295 - withered_hardcore_half_blinking
        ICONS.put(1295, new String[] { "heart" + File.separator + "withered_hardcore_half_blinking.png" });

        // x: 16, y: 5, id: 1296 - absorbing_hardcore_full/absorbing_hardcore_full_blinking
        ICONS.put(1296, new String[] { 
            "heart" + File.separator + "absorbing_hardcore_full.png", 
            "heart" + File.separator + "absorbing_hardcore_full_blinking.png"  });
        // x: 17, y: 5, id: 1297 - absorbing_hardcore_half/absorbing_hardcore_half_blinking
        ICONS.put(1297, new String[] { 
            "heart" + File.separator + "absorbing_hardcore_half.png", 
            "heart" + File.separator + "absorbing_hardcore_half_blinking.png"  });
        
        if (from >= Util.getVersionProtocol(gson, "1.17")) {
            // x: 20, y: 5, id: 1300 - frozen_hardcore_full/frozen_hardcore_full_blinking
            ICONS.put(1300, new String[] { 
                "heart" + File.separator + "frozen_hardcore_full.png", 
                "heart" + File.separator + "frozen_hardcore_full_blinking.png"  });
            // x: 21, y: 5, id: 1301 - frozen_hardcore_half/frozen_hardcore_half_blinking
            ICONS.put(1301, new String[] { 
                "heart" + File.separator + "frozen_hardcore_half.png", 
                "heart" + File.separator + "frozen_hardcore_half_blinking.png"  });
        }
    }

    @Override
    public void convert(Pack pack) throws IOException {
        Path texturesPath = pack.getWorkingPath().resolve("assets" + File.separator + "minecraft" + File.separator + "textures");

        Path guiPath = texturesPath.resolve("gui");
        if (!guiPath.toFile().exists())
            return; // No need to do any slicing.

        Path spritesPath = guiPath.resolve("sprites");
        if (!spritesPath.toFile().exists())
            spritesPath.toFile().mkdirs();

        Path hudPath = spritesPath.resolve("hud");
        if (!hudPath.toFile().exists())
            hudPath.toFile().mkdirs();

        slice_widgets(guiPath, spritesPath, hudPath);
        slice_icons(guiPath, hudPath);

        // todo: containers
    }

    // Widgets
    private void slice_widgets(Path guiPath, Path spritesPath, Path hudPath) throws IOException {
        Path widgetsPath = guiPath.resolve("widgets.png");
        if (!widgetsPath.toFile().exists()) 
            return;

        int default_dimensions = 256;
        ImageConverter widgets = new ImageConverter(default_dimensions, default_dimensions, widgetsPath);
        if (!widgets.isSquare()) {
            Logger.log("Failed to slice widgets.png, image is not square.");
            return;
        }

        Gson gson = packConverter.getGson();

        // Hotbar
        int hotbar_width = 182;
        int hotbar_height = 22;
        Path hotbar = hudPath.resolve("hotbar.png");
        widgets.slice_and_save(
            0, 
            0, 
            hotbar_width, 
            hotbar_height, 
            hotbar);

        // Hotbar Selector
        int hotbar_selector_width = 24;
        int hotbar_selector_height = 24;
        Path hotbar_selection = hudPath.resolve("hotbar_selection.png");
        widgets.slice_and_save(
            0, 
            hotbar_height, 
            hotbar_selector_width, 
            hotbar_selector_height,
            hotbar_selection);

        boolean pvp_update = from >= Util.getVersionProtocol(gson, "1.9");
        if (pvp_update) {
            // Off Hands
            int offhand_width = 29;
            int offhand_height = 24;

            // Off Hand Left
            Path hotbar_offhand_left = hudPath.resolve("hotbar_offhand_left.png");
            widgets.slice_and_save(
                hotbar_selector_width, 
                hotbar_height, 
                offhand_width,
                offhand_height, 
                hotbar_offhand_left);

            // Off Hand Right
            Path hotbar_offhand_right = hudPath.resolve("hotbar_offhand_right.png");
            widgets.slice_and_save(
                hotbar_selector_width + offhand_width, 
                hotbar_height,
                offhand_width,
                offhand_height, 
                hotbar_offhand_right);
        }

        Path widgetFolder = spritesPath.resolve("widget");
        if (!widgetFolder.toFile().exists())
            widgetFolder.toFile().mkdirs();

        // Buttons
        JsonObject metadata = as_json(gson, "{\"gui\":{\"scaling\":{\"type\":\"nine_slice\",\"width\":200,\"height\":20,\"border\":3}}}");

        int button_width = 200;
        int button_height = 20;

        // Button Disabled
        Path button_disabled = widgetFolder.resolve("button_disabled.png");
        widgets.slice_and_save(
            0, 
            hotbar_height + hotbar_selector_height, 
            button_width, 
            button_height,
            button_disabled);

        Path button_disabled_mcmeta = widgetFolder.resolve("button_disabled.png.mcmeta");
        write_json(button_disabled_mcmeta, metadata);

        // Button Highlighted/Pressed
        Path button_highlighted = widgetFolder.resolve("button_highlighted.png");
        widgets.slice_and_save(
            0, 
            hotbar_height + hotbar_selector_height + button_height, 
            button_width,
            button_height,
            button_highlighted);

        Path button_highlighted_mcmeta = widgetFolder.resolve("button_highlighted.png.mcmeta");
        write_json(button_highlighted_mcmeta, metadata);

        // Button Default
        Path button = widgetFolder.resolve("button.png");
        widgets.slice_and_save(
            0, 
            hotbar_height + hotbar_selector_height + (button_height * 2),
            button_width,
            button_height,
            button);

        Path button_mcmeta = widgetFolder.resolve("button.png.mcmeta");
        write_json(button_mcmeta, metadata);

        // Sliders

        // Slider Default
        Path slider = widgetFolder.resolve("slider.png");
        widgets.slice_and_save(
            0, 
            hotbar_height + hotbar_selector_height, 
            button_width, 
            button_height, 
            slider);

        Path slider_mcmeta = widgetFolder.resolve("slider.png.mcmeta");
        write_json(slider_mcmeta, metadata);

        // Slider Handle
        Path slider_handle = widgetFolder.resolve("slider_handle.png");
        widgets.slice_and_save(
            0, 
            hotbar_height + hotbar_selector_height + button_height, 
            button_width,
            button_height,
            slider_handle);

        Path slider_handle_mcmeta = widgetFolder.resolve("slider_handle.png.mcmeta");
        write_json(slider_handle_mcmeta, metadata);

        // Slider Handle Highlighted
        Path slider_handle_highlighted = widgetFolder.resolve("slider_handle_highlighted.png");
        widgets.slice_and_save(
            0, hotbar_height + hotbar_selector_height + (button_height * 2),
            button_width,
            button_height,
            slider_handle_highlighted);

        Path slider_handle_highlighted_mcmeta = widgetFolder.resolve("slider_handle_highlighted.png.mcmeta");
        write_json(slider_handle_highlighted_mcmeta, metadata);
        
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

        if (pvp_update) {
            // Locked (Background)
            Path locked_button = widgetFolder.resolve("locked_button.png");
            widgets.slice_and_save(
                0,
                smaller_button_y_start + (smaller_button_height * 2),
                smaller_button_width,
                smaller_button_height, locked_button);

            // Unlocked (Background)
            Path unlocked_button = widgetFolder.resolve("unlocked_button.png");
            widgets.slice_and_save(
                smaller_button_width,
                smaller_button_y_start + (smaller_button_height * 2),
                smaller_button_width,
                smaller_button_height, 
                unlocked_button);

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
    }

    // Icons
    private void slice_icons(Path guiPath, Path hudPath) throws IOException {
        Path iconsPath = guiPath.resolve("icons.png");
        if (!iconsPath.toFile().exists()) 
            return;

        int dw = 256;
        int dh = 256;
        ImageConverter icons = new ImageConverter(dw, dh, iconsPath);
        if (!icons.isSquare()) {
            Logger.log("Failed to slice icons.png, image is not square.");
            return;
        }

        Gson gson = packConverter.getGson();

        // Crosshair
        int crosshair_width = 15;
        int crosshair_height = 15;
        Path crosshair = hudPath.resolve("crosshair.png");
        icons.slice_and_save(
            0, 
            0, 
            crosshair_width, 
            crosshair_height, 
            crosshair);

        // The Ping bars in the top left are unused

        // XP Bars               
        int xp_bar_width = 182;
        int xp_bar_height = 5;
        icons_slice_xp_bars(icons, hudPath, xp_bar_width, xp_bar_height);

        // Mapped Icons
        int icons_offset_x = crosshair_width + 1;
        icons_slice_mapped_icons(icons, hudPath, icons_offset_x);

        if (from >= Util.getVersionProtocol(gson, "1.9")) {
            int cooldown_start_y = (xp_bar_height * 6);
            icons_slice_cooldown_indicators(icons, hudPath, cooldown_start_y);
        }

        // Ping bars (Bottom)

        iconsPath.toFile().delete();
    }

    private void icons_slice_xp_bars(ImageConverter icons, Path hudPath, int xp_bar_width, int xp_bar_height) throws IOException {     
        // XP Bar Background
        Path experience_bar_background = hudPath.resolve("experience_bar_background.png");
        icons.slice_and_save(
            0, 
            64, 
            xp_bar_width, 
            xp_bar_height, 
            experience_bar_background);

        // XP Bar Progress
        Path experience_bar_progress = hudPath.resolve("experience_bar_progress.png");
        icons.slice_and_save(
            0, 
            64 + xp_bar_height, 
            xp_bar_width, 
            xp_bar_height, 
            experience_bar_progress);

        // Jump Bar Cooldown
        Path jump_bar_cooldown = hudPath.resolve("jump_bar_cooldown.png");
        icons.slice_and_save(
            0, 
            64 + (xp_bar_height * 2), 
            xp_bar_width, 
            xp_bar_height, 
            jump_bar_cooldown);

        // UNUSED: XP Bar Purple Background 

        // Jump Bar Background
        Path jump_bar_background = hudPath.resolve("jump_bar_background.png");
        icons.slice_and_save(
            0, 
            64 + (xp_bar_height * 4), 
            xp_bar_width, 
            xp_bar_height,
            jump_bar_background);

        // Jump Bar Progress
        Path jump_bar_progress = hudPath.resolve("jump_bar_progress.png");
        icons.slice_and_save(
            0, 
            64 + (xp_bar_height * 5), 
            xp_bar_width, 
            xp_bar_height, 
            jump_bar_progress);
    }

    private void icons_slice_cooldown_indicators(ImageConverter icons, Path hudPath, int cooldown_start_y) throws IOException {
        // Cooldown Indicator (Hotbar)
        int cooldown_indicator_hotbar_width = 18;
        int cooldown_indicator_hotbar_height = 18;
        
        // Background 
        Path hotbar_attack_indicator_background = hudPath.resolve("hotbar_attack_indicator_background.png");
        icons.slice_and_save(
            0, 
            64 + cooldown_start_y, 
            cooldown_indicator_hotbar_width,
            cooldown_indicator_hotbar_height, 
            hotbar_attack_indicator_background);

        // Progress
        Path hotbar_attack_indicator_progress = hudPath.resolve("hotbar_attack_indicator_progress.png");
        icons.slice_and_save(
            cooldown_indicator_hotbar_width, 
            64 + cooldown_start_y,
            cooldown_indicator_hotbar_width, 
            cooldown_indicator_hotbar_height,
            hotbar_attack_indicator_progress);

        // Cooldown Indicator (Crosshair)
        int cooldown_indicator_crosshair_width = 16;
        int cooldown_indicator_crosshair_height = 4;

        // Background
        Path crosshair_attack_indicator_background = hudPath.resolve("crosshair_attack_indicator_background.png");
        icons.slice_and_save(
            (cooldown_indicator_hotbar_width * 2), 
            64 + cooldown_start_y, 
            cooldown_indicator_crosshair_width, 
            cooldown_indicator_crosshair_height, 
            crosshair_attack_indicator_background);

        // Progress
        Path crosshair_attack_indicator_progress = hudPath.resolve("crosshair_attack_indicator_progress.png");
        icons.slice_and_save(
            (cooldown_indicator_hotbar_width * 3), 
            64 + cooldown_start_y, 
            cooldown_indicator_crosshair_width, 
            cooldown_indicator_crosshair_height, 
            crosshair_attack_indicator_progress);

        // Full
        Path crosshair_attack_indicator_full = hudPath.resolve("crosshair_attack_indicator_full.png");
        icons.slice_and_save(
            (cooldown_indicator_hotbar_width * 4), 
            64 + cooldown_start_y, 
            cooldown_indicator_crosshair_width, 
            16, 
            crosshair_attack_indicator_full);
    }

    private void icons_slice_mapped_icons(ImageConverter icons, Path hudPath, int icons_offset_x) throws IOException {
        int icon_rows = 20;
        int icon_cols = 6;
        int icon_size = 9;
        for (int x = 0; x < icon_rows; ++x) {
            for (int y = 0; y < icon_cols; ++y) {
                int id = y * 256 + x; // 256 is a hack, idk what else to do
                if (!ICONS.containsKey(id)) {
                    // if (PackConverter.DEBUG)
                    //     Logger.log("(x=" + x + ", y=" + y + ") Could not find icon with RPID=" + id);
                    continue;
                }

                String[] paths = ICONS.get(id);
                for (String path : paths) {
                    Path icon = hudPath.resolve(path);
                    if (!icon.getParent().toFile().exists()) 
                        icon.getParent().toFile().mkdirs();
                    if (PackConverter.DEBUG)
                        Logger.log("Icon: " + icon.getFileName());
                    int sx = icons_offset_x + (x * icon_size);
                    int sy = y * icon_size;
                    icons.slice_and_save(
                        sx, 
                        sy, 
                        icon_size, 
                        icon_size, 
                        icon);
                }
            }
        }
    }
    
    private JsonObject as_json(Gson gson, String raw) {
        return gson.fromJson(raw, JsonObject.class);
    }

    private void write_json(Path path, JsonObject object) throws IOException {
        Files.write(path, object.toString().getBytes());
    }
}