package com.agentdid127.resourcepack.backwards.impl;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.Util;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ParticleConverter extends Converter {
    public ParticleConverter(PackConverter packConverter) {
        super(packConverter);
    }

    @Override
    public boolean shouldConvert(Gson gson, int from, int to) {
        return from >= Util.getVersionProtocol(gson, "1.18") && to <= Util.getVersionProtocol(gson, "1.17.1");
    }

    @Override
    public void convert(Pack pack) throws IOException {
        Path particles = pack.getWorkingPath().resolve("assets/minecraft/particles".replace("/", File.separator));
        if (particles.toFile().exists()) {
            Path blockMarkerPath = particles.resolve("block_marker.json");
            if (blockMarkerPath.toFile().exists()) {
                Files.move(blockMarkerPath, particles.resolve("light.json"));
            }
        }
    }
}
