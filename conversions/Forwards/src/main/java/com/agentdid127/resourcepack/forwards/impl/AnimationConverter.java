package com.agentdid127.resourcepack.forwards.impl;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.JsonUtil;
import com.agentdid127.resourcepack.library.utilities.Logger;
import com.agentdid127.resourcepack.library.utilities.Util;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class AnimationConverter extends Converter {
    public AnimationConverter(PackConverter packConverter) {
        super(packConverter);
    }

    @Override
    public void convert(Pack pack) throws IOException {
        fixAnimations(pack.getWorkingPath().resolve(
                "assets/minecraft/textures/block"));
        fixAnimations(pack.getWorkingPath().resolve(
                "assets/minecraft/textures/item"));
    }

    /**
     * Updats animated images to newer versions
     * 
     * @param animations
     * @throws IOException
     */
    protected void fixAnimations(Path animations) throws IOException {
        if (!animations.toFile().exists())
            return;
        Files.list(animations)
                .filter(file -> file.toString().endsWith(".png.mcmeta"))
                .forEach(file -> {
                    try {
                        JsonObject json = Util.readJson(packConverter.getGson(), file);

                        boolean anyChanges = false;
                        JsonElement animationElement = json.get("animation");
                        if (animationElement instanceof JsonObject) {
                            JsonObject animationObject = (JsonObject) animationElement;

                            // TODO: Confirm this doesn't break any packs
                            animationObject.remove("width");
                            animationObject.remove("height");

                            anyChanges = true;
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
}