package com.agentdid127.resourcepack.forwards.impl;

import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.RPConverter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class EnchantPathConverter extends RPConverter {
    public EnchantPathConverter(PackConverter packConverter) {
        super(packConverter, "EnchantPathConverter", 1);
    }

    @Override
    public void convert() throws IOException {
        Path misc = pack.getWorkingPath().resolve(
                "assets" + File.separator + "minecraft" + File.separator + "textures" + File.separator + "misc");

        if (misc.resolve("enchanted_item_glint.png").toFile().exists()) {
            Files.copy(misc.resolve("enchanted_item_glint.png"), misc.resolve("enchanted_glint_entity.png"));
            Files.copy(misc.resolve("enchanted_item_glint.png"), misc.resolve("enchanted_glint_item.png"));
            Files.delete(misc.resolve("enchanted_item_glint.png"));
        }

        if (misc.resolve("enchanted_item_glint.png.mcmeta").toFile().exists()) {
            Files.copy(misc.resolve("enchanted_item_glint.png.mcmeta"),
                    misc.resolve("enchanted_glint_entity.png.mcmeta"));
            Files.copy(misc.resolve("enchanted_item_glint.png.mcmeta"),
                    misc.resolve("enchanted_glint_item.png.mcmeta"));
            Files.delete(misc.resolve("enchanted_item_glint.png.mcmeta"));
        }
    }
}
