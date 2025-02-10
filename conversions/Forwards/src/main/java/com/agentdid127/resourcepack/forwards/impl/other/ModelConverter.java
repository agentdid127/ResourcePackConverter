package com.agentdid127.resourcepack.forwards.impl.other;

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
import java.util.Objects;
import java.util.stream.Stream;

public class ModelConverter extends Converter {
    private final int from;
    private final int to;
    protected String light = "none";

    public ModelConverter(PackConverter packConverter, String light, int from, int to) {
        super(packConverter);
        this.light = light;
        this.from = from;
        this.to = to;
    }

    @Override
    public boolean shouldConvert(Gson gson, int from, int to) {
        return true;
    }

    /**
     * Runs findfiles with the directory Models
     *
     * @param pack
     * @throws IOException
     */
    @Override
    public void convert(Pack pack) throws IOException {
        Path models = pack.getWorkingPath().resolve("assets/minecraft/models".replace("/", File.separator));
        if (models.toFile().exists()) {
            Logger.addTab();
            findFiles(models);
            Logger.subTab();
        }
    }

    /**
     * Recursively finds files with Path rootPath and runs remapModelJson
     *
     * @param rootPath
     * @throws IOException
     */
    protected void findFiles(Path rootPath) throws IOException {
        File directory = rootPath.toFile();
        for (File file : Objects.requireNonNull(directory.listFiles())) {
            Path filePath = file.toPath();
            if (!file.isDirectory()) {
                remapModelJson(filePath.getParent(), filePath);
            } else {
                remapModelJsons(file.toPath());
            }
        }
    }

    /**
     * Updates model Json to newer versions
     *
     * @param rootPath
     * @throws IOException
     */
    protected void remapModelJsons(Path rootPath) throws IOException {
        if (rootPath.toFile().exists()) {
            try (Stream<Path> pathStream = Files.list(rootPath).filter(path1 -> path1.toString().endsWith(".json"))) {
                pathStream.forEach(modelPath -> {
                    try {
                        this.remapModelJson(rootPath, modelPath);
                    } catch (IOException e) {
                        Util.propagate(e);
                    }
                });
            }
        }
    }

    protected void remapModelJson(Path rootPath, Path model) throws IOException {
        if (!model.toFile().exists()) {
            return;
        }

        try {
            JsonObject jsonObject;
            if (JsonUtil.readJson(packConverter.getGson(), model) != null && JsonUtil.readJson(packConverter.getGson(), model).isJsonObject()) {
                jsonObject = JsonUtil.readJson(packConverter.getGson(), model);
            } else {
                Logger.debug("Could not convert model: " + model.getFileName());
                Logger.addTab();
                if (JsonUtil.readJson(packConverter.getGson(), model) == null) {
                    Logger.debug("Check for Syntax Errors in file.");
                } else {
                    Logger.debug("File is not JSON Object.");
                }
                Logger.subTab();
                return;
            }

            // GUI light system for 1.15.2
            if (!light.equals("none") && (light.equals("front") || light.equals("side"))) {
                Logger.log("Model did not have light, adding light '" + light + "'");
                jsonObject.addProperty("gui_light", light);
            }

            if (jsonObject.has("textures") && jsonObject.get("textures").isJsonObject()) {
                NameConverter nameConverter = packConverter.getConverter(NameConverter.class);

                JsonObject initialTextureObject = jsonObject.getAsJsonObject("textures");
                JsonObject textureObject = initialTextureObject.deepCopy();
                for (Map.Entry<String, JsonElement> entry : initialTextureObject.entrySet()) {
                    String value = entry.getValue().getAsString();
                    textureObject.remove(entry.getKey());

                    // 1.8 mappings
                    if (to >= Util.getVersionProtocol(packConverter.getGson(), "1.9")) {
                        if (entry.getKey().equals("layer0") && (!value.startsWith(rootPath.getFileName().toString()) && !value.startsWith("minecraft:" + rootPath.getFileName()))) {
                            value = rootPath.getFileName() + "/" + value;
                        }
                    }

                    // 1.13 Mappings
                    if (from <= Util.getVersionProtocol(packConverter.getGson(), "1.12.2") &&
                            to >= Util.getVersionProtocol(packConverter.getGson(), "1.13")) {
                        if (value.startsWith("blocks/")) {
                            value = "block/" + nameConverter.getBlockMapping_1_13().remap(value.substring("blocks/".length())).toLowerCase().replaceAll("[()]", "");
                        } else if (value.startsWith("items/")) {
                            value = "item/" + nameConverter.getItemMapping_1_13().remap(value.substring("items/".length())).toLowerCase().replaceAll("[()]", "");
                        } else {
                            value = value.toLowerCase().replaceAll("[()]", "");
                        }
                    }

                    // 1.14 Mappings
                    if (from <= Util.getVersionProtocol(packConverter.getGson(), "1.13.2") &&
                            to >= Util.getVersionProtocol(packConverter.getGson(), "1.14")) {
                        if (value.startsWith("block/")) {
                            value = "block/" + nameConverter.getBlockMapping_1_14().remap(value.substring("block/".length()));
                        }

                        // Dyes
                        if (value.startsWith("item/") && value.contains("dye")) {
                            value = "item/" + nameConverter.getItemMapping_1_14().remap(value.substring("item/".length()));
                        }
                    }

                    // 1.17 Mappings
                    if (from <= Util.getVersionProtocol(packConverter.getGson(), "1.16.5") &&
                            to >= Util.getVersionProtocol(packConverter.getGson(), "1.17")) {
                        if (value.startsWith("block/")) {
                            value = "block/" + nameConverter.getBlockMapping_1_17().remap(value.substring("block/".length())).toLowerCase().replaceAll("[()]", "");
                        } else if (value.startsWith("item/")) {
                            value = "item/" + nameConverter.getItemMapping_1_17().remap(value.substring("item/".length())).toLowerCase().replaceAll("[()]", "");
                        }

                        value = value.toLowerCase().replaceAll("[()]", "");
                    }

                    // 1.19 Mappings
                    if (from <= Util.getVersionProtocol(packConverter.getGson(), "1.18.2") &&
                            to >= Util.getVersionProtocol(packConverter.getGson(), "1.19")) {
                        if (value.startsWith("block/")) {
                            value = "block/" + nameConverter.getBlockMapping_1_19().remap(value.substring("block/".length())).toLowerCase().replaceAll("[()]", "");
                        }

                        value = value.toLowerCase().replaceAll("[()]", "");
                    }

                    // 1.19.3 Mappings
                    if (from <= Util.getVersionProtocol(packConverter.getGson(), "1.19.2") &&
                            to >= Util.getVersionProtocol(packConverter.getGson(), "1.19.3")) {
                        if (!value.startsWith("minecraft:") && !value.startsWith("#")) {
                            value = "minecraft:" + value;
                        }
                    }

                    if (!textureObject.has(entry.getKey())) {
                        textureObject.addProperty(entry.getKey(), value);
                    }
                }

                jsonObject.remove("textures");
                jsonObject.add("textures", textureObject);
            }

            // Fix Display Model For Packs (<= 1.8.9)
            if (jsonObject.has("display") &&
                    from <= Util.getVersionProtocol(packConverter.getGson(), "1.8") &&
                    to >= Util.getVersionProtocol(packConverter.getGson(), "1.9")) {
                JsonObject display = updateDisplay(packConverter.getGson(), jsonObject.remove("display").getAsJsonObject());
                jsonObject.add("display", display);
            }

            if (jsonObject.has("overrides")) {
                for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                    if (entry.getKey().equals("overrides")) {
                        JsonArray overrides = jsonObject.get("overrides").getAsJsonArray();
                        JsonArray overridesArray = new JsonArray();
                        for (int i = 0; i < overrides.size(); i++) {
                            JsonObject object = overrides.get(i).getAsJsonObject();
                            for (Map.Entry<String, JsonElement> json : object.entrySet()) {
                                if (json.getKey().equals("model")) {
                                    object.addProperty(json.getKey(), json.getValue().getAsString().replaceAll("[()]", ""));
                                } else {
                                    object.add(json.getKey(), json.getValue());
                                }
                            }
                            overridesArray.add(object);
                        }
                        jsonObject.add(entry.getKey(), overridesArray);
                    }
                }
            }

            // Parent Stuff
            if (jsonObject.has("parent")) {
                // Change parent to lowercase
                for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                    if (entry.getKey().equals("parent")) {
                        String parent = entry.getValue().getAsString().toLowerCase().replace(" ", "_");

                        // Get block/item parents renamed
                        if (from < Util.getVersionProtocol(packConverter.getGson(), "1.13")
                                && to >= Util.getVersionProtocol(packConverter.getGson(), "1.13")) {
                            if (parent.startsWith("block/")) {
                                parent = setParent("block/", "/forwards/blocks.json", parent, "1_13");
                            } else if (parent.startsWith("item/")) {
                                parent = setParent("item/", "/forwards/items.json", parent, "1_13");
                            }
                        }

                        if (from < Util.getVersionProtocol(packConverter.getGson(), "1.14")
                                && to >= Util.getVersionProtocol(packConverter.getGson(), "1.14")) {
                            if (parent.startsWith("block/")) {
                                parent = setParent("block/", "/forwards/blocks.json", parent, "1_14");
                            } else if (parent.startsWith("item/")) {
                                parent = setParent("item/", "/forwards/items.json", parent, "1_14");
                            }
                        }

                        if (from < Util.getVersionProtocol(packConverter.getGson(), "1.17")
                                && to >= Util.getVersionProtocol(packConverter.getGson(), "1.17")) {
                            if (parent.startsWith("block/")) {
                                parent = setParent("block/", "/forwards/blocks.json", parent, "1_17");
                            } else if (parent.startsWith("item/")) {
                                parent = setParent("item/", "/forwards/items.json", parent, "1_17");
                            }
                        }

                        if (from < Util.getVersionProtocol(packConverter.getGson(), "1.19")
                                && to >= Util.getVersionProtocol(packConverter.getGson(), "1.19")) {
                            if (parent.startsWith("block/")) {
                                parent = setParent("block/", "/forwards/blocks.json", parent, "1_19");
                            }
                        }

                        if (to >= Util.getVersionProtocol(packConverter.getGson(), "1.19.3")
                                && from < Util.getVersionProtocol(packConverter.getGson(), "1.19.3")) {
                            if (!parent.startsWith("minecraft:")) {
                                parent = "minecraft:" + parent;
                            }
                        }

                        jsonObject.addProperty(entry.getKey(), parent);
                    }
                }
            }

            if (!JsonUtil.readJson(packConverter.getGson(), model).equals(jsonObject)) {
                Logger.debug("Updating Model: " + model.getFileName());
                JsonUtil.writeJson(packConverter.getGson(), model, jsonObject);
            } else {
                Logger.debug("Skipping Model: " + model.getFileName());
            }
        } catch (IOException e) {
            throw Util.propagate(e);
        }
    }

    /**
     * Gets parent object and sets a new one
     *
     * @param prefix prefix of file path
     * @param path   File path of json control
     * @param parent Parent String
     * @return New string with changed parent.
     */
    protected String setParent(String prefix, String path, String parent, String item) {
        String parentWithoutPrefix = parent.replace(prefix, "");
        JsonObject file = JsonUtil.readJsonResource(packConverter.getGson(), path).getAsJsonObject(item);
        if (file == null) {
            Logger.debug("Prefix Failed on: " + parent);
            return parent;
        } else {
            return file.has(parentWithoutPrefix) ? prefix + file.get(parentWithoutPrefix).getAsString() : parent;
        }
    }

    protected static JsonObject updateDisplay(Gson gson, JsonObject display) {
        JsonObject defaults = JsonUtil.readJsonResource(gson, "/forwards/display.json");
        if (display == null) {
            return defaults.deepCopy();
        }

        // First Person
        if (display.has("firstperson")) {
            JsonObject firstPerson = display.remove("firstperson").getAsJsonObject();
            display.add("firstperson_righthand", updateDisplayFirstPerson(gson, firstPerson));
        } else if (!display.has("firstperson_righthand")) {
            JsonObject rightHand = defaults.get("firstperson_righthand").getAsJsonObject().deepCopy();
            display.add("firstperson_righthand", rightHand);
        }

        if (!display.has("firstperson_lefthand")) {
            display.add("firstperson_lefthand", getLeftHand(display.get("firstperson_righthand").getAsJsonObject().deepCopy()));
        }

        // Third Person
        if (display.has("thirdperson")) {
            JsonObject thirdPerson = display.remove("thirdperson").getAsJsonObject();
            display.add("thirdperson_righthand", updateDisplayThirdPerson(gson, thirdPerson));
        } else if (!display.has("thirdperson_righthand")) {
            JsonObject rightHand = defaults.get("thirdperson_righthand").getAsJsonObject().deepCopy();
            display.add("thirdperson_righthand", rightHand);
        }

        if (!display.has("thirdperson_lefthand")) {
            display.add("thirdperson_lefthand", getLeftHand(display.get("thirdperson_righthand").getAsJsonObject().deepCopy()));
        }

        if (!display.has("ground")) {
            display.add("ground", defaults.get("ground").getAsJsonObject().deepCopy());
        }

        if (!display.has("head")) {
            display.add("head", defaults.get("head").getAsJsonObject().deepCopy());
        }

        return display;
    }

    private static JsonObject getLeftHand(JsonObject old) {
        JsonObject newObject = old.deepCopy();
        if (old.has("rotation")) {
            JsonArray oldRotation = newObject.remove("rotation").getAsJsonArray();
            JsonArray rotation = new JsonArray();
            rotation.add(oldRotation.get(0).getAsNumber());
            rotation.add(0 - oldRotation.get(1).getAsDouble());
            rotation.add(0 - oldRotation.get(2).getAsDouble());
            newObject.add("rotation", rotation);
        }

        return newObject;
    }

    private static JsonObject updateDisplayFirstPerson(Gson gson, JsonObject old) {
        JsonObject newObject = old.deepCopy();
        if (old.has("rotation")) {
            JsonArray rotation = newObject.remove("rotation").getAsJsonArray();
            newObject.add("rotation", JsonUtil.add(rotation, JsonUtil.asArray(gson, "[0, 45, 0]")));
        }

        if (old.has("translation")) {
            JsonArray translation = newObject.remove("translation").getAsJsonArray();
            newObject.add("translation",
                    JsonUtil.add(
                            JsonUtil.multiply(
                                    JsonUtil.subtract(
                                            translation,
                                            JsonUtil.asArray(gson, "[0, 4, 2]")),
                                    JsonUtil.asArray(gson, "[0.4, 0.4, 0.4]")),
                            JsonUtil.asArray(gson, "[1.13, 3.2, 1.13]")));
        }

        if (old.has("scale")) {
            JsonArray scale = newObject.remove("scale").getAsJsonArray();
            newObject.add("scale", JsonUtil.multiply(scale, JsonUtil.asArray(gson, "[0.4, 0.4, 0.4]")));
        }

        return newObject;
    }

    private static JsonObject updateDisplayThirdPerson(Gson gson, JsonObject old) {
        JsonObject newObject = old.deepCopy();
        if (old.has("rotation")) {
            JsonArray rotation = newObject.remove("rotation").getAsJsonArray();
            newObject.add("rotation",
                    JsonUtil.add(
                            JsonUtil.multiply(
                                    rotation,
                                    JsonUtil.asArray(gson, "[1, -1, -1]")),
                            JsonUtil.asArray(gson, "[0, 0, 20]")));
        }

        if (old.has("translation")) {
            JsonArray translation = newObject.remove("translation").getAsJsonArray();
            newObject.add("translation",
                    JsonUtil.add(
                            JsonUtil.multiply(
                                    translation,
                                    JsonUtil.asArray(gson, "[1, 1, -1]")),
                            JsonUtil.asArray(gson, "[0, 2.75, -3]")));
        }

        // For keeping order
        if (old.has("scale")) {
            JsonArray scale = newObject.remove("scale").getAsJsonArray();
            newObject.add("scale", scale);
        }

        return newObject;
    }
}
