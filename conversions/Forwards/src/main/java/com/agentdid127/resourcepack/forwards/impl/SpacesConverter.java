package com.agentdid127.resourcepack.forwards.impl;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.FileUtil;
import com.agentdid127.resourcepack.library.utilities.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SpacesConverter extends Converter {
    public SpacesConverter(PackConverter packConverter) {
        super(packConverter);
    }

    /**
     * Runs findFiles
     * 
     * @param pack
     * @throws IOException
     */
    @Override
    public void convert(Pack pack) throws IOException {
        Path assets = pack.getWorkingPath().resolve("assets");
        if (!assets.toFile().exists())
            return;
        findFiles(assets);
    }

    /**
     * Recursively finds files to fix Spaces
     * 
     * @param path
     * @throws IOException
     */
    protected void findFiles(Path path) throws IOException {
        if (!path.toFile().exists())
            return;
        File directory = path.toFile();
        for (File file : directory.listFiles()) {
            String dir = fixSpaces(file.toPath());
            if (file.isDirectory())
                findFiles(Paths.get(dir));
        }
    }

    /**
     * Replaces spaces in files with underscores
     * 
     * @param path
     * @return
     * @throws IOException
     */
    protected String fixSpaces(Path path) throws IOException {
        if (!path.getFileName().toString().contains(" "))
            return path.toString();

        String noSpaces = path.getFileName().toString().replaceAll(" ", "_");

        Boolean ret = FileUtil.renameFile(path, noSpaces);
        if (ret == null)
            return "null";

        if (ret) {
            Logger.debug("Renamed: " + path.getFileName().toString() + "->" + noSpaces);
            return path.getParent() + File.separator + noSpaces;
        } else if (!ret) {
            Logger.log("Failed to rename: " + path.getFileName().toString() + "->" + noSpaces);
            return path.getParent() + File.separator + noSpaces;
        }

        return null;
    }
}
