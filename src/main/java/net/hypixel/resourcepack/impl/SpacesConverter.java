package net.hypixel.resourcepack.impl;

import net.hypixel.resourcepack.Converter;
import net.hypixel.resourcepack.PackConverter;
import net.hypixel.resourcepack.Util;
import net.hypixel.resourcepack.pack.Pack;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SpacesConverter extends Converter {

    public SpacesConverter(PackConverter packConverter) {
        super(packConverter);
    }

    @Override
    public void convert(Pack pack) throws IOException {
        Path assets = pack.getWorkingPath().resolve("assets");
        if (!assets.toFile().exists()) return;


        findFiles(assets);
    }


    protected void findFiles(Path path) throws IOException {
        if (path.toFile().exists()) {
            File directory = new File(path.toString());
            File[] fList = directory.listFiles();
            for (File file : fList) {
                String dir = fixSpaces(file.toPath());
                if (file.isDirectory()) {
                    findFiles(Paths.get(dir));

                }

            }
        }
    }

    protected String fixSpaces(Path path) throws IOException
    {
        if (!path.getFileName().toString().contains(" ")) return path.toString();

        String noSpaces = path.getFileName().toString().replaceAll(" ", "_");

        Boolean ret = Util.renameFile(path, noSpaces);
        if (ret == null) return "null";
        if (ret && PackConverter.DEBUG) {
            System.out.println("      Renamed: " + path.getFileName().toString() + "->" + noSpaces);
            return path.getParent() + File.separator + noSpaces;
        } else if (!ret) {
            System.err.println("      Failed to rename: " + path.getFileName().toString() + "->" + noSpaces);
            return path.getParent() + File.separator + noSpaces;
        }
        else return null;
    }
}
