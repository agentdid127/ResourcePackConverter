package com.agentdid127.resourcepack.forwards.impl.textures;

import com.agentdid127.resourcepack.forwards.impl.textures.slicing.Slice;
import com.agentdid127.resourcepack.forwards.impl.textures.slicing.Slicer;
import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class ParticleTextureConverter extends Converter {
    private final int from;
    private final int to;

    public ParticleTextureConverter(PackConverter packConverter, int from, int to) {
        super(packConverter);
        this.from = from;
        this.to = to;
    }

    @Override
    public boolean shouldConvert(Gson gson, int from, int to) {
        return true;
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
        if (!texturesPath.toFile().exists()) {
            return;
        }

        Path particlePath = texturesPath.resolve("particle");
        if (!particlePath.toFile().exists()) {
            return;
        }

        Logger.addTab();

        Gson gson = packConverter.getGson();
        JsonObject particlesJson = JsonUtil.readJsonResource(gson, "/forwards/particles.json", JsonObject.class);
        assert particlesJson != null;

        Slice slice = Slice.parse(particlesJson);
        if (to >= Util.getVersionProtocol(gson, "1.13")) {
            if (from <= Util.getVersionProtocol(gson, "1.12.2")) {
                Path particlesPath = particlePath.resolve(slice.getPath());
                if (particlesPath.toFile().exists()) {
                    Logger.debug("Detected 'particles.png' from versions before 1.13, converting to newer size..");
                    ImageConverter converter = new ImageConverter(slice.getWidth(), slice.getHeight(), particlesPath);
                    converter.newImage(256, 256);
                    converter.subImage(0, 0, 128, 128, 0, 0);
                    converter.store();
                }
            }

            slice.setWidth(256);
            slice.setHeight(256);
        }

        if (from <= Util.getVersionProtocol(gson, "1.13.2") &&
                to >= Util.getVersionProtocol(gson, "1.14")) {
            Slicer.runSlicer(gson, slice, particlePath, SlicerConverter.PredicateRunnable.class, from, false);
            Path entityPath = texturesPath.resolve("entity");
            Path newFishingHookPath = entityPath.resolve("fishing_hook.png");
            if (newFishingHookPath.toFile().exists()) {
                newFishingHookPath.toFile().delete();
            }
            FileUtil.moveIfExists(particlePath.resolve("fishing_hook.png"), newFishingHookPath);
        }

        Logger.subTab();
    }
}