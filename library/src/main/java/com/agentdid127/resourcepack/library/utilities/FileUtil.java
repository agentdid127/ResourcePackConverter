package com.agentdid127.resourcepack.library.utilities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class FileUtil {
    public static void moveIfExists(Path in, Path out) throws IOException {
        if (!in.toFile().exists())
            return;
        ensureParentExists(out);
        Files.move(in, out);
    }

    public static void copyIfExists(Path in, Path out) throws IOException {
        if (!in.toFile().exists())
            return;
        ensureParentExists(out);
        Files.copy(in, out);
    }

    public static void ensureParentExists(Path path) {
        Path parentPath = path.getParent();
        if (!parentPath.toFile().exists())
            parentPath.toFile().mkdirs();
    }
}