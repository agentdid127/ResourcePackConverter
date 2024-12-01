package com.agentdid127.resourcepack.library.utilities;

import com.agentdid127.resourcepack.library.PackConverter;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public final class Util {
    private Util() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
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
        JsonObject protocols = JsonUtil.readJsonResource(gson, "/protocol.json");
        return protocols == null ? 0 : Integer.parseInt(protocols.get(version).getAsString());
    }

    public static int getLatestProtocol(Gson gson) {
        return getVersionProtocol(gson, "1.21.3");
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
        JsonObject protocols = JsonUtil.readJsonResource(gson, "/protocol.json");
        if (protocols == null)
            return null;
        Collection<String> keys = protocols.entrySet().stream().map(Map.Entry::getKey)
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
        JsonObject protocols = JsonUtil.readJsonResource(gson, "/protocol.json");
        if (protocols == null)
            return null;
        return protocols.entrySet()
                .stream()
                .map(Map.Entry::getKey)
                .toArray(String[]::new);
    }

    public static String readFromFile(Path path) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path.toFile()));
        StringBuilder resultStringBuilder = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null)
            resultStringBuilder.append(line).append("\n");
        br.close();
        return resultStringBuilder.toString();
    }

    public static RuntimeException propagate(Throwable t) {
        throw new RuntimeException(t);
    }
}
