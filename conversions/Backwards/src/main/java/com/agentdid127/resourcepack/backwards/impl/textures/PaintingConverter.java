package com.agentdid127.resourcepack.backwards.impl.textures;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.ImageConverter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class PaintingConverter extends Converter {
    private ArrayList<String> paintings = new ArrayList<>();

    public PaintingConverter(PackConverter packConverter) {
        super(packConverter);
    }

    /**
     * Remaps painting image to multiple images.
     * 
     * @param pack
     * @throws IOException
     */
    @Override
    public void convert(Pack pack) throws IOException {
        Path paintingPath = pack.getWorkingPath().resolve("assets/minecraft" + File.separator
                + "textures/painting" + File.separator);
        if (!paintingPath.toFile().exists())
            return;

        File[] paintingFiles = paintingPath.toFile().listFiles();
        String filename = "";
        for (File file : paintingFiles) {
            if (file.getName().endsWith(".png")) {
                filename = file.getName();
                break;
            }
        }

        ImageConverter normal = new ImageConverter(16, 16, paintingPath.resolve(filename));
        normal.newImage(256, 256);

        // 16x16
        painting(normal, paintingPath, "kebab.png", 0, 0, 1, 1);
        painting(normal, paintingPath, "aztec.png", 16, 0, 1, 1);
        painting(normal, paintingPath, "alban.png", 32, 0, 1, 1);
        painting(normal, paintingPath, "aztec2.png", 48, 0, 1, 1);
        painting(normal, paintingPath, "bomb.png", 64, 0, 1, 1);
        painting(normal, paintingPath, "plant.png", 80, 0, 1, 1);
        painting(normal, paintingPath, "wasteland.png", 96, 0, 1, 1);
        painting(normal, paintingPath, "back.png", 192, 0, 1, 1);

        // 32x16
        painting(normal, paintingPath, "pool.png", 0, 32, 2, 1);
        painting(normal, paintingPath, "courbet.png", 32, 32, 2, 1);
        painting(normal, paintingPath, "sea.png", 64, 32, 2, 1);
        painting(normal, paintingPath, "sunset.png", 96, 32, 2, 1);
        painting(normal, paintingPath, "creebet.png", 128, 32, 2, 1);

        // 16x3
        painting(normal, paintingPath, "wanderer.png", 0, 64, 1, 2);
        painting(normal, paintingPath, "graham.png", 16, 64, 1, 2);

        // 64x48
        painting(normal, paintingPath, "skeleton.png", 192, 64, 4, 3);
        painting(normal, paintingPath, "donkey_kong.png", 192, 112, 4, 3);

        // 64x32
        painting(normal, paintingPath, "fighters.png", 0, 96, 4, 2);

        // 32x32
        painting(normal, paintingPath, "match.png", 0, 128, 2, 2);
        painting(normal, paintingPath, "bust.png", 32, 128, 2, 2);
        painting(normal, paintingPath, "stage.png", 64, 128, 2, 2);
        painting(normal, paintingPath, "void.png", 96, 128, 2, 2);
        painting(normal, paintingPath, "skull_and_roses.png", 128, 128, 2, 2);
        painting(normal, paintingPath, "wither.png", 160, 128, 2, 2);

        // 64x64
        painting(normal, paintingPath, "pointer.png", 0, 192, 4, 4);
        painting(normal, paintingPath, "pigscene.png", 64, 192, 4, 4);
        painting(normal, paintingPath, "burning_skull.png", 128, 192, 4, 4);

        normal.store(paintingPath.resolve("paintings_kristoffer_zetterstrand.png"));
        for (String item : paintings)
            Files.deleteIfExists(paintingPath.resolve(item));
    }

    private void painting(ImageConverter normal, Path paintingPath, String name, int x, int y, int scaleX,
            int scaleY)
            throws IOException {
        if (paintingPath.resolve(name).toFile().exists()) {
            normal.addImage(paintingPath.resolve(name), x, y);
            paintings.add(name);
        }
    }
}
