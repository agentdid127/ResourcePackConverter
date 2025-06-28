package com.agentdid127.resourcepack.forwards.impl.v1_20.textures;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.ImageConverter;
import com.agentdid127.resourcepack.library.utilities.Util;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class TitleConverter1_20 extends Converter {
    public TitleConverter1_20(PackConverter packConverter) {
        super(packConverter);
    }

    @Override
    public boolean shouldConvert(Gson gson, int from, int to) {
        return from <= Util.getVersionProtocol(gson, "1.19.4") && to >= Util.getVersionProtocol(gson, "1.20");
    }

    @Override
    public void convert(Pack pack) throws IOException {
        Path texturesPath = pack.getWorkingPath().resolve("assets/minecraft/textures".replace("/", File.separator));
        Path titleFolderPath = texturesPath.resolve("gui/title".replace("/", File.separator));
        Path minecraftTilePath = titleFolderPath.resolve("minecraft.png");
        if (!minecraftTilePath.toFile().exists()) {
            return;
        }

        int oldWidth = 256, oldHeight = 256;
        ImageConverter image = new ImageConverter(oldWidth, oldHeight, minecraftTilePath);
        if (!image.fileIsPowerOfTwo() || !image.isSquare()) {
            return;
        }

        // Normal Minecraft Title
        image.newImage(274, 64);
        // TODO: Fix bigger resolutions not aligning properly?? and having extra space
        // to the right of the image
        image.subImage(0, 0, 156, 44);
        image.subImage(0, 45, 119, 90, 155, 0);

        // TODO: Realms Minecraft Title

        image.store();
    }
}
