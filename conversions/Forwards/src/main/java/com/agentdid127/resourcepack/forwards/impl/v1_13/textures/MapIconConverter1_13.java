package com.agentdid127.resourcepack.forwards.impl.v1_13.textures;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.Logger;
import com.agentdid127.resourcepack.library.utilities.Util;
import com.google.gson.Gson;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class MapIconConverter1_13 extends Converter {
    private static final Map<Long, Long> MAPPING = new HashMap<>();

    static {
        MAPPING.put(pack(0, 0), pack(0, 0));
        MAPPING.put(pack(8, 0), pack(8, 0));
        MAPPING.put(pack(16, 0), pack(16, 0));
        MAPPING.put(pack(24, 0), pack(24, 0));
        MAPPING.put(pack(0, 8), pack(32, 0));
        MAPPING.put(pack(8, 8), pack(40, 0));
        MAPPING.put(pack(16, 8), pack(48, 0));
        MAPPING.put(pack(24, 8), pack(56, 0));
        MAPPING.put(pack(0, 16), pack(64, 0));
        MAPPING.put(pack(8, 16), pack(72, 0));
    }

    public MapIconConverter1_13(PackConverter packConverter) {
        super(packConverter);
    }

    @Override
    public boolean shouldConvert(Gson gson, int from, int to) {
        return from <= Util.getVersionProtocol(gson, "1.12.2") && to >= Util.getVersionProtocol(gson, "1.13");
    }

    @Override
    public void convert(Pack pack) throws IOException {
        Path mapIconsPath = pack.getWorkingPath().resolve("assets/minecraft/textures/map/map_icons.png".replace("/", File.separator));
        if (!mapIconsPath.toFile().exists()) {
            return;
        }

        BufferedImage newImage = Util.readImageResource("/forwards/map_icons.png");
        if (newImage == null) {
            throw new NullPointerException();
        }

        Graphics2D g2d = (Graphics2D) newImage.getGraphics();
        BufferedImage image = ImageIO.read(mapIconsPath.toFile());
        int scale = image.getWidth() / 32;
        for (int x = 0; x <= 32 - 8; x += 8) {
            for (int y = 0; y <= 32 - 8; y += 8) {
                Long mapped = MAPPING.get(pack(x, y));
                if (mapped != null) {
                    int newX = (int) (mapped >> 32);
                    int newY = (int) (long) mapped;
                    Logger.debug("Mapping " + x + "," + y + " to " + newX + "," + newY);
                    g2d.drawImage(image.getSubimage(x * scale, y * scale, 8 * scale, 8 * scale), newX * scale, newY * scale, null);
                }
            }
        }

        ImageIO.write(newImage, "png", mapIconsPath.toFile());
    }

    private static long pack(int x, int y) {
        return (((long) x) << 32) | (y & 0xffffffffL);
    }
}
