package com.agentdid127.resourcepack.forwards.impl.other.textures;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.ImageConverter;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class InventoryConverter extends Converter {
    public InventoryConverter(PackConverter packConverter) {
        super(packConverter);
    }

    @Override
    public boolean shouldConvert(Gson gson, int from, int to) {
        return true;
    }

    // TODO: Figure out what this is for/make ^ correct range
    @Override
    public void convert(Pack pack) throws IOException {
        Path imagePath = pack.getWorkingPath().resolve("assets/minecraft/textures/gui/container/inventory.png".replace("/", File.separator));
        if (imagePath.toFile().exists()) {
            int defaultW = 256, defaultH = 256;
            ImageConverter image = new ImageConverter(defaultW, defaultH, imagePath);
            image.newImage(defaultH, defaultW);
            image.subImage(0, 0, 256, 256, 0, 0);
            image.subImage(0, 166, 16, 198, 0, 198);
            image.subImage(104, 166, 120, 198, 16, 198);
            image.store();
        }
    }
}
