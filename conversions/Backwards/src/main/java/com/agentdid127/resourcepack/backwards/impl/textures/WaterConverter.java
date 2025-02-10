package com.agentdid127.resourcepack.backwards.impl.textures;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.ImageConverter;
import com.agentdid127.resourcepack.library.utilities.Util;
import com.google.gson.Gson;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class WaterConverter extends Converter {
    public WaterConverter(PackConverter packConverter) {
        super(packConverter);
    }

    @Override
    public boolean shouldConvert(Gson gson, int from, int to) {
        return from >= Util.getVersionProtocol(gson, "1.13") && to <= Util.getVersionProtocol(gson, "1.12.2");
    }

    @Override
    public void convert(Pack pack) throws IOException {
        Path blocksFolder = pack.getWorkingPath().resolve("assets/minecraft/textures/blocks".replace("/", File.separator));
        if (blocksFolder.toFile().exists()) {
            colorize(blocksFolder.resolve("water_flow.png"), 32, 1024);
            colorize(blocksFolder.resolve("water_still.png"), 32, 1024);
            colorize(blocksFolder.resolve("water_overlay.png"), 32, 32);
        }
    }

    private void colorize(Path path, int w, int h) throws IOException {
        if (path.toFile().exists()) {
            ImageConverter imageConverter = new ImageConverter(w, h, path);
            if (imageConverter.fileIsPowerOfTwo()) {
                imageConverter.newImage(w, h);
                imageConverter.colorize(new Color(45, 63, 244, 170));
                imageConverter.store();
            }
        }
    }
}
