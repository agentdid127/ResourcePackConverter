package com.agentdid127.resourcepack.forwards.impl.v1_19.textures;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.ImageConverter;
import com.agentdid127.resourcepack.library.utilities.Util;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class CreativeTabsConverter1_19_3 extends Converter {
    private static final int OLD_TAB_WIDTH = 28;
    private static final int NEW_TAB_WIDTH = 26;
    private static final int OLD_TAB_HALF = OLD_TAB_WIDTH / 2;

    public CreativeTabsConverter1_19_3(PackConverter converter) {
        super(converter);
    }

    @Override
    public boolean shouldConvert(Gson gson, int from, int to) {
        return from <= Util.getVersionProtocol(gson, "1.19.2") && to >= Util.getVersionProtocol(gson, "1.19.3");
    }

    @Override
    public void convert(Pack pack) throws IOException {
        Path guiPath = pack.getWorkingPath().resolve("assets/minecraft/textures/gui".replace("/", File.separator));
        if (!guiPath.toFile().exists()) {
            return;
        }

        Path tabsImage = guiPath.resolve("container/creative_inventory/tabs.png".replace("/", File.separator));
        if (!tabsImage.toFile().exists()) {
            return;
        }

        int originalWidth = 256;
        int originalHeight = 256;
        ImageConverter converter = new ImageConverter(originalWidth, originalHeight, tabsImage);
        converter.newImage(originalWidth, originalHeight);

        // Tabs
        copy_tab(converter, 0, 0, 0, 2);
        copy_tab(converter, 1, 1, 0, 2);
        copy_tab(converter, 2, 2, 0, 2);
        copy_tab(converter, 3, 3, 0, 2);
        copy_tab(converter, 4, 4, 0, 2);
        copy_tab(converter, 5, 4, 0, 2);
        copy_tab(converter, 6, 5, 0, 2);
        copy_tab(converter, 7, 6, 0, 2);

        // Scrollers
        int scrollers_width = 24;
        int scroller_height = 15;
        int scroller_start_x = 232;
        converter.subImage(
                scroller_start_x,
                0,
                scroller_start_x + scrollers_width,
                scroller_height,
                scroller_start_x,
                0);

        converter.store();
    }

    private static void copy_tab(ImageConverter converter, int index, int original_index, int left_padding, int right_padding) {
        int first_tab_start_x = original_index * OLD_TAB_WIDTH;
        int first_tab_start_end_x = (original_index * OLD_TAB_WIDTH) + OLD_TAB_WIDTH;

        converter.subImage(
                first_tab_start_x,
                0,
                first_tab_start_end_x - OLD_TAB_HALF,
                160,
                (index * NEW_TAB_WIDTH) + left_padding,
                0);

        converter.subImage(
                first_tab_start_x + OLD_TAB_HALF,
                0,
                first_tab_start_end_x,
                160,
                ((index * NEW_TAB_WIDTH) + (OLD_TAB_HALF - right_padding)),
                0);
    }
}
