package com.agentdid127.resourcepack.forwards.impl;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.JsonUtil;
import com.agentdid127.resourcepack.library.utilities.Util;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.nio.file.Path;

// Reference: https://minecraft.wiki/w/Pack_format
public class PackMetaConverter extends Converter {
    private int to;
    private int from;

    public PackMetaConverter(PackConverter packConverter, int from, int to) {
        super(packConverter);
        this.from = from;
        this.to = to;
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

        int versionInt = 4;
        JsonObject versionObj = Util.getVersionObjectByProtocol(packConverter.getGson(), to);
        if (versionObj != null) {
            versionInt = Integer.parseInt(versionObj.get("pack_format").getAsString());
        }

        JsonObject json = JsonUtil.readJson(packConverter.getGson(), file);

        if (json == null) {
            json = new JsonObject();
        }

        {
            JsonObject meta = json.getAsJsonObject("meta");
            if (meta == null)
                meta = new JsonObject();
            meta.addProperty("game_version", Util.getVersionFromProtocol(packConverter.getGson(), to));
            json.add("meta", meta);
        }

        {
            JsonObject packObject = json.getAsJsonObject("pack");
            if (packObject == null)
                packObject = new JsonObject();
            packObject.addProperty("pack_format", versionInt);

            if (from < Util.getVersionProtocol(packConverter.getGson(), "1.20.2")
                    && to >= Util.getVersionProtocol(packConverter.getGson(), "1.20.2")) {
                JsonObject fromVersion = Util.getVersionObjectByProtocol(packConverter.getGson(), from);
                JsonObject toVersion = Util.getVersionObjectByProtocol(packConverter.getGson(), to);

                JsonObject supportedFormats = new JsonObject();
                supportedFormats.addProperty("min_inclusive", fromVersion != null ? Integer.parseInt(fromVersion.get("pack_format").getAsString()) : versionInt);
                supportedFormats.addProperty("max_inclusive", toVersion != null ? Integer.parseInt(toVersion.get("pack_format").getAsString()) : versionInt);
                packObject.add("supported_formats", supportedFormats);
            }

            json.add("pack", packObject);
        }

        JsonUtil.writeJson(packConverter.getGson(), file, json);
    }
}
