package com.agentdid127.resourcepack.forwards.impl.v1_15.textures;

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

public class EnchantConverter1_15 extends Converter {
    public EnchantConverter1_15(PackConverter packConverter) {
        super(packConverter);
    }

    @Override
    public boolean shouldConvert(Gson gson, int from, int to) {
        return from <= Util.getVersionProtocol(gson, "1.14.4") && to >= Util.getVersionProtocol(gson, "1.15");
    }

    @Override
    public void convert(Pack pack) throws IOException {
        Path itemGlintPath = pack.getWorkingPath().resolve("assets/minecraft/textures/misc/enchanted_item_glint.png".replace("/", File.separator));
        if (!itemGlintPath.toFile().exists()) {
            return;
        }

        ImageConverter imageConverter = new ImageConverter(64, 64, itemGlintPath);
        if (imageConverter.fileIsPowerOfTwo()) {
            imageConverter.colorizeGrayscale(new Color(128, 64, 204));
            imageConverter.store();
        }
    }
}
