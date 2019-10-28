package net.hypixel.resourcepack.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.hypixel.resourcepack.Converter;
import net.hypixel.resourcepack.PackConverter;
import net.hypixel.resourcepack.Util;
import net.hypixel.resourcepack.pack.Pack;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class NameConverter extends Converter {

    protected final Mapping blockMapping = new BlockMapping();
    protected final Mapping itemMapping = new ItemMapping();
    protected final Mapping entityMapping = new EntityMapping();

    public NameConverter(PackConverter packConverter) {
        super(packConverter);
    }

    @Override
    public void convert(Pack pack) throws IOException {
        Path mc = pack.getWorkingPath().resolve("assets" + File.separator + "minecraft");
        if (mc.resolve("mcpatcher").toFile().exists()) Files.move(mc.resolve("mcpatcher"), mc.resolve("optifine"));
        Path models = pack.getWorkingPath().resolve("assets" + File.separator + "minecraft" + File.separator + "models");
        if (models.resolve("blocks").toFile().exists()) Files.move(models.resolve("blocks"), models.resolve("block"));
        renameAll(blockMapping, ".json", models.resolve("block"));
        if (models.resolve("items").toFile().exists()) Files.move(models.resolve("items"), models.resolve("item"));
        renameAll(itemMapping, ".json", models.resolve("item"));

        Path blockStates = pack.getWorkingPath().resolve("assets" + File.separator + "minecraft" + File.separator + "blockstates");
        renameAll(itemMapping, ".json", blockStates);

        Path textures = pack.getWorkingPath().resolve("assets" + File.separator + "minecraft" + File.separator + "textures");
        if (textures.resolve("blocks").toFile().exists()) Files.move(textures.resolve("blocks"), textures.resolve("block"));
        renameAll(blockMapping, ".png", textures.resolve("block"));
        renameAll(blockMapping, ".png.mcmeta", textures.resolve("block"));
        if (textures.resolve("items").toFile().exists()) Files.move(textures.resolve("items"), textures.resolve("item"));
        renameAll(itemMapping, ".png", textures.resolve("item"));
        renameAll(itemMapping, ".png.mcmeta", textures.resolve("item"));
        if (textures.resolve("entity" + File.separator + "endercrystal").toFile().exists()) Files.move(textures.resolve("entity" + File.separator + "endercrystal"), textures.resolve("entity" + File.separator + "end_crystal"));
        findEntityFiles(textures.resolve("entity"));
    }
    // Added to find files in the entity folder
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

    protected void renameAll(Mapping mapping, String extension, Path path) throws IOException {
        if (path.toFile().exists()) {
            Files.list(path).forEach(path1 -> {
                if (!path1.toString().endsWith(extension)) return;

                String baseName = path1.getFileName().toString().substring(0, path1.getFileName().toString().length() - extension.length());
                String newName = mapping.remap(baseName);
                if (newName != null && !newName.equals(baseName)) {
                    Boolean ret = Util.renameFile(path1, newName + extension);
                    if (ret == null) return;
                    if (ret && PackConverter.DEBUG) {
                        System.out.println("      Renamed: " + path1.getFileName().toString() + "->" + newName + extension);
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

    protected class BlockMapping extends Mapping {

        @Override
        protected void load() {
            JsonObject blocks = Util.readJsonResource(packConverter.getGson(), "/blocks.json");
            if (blocks != null) {
                for (Map.Entry<String, JsonElement> entry : blocks.entrySet()) {
                    this.mapping.put(entry.getKey(), entry.getValue().getAsString());
                }
            }
        }

    }


    protected class EntityMapping extends Mapping {

        @Override
        protected void load() {
            JsonObject entities = Util.readJsonResource(packConverter.getGson(), "/entities.json");
            if (entities != null) {
                for (Map.Entry<String, JsonElement> entry : entities.entrySet()) {
                    this.mapping.put(entry.getKey(), entry.getValue().getAsString());
                }
            }
        }

    }


    protected class ItemMapping extends Mapping {

        @Override
        protected void load() {
            JsonObject items = Util.readJsonResource(packConverter.getGson(), "/items.json");
            if (items != null) {
                for (Map.Entry<String, JsonElement> entry : items.entrySet()) {
                    this.mapping.put(entry.getKey(), entry.getValue().getAsString());
                }
            }
        }

    }
}
