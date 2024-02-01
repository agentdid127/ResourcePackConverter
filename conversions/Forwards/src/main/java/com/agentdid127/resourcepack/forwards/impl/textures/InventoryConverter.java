package com.agentdid127.resourcepack.forwards.impl.textures;

import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.RPConverter;
import com.agentdid127.resourcepack.library.utilities.ImageConverter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class InventoryConverter extends RPConverter {
    public InventoryConverter(PackConverter packConverter) {
        super(packConverter, "InventoryConverter", 1);
    }

    @Override
    public void convert() throws IOException {
        Path imagePath = pack.getWorkingPath()
                .resolve("assets" + File.separator + "minecraft" + File.separator + "textures" + File.separator + "gui"
                        + File.separator + "container" + File.separator + "inventory.png");
        if (!imagePath.toFile().exists())
            return;

        int defaultW = 256, defaultH = 256;
        ImageConverter image = new ImageConverter(defaultW, defaultH, imagePath);

        image.newImage(defaultH, defaultW);
        image.subImage(0, 0, 256, 256, 0, 0);
        image.subImage(0, 166, 16, 198, 0, 198);
        image.subImage(104, 166, 120, 198, 16, 198);

        image.store();
    }
}
