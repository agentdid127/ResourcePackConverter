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

        // Possible TODO: Make this JSON? Possibly use protocol.json, but update it.
        if (to >= Util.getVersionProtocol(packConverter.getGson(), "1.20.4"))
            versionInt = 22;
        else if (to >= Util.getVersionProtocol(packConverter.getGson(), "1.20.2")
                && to < Util.getVersionProtocol(packConverter.getGson(), "1.20.3"))
            versionInt = 18;
        else if (to >= Util.getVersionProtocol(packConverter.getGson(), "1.20")
                && to < Util.getVersionProtocol(packConverter.getGson(), "1.20.2"))
            versionInt = 15;
        else if (to >= Util.getVersionProtocol(packConverter.getGson(), "1.19.4")
                && to < Util.getVersionProtocol(packConverter.getGson(), "1.20"))
            versionInt = 13;
        else if (to >= Util.getVersionProtocol(packConverter.getGson(), "1.19.3")
                && to < Util.getVersionProtocol(packConverter.getGson(), "1.19.4"))
            versionInt = 12;
        else if (to >= Util.getVersionProtocol(packConverter.getGson(), "1.19")
                && to < Util.getVersionProtocol(packConverter.getGson(), "1.19.3"))
            versionInt = 9;
        else if (to >= Util.getVersionProtocol(packConverter.getGson(), "1.18")
                && to < Util.getVersionProtocol(packConverter.getGson(), "1.19"))
            versionInt = 8;
        else if (to >= Util.getVersionProtocol(packConverter.getGson(), "1.17")
                && to < Util.getVersionProtocol(packConverter.getGson(), "1.18"))
            versionInt = 7;
        else if (to >= Util.getVersionProtocol(packConverter.getGson(), "1.16.2")
                && to < Util.getVersionProtocol(packConverter.getGson(), "1.17"))
            versionInt = 6;
        else if (to >= Util.getVersionProtocol(packConverter.getGson(), "1.15")
                && to < Util.getVersionProtocol(packConverter.getGson(), "1.16.2"))
            versionInt = 5;
        else if (to >= Util.getVersionProtocol(packConverter.getGson(), "1.13")
                && to < Util.getVersionProtocol(packConverter.getGson(), "1.15"))
            versionInt = 4;
        else if (to >= Util.getVersionProtocol(packConverter.getGson(), "1.11")
                && to < Util.getVersionProtocol(packConverter.getGson(), "1.13"))
            versionInt = 3;
        else if (to >= Util.getVersionProtocol(packConverter.getGson(), "1.9")
                && to < Util.getVersionProtocol(packConverter.getGson(), "1.11"))
            versionInt = 2;
        else if (to >= Util.getVersionProtocol(packConverter.getGson(), "1.7.2")
                && to < Util.getVersionProtocol(packConverter.getGson(), "1.9"))
            versionInt = 1;
        else
            versionInt = 0;

        JsonObject json = Util.readJson(packConverter.getGson(), file);
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
                JsonObject supportedFormats = new JsonObject();
                supportedFormats.addProperty("min_inclusive", versionInt);
                supportedFormats.addProperty("max_inclusive", versionInt); // TODO: A better way to do this
                packObject.add("supported_formats", supportedFormats);
            }

            json.add("pack", packObject);
        }

        JsonUtil.writeJson(packConverter.getGson(), file, json);
    }
}
