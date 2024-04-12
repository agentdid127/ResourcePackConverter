package com.agentdid127.resourcepack.library.utilities;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Mapping {
    protected final Map<String, String> mapping = new HashMap<>();

    public Mapping(Gson gson, String path, String key, boolean backwards) {
        load(gson, backwards ? "backwards" : "forwards", path, key);
    }

    protected void load(Gson gson, String source, String path, String key) {
        JsonObject object = JsonUtil.readJsonResource(gson, "/" + source + "/" + path + ".json")
                .getAsJsonObject(key);
        if (object == null)
            return;
        for (Map.Entry<String, JsonElement> entry : object.entrySet())
            this.mapping.put(entry.getKey(), entry.getValue().getAsString());
    }

    /**
     * @return remapped or in if not present
     */
    public String remap(String in) {
        return mapping.getOrDefault(in, in);
    }
}
