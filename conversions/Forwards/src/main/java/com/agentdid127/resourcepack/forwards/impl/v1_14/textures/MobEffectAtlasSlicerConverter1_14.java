package com.agentdid127.resourcepack.forwards.impl.v1_14.textures;

import com.agentdid127.resourcepack.forwards.impl.v1_20.textures.GuiSlicerConverter1_20_2;
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

public class MobEffectAtlasSlicerConverter1_14 extends Converter {
    private final int from;

    public MobEffectAtlasSlicerConverter1_14(PackConverter packConverter, int from) {
        super(packConverter);
        this.from = from;
    }

    @Override
    public boolean shouldConvert(Gson gson, int from, int to) {
        return from <= Util.getVersionProtocol(gson, "1.13.2") && to >= Util.getVersionProtocol(gson, "1.14");
    }

    /**
     * Slices the mob effect images from inventory.png for 1.14+
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

        Path inventoryPath = texturesPath.resolve("gui/container/inventory.png".replace("/", File.separator));
        if (!inventoryPath.toFile().exists()) {
            return;
        }

        Gson gson = packConverter.getGson();
        JsonObject effectJson = JsonUtil.readJsonResource(gson, "/forwards/mob_effect.json", JsonObject.class);
        if (effectJson != null) {
            Slice slice = Slice.parse(effectJson);
            Slicer.runSlicer(gson, slice, texturesPath, new GuiSlicerConverter1_20_2.GuiPredicateRunnable(), from, false);
        }
    }
}