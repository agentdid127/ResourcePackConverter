package com.agentdid127.resourcepack.forwards.impl;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.JsonUtil;
import com.agentdid127.resourcepack.library.utilities.Logger;
import com.agentdid127.resourcepack.library.utilities.Util;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class BlockStateConverter extends Converter {
    private boolean anyChanges;
    private int from, to;

    public BlockStateConverter(PackConverter packConverter, int from, int to) {
        super(packConverter);
        this.anyChanges = false;
        this.from = from;
        this.to = to;
    }

    /**
     * Updates blockstates in blockstates folder
     * 
     * @param pack
     * @throws IOException
     */
    @Override
    public void convert(Pack pack) throws IOException {
        Path states = pack.getWorkingPath().resolve("assets/minecraft/blockstates".replace("/", File.separator));
        if (!states.toFile().exists())
            return;
        Files.list(states)
                .filter(file -> file.toString().endsWith(".json"))
                .forEach(file -> {
                    try {
                        JsonObject json = JsonUtil.readJson(packConverter.getGson(), file);

                        // process multipart
                        if (json.has("multipart")) {
                            JsonElement multipart = json.get("multipart");
                            if (multipart.isJsonArray()) {
                                JsonArray multipartArray = multipart.getAsJsonArray();
                                for (JsonElement element : multipartArray) {
                                    JsonObject multipartObject = element.getAsJsonObject();
                                    for (Map.Entry<String, JsonElement> entry : multipartObject.entrySet())
                                        updateModelPath(entry);
                                }
                            }
                        }

                        // process variants
                        if (json.has("variants")) {
                            JsonElement variants = json.get("variants");
                            if (variants.isJsonObject()) {
                                JsonObject variantsObject = variants.getAsJsonObject();
                                // change "normal" key to ""
                                if (from <= Util.getVersionProtocol(packConverter.getGson(), "1.12.2")
                                        && to >= Util.getVersionProtocol(packConverter.getGson(), "1.13")
                                        && variantsObject.has("normal")) {
                                    JsonElement normal = variantsObject.get("normal");
                                    if (normal instanceof JsonObject || normal instanceof JsonArray) {
                                        variantsObject.add("", normal);
                                        variantsObject.remove("normal");
                                        anyChanges = true;
                                    }
                                }

                                // update model paths to prepend block
                                for (Map.Entry<String, JsonElement> entry : variantsObject.entrySet())
                                    updateModelPath(entry);
                            }
                        }

                        if (anyChanges) {
                            JsonUtil.writeJson(packConverter.getGson(), file, json);
                            if (PackConverter.DEBUG)
                                Logger.log("      Converted " + file.getFileName());
                        }
                    } catch (IOException e) {
                        Util.propagate(e);
                    }
                });
    }

    /**
     * Updates Model paths
     * 
     * @param entry
     */
    private void updateModelPath(Map.Entry<String, JsonElement> entry) {
        if (entry.getValue() instanceof JsonObject) {
            JsonObject value = (JsonObject) entry.getValue();
            if (value.has("model"))
                updateModelObject(value);
        } else if (entry.getValue() instanceof JsonArray) {
            for (JsonElement jsonElement : ((JsonArray) entry.getValue())) {
                if (jsonElement instanceof JsonObject) {
                    JsonObject value = (JsonObject) jsonElement;
                    if (value.has("model"))
                        updateModelObject(value);
                }
            }
        }
    }

    private void updateModelObject(JsonObject value) {
        NameConverter nameConverter = packConverter.getConverter(NameConverter.class);

        String[] split = value.get("model").getAsString().split("/");
        String val = split[split.length - 1];
        String prefix = value.get("model").getAsString().substring(0,
                value.get("model").getAsString().length() - val.length());

        if (from < Util.getVersionProtocol(packConverter.getGson(), "1.9")
                && to >= Util.getVersionProtocol(packConverter.getGson(), "1.9")) {
            prefix = "block/" + prefix;
            anyChanges = true;
        }

        if (from < Util.getVersionProtocol(packConverter.getGson(), "1.13")
                && to >= Util.getVersionProtocol(packConverter.getGson(), "1.13")) {
            val = nameConverter.getBlockMapping().remap(val);
            prefix = prefix.replaceAll("blocks", "block");
            anyChanges = true;
        }

        if (from < Util.getVersionProtocol(packConverter.getGson(), "1.14")
                && to >= Util.getVersionProtocol(packConverter.getGson(), "1.14")) {
            val = nameConverter.getNewBlockMapping().remap(val);
            anyChanges = true;
        }

        if (from < Util.getVersionProtocol(packConverter.getGson(), "1.17")
                && to >= Util.getVersionProtocol(packConverter.getGson(), "1.17")) {
            val = nameConverter.getBlockMapping17().remap(val);
            anyChanges = true;
        }

        if (from < Util.getVersionProtocol(packConverter.getGson(), "1.19")
                && to >= Util.getVersionProtocol(packConverter.getGson(), "1.19")) {
            val = nameConverter.getBlockMapping19().remap(val);
            anyChanges = true;
        }

        if (from < Util.getVersionProtocol(packConverter.getGson(), "1.19.3")
                && to >= Util.getVersionProtocol(packConverter.getGson(), "1.19.3")) {
            prefix = "minecraft:" + prefix;
            anyChanges = true;
        }

        if (anyChanges)
            value.addProperty("model", prefix + val);
    }
}