package com.agentdid127.resourcepack.forwards.impl.v1_13.other;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.JsonUtil;
import com.agentdid127.resourcepack.library.utilities.Logger;
import com.agentdid127.resourcepack.library.utilities.Util;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class AnimationConverter1_13 extends Converter {
    public AnimationConverter1_13(PackConverter packConverter) {
        super(packConverter);
    }

    @Override
    public boolean shouldConvert(Gson gson, int from, int to) {
        return from <= Util.getVersionProtocol(gson, "1.12.2") && to >= Util.getVersionProtocol(gson, "1.13");
    }

    @Override
    public void convert(Pack pack) throws IOException {
        Path texturesPath = pack.getWorkingPath().resolve("assets/minecraft/textures".replace("/", File.separator));
        fixAnimations(texturesPath.resolve("block"));
        fixAnimations(texturesPath.resolve("item"));
    }

    /**
     * Updates animated images to newer versions
     *
     * @param animations
     * @throws IOException
     */
    protected void fixAnimations(Path animations) throws IOException {
        if (!animations.toFile().exists())
            return;
        Logger.addTab();
        try (Stream<Path> pathStream = Files.list(animations).filter(file -> file.toString().endsWith(".png.mcmeta"))) {
            pathStream.forEach(file -> {
                try {
                    JsonObject json = JsonUtil.readJson(packConverter.getGson(), file);
                    if (json != null) {
                        boolean anyChanges = false;
                        JsonElement animationElement = json.get("animation");
                        if (animationElement instanceof JsonObject) {
                            JsonObject animationObject = animationElement.getAsJsonObject();
                            // TODO: Confirm this doesn't break any packs
                            animationObject.remove("width");
                            animationObject.remove("height");
                            anyChanges = true;
                        }

                        if (anyChanges) {
                            JsonUtil.writeJson(packConverter.getGson(), file, json);
                            Logger.debug("Converted " + file.getFileName());
                        }
                    } else {
                        Logger.log("File: " + file.getFileName() + " is not a valid JSON file.");
                    }
                } catch (IOException e) {
                    Logger.log("Failed to convert file: " + file.getFileName());
                    Util.propagate(e);
                }
            });
        }
        Logger.subTab();
    }
}