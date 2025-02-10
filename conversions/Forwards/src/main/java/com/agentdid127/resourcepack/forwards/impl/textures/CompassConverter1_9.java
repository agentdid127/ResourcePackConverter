package com.agentdid127.resourcepack.forwards.impl.textures;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.ImageConverter;
import com.agentdid127.resourcepack.library.utilities.Util;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CompassConverter1_9 extends Converter {
    private final int to;
    private Path items;

    public CompassConverter1_9(PackConverter packConverter, int to) {
        super(packConverter);
        this.to = to;
    }

    @Override
    public boolean shouldConvert(Gson gson, int from, int to) {
        return from <= Util.getVersionProtocol(gson, "1.8.9") && to >= Util.getVersionProtocol(gson, "1.9");
    }

    @Override
    public void convert(Pack pack) throws IOException {
        String itemsT = "items";
        if (to > Util.getVersionProtocol(packConverter.getGson(), "1.13"))
            itemsT = "item";
        Path compassPath = pack.getWorkingPath()
                .resolve(("assets/minecraft/textures/" + itemsT + "/compass.png").replace("/", File.separator));
        items = compassPath.getParent();
        if (compassPath.toFile().exists()) {
            ImageConverter imageConverter = new ImageConverter(16, 512, compassPath);
            if (!imageConverter.fileIsPowerOfTwo())
                return;

            for (int i = 0; i < 32; i++) {
                int h = i * 16;
                String it = String.valueOf(i);
                if (i < 10)
                    it = "0" + it;
                compass(0, h, 16, h + 16, "compass_" + it, imageConverter);
            }

            if (items.resolve("compass.png.mcmeta").toFile().exists())
                Files.delete(items.resolve("compass.png.mcmeta"));
        }
    }

    private void compass(int x, int y, int x2, int y2, String name, ImageConverter imageConverter) throws IOException {
        imageConverter.newImage(16, 16);
        imageConverter.subImage(x, y, x2, y2, 0, 0);
        imageConverter.store(items.resolve(name + ".png"));
    }
}
