package com.agentdid127.resourcepack.impl.forwards;

import com.agentdid127.resourcepack.Converter;
import com.agentdid127.resourcepack.PackConverter;
import com.agentdid127.resourcepack.Util;
import com.agentdid127.resourcepack.pack.Pack;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class MapIconConverter extends Converter {

    protected Map<Long, Long> mapping = new HashMap<>();

    public MapIconConverter(PackConverter packConverter) {
        super(packConverter);

        mapping.put(pack(0, 0), pack(0, 0));
        mapping.put(pack(8, 0), pack(8, 0));
        mapping.put(pack(16, 0), pack(16, 0));
        mapping.put(pack(24, 0), pack(24, 0));
        mapping.put(pack(0, 8), pack(32, 0));
        mapping.put(pack(8, 8), pack(40, 0));
        mapping.put(pack(16, 8), pack(48, 0));
        mapping.put(pack(24, 8), pack(56, 0));
        mapping.put(pack(0, 16), pack(64, 0));
        mapping.put(pack(8, 16), pack(72, 0));
    }

    /**
     * Converts maps
     * @param pack
     * @throws IOException
     */
    @Override
    public void convert(Pack pack) throws IOException {
        Path imagePath = pack.getWorkingPath().resolve("assets" + File.separator + "minecraft" + File.separator + "textures" + File.separator + "map" + File.separator + "forwards/map_icons.png");
        if (!imagePath.toFile().exists()) return;

        BufferedImage newImage = Util.readImageResource("/forwards/map_icons.png");
        if (newImage == null) throw new NullPointerException();
        Graphics2D g2d = (Graphics2D) newImage.getGraphics();

        BufferedImage image = ImageIO.read(imagePath.toFile());
        int scale = image.getWidth() / 32;

        for (int x = 0; x <= 32 - 8; x += 8) {
            for (int y = 0; y <= 32 - 8; y += 8) {
                Long mapped = mapping.get(pack(x, y));
                if (mapped == null) continue;

                int newX = (int) (mapped >> 32);
                int newY = (int) (long) mapped;
                System.out.println("      Mapping " + x + "," + y + " to " + newX + "," + newY);

                g2d.drawImage(image.getSubimage(x * scale, y * scale, 8 * scale, 8 * scale), newX * scale, newY * scale, null);
            }
        }

        ImageIO.write(newImage, "png", imagePath.toFile());
    }

    protected long pack(int x, int y) {
        return (((long) x) << 32) | (y & 0xffffffffL);
    }

}
