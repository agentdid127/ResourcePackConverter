package net.hypixel.resourcepack.impl;

import net.hypixel.resourcepack.Converter;
import net.hypixel.resourcepack.Options;
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

        //Particles
        int defaultW = 128, defaultH = 128;
        ImageConverter iconvert = new ImageConverter(defaultW, defaultH, imagePath);
            iconvert.newImage(iconvert.getWidth()*2, iconvert.getHeight()*2);
            iconvert.subImage(0,0,128,128,0,0);
            iconvert.store();
        }
    }

