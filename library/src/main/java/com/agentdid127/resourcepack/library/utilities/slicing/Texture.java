package com.agentdid127.resourcepack.library.utilities.slicing;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Texture {
    private final String path;
    private final Box box;
    private final boolean remove;
    private final JsonObject predicate;
    private final JsonObject metadata;

    public static HashMap<String, JsonObject> METADATA_CACHE = new HashMap<>();

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
            METADATA_CACHE.put("button_widget", buttonWidget);
        }
    }

    public Texture(String path, Box box, boolean remove, JsonObject predicate, JsonObject metadata) {
        this.path = path;
        this.box = box;
        this.remove = remove;
        this.predicate = predicate;
        this.metadata = metadata;
    }

    public Texture(String path, Box box, boolean remove, JsonObject predicate) {
        this.path = path;
        this.box = box;
        this.remove = remove;
        this.predicate = predicate;
        this.metadata = new JsonObject();
    }

    public Texture(String path, Box box, boolean remove) {
        this.path = path;
        this.box = box;
        this.remove = remove;
        this.predicate = new JsonObject();
        this.metadata = new JsonObject();
    }

    public Texture(String path, Box box) {
        this.path = path;
        this.box = box;
        this.remove = false;
        this.predicate = new JsonObject();
        this.metadata = new JsonObject();
    }

    public String getPath() {
        return path;
    }

    public Box getBox() {
        return box;
    }

    public boolean shouldRemove() {
        return remove;
    }

    public JsonObject getPredicate() {
        return predicate;
    }

    public JsonObject getMetadata() {
        return metadata;
    }

    public static Texture parse(JsonObject object) {
        String path = object.get("path").getAsString();
        Box box = Box.parse(object.get("box").getAsJsonObject());

        boolean remove = false;
        if (object.has("remove")) {
            remove = object.get("remove").getAsBoolean();
        }

        JsonObject predicate;
        if (object.has("predicate")) {
            predicate = object.get("predicate").getAsJsonObject();
        } else {
            predicate = new JsonObject();
        }

        JsonObject metadata;
        if (object.has("metadata")) {
            JsonElement metadataElement = object.get("metadata");
            metadata = metadataElement.isJsonObject() ? metadataElement.getAsJsonObject() : METADATA_CACHE.getOrDefault(metadataElement.getAsString(), new JsonObject());
        } else {
            metadata = new JsonObject();
        }

        return new Texture(path, box, remove, predicate, metadata);
    }

    public static Texture[] parseArray(JsonArray array) {
        List<Texture> textures = new LinkedList<>();
        array.forEach(element -> textures.add(Texture.parse(element.getAsJsonObject())));
        return textures.toArray(new Texture[]{});
    }
}
