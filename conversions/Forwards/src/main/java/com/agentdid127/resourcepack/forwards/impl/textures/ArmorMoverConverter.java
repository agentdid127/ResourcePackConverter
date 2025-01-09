package com.agentdid127.resourcepack.forwards.impl.textures;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ArmorMoverConverter extends Converter {
    private final List<String> MATERIALS = new ArrayList<>();

    public ArmorMoverConverter(PackConverter packConverter) {
        super(packConverter);
        MATERIALS.add("chainmail");
        MATERIALS.add("diamond");
        MATERIALS.add("gold");
        MATERIALS.add("iron");
        MATERIALS.add("leather");
        MATERIALS.add("netherite");
    }

    @Override
    public void convert(Pack pack) throws IOException {
        Path texturesPath = pack.getWorkingPath().resolve("assets/minecraft/textures/".replace("/", File.separator));
        if (!texturesPath.toFile().exists()) {
            return;
        }

        Path modelsPath = texturesPath.resolve("models");
        if (!modelsPath.toFile().exists()) {
            return;
        }

        Path modelsArmorPath = modelsPath.resolve("armor");
        if (!modelsArmorPath.toFile().exists()) {
            return;
        }

        // Just incase a pack wants to be special and include both versions 💀
        Path equipmentFolderPath = texturesPath.resolve("entity/equipment".replace("/", File.separator));
        if (equipmentFolderPath.toFile().exists()) {
            equipmentFolderPath.toFile().delete();
        }
        equipmentFolderPath.toFile().mkdirs();

        Path humanoidPath = equipmentFolderPath.resolve("humanoid");
        Path humanoidLeggingsPath = equipmentFolderPath.resolve("humanoid_leggings");

        // TODO: Cleanup this messy for loop
        for (String material : MATERIALS) {
            Path layer1Path = modelsArmorPath.resolve(material + "_layer_1.png");
            if (layer1Path.toFile().exists() && !humanoidPath.toFile().exists()) {
                humanoidPath.toFile().mkdirs();
            }

            Path layer2Path = modelsArmorPath.resolve(material + "_layer_2.png");
            if (layer2Path.toFile().exists() && !humanoidLeggingsPath.toFile().exists()) {
                humanoidLeggingsPath.toFile().mkdirs();
            }

            if (layer1Path.toFile().exists()) {
                Files.move(layer1Path, humanoidPath.resolve(material + ".png"));
            }

            if (layer2Path.toFile().exists()) {
                Files.move(layer2Path, humanoidLeggingsPath.resolve(material + ".png"));
            }

            if (material.equals("leather")) {
                Path overlayLayer1 = modelsPath.resolve("_layer_1_overlay.png");
                if (overlayLayer1.toFile().exists() && !humanoidPath.toFile().exists()) {
                    humanoidLeggingsPath.toFile().mkdirs();
                }

                Path overlayLayer2 = modelsPath.resolve("_layer_2_overlay.png");
                if (overlayLayer2.toFile().exists() && !humanoidLeggingsPath.toFile().exists()) {
                    humanoidPath.toFile().mkdirs();
                }

                if (overlayLayer1.toFile().exists()) {
                    Files.move(overlayLayer1, humanoidPath.resolve(material + "_overlay.png"));
                }

                if (overlayLayer2.toFile().exists()) {
                    Files.move(overlayLayer2, humanoidLeggingsPath.resolve(material + "_overlay.png"));
                }
            }
        }

        modelsPath.toFile().delete();
    }
}
