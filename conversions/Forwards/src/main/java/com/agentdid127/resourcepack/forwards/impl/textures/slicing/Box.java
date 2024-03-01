package com.agentdid127.resourcepack.forwards.impl.textures.slicing;

import com.google.gson.JsonObject;

public class Box {
    public int x;
    public int y;
    public int width;
    public int height;

    public Box(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public static Box parse(JsonObject object) {
        int x = object.get("x").getAsInt();
        int y = object.get("y").getAsInt();
        int width = object.get("width").getAsInt();
        int height = object.get("height").getAsInt();
        return new Box(x, y, width, height);
    }
}
