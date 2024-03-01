package com.agentdid127.resourcepack.forwards.impl.textures;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.agentdid127.resourcepack.forwards.impl.textures.slicing.Box;
import com.agentdid127.resourcepack.forwards.impl.textures.slicing.Slice;
import com.agentdid127.resourcepack.forwards.impl.textures.slicing.Texture;
import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.Util;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.ImageConverter;
import com.agentdid127.resourcepack.library.utilities.Logger;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class SlicerConverter extends Converter {
    private int from;

    public SlicerConverter(PackConverter converter, int from) {
        super(converter);
        this.from = from;
    }

    @Override
    public void convert(Pack pack) throws IOException {
        Path texturesPath = pack.getWorkingPath().resolve("assets" + File.separator + "minecraft" + File.separator + "textures");

        Path guiPath = texturesPath.resolve("gui");
        if (!guiPath.toFile().exists())
            return; // No need to do any slicing.

        Path spritesPath = guiPath.resolve("sprites");
        if (!spritesPath.toFile().exists())
            spritesPath.toFile().mkdirs();

        Gson gson = packConverter.getGson();

        JsonArray array = Util.readJsonResource(gson, "/forwards/gui.json", JsonArray.class);
        Slice[] slices = Slice.parseArray(array);

        for (Slice slice : slices) {
            if (!testPredicate(gson, slice.getPredicate()))
                continue;

            Path path = guiPath.resolve(slice.getPath());
            if (!path.toFile().exists()) {
                Logger.log("GUI Texture '" + slice.getPath() + "' does not exist! Skipping...");
                continue;
            }

            ImageConverter converter = new ImageConverter(slice.getWidth(), slice.getHeight(), path);
            
            Path imagePath = path;
            if (slice.getPathName() != null) 
                imagePath = path.resolveSibling(slice.getPathName());
            
            for (Texture texture : slice.getTextures()) {
                Path texturePath = guiPath.resolve(texture.getPath());
                ensureParentExists(texturePath);
                if (!testPredicate(gson, texture.getPredicate()))
                    continue;
                    
                try {
                    Box box = texture.getBox();

                    converter.saveSlice(
                        box.getX(), 
                        box.getY(), 
                        box.getWidth(), 
                        box.getHeight(), 
                        texturePath);

                    if (texture.shouldRemove()) {
                        converter.fillEmpty(
                            box.getX(), 
                            box.getY(), 
                            box.getWidth(),
                            box.getHeight());
                    }

                    JsonObject metadata = texture.getMetadata();
                    if (metadata.keySet().isEmpty() || metadata.entrySet().isEmpty())
                        continue;

                    Path metadataPath = texturePath.resolveSibling(texturePath.getFileName() + ".mcmeta");
                    write_json(metadataPath, metadata);
                } catch (Exception exception) {
                    Logger.log("Failed to slice texture '" + texture.getPath() + "' (error='" + exception.getLocalizedMessage() + "')");
                    Logger.log("  - box: (x=" + texture.getBox().getX() + ", y=" + texture.getBox().getY() + ", width=" + texture.getBox().getWidth() + ", height=" + texture.getBox().getHeight() + ")");
                }
            }

            if (!path.toFile().delete())
                Logger.log("Failed to remove '" + path.getFileName() + "' whilst slicing.");   

            if (!slice.shouldDelete())
                converter.store(imagePath);
        }
    }

    private boolean testPredicate(Gson gson, JsonObject predicate) {
        if (predicate.has("protocol")) {
            JsonObject protocol = predicate.getAsJsonObject("protocol");
            
            Integer min_inclusive = null;
            Integer max_inclusive = null;

            JsonElement min_inclusive_prim = protocol.get("min_inclusive");
            if (min_inclusive_prim.isJsonPrimitive() && min_inclusive_prim.getAsJsonPrimitive().isNumber()) 
                min_inclusive = min_inclusive_prim.getAsInt();
        
            if (min_inclusive == null)
                min_inclusive = 4; // hardcoded bruh

            JsonElement max_inclusive_prim = protocol.get("max_inclusive");
            if (max_inclusive_prim.isJsonPrimitive() && max_inclusive_prim.getAsJsonPrimitive().isNumber()) 
                max_inclusive = max_inclusive_prim.getAsInt();

            if (max_inclusive == null)
                max_inclusive = Util.getLatestProtocol(gson);

            if (from < min_inclusive || from > max_inclusive)
                return false;
        }

        return true;
    }

    private void ensureParentExists(Path path) {
        Path parentPath = Paths.get(path.toFile().getParent());
        if (!parentPath.toFile().exists())
            parentPath.toFile().mkdirs();
    }

    private void write_json(Path path, JsonObject object) throws IOException {
        Files.write(path, object.toString().getBytes());
    }
}