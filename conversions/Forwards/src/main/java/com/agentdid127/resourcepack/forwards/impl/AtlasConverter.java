package com.agentdid127.resourcepack.forwards.impl;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.Util;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class AtlasConverter extends Converter {

    public AtlasConverter(PackConverter packConverter) {
        super(packConverter);
    }

    @Override
    public void convert(Pack pack) throws IOException {
        Path atlases = pack.getWorkingPath().resolve("assets" + File.separator + "minecraft" + File.separator + "atlases");
        Path textures = pack.getWorkingPath().resolve("assets" + File.separator + "minecraft" + File.separator + "textures");

        if (!atlases.toFile().exists()) atlases.toFile().mkdirs();

        if (textures.toFile().exists()) {
            JsonObject out = new JsonObject();
            JsonArray sources = new JsonArray();
            File[] files = textures.toFile().listFiles();
            for (File file : files) {
                JsonObject source = new JsonObject();
                if (file.isDirectory() && !(file.getName().equals("block") || file.getName().equals("item"))) {
                    source.addProperty("type", "directory");
                    source.addProperty("source", file.getName());
                    source.addProperty("prefix", "");
                    sources.add(source);
                }
                else if (file.getName().endsWith(".png")) {
                    source.addProperty("type", "single");
                    source.addProperty("resource", file.getName().replaceAll("\\.png", ""));
                    sources.add(source);
                }
            }
            out.add("sources", sources);
            File output = atlases.resolve("pack_atlases.json").toFile();
            if (!output.exists()) {
                OutputStream os = new FileOutputStream(output);
                os.write(packConverter.getGson().toJson(out).getBytes(StandardCharsets.UTF_8));
                os.close();
            }
        }
    }
}
