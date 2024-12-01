package com.agentdid127.resourcepack.library.utilities;

import com.agentdid127.resourcepack.library.PackConverter;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Map;

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
     * Gets Minecraft Version from Protocol number
     *
     * @param gson     Gson object to use
     * @param protocol Protocol version number
     * @return Minecraft Version number
     */
    public static String getVersionFromProtocol(Gson gson, int protocol) {
        JsonObject protocols = JsonUtil.readJsonResource(gson, "/protocol.json");
        if (protocols == null) return null;

        for (Map.Entry<String, JsonElement> entry : protocols.entrySet()) {
            JsonObject versionObj = entry.getValue().getAsJsonObject();
            if (Integer.parseInt(versionObj.get("protocol_version").getAsString()) == protocol) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * Gets version object containing protocol_version and pack_format by protocol version
     *
     * @param gson     Gson object to use
     * @param protocol Protocol version number
     * @return JsonObject containing version information
     */
    public static JsonObject getVersionObjectByProtocol(Gson gson, int protocol) {
        JsonObject protocols = JsonUtil.readJsonResource(gson, "/protocol.json");
        if (protocols == null) return null;

        for (Map.Entry<String, JsonElement> entry : protocols.entrySet()) {
            JsonObject versionObj = entry.getValue().getAsJsonObject();
            if (Integer.parseInt(versionObj.get("protocol_version").getAsString()) == protocol) {
                return versionObj;
            }
        }
        return null;
    }

    /**
     * Gets version object containing protocol_version and pack_format
     *
     * @param gson    Gson object to use
     * @param version Minecraft Version Number.
     * @return JsonObject containing version information
     */
    public static JsonObject getVersionObject(Gson gson, String version) {
        JsonObject protocols = JsonUtil.readJsonResource(gson, "/protocol.json");
        return protocols == null ? null : protocols.getAsJsonObject(version);
    }

    /**
     * Gets version protocol number
     *
     * @param gson    Gson object to use
     * @param version Minecraft Version Number.
     * @return Protocol Integer Number
     */
    public static int getVersionProtocol(Gson gson, String version) {
        JsonObject versionObj = getVersionObject(gson, version);
        return versionObj == null ? 0 : Integer.parseInt(versionObj.get("protocol_version").getAsString());
    }

    public static int getLatestProtocol(Gson gson) {
        JsonObject protocols = JsonUtil.readJsonResource(gson, "/protocol.json");
        if (protocols == null) return 0;

        int latestProtocol = 0;
        for (Map.Entry<String, JsonElement> entry : protocols.entrySet()) {
            JsonObject versionObj = entry.getValue().getAsJsonObject();
            int protocol = Integer.parseInt(versionObj.get("protocol_version").getAsString());
            if (protocol > latestProtocol) {
                latestProtocol = protocol;
            }
        }
        return latestProtocol;
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
