package com.agentdid127.resourcepack.forwards.impl.textures.slicing;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Texture {
    public String path;
    public Position position;
    public int width;
    public int height;
    public boolean remove;
    public JsonObject predicate;
    public JsonObject metadata;

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

    public Texture(String path, Position position, int width, int height, boolean remove, JsonObject predicate, JsonObject metadata) {
        this.path = path;
        this.position = position;
        this.width = width;
        this.height = height;
        this.remove = remove;
        this.predicate = predicate;
        this.metadata = metadata;
    }

    public Texture(String path, Position position, int width, int height, boolean remove, JsonObject predicate) {
        this.path = path;
        this.position = position;
        this.width = width;
        this.height = height;
        this.remove = remove;
        this.predicate = predicate;
        this.metadata = new JsonObject();
    }

    public Texture(String path, Position position, int width, int height, boolean remove) {
        this.path = path;
        this.position = position;
        this.width = width;
        this.height = height;
        this.remove = remove;
        this.predicate = new JsonObject();
        this.metadata = new JsonObject();
    }

    public Texture(String path, Position position, int width, int height) {
        this.path = path;
        this.position = position;
        this.width = width;
        this.height = height;
        this.remove = false;
        this.predicate = new JsonObject();
        this.metadata = new JsonObject();
    }

    public static Texture[] parse(JsonArray array) {
        List<Texture> textures = new LinkedList<>();

        for (JsonElement element : array) {            
            JsonObject textureObject = element.getAsJsonObject();
            String path = textureObject.get("path").getAsString();
            Position position = Position.parse(textureObject.get("position").getAsJsonObject());
            int width = textureObject.get("width").getAsInt();
            int height = textureObject.get("height").getAsInt();  
            
            boolean remove = false;
            if (textureObject.has("remove")) 
                remove = textureObject.get("remove").getAsBoolean();    

            JsonObject predicate;
            if (textureObject.has("predicate")) 
                predicate = textureObject.get("predicate").getAsJsonObject();
            else
                predicate = new JsonObject();
            
            JsonObject metadata;
            if (textureObject.has("metadata")) {
                JsonElement metadataElement = textureObject.get("metadata");
                metadata = metadataElement.isJsonObject() ? metadataElement.getAsJsonObject() : metatdataCache.getOrDefault(metadataElement.getAsString(), new JsonObject());
            } else metadata = new JsonObject();

            textures.add(new Texture(path, position, width, height, remove, predicate, metadata));
        }
        
        return textures.toArray(new Texture[] {});
    }
}
