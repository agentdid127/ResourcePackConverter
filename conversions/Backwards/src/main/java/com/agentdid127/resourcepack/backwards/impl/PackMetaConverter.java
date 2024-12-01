package com.agentdid127.resourcepack.backwards.impl;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.JsonUtil;
import com.agentdid127.resourcepack.library.utilities.Util;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.nio.file.Path;

public class PackMetaConverter extends Converter {
    private int version;
    private int versionInt = 4;

    public PackMetaConverter(PackConverter packConverter, int versionIn) {
        super(packConverter);
        version = versionIn;
    }

    /**
     * Converts MCMeta to newer version
     *
     * @param pack
     * @throws IOException
     */
    @Override
    public void convert(Pack pack) throws IOException {
        Path file = pack.getWorkingPath().resolve("pack.mcmeta");
        if (!file.toFile().exists())
            return;

        JsonObject versionObj = Util.getVersionObjectByProtocol(packConverter.getGson(), version);
        if (versionObj != null) {
            versionInt = Integer.parseInt(versionObj.get("pack_format").getAsString());
        }

        JsonObject json = JsonUtil.readJson(packConverter.getGson(), file);
        {
            JsonObject meta = json.getAsJsonObject("meta");
            if (meta == null)
                meta = new JsonObject();
            meta.addProperty("game_version", Util.getVersionFromProtocol(packConverter.getGson(), version));
            json.add("meta", meta);
        }

        {
            JsonObject packObject = json.getAsJsonObject("pack");
            if (packObject == null)
                packObject = new JsonObject();
            packObject.addProperty("pack_format", versionInt);
            json.add("pack", packObject);
        }

        JsonUtil.writeJson(packConverter.getGson(), file, json);
    }
}
