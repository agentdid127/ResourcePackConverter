package com.agentdid127.resourcepack.forwards.impl.textures;

import com.agentdid127.resourcepack.forwards.impl.textures.slicing.Slice;
import com.agentdid127.resourcepack.forwards.impl.textures.slicing.Slicer;
import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.JsonUtil;
import com.agentdid127.resourcepack.library.utilities.Logger;
import com.agentdid127.resourcepack.library.utilities.Util;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class SlicerConverter extends Converter {
    private final int from;

    public SlicerConverter(PackConverter converter, int from) {
        super(converter);
        this.from = from;
    }

    @Override
    public void convert(Pack pack) throws IOException {
        Path texturesPath = pack.getWorkingPath().resolve("assets/minecraft/textures".replace("/", File.separator));
        if (!texturesPath.toFile().exists()) {
            return;
        }

        Path guiPath = texturesPath.resolve("gui");
        if (!guiPath.toFile().exists()) {
            return; // No need to do any slicing.
        }

        Path spritesPath = guiPath.resolve("sprites");
        if (!spritesPath.toFile().exists()) {
            spritesPath.toFile().mkdirs();
        }

        Gson gson = packConverter.getGson();
        JsonArray array = JsonUtil.readJsonResource(gson, "/forwards/gui.json", JsonArray.class);
        if (array != null) {
            Logger.addTab();
            Slice[] slices = Slice.parseArray(array);
            for (Slice slice : slices) {
                Slicer.runSlicer(gson, slice, guiPath, PredicateRunnable.class, from, true);
            }
            Logger.subTab();
        }
    }

    public static class PredicateRunnable {
        public static boolean run(Gson gson, int from, JsonObject predicate) {
            if (predicate.has("protocol")) {
                JsonObject protocol = predicate.getAsJsonObject("protocol");

                Integer min_inclusive = null;
                Integer max_inclusive = null;

                JsonElement min_inclusive_prim = protocol.get("min_inclusive");
                if (min_inclusive_prim.isJsonPrimitive() && min_inclusive_prim.getAsJsonPrimitive().isNumber()) {
                    min_inclusive = min_inclusive_prim.getAsInt();
                }

                if (min_inclusive == null) {
                    min_inclusive = 4; // hardcoded bruh
                }

                JsonElement max_inclusive_prim = protocol.get("max_inclusive");
                if (max_inclusive_prim.isJsonPrimitive() && max_inclusive_prim.getAsJsonPrimitive().isNumber()) {
                    max_inclusive = max_inclusive_prim.getAsInt();
                }

                if (max_inclusive == null) {
                    max_inclusive = Util.getLatestProtocol(gson);
                }

                if (from < min_inclusive || from > max_inclusive) {
                    return false;
                }
            }

            return true;
        }
    }
}