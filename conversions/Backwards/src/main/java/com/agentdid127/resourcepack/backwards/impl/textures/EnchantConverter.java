package com.agentdid127.resourcepack.backwards.impl.textures;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.Util;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.ImageConverter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class EnchantConverter extends Converter {

    public EnchantConverter(PackConverter packConverter) {
        super(packConverter);
    }

    @Override
    public void convert(Pack pack) throws IOException {
        Path paintingPath = pack.getWorkingPath().resolve("assets" + File.separator + "minecraft" + File.separator + "textures" + File.separator + "misc" + File.separator + "enchanted_item_glint.png");
        if (!paintingPath.toFile().exists()) return;

        ImageConverter imageConverter = new ImageConverter(64, 64, paintingPath);
        if (!imageConverter.fileIsPowerOfTwo()) return;
        imageConverter.newImage(64, 64);
        imageConverter.grayscale();
        imageConverter.store();
    }
}
