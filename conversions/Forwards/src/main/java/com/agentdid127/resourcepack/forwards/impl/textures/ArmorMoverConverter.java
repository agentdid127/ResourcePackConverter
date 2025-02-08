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

    // TODO: Trims/Wolf Armor/Turtle/Llama/Horse/Elytra
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
        for (String material : MATERIALS) {
            moveArmorLayers(material, modelsArmorPath, humanoidPath, humanoidLeggingsPath);
            if (material.equals("leather")) {
                moveLeatherArmorFiles(modelsArmorPath, humanoidPath, humanoidLeggingsPath);
            }
        }

        modelsArmorPath.toFile().delete(); // TODO/NOTE: For some reason, it will only delete "models" folder if I delete this first
        modelsPath.toFile().delete();
    }

    private Path ensureFolder(Path filePath, Path intendedFolderPath) {
        if (filePath.toFile().exists() && !intendedFolderPath.toFile().exists()) {
            intendedFolderPath.toFile().mkdirs();
        }

        return filePath;
    }

    private void moveArmorLayers(String material, Path modelsArmorPath, Path humanoidPath, Path humanoidLeggingsPath) throws IOException {
        Path layer1Path = ensureFolder(modelsArmorPath.resolve(material + "_layer_1.png"), humanoidPath);
        if (layer1Path.toFile().exists()) {
            Files.move(layer1Path, humanoidPath.resolve(material + ".png"));
        }

        Path layer2Path = ensureFolder(modelsArmorPath.resolve(material + "_layer_2.png"), humanoidLeggingsPath);
        if (layer2Path.toFile().exists()) {
            Files.move(layer2Path, humanoidLeggingsPath.resolve(material + ".png"));
        }
    }

    private void moveLeatherArmorFiles(Path modelsArmorPath, Path humanoidPath, Path humanoidLeggingsPath) throws IOException {
        Path overlayLayer1 = ensureFolder(modelsArmorPath.resolve("leather_layer_1_overlay.png"), humanoidPath);
        if (overlayLayer1.toFile().exists()) {
            Files.move(overlayLayer1, humanoidPath.resolve("leather_overlay.png"));
        }

        Path overlayLayer2 = ensureFolder(modelsArmorPath.resolve("leather_layer_2_overlay.png"), humanoidLeggingsPath);
        if (overlayLayer2.toFile().exists()) {
            Files.move(overlayLayer2, humanoidLeggingsPath.resolve("leather_overlay.png"));
        }
    }
}
