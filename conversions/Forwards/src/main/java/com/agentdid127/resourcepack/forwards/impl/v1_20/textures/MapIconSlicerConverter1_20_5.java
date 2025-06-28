package com.agentdid127.resourcepack.forwards.impl.v1_20.textures;

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

public class MapIconSlicerConverter1_20_5 extends Converter {
    private final int from;

    public MapIconSlicerConverter1_20_5(PackConverter packConverter, int from) {
        super(packConverter);
        this.from = from;
    }

    @Override
    public boolean shouldConvert(Gson gson, int from, int to) {
        return from <= Util.getVersionProtocol(gson, "1.20.4") && to >= Util.getVersionProtocol(gson, "1.20.5");
    }

    @Override
    public void convert(Pack pack) throws IOException {
        Path texturesPath = pack.getWorkingPath().resolve("assets/minecraft/textures".replace("/", File.separator));
        Path mapIconsPath = texturesPath.resolve("map/".replace("/", File.separator));
        if (!mapIconsPath.toFile().exists()) {
            return;
        }

        Gson gson = packConverter.getGson();
        JsonObject mapIconsJson = JsonUtil.readJsonResource(gson, "/forwards/map_icons.json", JsonObject.class);
        if (mapIconsJson != null) {
            Slicer.run(gson, Slice.parse(mapIconsJson), mapIconsPath, new GuiSlicerConverter1_20_2.GuiPredicateRunnable(gson), from, false);
        }
    }
}
