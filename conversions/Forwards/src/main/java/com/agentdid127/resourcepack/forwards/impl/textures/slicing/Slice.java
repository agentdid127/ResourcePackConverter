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

    public static Slice parse(JsonObject object) {
        Slice slice = new Slice();
        slice.path = object.get("path").getAsString();

        if (object.has("name"))
            slice.name = object.get("name").getAsString();
        else slice.name = null;

        slice.width = object.get("width").getAsInt();
        slice.height = object.get("height").getAsInt();
        
        slice.textures = Texture.parseArray(object.get("textures").getAsJsonArray());
        
        if (object.has("delete"))
            slice.delete = object.get("delete").getAsBoolean();
        else slice.delete = true;

        if (object.has("predicate")) 
            slice.predicate = object.get("predicate").getAsJsonObject();
        else
            slice.predicate = new JsonObject();

        return slice;
    }

    public static Slice[] parseArray(JsonArray array) {
        List<Slice> slices = new LinkedList<>();
        for (JsonElement element : array) 
            slices.add(Slice.parse(element.getAsJsonObject()));
        return slices.toArray(new Slice[] {});
    }
}