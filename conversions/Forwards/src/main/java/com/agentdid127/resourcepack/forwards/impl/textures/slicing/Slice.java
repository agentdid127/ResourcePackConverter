package com.agentdid127.resourcepack.forwards.impl.textures.slicing;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Slice {
    private String path;
    private String name;
    private int width;
    private int height;
    private JsonObject predicate;
    private Texture[] textures;
    private boolean delete;

    public String getPath() {
        return path.replace("/", File.separator);
    }

    public String getPathName() {
        return name;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public JsonObject getPredicate() {
        return predicate;
    }

    public Texture[] getTextures() {
        return textures;
    }

    public boolean shouldDelete() {
        return delete;
    }

    public static Slice parse(JsonObject object) {
        Slice slice = new Slice();
        slice.path = object.get("path").getAsString();

        if (object.has("name"))
            slice.name = object.get("name").getAsString();
        else
            slice.name = null;

        slice.width = object.get("width").getAsInt();
        slice.height = object.get("height").getAsInt();

        slice.textures = Texture.parseArray(object.get("textures").getAsJsonArray());

        if (object.has("delete"))
            slice.delete = object.get("delete").getAsBoolean();
        else
            slice.delete = true;

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