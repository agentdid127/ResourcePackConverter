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
        // Possible TODO: Make this JSON? Possibly use protocol.json, but update it.
        if (version >= Util.getVersionProtocol(packConverter.getGson(), "1.21"))
            versionInt = 34;
        else if (version >= Util.getVersionProtocol(packConverter.getGson(), "1.20.5")
                && version < Util.getVersionProtocol(packConverter.getGson(), "1.21"))
            versionInt = 32;
        else if (version >= Util.getVersionProtocol(packConverter.getGson(), "1.20.3")
                && version < Util.getVersionProtocol(packConverter.getGson(), "1.20.5"))
            versionInt = 22;
        else if (version >= Util.getVersionProtocol(packConverter.getGson(), "1.20.2")
                && version < Util.getVersionProtocol(packConverter.getGson(), "1.20.3"))
            versionInt = 18;
        else if (version >= Util.getVersionProtocol(packConverter.getGson(), "1.20")
                && version < Util.getVersionProtocol(packConverter.getGson(), "1.20.2"))
            versionInt = 15;
        else if (version >= Util.getVersionProtocol(packConverter.getGson(), "1.19.4")
                && version < Util.getVersionProtocol(packConverter.getGson(), "1.20"))
            versionInt = 13;
        else if (version >= Util.getVersionProtocol(packConverter.getGson(), "1.19.3")
                && version < Util.getVersionProtocol(packConverter.getGson(), "1.19.4"))
            versionInt = 12;
        else if (version >= Util.getVersionProtocol(packConverter.getGson(), "1.19")
                && version < Util.getVersionProtocol(packConverter.getGson(), "1.19.3"))
            versionInt = 9;
        else if (version >= Util.getVersionProtocol(packConverter.getGson(), "1.18")
                && version < Util.getVersionProtocol(packConverter.getGson(), "1.19"))
            versionInt = 8;
        else if (version >= Util.getVersionProtocol(packConverter.getGson(), "1.17")
                && version < Util.getVersionProtocol(packConverter.getGson(), "1.18"))
            versionInt = 7;
        else if (version >= Util.getVersionProtocol(packConverter.getGson(), "1.16.2")
                && version < Util.getVersionProtocol(packConverter.getGson(), "1.17"))
            versionInt = 6;
        else if (version >= Util.getVersionProtocol(packConverter.getGson(), "1.15")
                && version < Util.getVersionProtocol(packConverter.getGson(), "1.16.2"))
            versionInt = 5;
        else if (version >= Util.getVersionProtocol(packConverter.getGson(), "1.13")
                && version < Util.getVersionProtocol(packConverter.getGson(), "1.15"))
            versionInt = 4;
        else if (version >= Util.getVersionProtocol(packConverter.getGson(), "1.11")
                && version < Util.getVersionProtocol(packConverter.getGson(), "1.13"))
            versionInt = 3;
        else if (version >= Util.getVersionProtocol(packConverter.getGson(), "1.9")
                && version < Util.getVersionProtocol(packConverter.getGson(), "1.11"))
            versionInt = 2;
        else if (version >= Util.getVersionProtocol(packConverter.getGson(), "1.7.2")
                && version < Util.getVersionProtocol(packConverter.getGson(), "1.9"))
            versionInt = 1;
        else
            versionInt = 0;

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
