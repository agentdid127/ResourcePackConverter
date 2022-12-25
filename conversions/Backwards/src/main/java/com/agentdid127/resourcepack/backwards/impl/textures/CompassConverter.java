package com.agentdid127.resourcepack.backwards.impl.textures;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.Util;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.ImageConverter;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

public class CompassConverter extends Converter {
    private int from, to;
    private Path items;
    public CompassConverter(PackConverter packConverter, int from, int to) {
        super(packConverter);
        this.from = from;
        this.to = to;
    }

    @Override
    public void convert(Pack pack) throws IOException {
        String itemsT = "items";
        if (to > Util.getVersionProtocol(packConverter.getGson(), "1.13")) itemsT = "item";
        Path compassPath = pack.getWorkingPath().resolve("assets" + File.separator + "minecraft" + File.separator + "textures" + File.separator + itemsT + File.separator + "compass_00.png");
        items = compassPath.getParent();
        if (compassPath.toFile().exists()) {
            ImageConverter imageConverter = new ImageConverter(16, 512, compassPath);
            imageConverter.newImage(16, 16 * 32);
            if (!imageConverter.fileIsPowerOfTwo()) return;

            for (int i = 0; i < 32; i++) {
                int h = i * 16;
                String it = String.valueOf(i);
                if (i < 10) it = "0" + it;
                imageConverter.addImage(items.resolve(it + ".png"), 0, h);
                if (items.resolve(it + ".png").toFile().exists()) items.resolve(it + ".png").toFile().delete();
            }
            imageConverter.store(items.resolve("compass.png"));

            JsonObject meta = new JsonObject();
            meta.add("animation", new JsonObject());
            Files.write(items.resolve("compass.png.mcmeta"), Collections.singleton(packConverter.getGson().toJson(meta)), Charset.forName("UTF-8"));
        }
    }

    private void compass(int x, int y, String name, ImageConverter imageConverter) throws IOException {

    }
}
