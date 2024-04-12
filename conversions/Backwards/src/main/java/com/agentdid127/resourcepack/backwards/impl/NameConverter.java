package com.agentdid127.resourcepack.backwards.impl;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;
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
                if (PackConverter.DEBUG)
                    Logger.log("MCPatcher exists, switching to optifine");
                if (minecraftPath.resolve("optifine").toFile().exists()) {
                    if (PackConverter.DEBUG)
                        Logger.log("OptiFine exists, merging directories");
                    Util.mergeDirectories(minecraftPath.resolve("optifine").toFile(),
                            minecraftPath.resolve("mcpatcher").toFile());
                } else
                    Files.move(minecraftPath.resolve("mcpatcher"), minecraftPath.resolve("optifine"));
                if (minecraftPath.resolve("mcpatcher").toFile().exists())
                    Util.deleteDirectoryAndContents(minecraftPath.resolve("mcpatcher"));
            }
        }

        Path modelsPath = minecraftPath.resolve("models");

        Path itemModelsPath = modelsPath.resolve("item");
        Path blockModelsPath = modelsPath.resolve("block");

        if (modelsPath.toFile().exists()) {
            if (to < Util.getVersionProtocol(packConverter.getGson(), "1.19"))
                renameAll(blockMapping19, ".json", blockModelsPath);

            // Update 1.14 items
            if (to < Util.getVersionProtocol(packConverter.getGson(), "1.14"))
                renameAll(newItemMapping, ".json", itemModelsPath);

            // 1.13 Models
            if (to < Util.getVersionProtocol(packConverter.getGson(), "1.13")) {
                renameAll(itemMapping, ".json", itemModelsPath);
                renameAll(blockMapping, ".json", blockModelsPath);
                Path bannerModelPath = itemModelsPath.resolve("banner.json");
                if (bannerModelPath.toFile().exists())
                    bannerModelPath.toFile().delete();
            }
        }

        // Update BlockStates
        Path blockStates = minecraftPath.resolve("blockstates");
        if (blockStates.toFile().exists())
            renameAll(blockMapping, ".json", blockStates);

        // Update textures
        Path texturesPath = minecraftPath.resolve("textures");
        if (texturesPath.toFile().exists()) {
            Path texturesBlockPath = texturesPath.resolve("block");
            Path texturesItemPath = texturesPath.resolve("item");

            // 1.19
            if (to < Util.getVersionProtocol(packConverter.getGson(), "1.19")
                    && from >= Util.getVersionProtocol(packConverter.getGson(), "1.19"))
                renameAll(blockMapping19, ".png", texturesBlockPath);

            // Entities
            Path entityPath = texturesPath.resolve("entity");

            // 1.17 Squid
            if (to < Util.getVersionProtocol(packConverter.getGson(), "1.17")
                    && from >= Util.getVersionProtocol(packConverter.getGson(), "1.17")) {
                renameAll(blockMapping17, ".png", texturesBlockPath);
                renameAll(itemMapping17, ".png", texturesItemPath);
                renameAll(blockMapping17, ".png", blockModelsPath);
                renameAll(itemMapping17, ".png", itemModelsPath);
                Path newSquidPath = entityPath.resolve("squid/squid.png");
                if (newSquidPath.toFile().exists())
                    Files.move(newSquidPath, entityPath.resolve("squid.png"));
            }

            // 1.16 Iron golems
            if (from >= Util.getVersionProtocol(packConverter.getGson(), "1.16")
                    && to < Util.getVersionProtocol(packConverter.getGson(), "1.16")) {
                Path newIronGolemPath = entityPath
                        .resolve("iron_golem/iron_golem.png".replace("/", File.separator));
                if (newIronGolemPath.toFile().exists())
                    Files.move(newIronGolemPath, entityPath.resolve("iron_golem.png"));
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
                if (entityPath.resolve("end_crystal").toFile().exists())
                    Files.move(entityPath.resolve("end_crystal"), entityPath.resolve("endercrystal"));

                findEntityFiles(texturesPath.resolve("entity"));
            }
        }

        // Less than 1.12
        if (from > Util.getVersionProtocol(packConverter.getGson(), "1.13")
                && to < Util.getVersionProtocol(packConverter.getGson(), "1.13")) {
            if (PackConverter.DEBUG)
                Logger.log("Finding files that are greater than 1.12");
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
        if (!path.toFile().exists())
            return;
        File directory = path.toFile();
        for (File file : directory.listFiles()) {
            if (file.isDirectory())
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
        if (!path.toFile().exists())
            return;
        File directory = path.toFile();
        for (File file : directory.listFiles()) {
            if (file.isDirectory()) {
                if (file.getName().equals("item")) {
                    if (PackConverter.DEBUG)
                        Logger.log("Found Items folder, renaming");
                    Util.renameFile(path.resolve(file.getName()), file.getName().replaceAll("item", "items"));
                }

                if (file.getName().equals("block")) {
                    if (PackConverter.DEBUG)
                        Logger.log("Found blocks folder, renaming");
                    Util.renameFile(path.resolve(file.getName()), file.getName().replaceAll("block", "blocks"));
                }

                findFiles(file.toPath());
            }

            if (file.getName().contains("("))
                Util.renameFile(path.resolve(file.getName()), file.getName().replaceAll("[()]", ""));

            if (!file.getName().equals(file.getName().toLowerCase()))
                if (PackConverter.DEBUG)
                    Logger.log("Renamed: " + file.getName() + "->" + file.getName().toLowerCase());

            Util.renameFile(path.resolve(file.getName()), file.getName().toLowerCase());
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
            List<String> grasses = Arrays.asList("tall_grass", "grass", "grass_block");
            if (from <= Util.getVersionProtocol(packConverter.getGson(), "1.12.2")
                    && (path.endsWith("blockstates") || path.endsWith("textures/block"))) {
                grasses.stream().forEach(name -> {
                    String newName = mapping.remap(name);
                    Boolean ret = Util.renameFile(Paths.get(path + File.separator + name + extension),
                            newName + extension);
                    if (ret == null)
                        return;
                    if (ret && PackConverter.DEBUG)
                        Logger.log("      Renamed: " + name + extension + "->" + newName + extension);
                    else if (!ret)
                        System.err.println("      Failed to rename: " + name + extension + "->" + newName + extension);
                });
            }

            if (from > Util.getVersionProtocol(packConverter.getGson(), "1.12.2") &&
                    to <= Util.getVersionProtocol(packConverter.getGson(), "1.12.2")) {
                Arrays.asList("snow", "snow_block").stream().forEach(name -> {
                    String newName = mapping.remap(name);
                    if (!extension.equals(".png")) {
                        Boolean ret = Util.renameFile(Paths.get(path + File.separator + name + extension),
                                newName + extension);
                        if (ret == null)
                            return;
                        if (ret && PackConverter.DEBUG)
                            Logger.log("      Renamed: " + name + extension + "->" + newName + extension);
                        else if (!ret)
                            System.err.println(
                                    "      Failed to rename: " + name + extension + "->" + newName + extension);
                    }
                });
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
                    Boolean ret = Util.renameFile(path1, newName + extension);
                    if (ret == null)
                        return;
                    if (ret && PackConverter.DEBUG)
                        Logger.log(
                                "      Renamed: " + path1.getFileName().toString() + "->" + newName + extension);
                    else if (!ret)
                        System.err.println("      Failed to rename: " + path1.getFileName().toString() + "->" + newName
                                + extension);
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
