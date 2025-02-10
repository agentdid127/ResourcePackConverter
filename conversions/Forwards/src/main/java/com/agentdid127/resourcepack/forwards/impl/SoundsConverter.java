package com.agentdid127.resourcepack.forwards.impl;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.FileUtil;
import com.agentdid127.resourcepack.library.utilities.JsonUtil;
import com.agentdid127.resourcepack.library.utilities.Logger;
import com.agentdid127.resourcepack.library.utilities.Util;
import com.google.gson.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

public class SoundsConverter extends Converter {
    public SoundsConverter(PackConverter packConverter) {
        super(packConverter);
    }

    @Override
    public boolean shouldConvert(Gson gson, int from, int to) {
        return from <= Util.getVersionProtocol(gson, "1.12.2") && to >= Util.getVersionProtocol(gson, "1.13");
    }

    /**
     * Updates Sounds
     *
     * @param pack
     * @throws IOException
     */
    @Override
    public void convert(Pack pack) throws IOException {
        Path soundsJsonPath = pack.getWorkingPath().resolve("assets/minecraft/sounds.json".replace("/", File.separator));
        if (!soundsJsonPath.toFile().exists()) {
            return;
        }

        JsonObject sounds = JsonUtil.readJson(packConverter.getGson(), soundsJsonPath);
        JsonObject newSoundsObject = new JsonObject();
        for (Map.Entry<String, JsonElement> entry : sounds.entrySet()) {
            if (entry.getValue().isJsonObject()) {
                JsonObject soundObject = entry.getValue().getAsJsonObject();
                if (soundObject.has("sounds") && soundObject.get("sounds").isJsonArray()) {
                    JsonArray soundsArray = soundObject.getAsJsonArray("sounds");

                    JsonArray newSoundsArray = new JsonArray();
                    for (JsonElement jsonElement : soundsArray) {
                        String sound;
                        if (jsonElement instanceof JsonObject) {
                            sound = ((JsonObject) jsonElement).get("name").getAsString();
                        } else if (jsonElement instanceof JsonPrimitive) {
                            sound = jsonElement.getAsString();
                        } else {
                            throw new IllegalArgumentException("Unknown element type: " + jsonElement.getClass().getSimpleName());
                        }

                        Path baseSoundsPath = pack.getWorkingPath().resolve("assets/minecraft/sounds".replace("/", File.separator));
                        Path path = baseSoundsPath.resolve(sound + ".ogg");
                        if (!FileUtil.fileExistsCorrectCasing(path)) {
                            String rewrite = path.toFile().getCanonicalPath().substring(
                                    baseSoundsPath.toString().length() + 1,
                                    path.toFile().getCanonicalPath().length() - 4);
                            Logger.debug("Rewriting Sound: '" + sound + "' -> '" + rewrite + "'");
                            sound = rewrite;
                        } else {
                            sound = jsonElement.getAsString();
                        }

                        // windows fix
                        sound = sound.replaceAll("\\\\", "/");

                        JsonElement newSound = null;
                        if (jsonElement instanceof JsonObject) {
                            ((JsonObject) jsonElement).addProperty("name", sound);
                            newSound = jsonElement;
                        } else if (jsonElement instanceof JsonPrimitive) {
                            newSound = new JsonPrimitive(jsonElement.getAsString());
                        }

                        newSoundsArray.add(newSound);
                    }
                    soundObject.add("sounds", newSoundsArray);
                }

                newSoundsObject.add(entry.getKey().toLowerCase(), soundObject);
            }
        }

        JsonUtil.writeJson(packConverter.getGson(), soundsJsonPath, newSoundsObject);
    }
}
