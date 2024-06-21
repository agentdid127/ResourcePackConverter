package com.agentdid127.resourcepack.forwards.impl;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Converts Images to only use PNG formats.
 */
public class ImageFormatConverter extends Converter {

    private static final String[] types = new String[]{"jpg", "jpeg", "raw", "ico", "bmp"};

    /**
     * constructs a new ImageFormatConverter.
     *
     * @param packConverter Base PackConverter.
     */
    public ImageFormatConverter(PackConverter packConverter) {
        super(packConverter);
    }

    /**
     * Converts other types of images to PNG images.
     * @param pack Pack to convert
     * @throws IOException if the images are of an invalid type, or do not exist.
     */
    @Override
    public void convert(Pack pack) throws IOException {
        // All textures in the game
        Path texturesPath = pack.getWorkingPath().resolve("assets/minecraft/textures".replace("/", File.separator));

        // check for invalid images for all of these types.
        for (String type : types) {
            findImage(texturesPath, type);
        }

    }

    /**
     * Recursively finds files with {@link Path} path and remaps the files.
     *
     * @param path Path of directory to find files in.
     * @throws IOException If the images are not found, or the remapping fails.
     */
    protected void findImage(Path path, String type) throws IOException {
        File directory = path.toFile();
        for (File file : directory.listFiles()) {
            if (file.isDirectory())
                findImage(file.toPath(), type);
            else {
                if (file.getName().endsWith(type)) {
                    remapFile(file, type);
                }
            }
        }
    }

    /**
     * Remaps a single image to a PNG format.
     *
     * @param file File to remap.
     * @param oldFormat Original format of the file.
     * @throws IOException If the formatting fails.
     */
    protected void remapFile(File file, String oldFormat) throws IOException {
        // Get the image
        BufferedImage image = ImageIO.read(file);

        // Output the correct format
        ImageIO.write(image, "png", new File(file.getAbsolutePath().replaceAll(oldFormat, "png")));

        // Delete the old image.
        if (file.exists()) {
            file.delete();
        }
    }
}
