package com.agentdid127.resourcepack.forwards.impl.textures.slicing;

import java.util.LinkedList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Slice {
    public String path;
    public String name;
    public int width;
    public int height;
    public JsonObject predicate;
    public Texture[] textures;
    public boolean delete;

    public static Slice[] fromJson(JsonArray array) {
        List<Slice> slices = new LinkedList<>();

        for (JsonElement element : array) {
            Slice slice = new Slice();

            JsonObject guiObject = element.getAsJsonObject();
            slice.path = guiObject.get("path").getAsString();

            if (guiObject.has("name"))
                slice.name = guiObject.get("name").getAsString();
            else slice.name = null;

            slice.width = guiObject.get("width").getAsInt();
            slice.height = guiObject.get("height").getAsInt();
            slice.textures = Texture.parse(guiObject.get("textures").getAsJsonArray());
            
            if (guiObject.has("delete"))
                slice.delete = guiObject.get("delete").getAsBoolean();
            else slice.delete = true;

            if (guiObject.has("predicate")) 
                slice.predicate = guiObject.get("predicate").getAsJsonObject();
            else
                slice.predicate = new JsonObject();

            slices.add(slice);
        }

        return slices.toArray(new Slice[] {});
    }
}