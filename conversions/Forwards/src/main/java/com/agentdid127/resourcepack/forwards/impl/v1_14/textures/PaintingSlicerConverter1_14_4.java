package com.agentdid127.resourcepack.forwards.impl.v1_14.textures;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.JsonUtil;
import com.agentdid127.resourcepack.library.utilities.Util;
import com.agentdid127.resourcepack.library.utilities.slicing.Slice;
import com.agentdid127.resourcepack.library.utilities.slicing.Slicer;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class PaintingSlicerConverter1_14_4 extends Converter {
    public PaintingSlicerConverter1_14_4(PackConverter packConverter) {
        super(packConverter);
    }

    @Override
    public boolean shouldConvert(Gson gson, int from, int to) {
        return from <= Util.getVersionProtocol(gson, "1.13") && to >= Util.getVersionProtocol(gson, "1.14.4");
    }

    /**
     * Slices the paintings_kristoffer_zetterstrand painting image into multiple for
     * 1.14+
     *
     * @param pack
     * @throws IOException
     */
    @Override
    public void convert(Pack pack) throws IOException {
        Path texturesPath = pack.getWorkingPath().resolve("assets/minecraft/textures".replace("/", File.separator));
        Path paintingPath = texturesPath.resolve("painting");
        if (!paintingPath.toFile().exists()) {
            return;
        }

        final Gson gson = this.packConverter.getGson();
        final JsonObject effectJson = JsonUtil.readJsonResource(gson, "/forwards/paintings_kristoffer_zetterstrand.json", JsonObject.class);
        if (effectJson != null) {
            Slicer.run(gson, Slice.parse(effectJson), paintingPath, (from, predicate) -> true, -1, false);
        }
    }
}