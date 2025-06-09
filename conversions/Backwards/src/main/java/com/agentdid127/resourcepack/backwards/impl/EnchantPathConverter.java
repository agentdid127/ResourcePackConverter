package com.agentdid127.resourcepack.backwards.impl;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.Util;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class EnchantPathConverter extends Converter {
    public EnchantPathConverter(PackConverter packConverter) {
        super(packConverter);
    }

    @Override
    public boolean shouldConvert(Gson gson, int from, int to) {
        return from >= Util.getVersionProtocol(gson, "1.19.4") && to <= Util.getVersionProtocol(gson, "1.19.3");
    }

    @Override
    public void convert(Pack pack) throws IOException {
        Path miscPath = pack.getWorkingPath().resolve("assets/minecraft/textures/misc".replace("/", File.separator));
        if (miscPath.toFile().exists()) {
            return;
        }

        Path enchantGlintItemPath = miscPath.resolve("enchanted_glint_item.png");
        Path enchantGlintEntityPath = miscPath.resolve("enchanted_glint_entity.png");
        Path newEnchantGlintItemPath = miscPath.resolve("enchanted_item_glint.png");
        if (enchantGlintItemPath.toFile().exists()) {
            if (newEnchantGlintItemPath.toFile().exists()) {
                newEnchantGlintItemPath.toFile().delete();
            }
            Files.move(enchantGlintItemPath, newEnchantGlintItemPath);
        } else if (enchantGlintEntityPath.toFile().exists()) {
            if (newEnchantGlintItemPath.toFile().exists()) {
                newEnchantGlintItemPath.toFile().delete();
            }
            Files.move(enchantGlintEntityPath, newEnchantGlintItemPath);
        }

        Path enchantGlintItemMetaPath = miscPath.resolve("enchanted_glint_item.png.mcmeta");
        Path enchantGlintEntityMetaPath = miscPath.resolve("enchanted_glint_entity.png.mcmeta");
        Path newEnchantGlintItemMetaPath = miscPath.resolve("enchanted_item_glint.png.mcmeta");
        if (enchantGlintItemMetaPath.toFile().exists()) {
            if (newEnchantGlintItemMetaPath.toFile().exists()) {
                newEnchantGlintItemMetaPath.toFile().delete();
            }
            Files.move(enchantGlintItemMetaPath, newEnchantGlintItemMetaPath);
        } else if (enchantGlintEntityMetaPath.toFile().exists()) {
            if (newEnchantGlintItemMetaPath.toFile().exists()) {
                newEnchantGlintItemMetaPath.toFile().delete();
            }
            Files.move(enchantGlintEntityMetaPath, newEnchantGlintItemMetaPath);
        }

        Files.deleteIfExists(miscPath.resolve("enchanted_glint_entity.png.mcmeta"));
        Files.deleteIfExists(miscPath.resolve("enchanted_glint_item.png.mcmeta"));
        Files.deleteIfExists(miscPath.resolve("enchanted_glint_entity.png"));
        Files.deleteIfExists(miscPath.resolve("enchanted_glint_item.png"));
    }
}