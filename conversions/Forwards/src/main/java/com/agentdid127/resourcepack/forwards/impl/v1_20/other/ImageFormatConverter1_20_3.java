package com.agentdid127.resourcepack.forwards.impl.v1_20.other;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.Util;
import com.google.gson.Gson;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * Converts Images to only use PNG formats.
 */
public class ImageFormatConverter1_20_3 extends Converter {
    private static final String[] types = new String[]{"jpg", "jpeg", "raw", "ico", "bmp"};

    /**
     * constructs a new ImageFormatConverter.
     *
     * @param packConverter Base PackConverter.
     */
    public ImageFormatConverter1_20_3(PackConverter packConverter) {
        super(packConverter);
    }

    @Override
    public boolean shouldConvert(Gson gson, int from, int to) {
        return from <= Util.getVersionProtocol(gson, "1.20.2") && to >= Util.getVersionProtocol(gson, "1.20.3");
    }

    /**
     * Converts other types of images to PNG images.
     * @param pack Pack to convert
     * @throws IOException if the images are of an invalid type, or do not exist.
     */
    @Override
    public void convert(Pack pack) throws IOException {
        // All textures in the game
        Path texturesPath = pack.getWorkingPath().resolve(Paths.get("assets", "minecraft", "textures"));
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
        // Find all files in the directory
        File directory = path.toFile();
        if (!directory.exists() || !directory.isDirectory()) {
            return;
        }

        // Check each file
        for (File file : Objects.requireNonNull(directory.listFiles())) {
            if (file.isDirectory()) {
                findImage(file.toPath(), type);
            } else if (file.getName().endsWith(type)) {
                remapFile(file, type);
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
