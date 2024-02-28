package com.agentdid127.resourcepack.library;

import com.agentdid127.resourcepack.library.utilities.Logger;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public final class Util {
    private Util() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }

    /**
     * Copies Directory
     * 
     * @param src  directory Source
     * @param dest Directory Destination
     * @throws IOException
     */
    public static void copyDir(Path src, Path dest) throws IOException {
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
     * Reads Json
     * 
     * @param gson Gson Object to use
     * @param path Json File Path.
     * @return
     */
    public static JsonObject readJsonResource(Gson gson, String path) {
        try (InputStream stream = PackConverter.class.getResourceAsStream(path)) {
            if (stream == null)
                return null;
            try (InputStreamReader streamReader = new InputStreamReader(stream)) {
                return gson.fromJson(streamReader, JsonObject.class);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Reads Json
     * 
     * @param gson Gson Object to use
     * @param path Json File Path.
     * @return
     */
    public static <T> T readJsonResource(Gson gson, String path, Class<T> clazz) {
        try (InputStream stream = PackConverter.class.getResourceAsStream(path)) {
            if (stream == null)
                return null;
            try (InputStreamReader streamReader = new InputStreamReader(stream)) {
                return gson.fromJson(streamReader, clazz);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Reads Image as BufferedImage
     * 
     * @param path Path to file
     * @return Buffered Image
     */
    public static BufferedImage readImageResource(String path) {
        try (InputStream stream = PackConverter.class.getResourceAsStream(path)) {
            if (stream == null)
                return null;
            return ImageIO.read(stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets version protocol number
     * 
     * @param gson    Gson object to use
     * @param version Minecraft Version Number.
     * @return Protocol Integer Number
     */
    public static int getVersionProtocol(Gson gson, String version) {
        JsonObject protocols = Util.readJsonResource(gson, "/protocol.json");
        return protocols == null ? 0 : Integer.parseInt(protocols.get(version).getAsString());
    }

    public static int getLatestProtocol(Gson gson) {
        return getVersionProtocol(gson, "1.20.4");    
    }

    /**
     * Gets Minecraft Version from Protocol number
     * 
     * @param gson     Gson object to use
     * @param protocol Protocol version number
     * @return Minecraft Version number
     */
    public static String getVersionFromProtocol(Gson gson, int protocol) {
        AtomicReference<String> version = new AtomicReference<String>("ok boomer");
        JsonObject protocols = Util.readJsonResource(gson, "/protocol.json");
        if (protocols == null)
            return null;
        List<String> keys = protocols.entrySet().stream().map(i -> i.getKey())
                .collect(Collectors.toCollection(ArrayList::new));
        keys.forEach(key -> {
            if (Integer.parseInt(protocols.get(key).getAsString()) == protocol)
                version.set(key);
        });
        return version.toString();
    }

    /**
     * Gets Supported versions of Resource Pack Converter
     * 
     * @param gson Gson object to use
     * @return String list of all minecraft versions
     */
    public static String[] getSupportedVersions(Gson gson) {
        JsonObject protocols = Util.readJsonResource(gson, "/protocol.json");
        if (protocols == null)
            return null;
        return protocols.entrySet()
                .stream()
                .map(Map.Entry::getKey)
                .toArray(String[]::new);
    }

    public static JsonObject readJson(Gson gson, Path path) throws IOException {
        return Util.readJson(gson, path, JsonObject.class);
    }

    public static boolean isJson(Gson gson, String Json) {
        try {
            gson.fromJson(Json, Object.class);
            return true;
        } catch (com.google.gson.JsonSyntaxException ex) {
            return false;
        }
    }

    private static String readFromFile(Path path) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path.toFile()));
        StringBuilder resultStringBuilder = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null)
            resultStringBuilder.append(line).append("\n");
        br.close();
        return resultStringBuilder.toString();
    }

    public static <T> T readJson(Gson gson, Path path, Class<T> clazz) throws IOException {
        String json = readFromFile(path);
        if (!isJson(gson, json))
            return null;
        JsonReader reader = new JsonReader(new StringReader(json));
        reader.setLenient(true);
        return gson.fromJson(reader, clazz);
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
                Logger.log(targetDirPath + File.separator + file.getName());
                File file3 = new File(targetDirPath + File.separator + file.getName());
                file3.mkdirs();
                Logger.log("Created" + file3.getName());
                mergeDirectories(file3, file);
            } else {
                Logger.log(targetDirPath + File.separator + file.getName());
                file.renameTo(new File(targetDirPath + File.separator + file.getName()));
            }
        }

        if (dir2.list().length == 0)
            Files.delete(dir2.toPath());

        return true;
    }

    public static RuntimeException propagate(Throwable t) {
        throw new RuntimeException(t);
    }
}
