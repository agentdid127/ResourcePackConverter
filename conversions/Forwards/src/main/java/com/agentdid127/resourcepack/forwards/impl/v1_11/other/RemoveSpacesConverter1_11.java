package com.agentdid127.resourcepack.forwards.impl.v1_11.other;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.FileUtil;
import com.agentdid127.resourcepack.library.utilities.Logger;
import com.agentdid127.resourcepack.library.utilities.Util;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class RemoveSpacesConverter1_11 extends Converter {
    public RemoveSpacesConverter1_11(PackConverter packConverter) {
        super(packConverter);
    }

    @Override
    public boolean shouldConvert(Gson gson, int from, int to) {
        return from <= Util.getVersionProtocol(gson, "1.10.2") && to >= Util.getVersionProtocol(gson, "1.11");
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
        if (assets.toFile().exists()) {
            Logger.addTab();
            findFiles(assets);
            Logger.subTab();
        }
    }

    /**
     * Recursively finds files to fix Spaces
     *
     * @param path
     */
    protected void findFiles(Path path) {
        if (path.toFile().exists()) {
            File directory = path.toFile();
            for (File file : Objects.requireNonNull(directory.listFiles())) {
                String dir = fixSpaces(file.toPath());
                if (file.isDirectory()) {
                    findFiles(Paths.get(dir));
                }
            }
        }
    }

    /**
     * Replaces spaces in files with underscores
     *
     * @param path
     * @return
     */
    protected String fixSpaces(Path path) {
        if (!path.getFileName().toString().contains(" ")) {
            return path.toString();
        }

        String noSpaces = path.getFileName().toString().replaceAll(" ", "_");
        Boolean ret = FileUtil.renameFile(path, noSpaces);
        if (ret == null) {
            return "null";
        }

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
