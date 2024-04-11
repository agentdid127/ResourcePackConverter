package com.agentdid127.resourcepack.backwards.impl;

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

        if (miscPath.resolve("enchanted_glint_item.png").toFile().exists())
            Files.move(miscPath.resolve("enchanted_glint_item.png"), miscPath.resolve("enchanted_item_glint.png"));
        else if (miscPath.resolve("enchanted_glint_entity.png").toFile().exists())
            Files.move(miscPath.resolve("enchanted_glint_entity.png"), miscPath.resolve("enchanted_item_glint.png"));

        if (miscPath.resolve("enchanted_glint_item.png.mcmeta").toFile().exists())
            Files.move(miscPath.resolve("enchanted_glint_item.png.mcmeta"),
                    miscPath.resolve("enchanted_item_glint.png.mcmeta"));
        else if (miscPath.resolve("enchanted_glint_entity.png.mcmeta").toFile().exists())
            Files.move(miscPath.resolve("enchanted_glint_entity.png.mcmeta"),
                    miscPath.resolve("enchanted_item_glint.png.mcmeta"));

        Files.deleteIfExists(miscPath.resolve("enchanted_glint_entity.png.mcmeta"));
        Files.deleteIfExists(miscPath.resolve("enchanted_glint_item.png.mcmeta"));
        Files.deleteIfExists(miscPath.resolve("enchanted_glint_entity.png"));
        Files.deleteIfExists(miscPath.resolve("enchanted_glint_item.png"));
    }
}