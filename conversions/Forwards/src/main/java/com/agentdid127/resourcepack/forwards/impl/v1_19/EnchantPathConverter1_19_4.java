package com.agentdid127.resourcepack.forwards.impl.v1_19;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.Util;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class EnchantPathConverter1_19_4 extends Converter {
    public EnchantPathConverter1_19_4(PackConverter packConverter) {
        super(packConverter);
    }

    @Override
    public boolean shouldConvert(Gson gson, int from, int to) {
        return from <= Util.getVersionProtocol(gson, "1.19.3") && to >= Util.getVersionProtocol(gson, "1.19.4");
    }

    @Override
    public void convert(Pack pack) throws IOException {
        Path miscPath = pack.getWorkingPath().resolve("assets/minecraft/textures/misc".replace("/", File.separator));
        Path enchantedItemGlintPath = miscPath.resolve("enchanted_item_glint.png");
        if (miscPath.resolve("enchanted_item_glint.png").toFile().exists()) {
            Files.copy(enchantedItemGlintPath, miscPath.resolve("enchanted_glint_entity.png"));
            Files.copy(enchantedItemGlintPath, miscPath.resolve("enchanted_glint_item.png"));
            Files.delete(enchantedItemGlintPath);
        }

        Path enchantedItemGlintMetaPath = miscPath.resolve("enchanted_item_glint.png.mcmeta");
        if (enchantedItemGlintMetaPath.toFile().exists()) {
            Files.copy(enchantedItemGlintMetaPath, miscPath.resolve("enchanted_glint_entity.png.mcmeta"));
            Files.copy(enchantedItemGlintMetaPath, miscPath.resolve("enchanted_glint_item.png.mcmeta"));
            Files.delete(enchantedItemGlintMetaPath);
        }
    }
}
