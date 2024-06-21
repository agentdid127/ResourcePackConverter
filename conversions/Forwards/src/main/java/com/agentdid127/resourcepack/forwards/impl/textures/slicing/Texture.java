package com.agentdid127.resourcepack.forwards.impl.textures.slicing;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Texture {
    private String path;
    private Box box;
    private boolean remove;
    private JsonObject predicate;
    private JsonObject metadata;

    public static HashMap<String, JsonObject> metatdataCache = new HashMap<>();
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
        if (object.has("remove")) 
            remove = object.get("remove").getAsBoolean();    

        JsonObject predicate;
        if (object.has("predicate")) 
            predicate = object.get("predicate").getAsJsonObject();
        else
            predicate = new JsonObject();
        
        JsonObject metadata;
        if (object.has("metadata")) {
            JsonElement metadataElement = object.get("metadata");
            metadata = metadataElement.isJsonObject() ? metadataElement.getAsJsonObject() : metatdataCache.getOrDefault(metadataElement.getAsString(), new JsonObject());
        } else metadata = new JsonObject();

        return new Texture(path, box, remove, predicate, metadata);
    }

    public static Texture[] parseArray(JsonArray array) {
        List<Texture> textures = new LinkedList<>();
        for (JsonElement element : array) 
            textures.add(Texture.parse(element.getAsJsonObject()));
        return textures.toArray(new Texture[] {});
    }
}
