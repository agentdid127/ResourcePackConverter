package net.hypixel.resourcepack.impl;

import net.hypixel.resourcepack.Converter;
import net.hypixel.resourcepack.PackConverter;
import net.hypixel.resourcepack.extra.ImageConverter;
import net.hypixel.resourcepack.pack.Pack;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class ParticleConverter extends Converter {

    public ParticleConverter(PackConverter packConverter) {
        super(packConverter);
    }

    @Override
    public void convert(Pack pack) throws IOException {
        Path imagePath = pack.getWorkingPath().resolve("assets" + File.separator + "minecraft" + File.separator + "textures" + File.separator + "particle" + File.separator + "particles.png");
        if (!imagePath.toFile().exists()) return;

        int defaultW = 128, defaultH = 128;
        ImageConverter iconvert = new ImageConverter(defaultW, defaultH, imagePath);

        // TODO check how higher resolution will handle this.

            // make a new bigger image and just paste the existing on in the top left corner
            iconvert.newImage(iconvert.getWidth()*2, iconvert.getHeight()*2);
            iconvert.subImage(0,0,128,128,0,0);
            iconvert.store();
        }
    }

