package com.agentdid127.resourcepack.forwards.impl.textures.slicing;

import com.google.gson.JsonObject;

public class Box {
    private int x;
    private int y;
    private int width;
    private int height;

    public Box(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }

    public static Box parse(JsonObject object) {
        int x = object.get("x").getAsInt();
        int y = object.get("y").getAsInt();
        int width = object.get("width").getAsInt();
        int height = object.get("height").getAsInt();
        return new Box(x, y, width, height);
    }
}
