package com.agentdid127.resourcepack.backwards.impl;

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
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class NameConverter extends Converter {
    private final int from;
    private final int to;

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
//    private final Mapping itemMapping203;

    public NameConverter(PackConverter packConverter, int from, int to) {
        super(packConverter);
        this.from = from;
        this.to = to;
        Gson gson = packConverter.getGson();
        blockMapping = new Mapping(gson, "blocks", "1_13", true);
        newBlockMapping = new Mapping(gson, "blocks", "1_14", true);
        blockMapping17 = new Mapping(gson, "blocks", "1_17", true);
        blockMapping19 = new Mapping(gson, "blocks", "1_19", true);
        itemMapping = new Mapping(gson, "items", "1_13", true);
        newItemMapping = new Mapping(gson, "items", "1_14", true);
        itemMapping17 = new Mapping(gson, "items", "1_17", true);
        entityMapping = new Mapping(gson, "entities", "*", true);
        langMapping = new Mapping(gson, "lang", "1_13", true);
        langMapping14 = new Mapping(gson, "lang", "1_14", true);
        blockMapping203 = new Mapping(gson, "blocks", "1_20_3", true);
//      TODO/(not used?):  itemMapping203 = new Mapping(gson, "items", "1_20_3", true);
    }

    @Override
    public boolean shouldConvert(Gson gson, int from, int to) {
        return true;
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

        // Version is greater than 1.13
        if (to <= Util.getVersionProtocol(packConverter.getGson(), "1.13")
                && from > Util.getVersionProtocol(packConverter.getGson(), "1.13")) {
            // OptiFine conversion
            if (minecraftPath.resolve("mcpatcher").toFile().exists()) {
                Logger.debug("MCPatcher exists, switching to optifine");
                if (minecraftPath.resolve("optifine").toFile().exists()) {
                    Logger.debug("OptiFine exists, merging directories");
                    FileUtil.mergeDirectories(minecraftPath.resolve("optifine").toFile(), minecraftPath.resolve("mcpatcher").toFile());
                } else {
                    Files.move(minecraftPath.resolve("mcpatcher"), minecraftPath.resolve("optifine"));
                }

                if (minecraftPath.resolve("mcpatcher").toFile().exists()) {
                    FileUtil.deleteDirectoryAndContents(minecraftPath.resolve("mcpatcher"));
                }
            }
        }

        Path modelsPath = minecraftPath.resolve("models");
        Path itemModelsPath = modelsPath.resolve("item");
        Path blockModelsPath = modelsPath.resolve("block");
        if (modelsPath.toFile().exists()) {
            // 1.20.3 Models
            if (to < Util.getVersionProtocol(packConverter.getGson(), "1.20.3")) {
                renameAll(blockMapping203, ".json", blockModelsPath);
            }

            // 1.19 Models
            if (to < Util.getVersionProtocol(packConverter.getGson(), "1.19")) {
                renameAll(blockMapping19, ".json", blockModelsPath);
            }

            // Update 1.14 items
            if (to < Util.getVersionProtocol(packConverter.getGson(), "1.14")) {
                renameAll(newItemMapping, ".json", itemModelsPath);
            }

            // 1.13 Models
            if (to < Util.getVersionProtocol(packConverter.getGson(), "1.13")) {
                renameAll(itemMapping, ".json", itemModelsPath);
                renameAll(blockMapping, ".json", blockModelsPath);
                Path bannerModelPath = itemModelsPath.resolve("banner.json");
                if (bannerModelPath.toFile().exists()) {
                    bannerModelPath.toFile().delete();
                }
            }
        }

        // Update BlockStates
        Path blockStates = minecraftPath.resolve("blockstates");
        if (blockStates.toFile().exists()) {
            renameAll(blockMapping, ".json", blockStates);
        }

        // Update textures
        Path texturesPath = minecraftPath.resolve("textures");
        if (texturesPath.toFile().exists()) {
            Path texturesBlockPath = texturesPath.resolve("block");
            Path texturesItemPath = texturesPath.resolve("item");

            // 1.20.3
            if (to < Util.getVersionProtocol(packConverter.getGson(), "1.20.3")
                    && from >= Util.getVersionProtocol(packConverter.getGson(), "1.20.3")) {
                renameAll(blockMapping203, ".png", texturesBlockPath);
            }

            // 1.19
            if (to < Util.getVersionProtocol(packConverter.getGson(), "1.19")
                    && from >= Util.getVersionProtocol(packConverter.getGson(), "1.19")) {
                renameAll(blockMapping19, ".png", texturesBlockPath);
            }

            // Entities
            Path entityPath = texturesPath.resolve("entity");
            if (entityPath.toFile().exists()) {
                // 1.17 Squid
                if (to < Util.getVersionProtocol(packConverter.getGson(), "1.17")
                        && from >= Util.getVersionProtocol(packConverter.getGson(), "1.17")) {
                    renameAll(blockMapping17, ".png", texturesBlockPath);
                    renameAll(itemMapping17, ".png", texturesItemPath);
                    renameAll(blockMapping17, ".png", blockModelsPath);
                    renameAll(itemMapping17, ".png", itemModelsPath);
                    Path squidPath = entityPath.resolve("squid/squid.png");
                    if (squidPath.toFile().exists()) {
                        Path newSquidPath = entityPath.resolve("squid.png");
                        if (newSquidPath.toFile().exists()) {
                            newSquidPath.toFile().delete();
                        }
                        Files.move(squidPath, newSquidPath);
                    }
                }

                // 1.16 Iron golems
                if (from >= Util.getVersionProtocol(packConverter.getGson(), "1.16")
                        && to < Util.getVersionProtocol(packConverter.getGson(), "1.16")) {
                    Path ironGolemPath = entityPath.resolve("iron_golem/iron_golem.png".replace("/", File.separator));
                    if (ironGolemPath.toFile().exists()) {
                        Path newIronGolemPath = entityPath.resolve("iron_golem.png");
                        if (newIronGolemPath.toFile().exists()) {
                            newIronGolemPath.toFile().delete();
                        }
                        Files.move(ironGolemPath, newIronGolemPath);
                    }
                }

                if (to < Util.getVersionProtocol(packConverter.getGson(), "1.14")) {
                    renameAll(newBlockMapping, ".png", texturesBlockPath);
                    renameAll(newBlockMapping, ".png.mcmeta", texturesBlockPath);
                    renameAll(newItemMapping, ".png", texturesItemPath);
                    renameAll(newItemMapping, ".png.mcmeta", texturesItemPath);
                }

                if (from >= Util.getVersionProtocol(packConverter.getGson(), "1.13")
                        && to < Util.getVersionProtocol(packConverter.getGson(), "1.13")) {
                    renameAll(blockMapping, ".png", texturesBlockPath);
                    renameAll(blockMapping, ".png.mcmeta", texturesBlockPath);
                    renameAll(itemMapping, ".png", texturesItemPath);
                    renameAll(itemMapping, ".png.mcmeta", texturesItemPath);

                    // 1.13 End Crystals
                    if (entityPath.resolve("end_crystal").toFile().exists()) {
                        Path newEnderCrystalFolderPath = entityPath.resolve("endcrystal");
                        if (newEnderCrystalFolderPath.toFile().exists()) {
                            newEnderCrystalFolderPath.toFile().delete();
                        }
                        Files.move(entityPath.resolve("end_crystal"), newEnderCrystalFolderPath);
                    }

                    findEntityFiles(texturesPath.resolve("entity"));
                }
            }
        }

        // Less than 1.12
        if (from > Util.getVersionProtocol(packConverter.getGson(), "1.13")
                && to < Util.getVersionProtocol(packConverter.getGson(), "1.13")) {
            Logger.debug("Finding files that are greater than 1.12");
            findFiles(texturesPath);
        }
    }

    /**
     * Finds files in entity folder
     *
     * @param path
     * @throws IOException
     */
    protected void findEntityFiles(Path path) throws IOException {
        File directory = path.toFile();
        if (!directory.exists())
            return;
        File[] files = directory.listFiles();
        if (files == null)
            return; // why is this happening??
        for (File file : files) {
            if (file.isDirectory()) {
                renameAll(entityMapping, ".png", file.toPath());
                renameAll(entityMapping, ".png.mcmeta", file.toPath());
                findEntityFiles(file.toPath());
            }
        }
    }

    /**
     * Finds files in folders called
     *
     * @param path
     * @throws IOException
     */
    protected void findFiles(Path path) throws IOException {
        if (!path.toFile().exists())
            return;
        File directory = path.toFile();
        for (File file : Objects.requireNonNull(directory.listFiles())) {
            if (file.isDirectory()) {
                if (file.getName().equals("item")) {
                    Logger.debug("Found Items folder, renaming");
                    FileUtil.renameFile(path.resolve(file.getName()), file.getName().replaceAll("item", "items"));
                }

                if (file.getName().equals("block")) {
                    Logger.debug("Found blocks folder, renaming");
                    FileUtil.renameFile(path.resolve(file.getName()), file.getName().replaceAll("block", "blocks"));
                }

                findFiles(file.toPath());
            }

            if (file.getName().contains("(")) {
                FileUtil.renameFile(path.resolve(file.getName()), file.getName().replaceAll("[()]", ""));
            }

            if (!file.getName().equals(file.getName().toLowerCase())) {
                Logger.debug("Renamed: " + file.getName() + "->" + file.getName().toLowerCase());
            }

            FileUtil.renameFile(path.resolve(file.getName()), file.getName().toLowerCase());
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
        if (!path.toFile().exists()) {
            return;
        }

        // remap grass blocks in order due to the cyclical way their names have changed,
        // i.e grass -> grass_block, tall_grass -> grass, double_grass -> tall_grass
        List<String> grasses = Arrays.asList("tall_grass", "grass", "grass_block");
        if (from <= Util.getVersionProtocol(packConverter.getGson(), "1.12.2")
                && (path.endsWith("blockstates") || path.endsWith("textures/block"))) {
            grasses.forEach(name -> {
                Path basePath = path.resolve(name + extension);
                String remappedName = mapping.remap(name);

                Path newPath = path.resolve(remappedName + extension);
                if (newPath.toFile().exists()) {
                    // Same note from Slicer.java line 38
                    newPath.toFile().delete();
                }

                Boolean renameSuccess = FileUtil.renameFile(basePath, newPath);
                if (renameSuccess == null) {
                    return;
                }

                if (renameSuccess) {
                    Logger.debug("Renamed: " + name + extension + "->" + newPath.getFileName());
                } else if (!renameSuccess) {
                    Logger.error("      Failed to rename: " + name + extension + "->" + newPath.getFileName());
                }
            });
        }

        if (from > Util.getVersionProtocol(packConverter.getGson(), "1.12.2") &&
                to <= Util.getVersionProtocol(packConverter.getGson(), "1.12.2")) {
            Stream.of("snow", "snow_block").forEach(name -> {
                Path basePath = path.resolve(name + extension);
                String remappedName = mapping.remap(name);

                if (!extension.equals(".png")) {
                    Path newPath = path.resolve(remappedName + extension);
                    if (newPath.toFile().exists()) {
                        // Same note from Slicer.java line 38
                        newPath.toFile().delete();
                    }

                    Boolean renameSuccess = FileUtil.renameFile(basePath, newPath);
                    if (renameSuccess == null) {
                        return;
                    }

                    if (renameSuccess) {
                        Logger.debug("Renamed: " + name + extension + "->" + newPath.getFileName());
                    } else if (!renameSuccess) {
                        Logger.log("Failed to rename: " + name + extension + "->" + newPath.getFileName());
                    }
                }
            });
        }

        Files.list(path)
                .filter(path1 -> path1.toString().endsWith(extension))
                .forEach(path1 -> {
                    String baseName = path1.getFileName().toString().substring(0, path1.getFileName().toString().length() - extension.length());
                    // NOTE: skip the already renamed grass blocks
                    if (grasses.contains(baseName) && (path.endsWith("blockstates") || path.endsWith("textures/block"))) {
                        return;
                    }

                    String remappedName = mapping.remap(baseName);
                    if (remappedName != null && !remappedName.equals(baseName)) {
                        Path newPath = path1.getParent().resolve(remappedName + extension);
                        if (newPath.toFile().exists()) {
                            // Same note from Slicer.java line 38
                            newPath.toFile().delete();
                        }

                        Boolean renameSuccess = FileUtil.renameFile(path1, newPath);
                        if (renameSuccess == null) {
                            return;
                        }

                        if (renameSuccess) {
                            Logger.debug("Renamed: " + path1.getFileName().toString() + "->" + newPath.getFileName());
                        } else if (!renameSuccess) {
                            Logger.error("      Failed to rename: " + path1.getFileName().toString() + "->" + newPath.getFileName());
                        }
                    }
                });
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
