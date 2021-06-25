package com.agentdid127.resourcepack.impl.forwards;

import com.agentdid127.resourcepack.Converter;
import com.agentdid127.resourcepack.PackConverter;
import com.agentdid127.resourcepack.Util;
import com.agentdid127.resourcepack.pack.Pack;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;

public class SoundsConverter extends Converter {

    public SoundsConverter(PackConverter packConverter) {
        super(packConverter);
    }

    /**
     * Updates Sounds
     * @param pack
     * @throws IOException
     */
    @Override
    public void convert(Pack pack) throws IOException {
        Path soundsJsonPath = pack.getWorkingPath().resolve("assets" + File.separator + "minecraft" + File.separator + "sounds.json");
        if (!soundsJsonPath.toFile().exists()) return;

        JsonObject sounds = Util.readJson(packConverter.getGson(), soundsJsonPath);
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

                        Path baseSoundsPath = pack.getWorkingPath().resolve("assets" + File.separator + "minecraft" + File.separator + "sounds");
                        Path path = baseSoundsPath.resolve(sound + ".ogg");
                        if (!Util.fileExistsCorrectCasing(path)) {
                            String rewrite = path.toFile().getCanonicalPath().substring(baseSoundsPath.toString().length() + 1, path.toFile().getCanonicalPath().length() - 4);
                            if (PackConverter.DEBUG) System.out.println("      Rewriting Sound: '" + jsonElement.getAsString() + "' -> '" + rewrite + "'");
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

        Files.write(soundsJsonPath, Collections.singleton(packConverter.getGson().toJson(newSoundsObject)), Charset.forName("UTF-8"));
    }

}
