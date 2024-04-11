package com.agentdid127.resourcepack.forwards.impl.textures;

import com.agentdid127.resourcepack.forwards.impl.textures.slicing.Slice;
import com.agentdid127.resourcepack.forwards.impl.textures.slicing.Slicer;
import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.FileUtil;
import com.agentdid127.resourcepack.library.utilities.Util;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class ParticleTextureConverter extends Converter {
    int from, to;

    public ParticleTextureConverter(PackConverter packConverter, int from, int to) {
        super(packConverter);
        this.from = from;
        this.to = to;
    }

    /**
     * Slice particles.png into multiple for 1.14+
     * 
     * @param pack
     * @throws IOException
     */
    @Override
    public void convert(Pack pack) throws IOException {
        Path texturesPath = pack.getWorkingPath().resolve("assets/minecraft/textures".replace("/", File.separator));
        if (!texturesPath.toFile().exists())
            return;
        Path particlePath = texturesPath.resolve("particle");
        if (!particlePath.toFile().exists())
            return;
        Gson gson = packConverter.getGson();
        JsonObject particlesJson = Util.readJsonResource(gson, "/forwards/particles.json",
                JsonObject.class);
        Slice slice = Slice.parse(particlesJson);
        Slicer.runSlicer(gson, slice, particlePath, SlicerConverter.PredicateRunnable.class, from, false);
        Path entityPath = texturesPath.resolve("entity");
        FileUtil.moveIfExists(particlePath.resolve("fishing_hook.png"), entityPath.resolve("fishing_hook.png"));
    }
}