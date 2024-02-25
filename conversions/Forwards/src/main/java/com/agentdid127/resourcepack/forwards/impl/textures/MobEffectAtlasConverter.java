package com.agentdid127.resourcepack.forwards.impl.textures;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.ImageConverter;
import com.agentdid127.resourcepack.library.utilities.Logger;

public class MobEffectAtlasConverter extends Converter {
    public static HashMap<Integer, String> MOB_EFFECTS = new HashMap<Integer, String>();
    static {
        // Col #1
        // x: 0, y: 0, id: 0 - speed
        MOB_EFFECTS.put(0, "speed.png");
        // x: 1, y: 0, id: 1 - slowness
        MOB_EFFECTS.put(1, "slowness.png");
        // x: 2, y: 0, id: 2 - haste
        MOB_EFFECTS.put(2, "haste.png");
        // x: 3, y: 0, id: 3 - mining fatigue
        MOB_EFFECTS.put(3, "mining_fatigue.png");
        // x: 4, y: 0, id: 4 - strength
        MOB_EFFECTS.put(4, "strength.png");
        // x: 5, y: 0, id: 5 - weakness
        MOB_EFFECTS.put(5, "weakness.png");
        // x: 6, y: 0, id: 6 - poison
        MOB_EFFECTS.put(6, "poison.png");
        // x: 7, y: 0, id: 7 - regeneration
        MOB_EFFECTS.put(7, "regeneration.png");
        // x: 8, y: 0, id: 8 - slow falling
        MOB_EFFECTS.put(8, "slow_falling.png");
        // x: 9, y: 0, id: 9 - conduit power
        MOB_EFFECTS.put(9, "conduit_power.png");
        // x: 10, y: 0, id: 10 - dolphins grace
        MOB_EFFECTS.put(10, "dolphins_grace.png");
        // x: 11, y: 0, id: 11 - unknown

        // Col #2
        // x: 0, y: 1, id: 256 - invisibility
        MOB_EFFECTS.put(256, "invisibility.png");
        // x: 1, y: 1, id: 257 - hunger
        MOB_EFFECTS.put(257, "hunger.png");
        // x: 2, y: 1, id: 258 - jump boost
        MOB_EFFECTS.put(258, "jump_boost.png");
        // x: 3, y: 1, id: 259 - nausea
        MOB_EFFECTS.put(259, "nausea.png");
        // x: 4, y: 1, id: 260 - night vision
        MOB_EFFECTS.put(260, "night_vision.png");
        // x: 5, y: 1, id: 261 - blindness
        MOB_EFFECTS.put(261, "blindness.png");
        // x: 6, y: 1, id: 262 - resistance
        MOB_EFFECTS.put(262, "resistance.png");
        // x: 7, y: 1, id: 263 - fire resistance
        MOB_EFFECTS.put(263, "fire_resistance.png");
        // x: 8, y: 1, id: 264 - unknown
        // x: 9, y: 1, id: 265 - unknown
        // x: 10, y: 1, id: 266 - unknown
        // x: 11, y: 1, id: 267 - unknown

        // Col #3
        // x: 0, y: 2, id: 512 - water breathing
        MOB_EFFECTS.put(512, "water_breathing.png");
        // x: 1, y: 2, id: 513 - wither
        MOB_EFFECTS.put(513, "wither.png");
        // x: 2, y: 2, id: 514 - absorption
        MOB_EFFECTS.put(514, "absorption.png");
        // x: 3, y: 2, id: 515 - levitation
        MOB_EFFECTS.put(515, "levitation.png");
        // x: 4, y: 2, id: 516 - glowing
        MOB_EFFECTS.put(516, "glowing.png");
        // x: 5, y: 2, id: 517 - luck
        MOB_EFFECTS.put(517, "luck.png");
        // x: 6, y: 2, id: 518 - bad luck
        MOB_EFFECTS.put(518, "unluck.png");
        // x: 7, y: 2, id: 519 - health boost
        MOB_EFFECTS.put(519, "health_boost.png");
        // x: 8, y: 2, id: 520 - unknown
        // x: 9, y: 2, id: 521 - unknown
        // x: 10, y: 2, id: 522 - unknown
        // x: 11, y: 2, id: 523 - unknown
    }

    public MobEffectAtlasConverter(PackConverter packConverter) {
        super(packConverter);
    }

    /**
     * Converts inventory.png mob atlas into its own files
     * 
     * @param pack
     * @throws IOException
     */
    @Override
    public void convert(Pack pack) throws IOException {
        Path inventoryPath = pack.getWorkingPath().resolve("assets" + File.separator + "minecraft" + File.separator
                + "textures" + File.separator + "gui" + File.separator + "container" + File.separator
                + "inventory.png");
        if (!inventoryPath.toFile().exists())
            return;

        int DWIDTH = 256, DHEIGHT = 256;
        ImageConverter inventoryImage = new ImageConverter(DWIDTH, DHEIGHT, inventoryPath);
        if (!inventoryImage.imageIsPowerOfTwo() || !inventoryImage.isSquare()) {
            Logger.log("Failed to generate mob_effect-atlas, inventory image is not power of 2/is not square!");
            return;
        }

        Path mobEffectPath = pack.getWorkingPath().resolve("assets" + File.separator + "minecraft" + File.separator
                + "textures" + File.separator + "mob_effect");
        if (!mobEffectPath.toFile().exists())
            mobEffectPath.toFile().mkdirs();

        int iw = 18; // effect width
        int ih = 18; // effect height

        int rows = 3; // Default rows
        int cols = 12; // Default cols

        int START_HEIGHT = DHEIGHT - 58; // Start heigh

        for (int y = 0; y < rows; ++y) {
            for (int x = 0; x < cols; ++x) {
                int id = y * 256 + x; // 256 is a hack, idk what else to do
                if (!MOB_EFFECTS.containsKey(id)) {
                    // if (PackConverter.DEBUG)
                    //     Logger.log("Could not find effect with RPID=" + id);
                    continue;
                }

                String effect_file_name = MOB_EFFECTS.get(id);
                if (PackConverter.DEBUG)
                    Logger.log("Effect: " + effect_file_name);

                int sx = x * iw;
                int sy = START_HEIGHT + (y * ih);

                Path imagePath = mobEffectPath.resolve(effect_file_name);
                if (!imagePath.toFile().exists()) {
                    inventoryImage.newImage(iw, ih);
                    inventoryImage.subImage(sx, sy, sx + iw, sy + ih);
                    inventoryImage.store(imagePath);
                }

                // Logger.log("x: " + x + ", y: " + y + ", id: " + id);
            }
        }

        inventoryImage.newImage(DWIDTH, DHEIGHT);
        inventoryImage.subImage(0, 0, DWIDTH, START_HEIGHT);
        inventoryImage.store(inventoryPath);
    }
}