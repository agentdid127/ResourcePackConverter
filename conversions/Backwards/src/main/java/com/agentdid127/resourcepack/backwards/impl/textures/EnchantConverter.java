package com.agentdid127.resourcepack.backwards.impl.textures;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.ImageConverter;
import com.agentdid127.resourcepack.library.utilities.Util;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class EnchantConverter extends Converter {
    public EnchantConverter(PackConverter packConverter) {
        super(packConverter);
    }

    @Override
    public boolean shouldConvert(Gson gson, int from, int to) {
        return from >= Util.getVersionProtocol(gson, "1.15") && to <= Util.getVersionProtocol(gson, "1.14.4");
    }

    @Override
    public void convert(Pack pack) throws IOException {
        Path paintingPath = pack.getWorkingPath().resolve("assets/minecraft/textures/misc/enchanted_item_glint.png".replace("/", File.separator));
        if (paintingPath.toFile().exists()) {
            ImageConverter imageConverter = new ImageConverter(64, 64, paintingPath);
            if (imageConverter.fileIsPowerOfTwo()) {
                imageConverter.newImage(64, 64);
                imageConverter.grayscale();
                imageConverter.store();
            }
        }
    }
}
