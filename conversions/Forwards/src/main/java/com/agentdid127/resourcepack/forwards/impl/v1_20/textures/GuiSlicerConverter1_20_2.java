package com.agentdid127.resourcepack.forwards.impl.v1_20.textures;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.JsonUtil;
import com.agentdid127.resourcepack.library.utilities.Logger;
import com.agentdid127.resourcepack.library.utilities.Util;
import com.agentdid127.resourcepack.library.utilities.slicing.PredicateRunnable;
import com.agentdid127.resourcepack.library.utilities.slicing.Slice;
import com.agentdid127.resourcepack.library.utilities.slicing.Slicer;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class GuiSlicerConverter1_20_2 extends Converter {
    private final int from;

    public GuiSlicerConverter1_20_2(PackConverter converter, int from) {
        super(converter);
        this.from = from;
    }

    @Override
    public boolean shouldConvert(Gson gson, int from, int to) {
        return from <= Util.getVersionProtocol(gson, "1.20.1") && to >= Util.getVersionProtocol(gson, "1.20.2");
    }

    @Override
    public void convert(Pack pack) throws IOException {
        Path texturesPath = pack.getWorkingPath().resolve("assets/minecraft/textures".replace("/", File.separator));
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
            for (Slice slice : Slice.parseArray(array)) {
                Slicer.run(gson, slice, guiPath, new GuiPredicateRunnable(gson), from, true);
            }
            Logger.subTab();
        }
    }

    public static class GuiPredicateRunnable implements PredicateRunnable {
        private final Gson gson;

        public GuiPredicateRunnable(Gson gson) {
            this.gson = gson;
        }

        @Override
        public boolean run(int from, JsonObject predicate) {
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

                return from >= min_inclusive && from <= max_inclusive;
            }

            return true;
        }
    }
}