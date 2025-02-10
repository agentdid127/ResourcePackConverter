package com.agentdid127.resourcepack.forwards.impl.textures;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.ImageConverter;
import com.agentdid127.resourcepack.library.utilities.Util;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class WidgetSlidersCreator extends Converter {
    public WidgetSlidersCreator(PackConverter packConverter) {
        super(packConverter);
    }

    @Override
    public boolean shouldConvert(Gson gson, int from, int to) {
        return from <= Util.getVersionProtocol(gson, "1.19.3") && to >= Util.getVersionProtocol(gson, "1.19.4");
    }

    @Override
    public void convert(Pack pack) throws IOException {
        Path widgetsPath = pack.getWorkingPath().resolve("assets/minecraft/textures/gui/widgets.png".replace("/", File.separator));
        if (!widgetsPath.toFile().exists()) {
            return;
        }

        ImageConverter converter = new ImageConverter(256, 256, widgetsPath);

        converter.newImage(256, 256);

        int button_width = 200;
        int button_height = 20;
        int y = 46;
        converter.subImageSized(0, y, button_width, button_height, 0, 0);
        converter.subImageSized(0, y, button_width, button_height, 0, button_height);
        converter.subImageSized(0, (y + button_height), button_width, button_height, 0, (button_height * 2));
        converter.subImageSized(0, (y + (button_height * 2)), button_width, button_height, 0, (button_height * 3));

        converter.store(widgetsPath.resolveSibling("slider.png"));
    }
}
