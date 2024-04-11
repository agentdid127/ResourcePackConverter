package com.agentdid127.resourcepack.forwards.impl;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.Logger;
import com.agentdid127.resourcepack.library.utilities.PropertiesEx;
import com.agentdid127.resourcepack.library.utilities.Util;
import com.google.gson.JsonObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

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
        Path models = pack.getWorkingPath()
                .resolve("assets" + File.separator + "minecraft" + File.separator + "optifine");
        if (models.toFile().exists())
            findFiles(models);
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
        for (File file : directory.listFiles()) {
            if (!file.isDirectory())
                continue;
            remapProperties(file.toPath());
            findFiles(file.toPath());
        }
    }

    /**
     * Remaps properties to work in newer versions
     * 
     * @param path
     * @throws IOException
     */
    protected void remapProperties(Path path) throws IOException {
        if (!path.toFile().exists())
            return;

        Files.list(path).filter(path1 -> path1.toString().endsWith(".properties")).forEach(model -> {
            try (InputStream input = new FileInputStream(model.toString())) {
                if (PackConverter.DEBUG)
                    Logger.log("Updating:" + model.getFileName());

                PropertiesEx prop = new PropertiesEx();
                prop.load(input);

                try (OutputStream output = new FileOutputStream(model.toString())) {
                    // updates textures
                    if (prop.containsKey("texture"))
                        prop.setProperty("texture", replaceTextures(prop));

                    // Updates Item IDs
                    if (prop.containsKey("matchItems"))
                        prop.setProperty("matchItems", updateID("matchItems", prop, "regular").replaceAll("\"", ""));

                    if (prop.containsKey("items"))
                        prop.setProperty("items", updateID("items", prop, "regular").replaceAll("\"", ""));

                    if (prop.containsKey("matchBlocks"))
                        prop.setProperty("matchBlocks", updateID("matchBlocks", prop, "regular").replaceAll("\"", ""));

                    // Saves File
                    prop.store(output, "");
                    output.close();
                } catch (IOException io) {
                    io.printStackTrace();
                }
            } catch (IOException e) {
                throw Util.propagate(e);
            }
        });
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
        if (properties.startsWith("textures/blocks/"))
            properties = "textures/block/" + nameConverter.getBlockMapping();
        else if (properties.startsWith("textures/items/"))
            properties = "textures/item/" + nameConverter.getItemMapping();
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
        JsonObject id = Util.readJsonResource(packConverter.getGson(), "/forwards/ids.json").get(selection)
                .getAsJsonObject();
        String[] split = prop.getProperty(type).split(" ");
        String properties2 = " ";
        for (int i = 0; i < split.length; i++)
            if (id.get(split[i]) != null)
                split[i] = "minecraft:" + id.get(split[i]).getAsString();
        for (String item : split)
            properties2 += item + " ";
        properties2.substring(0, properties2.length() - 1);
        if (prop.containsKey("metadata"))
            prop.remove("metadata");
        return properties2;
    }
}
