package com.agentdid127.resourcepack.forwards.impl;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.FileUtil;
import com.agentdid127.resourcepack.library.utilities.Logger;
import com.agentdid127.resourcepack.library.utilities.Mapping;
import com.agentdid127.resourcepack.library.utilities.Util;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class NameConverter extends Converter {
    private int to;
    private int from;

    private final Mapping blockMapping;
    private final Mapping newBlockMapping;
    private final Mapping blockMapping17;
    private final Mapping blockMapping19;
    private final Mapping itemMapping;
    private final Mapping newItemMapping;
    private final Mapping itemMapping17;
    private final Mapping entityMapping;
    private final Mapping langMapping;
    private final Mapping langMapping14;
    private final Mapping blockMapping203;
    private final Mapping itemMapping203;

    public NameConverter(PackConverter packConverter, int from, int to) {
        super(packConverter);
        this.from = from;
        this.to = to;
        Gson gson = packConverter.getGson();
        blockMapping = new Mapping(gson, "blocks", "1_13", false);
        newBlockMapping = new Mapping(gson, "blocks", "1_14", false);
        blockMapping17 = new Mapping(gson, "blocks", "1_17", false);
        blockMapping19 = new Mapping(gson, "blocks", "1_19", false);
        itemMapping = new Mapping(gson, "items", "1_13", false);
        newItemMapping = new Mapping(gson, "items", "1_14", false);
        itemMapping17 = new Mapping(gson, "items", "1_17", false);
        entityMapping = new Mapping(gson, "entities", "*", false);
        langMapping = new Mapping(gson, "lang", "1_13", false);
        langMapping14 = new Mapping(gson, "lang", "1_14", false);
        blockMapping203 = new Mapping(gson, "blocks", "1_20_3", false);
        itemMapping203 = new Mapping(gson, "items", "1_20_3", false);
    }

    /**
     * Fixes folder names and file names
     * 
     * @param pack Resource Pack
     * @throws IOException Error handler
     */
    @Override
    public void convert(Pack pack) throws IOException {
        Path minecraftPath = pack.getWorkingPath().resolve("assets/minecraft".replace("/", File.separator));
        // Less than 1.12
        if (from <= Util.getVersionProtocol(packConverter.getGson(), "1.12.2")
                && to > Util.getVersionProtocol(packConverter.getGson(), "1.13")) {
            if (PackConverter.DEBUG)
                Logger.log("Finding files that are less than 1.12");
            findFiles(minecraftPath);
        }

        // Version is greater than 1.13
        if (to >= Util.getVersionProtocol(packConverter.getGson(), "1.13")) {
            // OptiFine conversion
            if (minecraftPath.resolve("mcpatcher").toFile().exists()) {
                if (PackConverter.DEBUG)
                    Logger.log("MCPatcher exists, switching to optifine");
                if (minecraftPath.resolve("optifine").toFile().exists()) {
                    if (PackConverter.DEBUG)
                        Logger.log("OptiFine exists, merging directories");
                    FileUtil.mergeDirectories(minecraftPath.resolve("optifine").toFile(),
                            minecraftPath.resolve("mcpatcher").toFile());
                } else
                    Files.move(minecraftPath.resolve("mcpatcher"), minecraftPath.resolve("optifine"));
                if (minecraftPath.resolve("mcpatcher").toFile().exists())
                    FileUtil.deleteDirectoryAndContents(minecraftPath.resolve("mcpatcher"));
            }

            // 1.13 Models
            Path modelsPath = pack.getWorkingPath().resolve("assets/minecraft/models".replace("/", File.separator));
            if (modelsPath.toFile().exists()) {
                // 1.13 block/item name change
                if (modelsPath.resolve("blocks").toFile().exists()) {
                    if (modelsPath.resolve("block").toFile().exists())
                        FileUtil.deleteDirectoryAndContents(modelsPath.resolve("block"));
                    Files.move(modelsPath.resolve("blocks"), modelsPath.resolve("block"));
                }

                // Update all blocks for 1.13
                renameAll(blockMapping, ".json", modelsPath.resolve("block"));
                if (modelsPath.resolve("items").toFile().exists()) {
                    if (modelsPath.resolve("item").toFile().exists())
                        FileUtil.deleteDirectoryAndContents(modelsPath.resolve("item"));
                    Files.move(modelsPath.resolve("items"), modelsPath.resolve("item"));
                }

                // Update all items for 1.13
                renameAll(itemMapping, ".json", modelsPath.resolve("item"));

                // Update 1.14 items
                if (to > Util.getVersionProtocol(packConverter.getGson(), "1.13")) {
                    Path itemModelPath = modelsPath.resolve("iem");
                    renameAll(newItemMapping, ".json", itemModelPath);
                    if (!itemModelPath.resolve("ink_sac.json").toFile().exists()
                            && itemModelPath.resolve("black_dye.json").toFile().exists())
                        Files.copy(itemModelPath.resolve("black_dye.json"),
                                itemModelPath.resolve("ink_sac.json"));
                    if (!itemModelPath.resolve("cocoa_beans.json").toFile().exists()
                            && itemModelPath.resolve("brown_dye.json").toFile().exists())
                        Files.copy(itemModelPath.resolve("brown_dye.json"),
                                itemModelPath.resolve("cocoa_beans.json"));
                    if (!itemModelPath.resolve("bone_meal.json").toFile().exists()
                            && itemModelPath.resolve("white_dye.json").toFile().exists())
                        Files.copy(itemModelPath.resolve("white_dye.json"),
                                itemModelPath.resolve("bone_meal.json"));
                    if (!itemModelPath.resolve("lapis_lazuli.json").toFile().exists()
                            && itemModelPath.resolve("blue_dye.json").toFile().exists())
                        Files.copy(itemModelPath.resolve("blue_dye.json"),
                                itemModelPath.resolve("lapis_lazuli.json"));
                }

                if (to > Util.getVersionProtocol(packConverter.getGson(), "1.19"))
                    renameAll(blockMapping19, ".json", modelsPath.resolve("block"));

                if (to > Util.getVersionProtocol(packConverter.getGson(), "1.20.3")) {
                    renameAll(itemMapping203, ".json", modelsPath.resolve("item"));
                    renameAll(blockMapping203, ".json", modelsPath.resolve("block"));
                }
            }

            // Update BlockStates
            Path blockStates = pack.getWorkingPath()
                    .resolve("assets/minecraft/blockstates".replace("/", File.separator));
            if (blockStates.toFile().exists())
                renameAll(blockMapping, ".json", blockStates);

            // Update textures
            Path texturesPath = pack.getWorkingPath().resolve("assets/minecraft/textures".replace("/", File.separator));
            if (texturesPath.toFile().exists()) {
                Path blockPath = texturesPath.resolve("block");
                if (texturesPath.resolve("blocks").toFile().exists())
                    Files.move(texturesPath.resolve("blocks"), blockPath);

                if (from < Util.getVersionProtocol(packConverter.getGson(), "1.13")) {
                    renameAll(blockMapping, ".png", blockPath);
                    renameAll(blockMapping, ".png.mcmeta", blockPath);
                }

                if (to > Util.getVersionProtocol(packConverter.getGson(), "1.13")) {
                    renameAll(newBlockMapping, ".png", blockPath);
                    renameAll(newBlockMapping, ".png.mcmeta", blockPath);
                }

                Path itemPath = texturesPath.resolve("item");
                if (from < Util.getVersionProtocol(packConverter.getGson(), "1.13")) {
                    renameAll(itemMapping, ".png", itemPath);
                    renameAll(itemMapping, ".png.mcmeta", itemPath);
                }

                if (to > Util.getVersionProtocol(packConverter.getGson(), "1.13.2")) {
                    renameAll(newItemMapping, ".png", itemPath);
                    renameAll(newItemMapping, ".png.mcmeta", itemPath);
                    Path itemModelPath = modelsPath.resolve("item");
                    if (!itemModelPath.resolve("ink_sac.png").toFile().exists()
                            && itemModelPath.resolve("black_dye.png").toFile().exists())
                        Files.copy(itemModelPath.resolve("black_dye.png"),
                                itemModelPath.resolve("ink_sac.png"));
                    if (!itemModelPath.resolve("cocoa_beans.png").toFile().exists()
                            && itemModelPath.resolve("brown_dye.png").toFile().exists())
                        Files.copy(itemModelPath.resolve("brown_dye.png"),
                                itemModelPath.resolve("cocoa_beans.png"));
                    if (!itemModelPath.resolve("bone_meal.png").toFile().exists()
                            && itemModelPath.resolve("white_dye.png").toFile().exists())
                        Files.copy(itemModelPath.resolve("white_dye.png"),
                                itemModelPath.resolve("bone_meal.png"));
                    if (!itemModelPath.resolve("lapis_lazuli.png").toFile().exists()
                            && itemModelPath.resolve("blue_dye.png").toFile().exists())
                        Files.copy(itemModelPath.resolve("blue_dye.png"),
                                itemModelPath.resolve("lapis_lazuli.png"));
                }

                // Entities
                Path entityPath = texturesPath.resolve("entity");

                // 1.16 Iron golems
                if (from < Util.getVersionProtocol(packConverter.getGson(), "1.15")
                        && to >= Util.getVersionProtocol(packConverter.getGson(), "1.15")) {
                    if (!entityPath.resolve("iron_golem").toFile().exists())
                        entityPath.resolve("iron_golem").toFile().mkdir();
                    if (entityPath.resolve("iron_golem.png").toFile().exists())
                        Files.move(entityPath.resolve("iron_golem.png"), entityPath
                                .resolve("iron_golem/iron_golem.png"));
                }

                // 1.17 Squid
                if (to >= Util.getVersionProtocol(packConverter.getGson(), "1.17")
                        && from < Util.getVersionProtocol(packConverter.getGson(), "1.17")) {
                    renameAll(blockMapping17, ".png", blockPath);
                    renameAll(itemMapping17, ".png", texturesPath.resolve("item"));
                    renameAll(blockMapping17, ".png", modelsPath.resolve("block"));
                    renameAll(itemMapping17, ".png", modelsPath.resolve("item"));
                    if (!entityPath.resolve("squid").toFile().exists())
                        entityPath.resolve("squid").toFile().mkdir();
                    if (entityPath.resolve("squid.png").toFile().exists())
                        Files.move(entityPath.resolve("squid.png"),
                                entityPath.resolve("squid/squid.png"));
                }

                if (to >= Util.getVersionProtocol(packConverter.getGson(), "1.19")
                        && from < Util.getVersionProtocol(packConverter.getGson(), "1.19"))
                    renameAll(blockMapping19, ".png", blockPath);

                // 1.13 End Crystals
                if (entityPath.resolve("endercrystal").toFile().exists()
                        && !entityPath.resolve("end_crystal").toFile().exists())
                    Files.move(entityPath.resolve("endercrystal"), entityPath.resolve("end_crystal"));
                findEntityFiles(entityPath);
            }
        }
    }

    /**
     * Finds files in entity folder
     * 
     * @param path
     * @throws IOException
     */
    protected void findEntityFiles(Path path) throws IOException {
        if (!path.toFile().exists())
            return;
        File directory = path.toFile();
        for (File file : directory.listFiles()) {
            if (!file.isDirectory())
                continue;
            renameAll(entityMapping, ".png", file.toPath());
            renameAll(entityMapping, ".png.mcmeta", file.toPath());
            findEntityFiles(file.toPath());
        }
    }

    /**
     * Finds files in folders called
     * 
     * @param path
     * @throws IOException
     */
    protected void findFiles(Path path) throws IOException {
        if (path.toFile().exists()) {
            File directory = path.toFile();
            for (File file : directory.listFiles()) {
                if (file.isDirectory()) {
                    if (file.getName().equals("items")) {
                        if (PackConverter.DEBUG)
                            Logger.log("Found Items folder, renaming");
                        FileUtil.renameFile(path.resolve(file.getName()), file.getName().replaceAll("items", "item"));
                    }

                    if (file.getName().equals("blocks")) {
                        if (PackConverter.DEBUG)
                            Logger.log("Found blocks folder, renaming");
                        FileUtil.renameFile(path.resolve(file.getName()), file.getName().replaceAll("blocks", "block"));
                    }

                    findFiles(file.toPath());
                }

                if (file.getName().contains("("))
                    FileUtil.renameFile(path.resolve(file.getName()), file.getName().replaceAll("[()]", ""));

                if (!file.getName().equals(file.getName().toLowerCase()))
                    if (PackConverter.DEBUG)
                        Logger.log("Renamed: " + file.getName() + "->" + file.getName().toLowerCase());

                FileUtil.renameFile(path.resolve(file.getName()), file.getName().toLowerCase());
            }
        }
    }

    /**
     * Renames folder
     * 
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
            if (from <= Util.getVersionProtocol(packConverter.getGson(), "1.12.2")) {
                if ((path.endsWith("blockstates") || path.endsWith("textures/block"))) {
                    grasses.stream().forEach(name -> {
                        String newName = mapping.remap(name);
                        Boolean ret = FileUtil.renameFile(Paths.get(path + File.separator + name + extension),
                                newName + extension);
                        if (ret == null)
                            return;
                        if (ret && PackConverter.DEBUG) {
                            Logger.log("      Renamed: " + name + extension + "->" + newName + extension);
                        } else if (!ret) {
                            System.err.println(
                                    "      Failed to rename: " + name + extension + "->" + newName + extension);
                        }
                    });
                }
            }

            // remap snow jsons, but not images.
            if (from < Util.getVersionProtocol(packConverter.getGson(), "1.13")
                    && to >= Util.getVersionProtocol(packConverter.getGson(), "1.13")) {
                if (path.resolve("snow_layer.json").toFile().exists())
                    FileUtil.renameFile(path.resolve("snow_layer" + extension), "snow" + extension);
            }

            Files.list(path).forEach(path1 -> {
                if (!path1.toString().endsWith(extension))
                    return;

                String baseName = path1.getFileName().toString().substring(0,
                        path1.getFileName().toString().length() - extension.length());

                // skip the already renamed grass blocks
                if (grasses.contains(baseName)
                        && (path.endsWith("blockstates") || path.endsWith("textures/block")))
                    return;

                String newName = mapping.remap(baseName);
                if (newName != null && !newName.equals(baseName)) {
                    Boolean ret = FileUtil.renameFile(path1, newName + extension);
                    if (ret == null)
                        return;
                    if (ret && PackConverter.DEBUG) {
                        Logger.log(
                                "      Renamed: " + path1.getFileName().toString() + "->" + newName + extension);
                    } else if (!ret) {
                        System.err.println("      Failed to rename: " + path1.getFileName().toString() + "->" + newName
                                + extension);
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

    public Mapping getItemMapping17() {
        return itemMapping17;
    }

    public Mapping getBlockMapping17() {
        return blockMapping17;
    }

    public Mapping getBlockMapping19() {
        return blockMapping19;
    }

    public Mapping getLangMapping() {
        return langMapping;
    }

    public Mapping getLangMapping14() {
        return langMapping14;
    }
}