package technology.agentdid127.resourcepack.impl;

import technology.agentdid127.resourcepack.Converter;
import technology.agentdid127.resourcepack.PackConverter;
import technology.agentdid127.resourcepack.extra.ImageConverter;
import technology.agentdid127.resourcepack.pack.Pack;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class ParticleConverter extends Converter {

    public ParticleConverter(PackConverter packConverter) {
        super(packConverter);
    }

    /**
     * Updates Particles
     * @param pack
     * @throws IOException
     */
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

