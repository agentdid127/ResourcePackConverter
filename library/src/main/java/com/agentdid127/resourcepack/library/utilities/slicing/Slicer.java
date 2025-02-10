package com.agentdid127.resourcepack.library.utilities.slicing;

import com.agentdid127.resourcepack.library.utilities.FileUtil;
import com.agentdid127.resourcepack.library.utilities.ImageConverter;
import com.agentdid127.resourcepack.library.utilities.JsonUtil;
import com.agentdid127.resourcepack.library.utilities.Logger;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.istack.internal.Nullable;

import java.io.IOException;
import java.nio.file.Path;

public class Slicer {
    public static void runSlicer(Gson gson, Slice slice, Path root, @Nullable PredicateRunnable predicateRunnable, int from, boolean has_metadata) throws IOException {
        if (predicateRunnable != null && !predicateRunnable.run(gson, from, slice.getPredicate())) {
            return;
        }

        Path path = root.resolve(slice.getPath());
        if (!path.toFile().exists()) {
            Logger.debug("Texture '" + slice.getPath() + "' doesn't exist, ignoring...");
            return;
        }

        ImageConverter converter = new ImageConverter(slice.getWidth(), slice.getHeight(), path);

        Path imagePath = path;
        if (slice.getPathName() != null) {
            imagePath = path.resolveSibling(slice.getPathName());
        }

        for (Texture texture : slice.getTextures()) {
            Path texturePath = root.resolve(texture.getPath());
            if (texturePath.toFile().exists()) {
                // Sometimes packs include both newer and older textures but
                // since we are upgrading to a newer version, we want what was shown in the original
                // version so we delete the existing new one, so we can replace it with the
                // version used in said version.
                texturePath.toFile().delete();
            }
            FileUtil.ensureParentExists(texturePath);

            if (predicateRunnable != null && !predicateRunnable.run(gson, from, texture.getPredicate())) {
                continue;
            }

            try {
                Box box = texture.getBox();
                converter.saveSlice(
                        box.getX(),
                        box.getY(),
                        box.getWidth(),
                        box.getHeight(),
                        texturePath);

                if (texture.shouldRemove()) {
                    converter.fillEmpty(
                            box.getX(),
                            box.getY(),
                            box.getWidth(),
                            box.getHeight());
                }

                if (has_metadata) {
                    JsonObject metadata = texture.getMetadata();
                    if (!metadata.keySet().isEmpty() && !metadata.entrySet().isEmpty()) {
                        Path metadataPath = texturePath.resolveSibling(texturePath.getFileName() + ".mcmeta");
                        JsonUtil.writeJson(gson, metadataPath, metadata);
                    }
                }
            } catch (Exception exception) {
                Logger.log("Failed to slice texture '" + texture.getPath() + "' (error='"
                        + exception.getLocalizedMessage() + "')");
                Logger.log("  - box: (x=" + texture.getBox().getX() + ", y=" + texture.getBox().getY() + ", width="
                        + texture.getBox().getWidth() + ", height=" + texture.getBox().getHeight() + ")");
            }
        }

        if (!path.toFile().delete()) {
            Logger.debug("Failed to remove '" + path.getFileName() + "' whilst slicing.");
        }

        if (!slice.shouldDelete()) {
            converter.store(imagePath);
        }
    }
}
