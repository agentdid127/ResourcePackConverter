package com.agentdid127.resourcepack.forwards.impl.textures;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

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

    public static HashMap<String, JsonObject> metatdataCache = new HashMap<>();

    public SlicerConverter(PackConverter converter, int from) {
        super(converter);
        this.from = from;
    }

    static {    
        {
            // Button Widget
            JsonObject buttonWidget = new JsonObject();
            JsonObject gui = new JsonObject();
            JsonObject scaling = new JsonObject();

            scaling.addProperty("type", "nine_slice");
            scaling.addProperty("width", 200);
            scaling.addProperty("height", 20);

            JsonObject border = new JsonObject();
            border.addProperty("left", 20);
            border.addProperty("top", 4);
            border.addProperty("right", 20);
            border.addProperty("bottom", 4);
            scaling.add("border", border);

            gui.add("scaling", scaling);
            buttonWidget.add("gui", gui);
            metatdataCache.put("button_widget", buttonWidget);
        }
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
        Slice[] slices = Slice.parse(array);

        for (Slice slice : slices) {
            if (!testPredicate(gson, slice.predicate))
                continue;
            try {
                Path path = guiPath.resolve(slice.path);
                if (!path.toFile().exists()) {
                    Logger.log("GUI Texture '" + slice.path + "' does not exist! Skipping...");
                    continue;
                }

                ImageConverter converter = new ImageConverter(slice.width, slice.height, path);
                for (Texture texture : slice.textures) {
                    Path texturePath = guiPath.resolve(texture.path);
                    ensureParentExists(texturePath);
                    if (!testPredicate(gson, texture.predicate))
                        continue;

                    converter.slice_and_save(
                        texture.position.x, 
                        texture.position.y, 
                        texture.width, 
                        texture.height, 
                        texturePath);

                    JsonObject metadata = texture.metadata;
                    if (metadata.keySet().isEmpty() || metadata.entrySet().isEmpty())
                        continue;

                    Path metadataPath = texturePath.resolveSibling(texturePath.getFileName() + ".mcmeta");
                    write_json(metadataPath, metadata);
                }

                if (!slice.delete)
                    continue;
                
                if (!path.toFile().delete())
                    Logger.log("Failed to remove " + pack.getFileName() + " after slicing.");
            } catch (Exception exception) {
                Logger.log("Failed to slice '" + slice.path + "'. Reason: '" + exception.getLocalizedMessage() + "'");
            }
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