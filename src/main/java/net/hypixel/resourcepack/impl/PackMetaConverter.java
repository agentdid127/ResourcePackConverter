package net.hypixel.resourcepack.impl;

import com.google.gson.JsonObject;
import net.hypixel.resourcepack.Converter;
import net.hypixel.resourcepack.PackConverter;
import net.hypixel.resourcepack.Util;
import net.hypixel.resourcepack.pack.Pack;

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

    @Override
    public void convert(Pack pack) throws IOException {
        Path file = pack.getWorkingPath().resolve("pack.mcmeta");
        if (!file.toFile().exists()) return;

        if (version.equals("1.15")) versionInt = 5;
        else if (version.equals("1.13") || version.equals("1.14")) versionInt = 4;

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