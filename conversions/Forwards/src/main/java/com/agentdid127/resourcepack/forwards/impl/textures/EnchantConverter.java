package com.agentdid127.resourcepack.forwards.impl.textures;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.ImageConverter;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class EnchantConverter extends Converter {
    public EnchantConverter(PackConverter packConverter) {
        super(packConverter);
    }

    @Override
    public void convert(Pack pack) throws IOException {
        Path itemGlintPath = pack.getWorkingPath()
                .resolve("assets/minecraft/textures/misc/enchanted_item_glint.png".replace("/", File.separator));
        if (!itemGlintPath.toFile().exists())
            return;
        ImageConverter imageConverter = new ImageConverter(64, 64, itemGlintPath);
        if (!imageConverter.fileIsPowerOfTwo())
            return;
        imageConverter.newImage(64, 64);
        imageConverter.colorize(new Color(94, 9, 178, 120));
        imageConverter.store();
    }
}
