package com.agentdid127.resourcepack.backwards.impl.textures;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.ImageConverter;
import com.agentdid127.resourcepack.library.utilities.Util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class CompassConverter extends Converter {
    private int to;
    private Path items;

    public CompassConverter(PackConverter packConverter, int to) {
        super(packConverter);
        this.to = to;
    }

    @Override
    public void convert(Pack pack) throws IOException {
        String itemsT = "items";
        if (to > Util.getVersionProtocol(packConverter.getGson(), "1.13"))
            itemsT = "item";
        Path compassPath = pack.getWorkingPath().resolve("assets" + File.separator + "minecraft" + File.separator
                + "textures" + File.separator + itemsT + File.separator + "compass.png");
        items = compassPath.getParent();
        if (compassPath.toFile().exists()) {
            ImageConverter imageConverter = new ImageConverter(16, 16 * 32, compassPath);
            if (!imageConverter.fileIsPowerOfTwo())
                return;

            for (int i = 0; i < 32; i++) {
                int h = i * 16;
                String it = String.valueOf(i);
                if (i < 10)
                    it = "0" + it;
                imageConverter.newImage(16, 16);
                imageConverter.subImage(0, h, 16, h + 16);
                imageConverter.store(items.resolve(it + ".png"));
            }

            if (items.resolve("compass.png.mcmeta").toFile().exists()) {
                items.resolve("compass.png.mcmeta").toFile().delete();
            }
            compassPath.toFile().delete();
        }
    }
}
