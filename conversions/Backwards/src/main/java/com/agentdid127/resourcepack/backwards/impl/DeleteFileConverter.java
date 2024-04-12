package com.agentdid127.resourcepack.backwards.impl;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.JsonUtil;
import com.agentdid127.resourcepack.library.utilities.Util;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DeleteFileConverter extends Converter {
    int from, to;

    public DeleteFileConverter(PackConverter packConverter, int from, int to) {
        super(packConverter);
        this.from = from;
        this.to = to;
    }

    @Override
    public void convert(Pack pack) throws IOException {
        Path models = pack.getWorkingPath()
                .resolve("assets/minecraft/models".replace("/", File.separator));
        Path textures = pack.getWorkingPath()
                .resolve("assets/minecraft/textures".replace("/", File.separator));

        for (int i = from; i > to; i--) {
            deleteBlocks(models, textures, i);
            deleteItems(models, textures, i);
            deleteEntities(textures, i);
        }

        findFiles(models);
        findFiles(textures);

        if (from >= Util.getVersionProtocol(packConverter.getGson(), "1.19.3")
                && to < Util.getVersionProtocol(packConverter.getGson(), "1.19.3")) {
            Path atlases = pack.getWorkingPath()
                    .resolve("assets/minecraft/atlases".replace("/", File.separator));
            if (atlases.toFile().exists())
                Util.deleteDirectoryAndContents(atlases);
        }
    }

    protected void findFiles(Path path) throws IOException {
        if (!path.toFile().exists())
            return;
        File directory = path.toFile();
        File[] filesList = directory.listFiles();
        for (File file : filesList)
            if (file.isDirectory())
                findFiles(file.toPath());
        filesList = directory.listFiles();
        if (filesList.length == 0)
            Files.deleteIfExists(directory.toPath());
    }

    public void deleteBlocks(Path models, Path textures, int version) throws IOException {
        String protocol = Util.getVersionFromProtocol(packConverter.getGson(), version);
        JsonObject blocks = JsonUtil.readJsonResource(packConverter.getGson(), "/backwards/delete/blocks.json");

        if (blocks.has(protocol)) {
            Path blockMPath, blockTPath;
            if (version < Util.getVersionProtocol(packConverter.getGson(), "1.13")) {
                blockMPath = models.resolve("block");
                blockTPath = textures.resolve("blocks");
            } else {
                blockMPath = models.resolve("block");
                blockTPath = textures.resolve("block");
            }

            JsonArray versionBlock = blocks.get(protocol).getAsJsonArray();
            for (JsonElement item : versionBlock) {
                String pathName = item.getAsString();
                Files.deleteIfExists(blockMPath.resolve(pathName + ".json"));
                Files.deleteIfExists(blockTPath.resolve(pathName + ".png"));
                Files.deleteIfExists(blockTPath.resolve(pathName + ".png.mcmeta"));
            }
        }
    }

    public void deleteItems(Path models, Path textures, int version) throws IOException {
        String protocol = Util.getVersionFromProtocol(packConverter.getGson(), version);
        JsonObject items = JsonUtil.readJsonResource(packConverter.getGson(), "/backwards/delete/items.json");

        if (items.has(protocol)) {
            Path itemMPath, itemTPath;
            if (version < Util.getVersionProtocol(packConverter.getGson(), "1.13")) {
                itemMPath = models.resolve("item");
                itemTPath = textures.resolve("items");
            } else {
                itemMPath = models.resolve("item");
                itemTPath = textures.resolve("item");
            }

            JsonArray versionItem = items.get(protocol).getAsJsonArray();
            for (JsonElement item : versionItem) {
                String pathName = item.getAsString();
                Files.deleteIfExists(itemMPath.resolve(pathName + ".json"));
                Files.deleteIfExists(itemTPath.resolve(pathName + ".png"));
                Files.deleteIfExists(itemTPath.resolve(pathName + ".png.mcmeta"));
            }
        }
    }

    public void deleteEntities(Path textures, int version) throws IOException {
        String protocol = Util.getVersionFromProtocol(packConverter.getGson(), version);
        JsonObject entities = JsonUtil.readJsonResource(packConverter.getGson(), "/backwards/delete/entities.json");
        if (entities.has(protocol)) {
            Path entityTPath = textures.resolve("entity");
            JsonArray versionEntity = entities.get(protocol).getAsJsonArray();
            for (JsonElement item : versionEntity) {
                String pathName = item.getAsString();
                Files.deleteIfExists(entityTPath.resolve(pathName + ".png"));
                Files.deleteIfExists(entityTPath.resolve(pathName + ".png.mcmeta"));
            }
        }
    }
}
