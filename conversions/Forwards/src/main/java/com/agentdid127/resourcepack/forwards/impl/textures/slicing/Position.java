package com.agentdid127.resourcepack.forwards.impl.textures.slicing;

import com.google.gson.JsonObject;

public class Position {
    public int x;
    public int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Position parse(JsonObject object) {
        int x = object.get("x").getAsInt();
        int y = object.get("y").getAsInt();
        return new Position(x, y);
    }
}
