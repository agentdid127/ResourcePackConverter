package com.agentdid127.resourcepack.forwards.impl.v1_19.other;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.Util;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class AtlasConverter1_19_3 extends Converter {
    JsonObject out = new JsonObject();
    JsonArray sources = new JsonArray();

    public AtlasConverter1_19_3(PackConverter packConverter) {
        super(packConverter);
    }

    @Override
    public boolean shouldConvert(Gson gson, int from, int to) {
        return from <= Util.getVersionProtocol(gson, "1.19.2") && to >= Util.getVersionProtocol(gson, "1.19.3");
    }

    @Override
    public void convert(Pack pack) throws IOException {
        Path atlasesPath = pack.getWorkingPath().resolve("assets/minecraft/atlases".replace("/", File.separator));
        if (!atlasesPath.toFile().exists()) {
            atlasesPath.toFile().mkdirs();
        }

        Path texturesPath = pack.getWorkingPath().resolve("assets/minecraft/textures".replace("/", File.separator));
        File texturesFile = texturesPath.toFile();
        if (texturesFile.exists()) {
            for (File file : Objects.requireNonNull(texturesFile.listFiles())) {
                JsonObject source = new JsonObject();
                if (file.isDirectory()) {
                    source.addProperty("type", "directory");
                    source.addProperty("source", file.getName());
                    source.addProperty("prefix", file.getName() + "/");
                    sources.add(source);
                    findFiles(texturesPath.resolve(file.getName()), file.getName());
                } else if (file.getName().endsWith(".png")) {
                    source.addProperty("type", "single");
                    source.addProperty("resource", file.getName().replaceAll("\\.png", ""));
                    sources.add(source);
                }
            }

            out.add("sources", sources);

            File output = atlasesPath.resolve("blocks.json").toFile();
            if (!output.exists()) {
                OutputStream os = Files.newOutputStream(output.toPath());
                os.write(packConverter.getGson().toJson(out).getBytes(StandardCharsets.UTF_8));
                os.close();
            }
        }
    }

    public void findFiles(Path directory, String prefix) {
        File directoryFile = directory.toFile();
        if (directoryFile.isDirectory()) {
            for (File file : Objects.requireNonNull(directoryFile.listFiles())) {
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
}
