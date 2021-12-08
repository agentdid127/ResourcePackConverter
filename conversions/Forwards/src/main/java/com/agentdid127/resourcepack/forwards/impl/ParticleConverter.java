package com.agentdid127.resourcepack.forwards.impl;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ParticleConverter extends Converter {

    private Path particles;

    //Set it up.
    public ParticleConverter(PackConverter packConverter) {
        super(packConverter);
    }

    @Override
    public void convert(Pack pack) throws IOException {

        //The directory to convert
        particles = pack.getWorkingPath().resolve("assets" + File.separator + "minecraft" + File.separator + "particles");

        //Check if the two merged files exist.
        boolean barrier = false;
        boolean light = false;

        if (particles.resolve("barrier.json").toFile().exists())
            barrier = true;
        else if (particles.resolve("light.json").toFile().exists())
            light = true;

        //Move around files depending on what exists or what doesn't exist.
        if (barrier) {
            Files.move(particles.resolve("barrier.json"), particles.resolve("block_marker.json"));
            if (light) Files.delete(particles.resolve("light.json"));
        }
        else if (light) {
            Files.move(particles.resolve("light.json"), particles.resolve("block_marker.json"));
        }

    }
}
