package com.agentdid127.resourcepack.forwards.impl;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.Util;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NameConverter extends Converter {


    protected int to;
    protected int from;
    protected final Mapping blockMapping = new BlockMapping13();
    protected final Mapping newBlockMapping = new BlockMapping14();
    protected final Mapping blockMapping17 = new BlockMapping17();
    protected final Mapping blockMapping19 = new BlockMapping19();
    protected final Mapping itemMapping = new ItemMapping13();
    protected final Mapping newItemMapping = new ItemMapping14();
    protected final Mapping itemMapping17 = new ItemMapping17();
    protected final Mapping entityMapping = new EntityMapping();
    protected final Mapping langMapping = new LangMapping();
    protected final Mapping langMapping14 = new LangMapping14();
    public NameConverter(PackConverter packConverter, int from, int to) {
        super(packConverter);
        this.from = from;
        this.to = to;
    }

    /**
     * Fixes folder names and file names
     * @param pack Resource Pack
     * @throws IOException Error handler
     */
    @Override
    public void convert(Pack pack) throws IOException {
        Path mc = pack.getWorkingPath().resolve("assets" + File.separator + "minecraft");
        //Less than 1.12
        if (from <= Util.getVersionProtocol(packConverter.getGson(), "1.12.2") && to > Util.getVersionProtocol(packConverter.getGson(), "1.13")) {
            if (packConverter.DEBUG) PackConverter.log("Finding files that are less than 1.12");
            findFiles(mc);
        }

        //Version is greater than 1.13
        if (to >= Util.getVersionProtocol(packConverter.getGson(), "1.13")) {
            //OptiFine conversion
            if (mc.resolve("mcpatcher").toFile().exists()) {
                if (packConverter.DEBUG) PackConverter.log("MCPatcher exists, switching to optifine");
                if (mc.resolve("optifine").toFile().exists()) {
                    if (packConverter.DEBUG) PackConverter.log("OptiFine exists, merging directories");
                    Util.mergeDirectories(mc.resolve("optifine").toFile(), mc.resolve("mcpatcher").toFile());
                } else Files.move(mc.resolve("mcpatcher"), mc.resolve("optifine"));
                if (mc.resolve("mcpatcher").toFile().exists()) Util.deleteDirectoryAndContents(mc.resolve("mcpatcher"));
            }

            //1.13 Models
            Path models = pack.getWorkingPath().resolve("assets" + File.separator + "minecraft" + File.separator + "models");
            if (models.toFile().exists()) {
                //1.13 block/item name change
                if (models.resolve("blocks").toFile().exists()) {
                    if (models.resolve("block").toFile().exists()) Util.deleteDirectoryAndContents( models.resolve("block"));
                    Files.move(models.resolve("blocks"), models.resolve("block"));
                }
                //Update all blocks for 1.13
                renameAll(blockMapping, ".json", models.resolve("block"));
                if (models.resolve("items").toFile().exists()) {
                    if (models.resolve("item").toFile().exists()) Util.deleteDirectoryAndContents(models.resolve("item"));
                    Files.move(models.resolve("items"), models.resolve("item"));
                }
                //Update all items for 1.13
                renameAll(itemMapping, ".json", models.resolve("item"));

                //Update 1.14 items
                if (to > Util.getVersionProtocol(packConverter.getGson(), "1.13")) {
                    renameAll(newItemMapping, ".json", models.resolve("item"));
                }

                if (to > Util.getVersionProtocol(packConverter.getGson(), "1.19")) {
                    renameAll(blockMapping19, ".json", models.resolve("block"));
                }
            }

            //Update BlockStates
            Path blockStates = pack.getWorkingPath().resolve("assets" + File.separator + "minecraft" + File.separator + "blockstates");

            if (blockStates.toFile().exists()) renameAll(blockMapping, ".json", blockStates);


            //Update textures
            Path textures = pack.getWorkingPath().resolve("assets" + File.separator + "minecraft" + File.separator + "textures");
            if (textures.toFile().exists()) {
                if (textures.resolve("blocks").toFile().exists())
                    Files.move(textures.resolve("blocks"), textures.resolve("block"));
                if (from < Util.getVersionProtocol(packConverter.getGson(), "1.13")) {
                    renameAll(blockMapping, ".png", textures.resolve("block"));
                    renameAll(blockMapping, ".png.mcmeta", textures.resolve("block"));
                }
                if (to > Util.getVersionProtocol(packConverter.getGson(), "1.13")) {
                    renameAll(newBlockMapping, ".png", textures.resolve("block"));
                    renameAll(newBlockMapping, ".png.mcmeta", textures.resolve("block"));
                }
                if (from < Util.getVersionProtocol(packConverter.getGson(), "1.13")) {
                    renameAll(itemMapping, ".png", textures.resolve("item"));
                    renameAll(itemMapping, ".png.mcmeta", textures.resolve("item"));
                }
                if (to > Util.getVersionProtocol(packConverter.getGson(), "1.13.2")) {
                    renameAll(newItemMapping, ".png", textures.resolve("item"));
                    renameAll(newItemMapping, ".png.mcmeta", textures.resolve("item"));
                }
                //1.16 Iron golems
                if (from < Util.getVersionProtocol(packConverter.getGson(), "1.15") && to >= Util.getVersionProtocol(packConverter.getGson(), "1.15")) {
                    if (!textures.resolve("entity" + File.separator + "iron_golem").toFile().exists())
                        textures.resolve("entity" + File.separator + "iron_golem" + File.separator).toFile().mkdir();
                    if (textures.resolve("entity" + File.separator + "iron_golem.png").toFile().exists())
                        Files.move(textures.resolve("entity" + File.separator + "iron_golem.png"), textures.resolve("entity" + File.separator + "iron_golem" + File.separator + "iron_golem.png"));
                }
                //1.17 Squid
                if (to >= Util.getVersionProtocol(packConverter.getGson(), "1.17") && from < Util.getVersionProtocol(packConverter.getGson(), "1.17")) {
                    renameAll(blockMapping17, ".png", textures.resolve("block"));
                    renameAll(itemMapping17, ".png", textures.resolve("item"));
                    renameAll(blockMapping17, ".png", models.resolve("block"));
                    renameAll(itemMapping17, ".png", models.resolve("item"));
                    if (!textures.resolve("entity" + File.separator + "squid").toFile().exists())
                        textures.resolve("entity" + File.separator + "squid" + File.separator).toFile().mkdir();
                    if (textures.resolve("entity" + File.separator + "squid.png").toFile().exists())
                        Files.move(textures.resolve("entity" + File.separator + "squid.png"), textures.resolve("entity" + File.separator + "squid" + File.separator + "squid.png"));
                }

                if (to >= Util.getVersionProtocol(packConverter.getGson(), "1.19") && from < Util.getVersionProtocol(packConverter.getGson(),"1.19")) {
                    renameAll(blockMapping19, ".png", textures.resolve("block"));
                }

                //1.13 End Crystals
                if (textures.resolve("entity" + File.separator + "endercrystal").toFile().exists())
                    Files.move(textures.resolve("entity" + File.separator + "endercrystal"), textures.resolve("entity" + File.separator + "end_crystal"));
                findEntityFiles(textures.resolve("entity"));
            }
        }

    }

    /**
     * Finds files in entity folder
     * @param path
     * @throws IOException
     */
    protected void findEntityFiles(Path path) throws IOException {
        if (path.toFile().exists()) {
            File directory = new File(path.toString());
            File[] fList = directory.listFiles();
            for (File file : fList) {
                if (file.isDirectory()) {
                    renameAll(entityMapping, ".png", Paths.get(file.getPath()));
                    renameAll(entityMapping, ".png.mcmeta", Paths.get(file.getPath()));
                    findEntityFiles(Paths.get(file.getPath()));

                }
            }
        }
    }

    /**
     * Finds files in folders called
     * @param path
     * @throws IOException
     */
    protected void findFiles(Path path) throws IOException {
        if (path.toFile().exists()) {
            File directory = new File(path.toString());
            File[] fList = directory.listFiles();
            for (File file : fList) {
                if (file.isDirectory()) {

                    if (file.getName().equals("items")) {
                        if (packConverter.DEBUG) packConverter.log("Found Items folder, renaming");
                        Util.renameFile(path.resolve(file.getName()), file.getName().replaceAll("items", "item"));
                    }
                    if (file.getName().equals("blocks")) {
                        if (packConverter.DEBUG) packConverter.log("Found blocks folder, renaming");
                        Util.renameFile(path.resolve(file.getName()), file.getName().replaceAll("blocks", "block"));
                    }

                    findFiles(Paths.get(file.getPath()));
                }
                if (file.getName().contains("(")) Util.renameFile(path.resolve(file.getName()), file.getName().replaceAll("[()]", ""));
                if (!file.getName().equals(file.getName().toLowerCase()))
                    if (packConverter.DEBUG) packConverter.log("Renamed: " + file.getName() + "->" +file.getName().toLowerCase());
                Util.renameFile(path.resolve(file.getName()), file.getName().toLowerCase());
            }
        }
    }

    /**
     * Renames folder
     * @param mapping
     * @param extension
     * @param path
     * @throws IOException
     */
    protected void renameAll(Mapping mapping, String extension, Path path) throws IOException {
        if (path.toFile().exists()) {
            // remap grass blocks in order due to the cyclical way their names have changed,
            // i.e grass -> grass_block, tall_grass -> grass, double_grass -> tall_grass
            List<String> grasses = Arrays.asList("grass", "tall_grass", "double_grass");
            if (from <= Util.getVersionProtocol(packConverter.getGson(), "1.12.2")  && (path.endsWith("blockstates") || path.endsWith("textures" + File.separator + "block"))) {
                grasses.stream().forEach(name -> {
                    String newName = mapping.remap(name);
                    Boolean ret = Util.renameFile(Paths.get(path + File.separator + name + extension), newName + extension);
                    if (ret == null) return;
                    if (ret && packConverter.DEBUG) {
                        packConverter.log("      Renamed: " + name + extension + "->" + newName + extension);
                    } else if (!ret) {
                        System.err.println("      Failed to rename: " + name + extension + "->" + newName + extension);
                    }
                });
            }
            //remap snow jsons, but not images.
            if (from < Util.getVersionProtocol(packConverter.getGson(), "1.13") && to >= Util.getVersionProtocol(packConverter.getGson(), "1.13")) {
                if (path.resolve("snow.json").toFile().exists()) Util.renameFile(path.resolve("snow" + extension), "snow_block" + extension);
                if (path.resolve("snow_layer.json").toFile().exists()) Util.renameFile(path.resolve("snow_layer" + extension), "snow" + extension);
            }
            Files.list(path).forEach(path1 -> {
                if (!path1.toString().endsWith(extension)) return;

                String baseName = path1.getFileName().toString().substring(0, path1.getFileName().toString().length() - extension.length());
                // skip the already renamed grass blocks
                if (grasses.contains(baseName) && (path.endsWith("blockstates") || path.endsWith("textures" + File.separator + "block"))) {
                    return;
                }
                String newName = mapping.remap(baseName);
                if (newName != null && !newName.equals(baseName)) {
                    Boolean ret = Util.renameFile(path1, newName + extension);
                    if (ret == null) return;
                    if (ret && packConverter.DEBUG) {
                        packConverter.log("      Renamed: " + path1.getFileName().toString() + "->" + newName + extension);
                    } else if (!ret) {
                        System.err.println("      Failed to rename: " + path1.getFileName().toString() + "->" + newName + extension);
                    }
                }
            });
        }
    }

    public Mapping getBlockMapping() {
        return blockMapping;
    }


    public Mapping getItemMapping() {
        return itemMapping;
    }

    public Mapping getNewBlockMapping() {
        return newBlockMapping;
    }

    public Mapping getNewItemMapping() {
        return newItemMapping;
    }

    public Mapping getItemMapping17() {return itemMapping17;}

    public Mapping getBlockMapping17() {return blockMapping17; }

    public Mapping getBlockMapping19() {return blockMapping19; }

    public Mapping getLangMapping() {
        return langMapping;
    }

    public Mapping getLangMapping14() {return langMapping14; };

    protected abstract static class Mapping {

        protected final Map<String, String> mapping = new HashMap<>();

        public Mapping() {
            load();
        }

        protected abstract void load();

        /**
         * @return remapped or in if not present
         */
        public String remap(String in) {
            return mapping.getOrDefault(in, in);
        }
    }

    protected class BlockMapping13 extends Mapping {

        @Override
        protected void load() {
            JsonObject blocks = Util.readJsonResource(packConverter.getGson(), "/forwards/blocks.json").getAsJsonObject("1_13");
            if (blocks != null) {
                for (Map.Entry<String, JsonElement> entry : blocks.entrySet()) {
                    this.mapping.put(entry.getKey(), entry.getValue().getAsString());
                }
            }
        }
    }

    protected class BlockMapping14 extends Mapping {

        @Override
        protected void load() {
            JsonObject blocks = Util.readJsonResource(packConverter.getGson(), "/forwards/blocks.json").getAsJsonObject("1_14");
            if (blocks != null) {
                for (Map.Entry<String, JsonElement> entry : blocks.entrySet()) {
                    this.mapping.put(entry.getKey(), entry.getValue().getAsString());
                }
            }
        }
    }

    protected class BlockMapping17 extends Mapping {

        @Override
        protected void load() {
            JsonObject blocks = Util.readJsonResource(packConverter.getGson(), "/forwards/blocks.json").getAsJsonObject("1_17");
            if (blocks != null) {
                for (Map.Entry<String, JsonElement> entry : blocks.entrySet()) {
                    this.mapping.put(entry.getKey(), entry.getValue().getAsString());
                }
            }
        }
    }

    protected class BlockMapping19 extends Mapping {

        @Override
        protected void load() {
            JsonObject blocks = Util.readJsonResource(packConverter.getGson(), "/forwards/blocks.json").getAsJsonObject("1_19");
            if (blocks != null) {
                for (Map.Entry<String, JsonElement> entry : blocks.entrySet()) {
                    this.mapping.put(entry.getKey(), entry.getValue().getAsString());
                }
            }
        }
    }

    protected class LangMapping extends Mapping {
        @Override
        protected void load() {
            JsonObject entities = Util.readJsonResource(packConverter.getGson(), "/forwards/lang.json").getAsJsonObject("1_13");
            if (entities != null) {
                for (Map.Entry<String, JsonElement> entry : entities.entrySet()) {
                    this.mapping.put(entry.getKey(), entry.getValue().getAsString());
                }
            }
        }
    }

    protected class LangMapping14 extends Mapping {
        @Override
        protected void load() {
            JsonObject entities = Util.readJsonResource(packConverter.getGson(), "/forwards/lang.json").getAsJsonObject("1_14");
            if (entities != null) {
                for (Map.Entry<String, JsonElement> entry : entities.entrySet()) {
                    this.mapping.put(entry.getKey(), entry.getValue().getAsString());
                }
            }
        }
    }

    protected class EntityMapping extends Mapping {
        @Override
        protected void load() {
            JsonObject entities = Util.readJsonResource(packConverter.getGson(), "/forwards/entities.json");
            if (entities != null) {
                for (Map.Entry<String, JsonElement> entry : entities.entrySet()) {
                    this.mapping.put(entry.getKey(), entry.getValue().getAsString());
                }
            }
        }
    }

    protected class ItemMapping13 extends Mapping {

        @Override
        protected void load() {
            JsonObject items = Util.readJsonResource(packConverter.getGson(), "/forwards/items.json").getAsJsonObject("1_13");
            if (items != null) {
                for (Map.Entry<String, JsonElement> entry : items.entrySet()) {
                    this.mapping.put(entry.getKey(), entry.getValue().getAsString());
                }
            }
        }
    }

    protected class ItemMapping14 extends Mapping {

        @Override
        protected void load() {
            JsonObject items = Util.readJsonResource(packConverter.getGson(), "/forwards/items.json").getAsJsonObject("1_14");
            if (items != null) {
                for (Map.Entry<String, JsonElement> entry : items.entrySet()) {
                    this.mapping.put(entry.getKey(), entry.getValue().getAsString());
                }
            }
        }
    }

    protected class ItemMapping17 extends Mapping {
        @Override
        protected void load() {
            JsonObject items = Util.readJsonResource(packConverter.getGson(), "/forwards/items.json").getAsJsonObject("1_17");
            if (items != null) {
                for (Map.Entry<String, JsonElement> entry : items.entrySet()) {
                    this.mapping.put(entry.getKey(), entry.getValue().getAsString());
                }
            }
        }
    }

}
