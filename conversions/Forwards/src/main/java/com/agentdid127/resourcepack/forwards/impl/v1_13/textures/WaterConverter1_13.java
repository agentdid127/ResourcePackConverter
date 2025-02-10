package com.agentdid127.resourcepack.forwards.impl.v1_13.textures;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.ImageConverter;
import com.agentdid127.resourcepack.library.utilities.Util;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class WaterConverter1_13 extends Converter {
    public WaterConverter1_13(PackConverter packConverter) {
        super(packConverter);
    }

    @Override
    public boolean shouldConvert(Gson gson, int from, int to) {
        return from <= Util.getVersionProtocol(gson, "1.12.2") && to >= Util.getVersionProtocol(gson, "1.13");
    }

    @Override
    public void convert(Pack pack) throws IOException {
        Path blockFolder = pack.getWorkingPath().resolve("assets/minecraft/textures/block".replace("/", File.separator));
        if (blockFolder.toFile().exists()) {
            grayscale(blockFolder.resolve("water_flow.png"), 16, 1024);
            grayscale(blockFolder.resolve("water_still.png"), 16, 512);
            grayscale(blockFolder.resolve("water_overlay.png"), 16, 16);
        }
    }

    private void grayscale(Path path, int w, int h) throws IOException {
        if (path.toFile().exists()) {
            ImageConverter imageConverter = new ImageConverter(w, h, path);
            if (imageConverter.fileIsPowerOfTwo()) {
                imageConverter.newImage(w, h);
                imageConverter.grayscale();
                imageConverter.store();
            }
        }
    }
}
