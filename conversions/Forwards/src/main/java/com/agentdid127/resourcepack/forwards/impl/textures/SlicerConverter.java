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

        Path hudPath = spritesPath.resolve("hud");

        Path widgetsPath = guiPath.resolve("widgets.png");
        Path iconsPath = guiPath.resolve("widgets.png");
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
                    widgets.slice_and_save(hotbar_selector_width + (offhand_width * 2), hotbar_height, offhand_width,
                            offhand_height, offhandRight);
                }

                // TODO: slice widgets.png
                Logger.log("TODO: widgets.png slicing");

                widgetsPath.toFile().delete();
            } else
                Logger.log("Failed to slice widgets.png, image is not square.");
        }

        if (iconsPath.toFile().exists()) {
            int dw = 256;
            int dh = 256;
            ImageConverter icons = new ImageConverter(dw, dh, iconsPath);
            if (icons.isSquare()) {
                // TODO: slice icons.png
                Logger.log("TODO: icons.png slicing");

                iconsPath.toFile().delete();
            } else
                Logger.log("Failed to slice icons.png, image is not square.");
        }

        // TODO: other slicing for stuff
        // - containers
    }

    private void slice(ImageConverter image, int sx, int sy, int ex, int ey, int ow, int oh, Path outputPath)
            throws IOException {
        image.newImage(ow, oh);
        image.subImage(sx, sy, ex, ey);
        image.store(outputPath);
    }

    private void slice(ImageConverter image, int sx, int sy, int ex, int ey, Path outputPath)
            throws IOException {
        slice(image, sx, sy, ex, ey, ex, ey, outputPath);
    }
}