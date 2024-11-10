package com.agentdid127.resourcepack.library.utilities;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

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

    /**
     * Copies Directory
     * 
     * @param src  directory Source
     * @param dest Directory Destination
     * @throws IOException
     */
    public static void copyDirectory(Path src, Path dest) throws IOException {
        Files.walk(src).forEach(path -> {
            try {
                if (dest.resolve(src.relativize(path)).toFile().exists())
                    Files.delete(dest.resolve(src.relativize(path)));
                Files.copy(path, dest.resolve(src.relativize(path)));
            } catch (Throwable e) {
                throw Util.propagate(e);
            }
        });
    }

    /**
     * Deletes full directory
     * 
     * @param dirPath Path of Directory to delete
     * @throws IOException
     */
    public static void deleteDirectoryAndContents(Path dirPath) throws IOException {
        if (!dirPath.toFile().exists())
            return;

        // noinspection ResultOfMethodCallIgnored
        Files.walk(dirPath).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(file -> {
            file.delete();
        });
    }

    /**
     * If file exists, return file with correct casing.
     * 
     * @param path
     * @return
     * @throws IOException
     */
    public static boolean fileExistsCorrectCasing(Path path) throws IOException {
        return path.toFile().exists() && path.toString().equals(path.toFile().getCanonicalPath());
    }

    /**
     * @return null if file doesn't exist, {@code true} if successfully renamed,
     *         {@code false} if failed
     */
    public static Boolean renameFile(Path file, String newName) {
        if (!file.toFile().exists())
            return null;
        try {
            Files.move(file, file.getParent().resolve(newName));
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Takes dir2 and merges it into dir1
     * 
     * @param dir1
     * @param dir2
     */
    public static Boolean mergeDirectories(File dir1, File dir2) throws IOException {
        if (!dir1.exists() && !dir2.exists())
            return null;

        String targetDirPath = dir1.getAbsolutePath();
        File[] files = dir2.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                Logger.debug(targetDirPath + File.separator + file.getName());
                File file3 = new File(targetDirPath + File.separator + file.getName());
                file3.mkdirs();
                Logger.debug("Created" + file3.getName());
                mergeDirectories(file3, file);
            } else {
                Logger.debug(targetDirPath + File.separator + file.getName());
                file.renameTo(new File(targetDirPath + File.separator + file.getName()));
            }
        }

        if (dir2.list().length == 0)
            Files.delete(dir2.toPath());

        return true;
    }
}