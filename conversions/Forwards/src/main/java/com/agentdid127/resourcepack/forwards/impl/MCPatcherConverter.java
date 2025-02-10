package com.agentdid127.resourcepack.forwards.impl;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.JsonUtil;
import com.agentdid127.resourcepack.library.utilities.Logger;
import com.agentdid127.resourcepack.library.utilities.PropertiesEx;
import com.agentdid127.resourcepack.library.utilities.Util;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

@Deprecated // will be removed when extensions are made
public class MCPatcherConverter extends Converter {
    public MCPatcherConverter(PackConverter packConverter) {
        super(packConverter);
    }

    @Override
    public boolean shouldConvert(Gson gson, int from, int to) {
        return from <= Util.getVersionProtocol(gson, "1.12.2") && to >= Util.getVersionProtocol(gson, "1.13");
    }

    /**
     * Parent conversion for MCPatcher
     *
     * @param pack
     * @throws IOException
     */
    @Override
    public void convert(Pack pack) throws IOException {
        Path optifinePath = pack.getWorkingPath().resolve("assets/minecraft/optifine".replace("/", File.separator));
        if (optifinePath.toFile().exists()) {
            Logger.addTab();
            findFiles(optifinePath);
            Logger.subTab();
        }
        // remapModelJson(models.resolve("item"));
        // remapModelJson(models.resolve("block"));
    }

    /**
     * Finds all files in the specified rootPath
     *
     * @param rootPath
     * @throws IOException
     */
    protected void findFiles(Path rootPath) throws IOException {
        File directory = rootPath.toFile();
        for (File file : Objects.requireNonNull(directory.listFiles())) {
            Path filePath = file.toPath();
            if (!file.isDirectory()) {
                remapProperties(filePath);
            } else {
                remapPropertiesFolder(filePath);
            }
        }
    }

    protected void remapPropertiesFolder(Path rootPath) throws IOException {
        if (rootPath.toFile().exists()) {
            try (Stream<Path> pathStream = Files.list(rootPath).filter(path1 -> path1.toString().endsWith(".properties"))) {
                pathStream.forEach(propertyPath -> {
                    try {
                        this.remapProperties(propertyPath);
                    } catch (IOException e) {
                        Util.propagate(e);
                    }
                });
            }
        }
    }

    /**
     * Remaps properties to work in newer versions
     *
     * @param rootPath
     * @param propertyPath
     * @throws IOException
     */
    protected void remapProperties(Path propertyPath) throws IOException {
        if (!propertyPath.toFile().exists()) {
            return;
        }

        Path inputStreamPath = Paths.get(propertyPath.toString());
        try (InputStream input = Files.newInputStream(inputStreamPath)) {
            Logger.debug("Updating:" + propertyPath.getFileName());

            PropertiesEx prop = new PropertiesEx();
            prop.load(input);

            try (OutputStream output = Files.newOutputStream(inputStreamPath)) {
                // updates textures
                if (prop.containsKey("texture")) {
                    prop.setProperty("texture", replaceTextures(prop));
                }

                // Updates Item IDs
                if (prop.containsKey("matchItems")) {
                    prop.setProperty("matchItems", updateID("matchItems", prop, "regular").replaceAll("\"", ""));
                }

                if (prop.containsKey("items")) {
                    prop.setProperty("items", updateID("items", prop, "regular").replaceAll("\"", ""));
                }

                if (prop.containsKey("matchBlocks")) {
                    prop.setProperty("matchBlocks", updateID("matchBlocks", prop, "regular").replaceAll("\"", ""));
                }

                // Saves File
                prop.store(output, "");
            } catch (IOException io) {
                io.printStackTrace();
            }
        } catch (IOException e) {
            throw Util.propagate(e);
        }
    }

    /**
     * Replaces texture paths with blocks and items
     *
     * @param prop
     * @return
     */
    protected String replaceTextures(PropertiesEx prop) {
        NameConverter nameConverter = packConverter.getConverter(NameConverter.class);
        String properties = prop.getProperty("texture");
        if (properties.startsWith("textures/blocks/")) {
            properties = "textures/block/" + nameConverter.getBlockMapping_1_13();
        } else if (properties.startsWith("textures/items/")) {
            properties = "textures/item/" + nameConverter.getItemMapping_1_13();
        }
        return properties;
    }

    /**
     * Fixes item IDs and switches them from a numerical id to minecraft: something
     *
     * @param type
     * @param prop
     * @return
     */
    protected String updateID(String type, PropertiesEx prop, String selection) {
        JsonObject id = JsonUtil.readJsonResource(packConverter.getGson(), "/forwards/ids.json").get(selection).getAsJsonObject();
        String[] split = prop.getProperty(type).split(" ");
        StringBuilder properties2 = new StringBuilder(" ");
        for (int i = 0; i < split.length; i++) {
            if (id.get(split[i]) != null) {
                split[i] = "minecraft:" + id.get(split[i]).getAsString();
            }
        }
        Arrays.stream(split).forEach(item -> properties2.append(item).append(" "));
        // TODO/NOTE: Might of broken this? Originally returned properties2.toString() & ignored the line below
        String output = properties2.substring(0, properties2.length() - 1);
        prop.remove("metadata");
        return output;
    }
}
