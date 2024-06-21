package com.agentdid127.resourcepack.forwards.impl.textures;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.ImageConverter;

public class SlidersCreator extends Converter {
    public SlidersCreator(PackConverter packConverter) {
        super(packConverter);
    }

    @Override
    public void convert(Pack pack) throws IOException {
        Path widgetsPath = pack.getWorkingPath()
                .resolve("assets/minecraft/textures/gui/widgets.png".replace("/", File.separator));
        if (!widgetsPath.toFile().exists())
            return;

        ImageConverter converter = new ImageConverter(256, 256, widgetsPath);

        int button_width = 200;
        int button_height = 20;

        converter.newImage(256, 256);

        int y = 46;
        converter.subImageSized(0, y, button_width, button_height, 0, 0);
        converter.subImageSized(0, y, button_width, button_height, 0, button_height);
        converter.subImageSized(0, (y + button_height), button_width, button_height, 0, (button_height * 2));
        converter.subImageSized(0, (y + (button_height * 2)), button_width, button_height, 0, (button_height * 3));

        converter.store(widgetsPath.resolveSibling("slider.png"));
    }
}
