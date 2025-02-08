package com.agentdid127.resourcepack.forwards.impl;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.JsonUtil;
import com.agentdid127.resourcepack.library.utilities.Logger;
import com.agentdid127.resourcepack.library.utilities.PropertiesEx;
import com.agentdid127.resourcepack.library.utilities.Util;
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

    /**
     * Parent conversion for MCPatcher
     *
     * @param pack
     * @throws IOException
     */
    @Override
    public void convert(Pack pack) throws IOException {
        Path models = pack.getWorkingPath().resolve("assets/minecraft/optifine".replace("/", File.separator));
        if (models.toFile().exists()) {
            Logger.addTab();
            findFiles(models);
            Logger.subTab();
        }
        // remapModelJson(models.resolve("item"));
        // remapModelJson(models.resolve("block"));
    }

    /**
     * Finds all files in the specified path
     *
     * @param path
     * @throws IOException
     */
    protected void findFiles(Path path) throws IOException {
        File directory = path.toFile();
        for (File file : Objects.requireNonNull(directory.listFiles())) {
            if (!file.isDirectory()) {
                remapProperties(file.toPath());
            } else {
                findFiles(file.toPath());
            }
        }
    }

    /**
     * Remaps properties to work in newer versions
     *
     * @param path
     * @throws IOException
     */
    protected void remapProperties(Path path) throws IOException {
        if (path.toFile().exists()) {
            try (Stream<Path> pathStream = Files.list(path).filter(path1 -> path1.toString().endsWith(".properties"))) {
                pathStream.forEach(model -> {
                    Path inputStreamPath = Paths.get(model.toString());
                    try (InputStream input = Files.newInputStream(inputStreamPath)) {
                        Logger.debug("Updating:" + model.getFileName());

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
                });
            }
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
