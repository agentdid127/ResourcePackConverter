package technology.agentdid127.resourcepack.impl;

import com.google.gson.JsonObject;
import technology.agentdid127.resourcepack.Converter;
import technology.agentdid127.resourcepack.PackConverter;
import technology.agentdid127.resourcepack.Util;
import technology.agentdid127.resourcepack.pack.Pack;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

public class PackMetaConverter extends Converter {

    private String version = "";
    private int versionInt = 4;
    public PackMetaConverter(PackConverter packConverter, String versionIn) {
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

        switch(version) {
            case "1.16":
                versionInt = 6;
                break;
            case "1.15":
                versionInt = 5;
                break;
            case "1.14":
            case "1.13":
                versionInt = 4;
                break;
            default:
                versionInt = 3;
        }

        JsonObject json = Util.readJson(packConverter.getGson(), file);
        {
            JsonObject meta = json.getAsJsonObject("meta");
            if (meta == null) meta = new JsonObject();
            meta.addProperty("game_version", version);
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