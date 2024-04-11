package com.agentdid127.resourcepack.forwards.impl;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class EnchantPathConverter extends Converter {
    public EnchantPathConverter(PackConverter packConverter) {
        super(packConverter);
    }

    @Override
    public void convert(Pack pack) throws IOException {
        Path miscPath = pack.getWorkingPath().resolve("assets/minecraft/textures/misc".replace("/", File.separator));

        if (miscPath.resolve("enchanted_item_glint.png").toFile().exists()) {
            Files.copy(miscPath.resolve("enchanted_item_glint.png"), miscPath.resolve("enchanted_glint_entity.png"));
            Files.copy(miscPath.resolve("enchanted_item_glint.png"), miscPath.resolve("enchanted_glint_item.png"));
            Files.delete(miscPath.resolve("enchanted_item_glint.png"));
        }

        if (miscPath.resolve("enchanted_item_glint.png.mcmeta").toFile().exists()) {
            Files.copy(miscPath.resolve("enchanted_item_glint.png.mcmeta"),
                    miscPath.resolve("enchanted_glint_entity.png.mcmeta"));
            Files.copy(miscPath.resolve("enchanted_item_glint.png.mcmeta"),
                    miscPath.resolve("enchanted_glint_item.png.mcmeta"));
            Files.delete(miscPath.resolve("enchanted_item_glint.png.mcmeta"));
        }
    }
}
