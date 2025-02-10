package com.agentdid127.resourcepack.forwards.impl.v1_21.textures;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.JsonUtil;
import com.agentdid127.resourcepack.library.utilities.Logger;
import com.agentdid127.resourcepack.library.utilities.Util;
import com.google.gson.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// https://www.minecraft.net/en-us/article/minecraft-java-edition-1-21-2
public class PostEffectShadersConverter1_21_2 extends Converter {
    private static final Map<String, String> MAPPING = new HashMap<>();

    static {
        // TODO/NOTE: There could be more?
        MAPPING.put("largeBlur", "large_blur");
        MAPPING.put("smallBlur", "small_blur");
        MAPPING.put("itemEntity", "item_entity");
    }

    public PostEffectShadersConverter1_21_2(PackConverter packConverter) {
        super(packConverter);
    }

    @Override
    public boolean shouldConvert(Gson gson, int from, int to) {
        return from <= Util.getVersionProtocol(gson, "1.21.1") && to >= Util.getVersionProtocol(gson, "1.21.2");
    }

    @Override
    public void convert(Pack pack) throws IOException {
        Path minecraftPath = pack.getWorkingPath().resolve("assets/minecraft".replace("/", File.separator));
        if (!minecraftPath.toFile().exists()) {
            return;
        }

        // Move shaders
        Path shadersPostPath = minecraftPath.resolve("shaders/post".replace("/", File.separator));
        if (!shadersPostPath.toFile().exists()) {
            return;
        }

        Path postEffectPath = minecraftPath.resolve("post_effect");
        if (postEffectPath.toFile().exists()) {
            postEffectPath.toFile().delete();
        }

        // TODO: Move the corresponding program shader from shaders/program to shaders/post
        Files.move(shadersPostPath, postEffectPath);

        // TODO: Determind if legacy post-effect & ignore it/delete it

        // Update shaders
        Logger.addTab();
        try (Stream<Path> pathStream = Files.list(postEffectPath).filter(file -> file.toString().endsWith(".json"))) {
            pathStream.forEach(file -> {
                try {
                    JsonObject json = JsonUtil.readJson(packConverter.getGson(), file);

                    // Targets
                    if (json.has("targets") && json.get("targets").isJsonArray()) {
                        JsonObject targets = new JsonObject();
                        JsonArray targetElements = json.remove("targets").getAsJsonArray();
                        for (JsonElement element : targetElements) {
                            if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
                                String target = element.getAsString();
                                targets.add(MAPPING.getOrDefault(target, target), new JsonObject());
                            } else if (element.isJsonObject()) {
                                JsonObject object = element.getAsJsonObject();
                                if (object.has("name")) {
                                    String name = object.remove("name").getAsString();
                                    targets.add(name, object);
                                }
                            }
                        }
                        json.add("targets", targets);
                    }

                    // Passes
                    if (json.has("passes") && json.get("passes").isJsonArray()) {
                        JsonArray passes = new JsonArray();
                        JsonArray passesElements = json.remove("passes").getAsJsonArray();
                        for (JsonObject object : passesElements.asList().stream().map(JsonElement::getAsJsonObject).collect(Collectors.toList())) {
                            passes.add(this.remapPass(object));
                        }
                        json.add("passes", passes);
                    }

                    JsonUtil.writeJson(this.packConverter.getGson(), file, json);
                    Logger.debug("Saving updated post-effect shader \"" + file.toString() + "\"");
                } catch (Exception e) {
                    Logger.debug("Failed to update post-effect shader \"" + file.toString() + "\": " + e.getMessage());
                }
            });
        } catch (Exception e) {
            Logger.debug("Failed to update shader, unable to open file stream");
        }
        Logger.subTab();
    }

    private String fixNamespace(String name) {
        // TODO: Fix for other namespaces
        if (!name.startsWith("minecraft:")) {
            return "minecraft:" + name;
        } else {
            return name;
        }
    }

    private JsonObject remapPass(JsonObject object) {
        JsonObject pass = object.deepCopy();
        if (pass.has("name")) {
            String name = pass.remove("name").getAsString();
            pass.addProperty("program", fixNamespace("post/" + name));
        }

        if (pass.has("intarget")) {
            String inTarget = pass.remove("intarget").getAsString();
            JsonObject inputObject = new JsonObject();
            inputObject.addProperty("sampler_name", "In");
            inputObject.addProperty("target", fixNamespace(MAPPING.getOrDefault(inTarget, inTarget)));
            if (pass.has("use_linear_filter") && pass.get("use_linear_filter").isJsonPrimitive()) {
                inputObject.addProperty("bilinear", pass.remove("use_linear_filter").getAsBoolean());
            }

            JsonArray inputs = new JsonArray();
            inputs.add(inputObject);
            pass.add("inputs", inputs);
        } else if (pass.has("use_linear_filter")) {
            pass.remove("use_linear_filter"); // TODO/NOTE: ? If this is a thing, idk what to do with it
        }

        if (pass.has("auxtargets")) {
            JsonArray inputs;
            if (pass.has("inputs")) {
                inputs = pass.remove("inputs").getAsJsonArray();
            } else {
                inputs = new JsonArray();
            }

            JsonArray auxTargets = pass.remove("auxtargets").getAsJsonArray();
            for (JsonObject target : auxTargets.asList().stream().map(JsonElement::getAsJsonObject).collect(Collectors.toList())) {
                JsonObject newTarget = target.deepCopy();
                if (newTarget.has("name")) {
                    String name = newTarget.remove("name").getAsString();
                    newTarget.addProperty("sampler_name", name);
                }

                if (newTarget.has("id")) {
                    String id = newTarget.remove("id").getAsString();
                    if (id.endsWith(":depth")) {
                        newTarget.add("use_depth_buffer", new JsonPrimitive(true));
                        id = id.substring(0, id.length() - ":depth".length());
                    }
                    newTarget.addProperty("target", fixNamespace(MAPPING.getOrDefault(id, id)));
                }

                inputs.add(newTarget);
            }

            pass.add("inputs", inputs);
        }

        if (pass.has("outtarget")) {
            String outTarget = pass.remove("outtarget").getAsString();
            pass.addProperty("output", MAPPING.getOrDefault(outTarget, outTarget));
        }

        return pass;
    }
}
