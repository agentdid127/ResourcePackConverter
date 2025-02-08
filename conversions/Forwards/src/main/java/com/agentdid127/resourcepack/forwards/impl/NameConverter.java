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
import java.util.Objects;
import java.util.stream.Stream;

public class NameConverter extends Converter {
    private final int to;
    private final int from;

    private final Mapping entityMapping;

    private final Mapping blockMapping_1_13;
    private final Mapping itemMapping_1_13;

    private final Mapping blockMapping_1_14;
    private final Mapping itemMapping_1_14;

    private final Mapping blockMapping_1_17;
    private final Mapping itemMapping_1_17;

    private final Mapping blockMapping_1_19;

    private final Mapping blockMapping_1_20_3;
    private final Mapping itemMapping1_20_3;

    public NameConverter(PackConverter packConverter, int from, int to) {
        super(packConverter);

        this.from = from;
        this.to = to;

        Gson gson = packConverter.getGson();
        entityMapping = new Mapping(gson, "entities", "*", false);

//      TODO/(Not used):  langMapping_1_13 = new Mapping(gson, "lang", "1_13", false);
        blockMapping_1_13 = new Mapping(gson, "blocks", "1_13", false);
        itemMapping_1_13 = new Mapping(gson, "items", "1_13", false);

//      TODO/(Not used):  langMapping_1_14 = new Mapping(gson, "lang", "1_14", false);
        blockMapping_1_14 = new Mapping(gson, "blocks", "1_14", false);
        itemMapping_1_14 = new Mapping(gson, "items", "1_14", false);

        blockMapping_1_17 = new Mapping(gson, "blocks", "1_17", false);
        itemMapping_1_17 = new Mapping(gson, "items", "1_17", false);

        blockMapping_1_19 = new Mapping(gson, "blocks", "1_19", false);

        blockMapping_1_20_3 = new Mapping(gson, "blocks", "1_20_3", false);
        itemMapping1_20_3 = new Mapping(gson, "items", "1_20_3", false);
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
            Logger.debug("Finding files that are less than 1.12");
            findFiles(minecraftPath);
        }

        // Version is greater than 1.13
        if (to >= Util.getVersionProtocol(packConverter.getGson(), "1.13")) {
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

            // 1.13 Models
            Path modelsPath = pack.getWorkingPath().resolve("assets/minecraft/models".replace("/", File.separator));
            if (modelsPath.toFile().exists()) {
                // 1.13 block/item name change
                if (modelsPath.resolve("blocks").toFile().exists()) {
                    if (modelsPath.resolve("block").toFile().exists()) {
                        FileUtil.deleteDirectoryAndContents(modelsPath.resolve("block"));
                    }

                    Files.move(modelsPath.resolve("blocks"), modelsPath.resolve("block"));
                }

                // Update all blocks for 1.13
                renameAll(blockMapping_1_13, ".json", modelsPath.resolve("block"));
                if (modelsPath.resolve("items").toFile().exists()) {
                    if (modelsPath.resolve("item").toFile().exists()) {
                        FileUtil.deleteDirectoryAndContents(modelsPath.resolve("item"));
                    }

                    Files.move(modelsPath.resolve("items"), modelsPath.resolve("item"));
                }

                // Update all items for 1.13
                renameAll(itemMapping_1_13, ".json", modelsPath.resolve("item"));

                // Update 1.14 items
                if (to > Util.getVersionProtocol(packConverter.getGson(), "1.13")) {
                    Path itemModelPath = modelsPath.resolve("iem");
                    renameAll(itemMapping_1_14, ".json", itemModelPath);

                    if (!itemModelPath.resolve("ink_sac.json").toFile().exists()
                            && itemModelPath.resolve("black_dye.json").toFile().exists()) {
                        Files.copy(itemModelPath.resolve("black_dye.json"), itemModelPath.resolve("ink_sac.json"));
                    }

                    if (!itemModelPath.resolve("cocoa_beans.json").toFile().exists()
                            && itemModelPath.resolve("brown_dye.json").toFile().exists()) {
                        Files.copy(itemModelPath.resolve("brown_dye.json"), itemModelPath.resolve("cocoa_beans.json"));
                    }

                    if (!itemModelPath.resolve("bone_meal.json").toFile().exists()
                            && itemModelPath.resolve("white_dye.json").toFile().exists()) {
                        Files.copy(itemModelPath.resolve("white_dye.json"), itemModelPath.resolve("bone_meal.json"));
                    }

                    if (!itemModelPath.resolve("lapis_lazuli.json").toFile().exists()
                            && itemModelPath.resolve("blue_dye.json").toFile().exists()) {
                        Files.copy(itemModelPath.resolve("blue_dye.json"), itemModelPath.resolve("lapis_lazuli.json"));
                    }
                }

                if (to > Util.getVersionProtocol(packConverter.getGson(), "1.19")) {
                    renameAll(blockMapping_1_19, ".json", modelsPath.resolve("block"));
                }

                if (to > Util.getVersionProtocol(packConverter.getGson(), "1.20.3")) {
                    renameAll(itemMapping1_20_3, ".json", modelsPath.resolve("item"));
                    renameAll(blockMapping_1_20_3, ".json", modelsPath.resolve("block"));
                }
            }

            // Update BlockStates
            Path blockStates = pack.getWorkingPath().resolve("assets/minecraft/blockstates".replace("/", File.separator));
            if (blockStates.toFile().exists()) {
                renameAll(blockMapping_1_13, ".json", blockStates);
            }

            // Update textures
            Path texturesPath = pack.getWorkingPath().resolve("assets/minecraft/textures".replace("/", File.separator));
            if (texturesPath.toFile().exists()) {
                Path blockPath = texturesPath.resolve("block");
                if (texturesPath.resolve("blocks").toFile().exists()) {
                    Files.move(texturesPath.resolve("blocks"), blockPath);
                }

                if (from < Util.getVersionProtocol(packConverter.getGson(), "1.13")) {
                    renameAll(blockMapping_1_13, ".png", blockPath);
                    renameAll(blockMapping_1_13, ".png.mcmeta", blockPath);
                }

                if (to > Util.getVersionProtocol(packConverter.getGson(), "1.13")) {
                    renameAll(blockMapping_1_14, ".png", blockPath);
                    renameAll(blockMapping_1_14, ".png.mcmeta", blockPath);
                }

                Path itemPath = texturesPath.resolve("item");
                if (from < Util.getVersionProtocol(packConverter.getGson(), "1.13")) {
                    renameAll(itemMapping_1_13, ".png", itemPath);
                    renameAll(itemMapping_1_13, ".png.mcmeta", itemPath);
                }

                if (to > Util.getVersionProtocol(packConverter.getGson(), "1.13.2")) {
                    renameAll(itemMapping_1_14, ".png", itemPath);
                    renameAll(itemMapping_1_14, ".png.mcmeta", itemPath);

                    Path itemModelPath = modelsPath.resolve("item");
                    if (!itemModelPath.resolve("ink_sac.png").toFile().exists()
                            && itemModelPath.resolve("black_dye.png").toFile().exists()) {
                        Files.copy(itemModelPath.resolve("black_dye.png"), itemModelPath.resolve("ink_sac.png"));
                    }

                    if (!itemModelPath.resolve("cocoa_beans.png").toFile().exists()
                            && itemModelPath.resolve("brown_dye.png").toFile().exists()) {
                        Files.copy(itemModelPath.resolve("brown_dye.png"), itemModelPath.resolve("cocoa_beans.png"));
                    }

                    if (!itemModelPath.resolve("bone_meal.png").toFile().exists()
                            && itemModelPath.resolve("white_dye.png").toFile().exists()) {
                        Files.copy(itemModelPath.resolve("white_dye.png"), itemModelPath.resolve("bone_meal.png"));
                    }

                    if (!itemModelPath.resolve("lapis_lazuli.png").toFile().exists()
                            && itemModelPath.resolve("blue_dye.png").toFile().exists()) {
                        Files.copy(itemModelPath.resolve("blue_dye.png"), itemModelPath.resolve("lapis_lazuli.png"));
                    }
                }

                // Entities
                Path entityPath = texturesPath.resolve("entity");

                // 1.16 Iron golems
                if (from < Util.getVersionProtocol(packConverter.getGson(), "1.15")
                        && to >= Util.getVersionProtocol(packConverter.getGson(), "1.15")) {
                    if (!entityPath.resolve("iron_golem").toFile().exists()) {
                        entityPath.resolve("iron_golem").toFile().mkdir();
                    }

                    if (entityPath.resolve("iron_golem.png").toFile().exists()) {
                        Path newIronGolemPath = entityPath.resolve("iron_golem/iron_golem.png");
                        if (newIronGolemPath.toFile().exists()) {
                            newIronGolemPath.toFile().delete();
                        }

                        Files.move(entityPath.resolve("iron_golem.png"), newIronGolemPath);
                    }
                }

                // 1.17 Squid
                if (to >= Util.getVersionProtocol(packConverter.getGson(), "1.17")
                        && from < Util.getVersionProtocol(packConverter.getGson(), "1.17")) {
                    renameAll(blockMapping_1_17, ".png", blockPath);
                    renameAll(itemMapping_1_17, ".png", itemPath);
                    renameAll(blockMapping_1_17, ".png", modelsPath.resolve("block"));
                    renameAll(itemMapping_1_17, ".png", modelsPath.resolve("item"));

                    Path squidFolderPath = entityPath.resolve("squid");
                    if (!squidFolderPath.toFile().exists()) {
                        squidFolderPath.toFile().mkdir();
                    }

                    if (entityPath.resolve("squid.png").toFile().exists()) {
                        Path newSquidPath = squidFolderPath.resolve("squid.png");
                        if (newSquidPath.toFile().exists()) {
                            newSquidPath.toFile().delete();
                        }

                        Files.move(entityPath.resolve("squid.png"), newSquidPath);
                    }
                }

                if (to >= Util.getVersionProtocol(packConverter.getGson(), "1.19")
                        && from < Util.getVersionProtocol(packConverter.getGson(), "1.19")) {
                    renameAll(blockMapping_1_19, ".png", blockPath);
                }

                // 1.13 End Crystals
                if (entityPath.resolve("endercrystal").toFile().exists()
                        && !entityPath.resolve("end_crystal").toFile().exists()) {
                    Path newEnderCrystalPath = entityPath.resolve("end_crystal");
                    if (newEnderCrystalPath.toFile().exists()) {
                        newEnderCrystalPath.toFile().delete();
                    }

                    Files.move(entityPath.resolve("endercrystal"), newEnderCrystalPath);
                }

                findEntityFiles(entityPath);
            }
        }
    }

    /**
     * Finds files in entity folder
     *
     * @param path The input path
     * @throws IOException The IO exception
     */
    protected void findEntityFiles(Path path) throws IOException {
        if (path.toFile().exists()) {
            File directory = path.toFile();
            for (File file : Objects.requireNonNull(directory.listFiles())) {
                if (file.isDirectory()) {
                    renameAll(entityMapping, ".png", file.toPath());
                    renameAll(entityMapping, ".png.mcmeta", file.toPath());
                    findEntityFiles(file.toPath());
                }
            }
        }
    }

    /**
     * Finds files in folders called
     * @param path The input path
     */
    protected void findFiles(Path path) {
        if (path.toFile().exists()) {
            File directory = path.toFile();
            for (File file : Objects.requireNonNull(directory.listFiles())) {
                if (file.isDirectory()) {
                    if (file.getName().equals("items")) {
                        Logger.debug("Found Items folder, renaming");
                        FileUtil.renameFile(path.resolve(file.getName()), file.getName().replaceAll("items", "item"));
                    }

                    if (file.getName().equals("blocks")) {
                        Logger.debug("Found blocks folder, renaming");
                        FileUtil.renameFile(path.resolve(file.getName()), file.getName().replaceAll("blocks", "block"));
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
    }

    /**
     * Renames folder
     *
     * @param mapping The input mapping
     * @param extension The file extension
     * @param path The input path
     * @throws IOException The IO exception
     */
    protected void renameAll(Mapping mapping, String extension, Path path) throws IOException {
        if (path.toFile().exists()) {
            // remap grass blocks in order due to the cyclical way their names have changed,
            // i.e grass -> grass_block, tall_grass -> grass, double_grass -> tall_grass
            List<String> grasses = Arrays.asList("grass", "tall_grass", "double_grass");
            if (from <= Util.getVersionProtocol(packConverter.getGson(), "1.12.2")) {
                if ((path.endsWith("blockstates") || path.endsWith("textures/block"))) {
                    grasses.forEach(name -> {
                        String newName = mapping.remap(name);
                        Boolean ret = FileUtil.renameFile(Paths.get(path + File.separator + name + extension), newName + extension);
                        if (ret == null) {
                            return;
                        }

                        if (ret) {
                            Logger.debug("Renamed: " + name + extension + "->" + newName + extension);
                        } else if (!ret) {
                            Logger.log("Failed to rename: " + name + extension + "->" + newName + extension);
                        }
                    });
                }
            }

            // remap snow jsons, but not images.
            if (from < Util.getVersionProtocol(packConverter.getGson(), "1.13")
                    && to >= Util.getVersionProtocol(packConverter.getGson(), "1.13")) {
                if (path.resolve("snow_layer.json").toFile().exists()) {
                    FileUtil.renameFile(path.resolve("snow_layer" + extension), "snow" + extension);
                }
            }

            Logger.addTab();
            try (Stream<Path> pathStream = Files.list(path).filter(path1 -> path1.toString().endsWith(extension))) {
                pathStream.forEach(path1 -> {
                    String baseName = path1.getFileName().toString().substring(0, path1.getFileName().toString().length() - extension.length());

                    // skip the already renamed grass blocks
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
                            Logger.log("Failed to rename: " + path1.getFileName().toString() + "->" + newPath.getFileName());
                        }
                    }
                });
            }

            Logger.subTab();
        }
    }

    public Mapping getBlockMapping_1_13() {
        return blockMapping_1_13;
    }

    public Mapping getItemMapping_1_13() {
        return itemMapping_1_13;
    }

    public Mapping getBlockMapping_1_14() {
        return blockMapping_1_14;
    }

    public Mapping getItemMapping_1_14() {
        return itemMapping_1_14;
    }

    public Mapping getItemMapping_1_17() {
        return itemMapping_1_17;
    }

    public Mapping getBlockMapping_1_17() {
        return blockMapping_1_17;
    }

    public Mapping getBlockMapping_1_19() {
        return blockMapping_1_19;
    }
}