package com.agentdid127.resourcepack.forwards.impl.textures.slicing;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Path;

import com.agentdid127.resourcepack.forwards.impl.textures.SlicerConverter;
import com.agentdid127.resourcepack.library.utilities.FileUtil;
import com.agentdid127.resourcepack.library.utilities.ImageConverter;
import com.agentdid127.resourcepack.library.utilities.JsonUtil;
import com.agentdid127.resourcepack.library.utilities.Logger;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class Slicer {
    public static <T> void runSlicer(Gson gson, Slice slice, Path root, Class<T> predicateClass, int from,
            boolean texture_has_metadata)
            throws IOException {
        if (!SlicerConverter.PredicateRunnable.run(gson, from, slice.getPredicate()))
            return;

        Path path = root.resolve(slice.getPath());
        if (!path.toFile().exists()) {
            Logger.debug("Texture '" + slice.getPath() + "' doesn't exist, ignoring...");
            return;
        }

        ImageConverter converter = new ImageConverter(slice.getWidth(), slice.getHeight(), path);

        Path imagePath = path;
        if (slice.getPathName() != null)
            imagePath = path.resolveSibling(slice.getPathName());

        for (Texture texture : slice.getTextures()) {
            Path texturePath = root.resolve(texture.getPath());
            FileUtil.ensureParentExists(texturePath);

            if (predicateClass != null) {
                // Hacky
                try {
                    Method predicateTest = predicateClass.getMethod("run", Gson.class, int.class, JsonObject.class);
                    if (!((boolean) predicateTest.invoke(null, gson, from, texture.getPredicate())))
                        continue;
                } catch (Exception exception) {
                    Logger.log("Failed to get test predicate from predicate class");
                }
            }

            try {
                Box box = texture.getBox();

                converter.saveSlice(
                        box.getX(),
                        box.getY(),
                        box.getWidth(),
                        box.getHeight(),
                        texturePath);

                if (texture.shouldRemove())
                    converter.fillEmpty(
                            box.getX(),
                            box.getY(),
                            box.getWidth(),
                            box.getHeight());

                if (texture_has_metadata) {
                    JsonObject metadata = texture.getMetadata();
                    if (metadata.keySet().isEmpty() || metadata.entrySet().isEmpty())
                        continue;
                    Path metadataPath = texturePath.resolveSibling(texturePath.getFileName() + ".mcmeta");
                    JsonUtil.writeJson(gson, metadataPath, metadata);
                }
            } catch (Exception exception) {
                Logger.log("Failed to slice texture '" + texture.getPath() + "' (error='"
                        + exception.getLocalizedMessage() + "')");
                Logger.log("  - box: (x=" + texture.getBox().getX() + ", y=" + texture.getBox().getY() + ", width="
                        + texture.getBox().getWidth() + ", height=" + texture.getBox().getHeight() + ")");
            }
        }

        if (!path.toFile().delete())
            Logger.debug("Failed to remove '" + path.getFileName() + "' whilst slicing.");

        if (!slice.shouldDelete())
            converter.store(imagePath);
    }
}
