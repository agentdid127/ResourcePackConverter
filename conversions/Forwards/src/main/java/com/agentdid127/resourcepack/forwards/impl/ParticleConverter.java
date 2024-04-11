package com.agentdid127.resourcepack.forwards.impl;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ParticleConverter extends Converter {
    // Set it up.
    public ParticleConverter(PackConverter packConverter) {
        super(packConverter);
    }

    @Override
    public void convert(Pack pack) throws IOException {
        Path particlesFolderPath = pack.getWorkingPath()
                .resolve("assets/minecraft/particles".replace("/", File.separator));
        if (!particlesFolderPath.toFile().exists())
            return;

        // Check if the two merged files exist.
        boolean barrier = false;
        boolean light = false;

        if (particlesFolderPath.resolve("barrier.json").toFile().exists())
            barrier = true;
        else if (particlesFolderPath.resolve("light.json").toFile().exists())
            light = true;

        // Move around files depending on what exists or what doesn't exist.
        if (barrier) {
            Files.move(particlesFolderPath.resolve("barrier.json"), particlesFolderPath.resolve("block_marker.json"));
            if (light)
                Files.delete(particlesFolderPath.resolve("light.json"));
        } else if (light) {
            Files.move(particlesFolderPath.resolve("light.json"), particlesFolderPath.resolve("block_marker.json"));
        }
    }
}
