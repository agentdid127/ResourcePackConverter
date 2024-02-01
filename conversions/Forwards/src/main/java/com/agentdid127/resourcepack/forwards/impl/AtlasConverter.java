package com.agentdid127.resourcepack.forwards.impl;

import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.RPConverter;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class AtlasConverter extends RPConverter {
    JsonObject out = new JsonObject();
    JsonArray sources = new JsonArray();

    public AtlasConverter(PackConverter packConverter) {
        super(packConverter, "AtlasConverter", 1);
    }

    @Override
    public void convert() throws IOException {
        Path atlases = pack.getWorkingPath()
                .resolve("assets" + File.separator + "minecraft" + File.separator + "atlases");
        Path textures = pack.getWorkingPath()
                .resolve("assets" + File.separator + "minecraft" + File.separator + "textures");

        if (!atlases.toFile().exists())
            atlases.toFile().mkdirs();

        if (textures.toFile().exists()) {
            File[] files = textures.toFile().listFiles();
            for (File file : files) {
                JsonObject source = new JsonObject();
                if (file.isDirectory()) {
                    source.addProperty("type", "directory");
                    source.addProperty("source", file.getName());
                    source.addProperty("prefix", file.getName() + "/");
                    sources.add(source);
                    findFiles(textures.resolve(file.getName()), file.getName());
                } else if (file.getName().endsWith(".png")) {
                    source.addProperty("type", "single");
                    source.addProperty("resource", file.getName().replaceAll("\\.png", ""));
                    sources.add(source);
                }
            }
            out.add("sources", sources);
            File output = atlases.resolve("blocks.json").toFile();
            if (!output.exists()) {
                OutputStream os = new FileOutputStream(output);
                os.write(packConverter.getGson().toJson(out).getBytes(StandardCharsets.UTF_8));
                os.close();
            }
        }
    }

    public void findFiles(Path directory, String prefix) {
        if (directory.toFile().isDirectory())
            for (File file : directory.toFile().listFiles()) {
                JsonObject source = new JsonObject();
                if (file.isDirectory()) {
                    source.addProperty("type", "directory");
                    source.addProperty("source", prefix + "/" + file.getName());
                    source.addProperty("prefix", prefix + "/" + file.getName() + "/");
                    sources.add(source);
                    String nextPrefix = prefix + "/" + file.getName();
                    findFiles(directory.resolve(file.getName()), nextPrefix);
                }
            }
    }
}
