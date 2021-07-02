package com.agentdid127.resourcepack.impl.forwards;

import com.agentdid127.resourcepack.Converter;
import com.agentdid127.resourcepack.PackConverter;
import com.agentdid127.resourcepack.utilities.ImageConverter;
import com.agentdid127.resourcepack.pack.Pack;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class ChestConverter extends Converter {

    public ChestConverter(PackConverter packConverter) {
        super(packConverter);
    }

    /**
     * Fixes Chest Textures in 1.15 Remaps textures, and updates images
     * @param pack
     * @throws IOException
     */
    @Override
    public void convert(Pack pack) throws IOException {
        Path imagePath = pack.getWorkingPath().resolve("assets" + File.separator + "minecraft" + File.separator + "textures" + File.separator + "entity" + File.separator + "chest" + File.separator);
        if (!imagePath.toFile().exists()) return;

        //Double chest
        doubleChest(imagePath, "normal");
        doubleChest(imagePath, "trapped");
        doubleChest(imagePath, "christmas");

        //Normal Chest
        chest(imagePath, "normal");
        chest(imagePath, "trapped");
        chest(imagePath, "christmas");
        chest(imagePath, "ender");



    }

    /**
     * Fixes Normal chests
     * @param imagePath
     * @param name
     * @throws IOException
     */
    private void chest(Path imagePath, String name) throws IOException
    {
        int defaultW = 64, defaultH = 64;
        if (!imagePath.resolve(name + ".png").toFile().exists()) return;
        ImageConverter normal = new ImageConverter(defaultW, defaultH, imagePath.resolve(name + ".png"));
        //Create a new Image
        normal.newImage(defaultW, defaultH);

        //Body
        normal.subImage(14, 0, 28, 14, 28, 0, true);
        normal.subImage(28, 0, 42, 14, 14, 0, true);
        normal.subImage(28, 19, 42, 33, 14, 19, true);
        normal.subImage(14, 19, 28, 33, 28, 19, true);
        normal.subImage(28, 14, 42, 19, 28, 14, 1);
        normal.subImage(42, 14, 56, 19, 14, 14, 1);
        normal.subImage(14, 14, 28, 19, 42, 14, 1);
        normal.subImage(0, 14, 14, 19, 0, 14, 1);
        normal.subImage(0, 33, 14, 43, 0, 33, 1);
        normal.subImage(42, 33, 56, 43, 14, 33, 1);
        normal.subImage(28, 33, 42, 43, 28, 33, 1);
        normal.subImage(14, 33, 28, 43, 42, 33, 1);
        //Latch
        normal.subImage(0, 1, 1, 5, 0, 1, true);
        normal.subImage(3, 0, 5, 1, 1, 0);
        normal.subImage(1, 0, 3, 1, 3, 0);
        normal.subImage(1, 1, 3, 5, 4, 1, 1);
        normal.subImage(3, 1, 6, 5, 1, 1, 1);
        //Store new Image
        normal.store();
    }

    /**
     * Splits Double Chests into 2 images
     * @param imagePath
     * @param name
     * @throws IOException
     */
    private void doubleChest ( Path imagePath, String name) throws IOException
    {
        int defaultW = 128, defaultH = 64;

        if (imagePath.resolve(name + "_double.png").toFile().exists()) {
            ImageConverter normal = new ImageConverter(defaultW, defaultH, imagePath.resolve(name + "_double.png"));

            //Left Side
            //Body
            normal.newImage(64, 64);
            normal.subImage(59, 19, 74, 33, 14, 19, true);
            normal.subImage(59, 0, 74, 14, 14, 0, true);
            normal.subImage(29, 19, 44, 33, 29, 19, true);
            normal.subImage(29, 14, 44, 19, 43, 14, 1);
            normal.subImage(29, 0, 44, 14, 29, 0, true);
            normal.subImage(44, 33, 58, 43, 29, 33, 1);
            normal.subImage(29, 33, 44, 43, 43, 33, 1);
            normal.subImage(58, 33, 73, 43, 14, 33, 1);
            normal.subImage(44, 14, 58, 19, 29, 14, 1);
            normal.subImage(58, 14, 73, 19, 14, 14, 1);
            //Latch
            normal.subImage(4, 0, 5, 1, 1, 0);
            normal.subImage(2, 0, 3, 1, 2, 0);
            normal.subImage(2, 1, 5, 5, 1, 1, 1);
            //Stores Image
            normal.store(imagePath.resolve(name + "_left.png"));


            //Right Side
            //Body
            normal.newImage(64, 64);
            normal.subImage(44, 0, 59, 14, 14, 0, true);
            normal.subImage(14, 0, 29, 14, 29, 0, true);
            normal.subImage(14, 19, 29, 33, 29, 19, true);
            normal.subImage(0, 14, 14, 19, 0, 14, 1);
            normal.subImage(73, 14, 88, 19, 14, 14, 1);
            normal.subImage(14, 14, 29, 19, 43, 14, 1);
            normal.subImage(44, 19, 59, 33, 14, 19, true);
            normal.subImage(14, 33, 29, 43, 43, 33, 1);
            normal.subImage(0, 33, 14, 43, 0, 33, 1);
            normal.subImage(73, 33, 88, 43, 14, 33, 1);
            //Latch
            normal.subImage(3, 0, 4, 1, 1, 0);
            normal.subImage(1, 0, 2, 1, 2, 0);
            normal.subImage(0, 1, 1, 5, 0, 1, true);
            normal.subImage(5, 1, 6, 5, 1, 1, true);
            normal.subImage(1, 1, 2, 5, 3, 1, true);
            normal.store(imagePath.resolve(name + "_right.png"));

            imagePath.resolve(name + "_double.png").toFile().delete();
        }
    }
}

