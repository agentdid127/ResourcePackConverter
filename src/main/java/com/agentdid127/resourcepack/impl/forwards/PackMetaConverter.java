package com.agentdid127.resourcepack.impl.forwards;

import com.agentdid127.resourcepack.PackConverter;
import com.google.gson.JsonObject;
import com.agentdid127.resourcepack.Converter;
import com.agentdid127.resourcepack.Util;
import com.agentdid127.resourcepack.pack.Pack;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

public class PackMetaConverter extends Converter {

    private int version;
    private int versionInt = 4;
    public PackMetaConverter(PackConverter packConverter, int versionIn) {
        super(packConverter);
        version = versionIn;
    }


    /**
     * Converts MCMeta to newer version
     * @param pack
     * @throws IOException
     */
    @Override
    public void convert(Pack pack) throws IOException {
        Path file = pack.getWorkingPath().resolve("pack.mcmeta");
        if (!file.toFile().exists()) return;

        if (version >= Util.getVersionProtocol(packConverter.getGson(), "1.17")) versionInt = 7;
        else if (version >= Util.getVersionProtocol(packConverter.getGson(), "1.16.2") && version <= Util.getVersionProtocol(packConverter.getGson(), "1.16.5")) versionInt = 6;
        else if (version >= Util.getVersionProtocol(packConverter.getGson(), "1.15") && version < Util.getVersionProtocol(packConverter.getGson(), "1.16.2")) versionInt = 5;
        else if (version >= Util.getVersionProtocol(packConverter.getGson(), "1.13") && version < Util.getVersionProtocol(packConverter.getGson(), "1.15")) versionInt = 4;
        else if (version >= Util.getVersionProtocol(packConverter.getGson(), "1.11") && version < Util.getVersionProtocol(packConverter.getGson(), "1.13")) versionInt = 3;
        else if (version >= Util.getVersionProtocol(packConverter.getGson(), "1.9") && version < Util.getVersionProtocol(packConverter.getGson(), "1.11")) versionInt = 2;
        else if (version >= Util.getVersionProtocol(packConverter.getGson(), "1.8") && version < Util.getVersionProtocol(packConverter.getGson(), "1.9")) versionInt = 1;
        else versionInt = 0;


        JsonObject json = Util.readJson(packConverter.getGson(), file);
        {
            JsonObject meta = json.getAsJsonObject("meta");
            if (meta == null) meta = new JsonObject();
            meta.addProperty("game_version", Util.getVersionFromProtocol(packConverter.getGson(), version));
            json.add("meta", meta);
        }
        {
            JsonObject packObject = json.getAsJsonObject("pack");
            if (packObject == null) packObject = new JsonObject();
            packObject.addProperty("pack_format", versionInt);
            json.add("pack", packObject);
        }

        Files.write(file, Collections.singleton(packConverter.getGson().toJson(json)), Charset.forName("UTF-8"));
    }
}