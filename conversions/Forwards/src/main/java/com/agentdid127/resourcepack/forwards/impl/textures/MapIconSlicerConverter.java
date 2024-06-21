package com.agentdid127.resourcepack.forwards.impl.textures;

import com.agentdid127.resourcepack.forwards.impl.textures.slicing.Slice;
import com.agentdid127.resourcepack.forwards.impl.textures.slicing.Slicer;
import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.JsonUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class MapIconSlicerConverter extends Converter {
    private final int protocolFrom;

    public MapIconSlicerConverter(PackConverter packConverter, int protocolFrom) {
        super(packConverter);
        this.protocolFrom = protocolFrom;
    }

    /**
     * Converts maps
     * 
     * @param pack
     * @throws IOException
     */
    @Override
    public void convert(Pack pack) throws IOException {
        Path texturesPath = pack.getWorkingPath().resolve("assets/minecraft/textures".replace("/", File.separator));
        if (!texturesPath.toFile().exists())
            return;
        Path mapIconsPath = texturesPath.resolve("map/".replace("/", File.separator));
        if (!mapIconsPath.toFile().exists())
            return;
        Gson gson = packConverter.getGson();
        JsonObject mapIconsJson = JsonUtil.readJsonResource(gson, "/forwards/map_icons.json", JsonObject.class);
        assert mapIconsJson != null;
        Slice slice = Slice.parse(mapIconsJson);
        Slicer.runSlicer(gson, slice, mapIconsPath, SlicerConverter.PredicateRunnable.class, protocolFrom, false);
    }
}
