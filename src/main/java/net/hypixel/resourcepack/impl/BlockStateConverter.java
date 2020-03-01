package net.hypixel.resourcepack.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.hypixel.resourcepack.Converter;
import net.hypixel.resourcepack.PackConverter;
import net.hypixel.resourcepack.Util;
import net.hypixel.resourcepack.pack.Pack;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;

public class BlockStateConverter extends Converter {

    public BlockStateConverter(PackConverter packConverter) {
        super(packConverter);
    }

    @Override
    public void convert(Pack pack) throws IOException {
        Path states = pack.getWorkingPath().resolve("assets" + File.separator + "minecraft" + File.separator + "blockstates");
        if (!states.toFile().exists()) return;

        Files.list(states)
                .filter(file -> file.toString().endsWith(".json"))
                .forEach(file -> {
                    try {
                        JsonObject json = Util.readJson(packConverter.getGson(), file);

                        boolean anyChanges = false;
                        JsonObject variantsObject = json.getAsJsonObject("variants");
                        if (variantsObject != null) {
                            // change "normal" key to ""
                            JsonElement normal = variantsObject.get("normal");
                            if (normal instanceof JsonObject || normal instanceof JsonArray) {
                                variantsObject.add("", normal);
                                variantsObject.remove("normal");

                                anyChanges = true;
                            }

                            // update model paths to prepend block
                            for (Map.Entry<String, JsonElement> entry : variantsObject.entrySet()) {
                                if (entry.getValue() instanceof JsonObject) {
                                    JsonObject value = (JsonObject) entry.getValue();
                                    if (value.has("model")) {
                                        value.addProperty("model", "block/" + value.get("model").getAsString());
                                        anyChanges = true;
                                    }

                                } else if (entry.getValue() instanceof JsonArray) { // some states have arrays
                                    for (JsonElement jsonElement : ((JsonArray) entry.getValue())) {
                                        if (jsonElement instanceof JsonObject) {
                                            JsonObject value = (JsonObject) jsonElement;
                                            if (value.has("model")) {
                                                value.addProperty("model", "block/" + value.get("model").getAsString());
                                                anyChanges = true;
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        if (anyChanges) {
                            Files.write(file, Collections.singleton(packConverter.getGson().toJson(json)), Charset.forName("UTF-8"));

                            if (PackConverter.DEBUG) System.out.println("      Converted " + file.getFileName());
                        }
                    } catch (IOException e) {
                        Util.propagate(e);
                    }
                });
    }
}