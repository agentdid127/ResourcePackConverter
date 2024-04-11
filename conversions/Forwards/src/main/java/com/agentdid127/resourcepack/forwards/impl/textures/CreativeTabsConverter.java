package com.agentdid127.resourcepack.forwards.impl.textures;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.ImageConverter;

public class CreativeTabsConverter extends Converter {
    private static int old_tab_width = 28;
    private static int new_tab_width = 26;
    private static int old_half = old_tab_width / 2;

    public CreativeTabsConverter(PackConverter converter) {
        super(converter);
    }

    @Override
    public void convert(Pack pack) throws IOException {
        Path guiPath = pack.getWorkingPath().resolve(
                "assets/minecraft/textures/gui");
        if (!guiPath.toFile().exists())
            return;

        Path tabsImage = guiPath
                .resolve("container/creative_inventory/tabs.png".replace("/", File.separator));
        if (!tabsImage.toFile().exists())
            return;

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

    private static void copy_tab(ImageConverter converter, int index, int original_index, int left_padding,
            int right_padding) {
        int first_tab_start_x = original_index * old_tab_width;
        int first_tab_start_end_x = (original_index * old_tab_width) + old_tab_width;

        converter.subImage(
                first_tab_start_x,
                0,
                first_tab_start_end_x - old_half,
                160,
                (index * new_tab_width) + left_padding,
                0);

        converter.subImage(
                first_tab_start_x + old_half,
                0,
                first_tab_start_end_x,
                160,
                ((index * new_tab_width) + (old_half - right_padding)),
                0);
    }
}
