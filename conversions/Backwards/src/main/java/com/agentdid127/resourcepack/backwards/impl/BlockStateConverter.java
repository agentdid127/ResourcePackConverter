package com.agentdid127.resourcepack.backwards.impl;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.JsonUtil;
import com.agentdid127.resourcepack.library.utilities.Logger;
import com.agentdid127.resourcepack.library.utilities.Util;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Stream;

public class BlockStateConverter extends Converter {
    private final int from;
    private final int to;
    private boolean anyChanges;

    public BlockStateConverter(PackConverter packConverter, int from, int to) {
        super(packConverter);
        this.from = from;
        this.to = to;
    }

    @Override
    public boolean shouldConvert(Gson gson, int from, int to) {
        return true;
    }

    /**
     * Updates blockstates in blockstates folder
     *
     * @param pack
     * @throws IOException
     */
    @Override
    public void convert(Pack pack) throws IOException {
        Path blockstatesPath = pack.getWorkingPath().resolve("assets/minecraft/blockstates".replace("/", File.separator));
        if (!blockstatesPath.toFile().exists()) {
            return;
        }

        try (Stream<Path> pathStream = Files.list(blockstatesPath).filter(file -> file.toString().endsWith(".json"))) {
            pathStream.forEach(file -> {
                try {
                    JsonObject json = JsonUtil.readJson(packConverter.getGson(), file);
                    anyChanges = false;

                    // process multipart
                    JsonArray multipartArray = json.getAsJsonArray("multipart");

                    if (multipartArray != null) {
                        if (to < Util.getVersionProtocol(packConverter.getGson(), "1.9")) {
                            // TODO: Convert Multipart to variants
                            Files.delete(file);
                            return;
                        } else {
                            for (int i = 0; i < multipartArray.size(); i++) {
                                JsonObject multipartObject = multipartArray.get(i)
                                        .getAsJsonObject();
                                for (Map.Entry<String, JsonElement> entry : multipartObject.entrySet())
                                    updateModelPath(entry);
                            }
                        }
                    }

                    // process variants
                    JsonObject variantsObject = json.getAsJsonObject("variants");
                    if (variantsObject != null) {
                        // change "normal" key to ""
                        if (from >= Util.getVersionProtocol(packConverter.getGson(), "1.13")
                                && to < Util.getVersionProtocol(packConverter.getGson(), "1.13")) {

                            if (!variantsObject.has("")) {
                                // TODO: find a better way to deal with this.
                                Files.delete(file);
                                return;
                            }
                            JsonElement normal = variantsObject.get("");
                            if (normal instanceof JsonObject || normal instanceof JsonArray) {
                                variantsObject.add("normal", normal);
                                variantsObject.remove("");
                                anyChanges = true;
                            }
                        }

                        // update model paths to prepend block
                        for (Map.Entry<String, JsonElement> entry : variantsObject.entrySet())
                            updateModelPath(entry);
                    }
                    if (anyChanges) {
                        JsonUtil.writeJson(packConverter.getGson(), file, json);
                        Logger.debug("Converted " + file.getFileName());
                    }
                } catch (IOException e) {
                    Util.propagate(e);
                }
            });
        }
    }

    /**
     * Updates Model paths
     *
     * @param entry
     */
    private void updateModelPath(Map.Entry<String, JsonElement> entry) {
        NameConverter nameConverter = packConverter.getConverter(NameConverter.class);
        if (entry.getValue() instanceof JsonObject) {
            JsonObject value = (JsonObject) entry.getValue();
            if (value.has("model")) {
                String[] split = value.get("model").getAsString().split("/");
                String val = split[split.length - 1];
                String prefix = value.get("model").getAsString().substring(0, value.get("model").getAsString().length() - val.length());
                if (from >= Util.getVersionProtocol(packConverter.getGson(), "1.19.3")
                        && to < Util.getVersionProtocol(packConverter.getGson(), "1.19.3")) {
                    prefix = prefix.replaceAll("minecraft:", "");
                    anyChanges = true;
                }

                if (from >= Util.getVersionProtocol(packConverter.getGson(), "1.19")
                        && to < Util.getVersionProtocol(packConverter.getGson(), "1.19")) {
                    val = nameConverter.getBlockMapping19().remap(val);
                    anyChanges = true;
                }

                if (from >= Util.getVersionProtocol(packConverter.getGson(), "1.17")
                        && to < Util.getVersionProtocol(packConverter.getGson(), "1.17")) {
                    val = nameConverter.getBlockMapping17().remap(val);
                    anyChanges = true;
                }

                if (from >= Util.getVersionProtocol(packConverter.getGson(), "1.14")
                        && to < Util.getVersionProtocol(packConverter.getGson(), "1.14")) {
                    val = nameConverter.getNewBlockMapping().remap(val);
                    anyChanges = true;
                }

                if (from >= Util.getVersionProtocol(packConverter.getGson(), "1.13")
                        && to < Util.getVersionProtocol(packConverter.getGson(), "1.13")) {
                    val = nameConverter.getBlockMapping().remap(val);
                    prefix = "";
                    anyChanges = true;
                }

                if (anyChanges) {
                    value.addProperty("model", prefix + val);
                }
            }
        } else if (entry.getValue() instanceof JsonArray) { // some states have arrays
            for (JsonElement jsonElement : ((JsonArray) entry.getValue())) {
                if (jsonElement instanceof JsonObject) {
                    JsonObject value = (JsonObject) jsonElement;
                    if (value.has("model")) {
                        String[] split = value.get("model").getAsString().split("/");
                        String val = split[split.length - 1];
                        String prefix = value.get("model").getAsString().substring(0,
                                value.get("model").getAsString().length() - val.length());

                        if (from >= Util.getVersionProtocol(packConverter.getGson(), "1.19.3")
                                && to < Util.getVersionProtocol(packConverter.getGson(), "1.19.3")) {
                            prefix = prefix.replaceAll("minecraft:", "");
                            anyChanges = true;
                        }

                        if (from >= Util.getVersionProtocol(packConverter.getGson(), "1.19")
                                && to < Util.getVersionProtocol(packConverter.getGson(), "1.19")) {
                            val = nameConverter.getBlockMapping19().remap(val);
                            anyChanges = true;
                        }

                        if (from >= Util.getVersionProtocol(packConverter.getGson(), "1.17")
                                && to < Util.getVersionProtocol(packConverter.getGson(), "1.17")) {
                            val = nameConverter.getBlockMapping17().remap(val);
                            anyChanges = true;
                        }

                        if (from >= Util.getVersionProtocol(packConverter.getGson(), "1.14")
                                && to < Util.getVersionProtocol(packConverter.getGson(), "1.14")) {
                            val = nameConverter.getNewBlockMapping().remap(val);
                            anyChanges = true;
                        }

                        if (from >= Util.getVersionProtocol(packConverter.getGson(), "1.13")
                                && to < Util.getVersionProtocol(packConverter.getGson(), "1.13")) {
                            val = nameConverter.getBlockMapping().remap(val);
                            prefix = "";
                            anyChanges = true;
                        }

                        if (anyChanges) {
                            value.addProperty("model", prefix + val);
                        }
                    }
                }
            }
        }
    }
}