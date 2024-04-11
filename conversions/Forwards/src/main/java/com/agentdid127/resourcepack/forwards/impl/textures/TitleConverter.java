package com.agentdid127.resourcepack.forwards.impl.textures;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.ImageConverter;

public class TitleConverter extends Converter {
    public TitleConverter(PackConverter packConverter) {
        super(packConverter);
    }

    /**
     * Updates Minecraft Title & Minecraft Realms Title
     * 
     * @param pack
     * @throws IOException
     */
    @Override
    public void convert(Pack pack) throws IOException {
        Path texturesPath = pack.getWorkingPath().resolve("assets/minecraft/textures".replace("/", File.separator));
        if (!texturesPath.toFile().exists())
            return;

        Path titleFolderPath = texturesPath.resolve("gui/title".replace("/", File.separator));
        if (!titleFolderPath.toFile().exists())
            return;

        Path minecraftTilePath = titleFolderPath.resolve("minecraft.png");
        if (!minecraftTilePath.toFile().exists())
            return;

        int oldWidth = 256, oldHeight = 256;
        ImageConverter image = new ImageConverter(oldWidth, oldHeight, minecraftTilePath);
        if (!image.fileIsPowerOfTwo() || !image.isSquare())
            return;

        // Normal Minecraft Title
        image.newImage(274, 64);
        // TODO: fix bigger resolutions not aligning properly?? and having extra space
        // to the right of the image
        image.subImage(0, 0, 156, 44);
        image.subImage(0, 45, 119, 90, 155, 0);

        // TODO: Realms Minecraft Title

        image.store();
    }
}
