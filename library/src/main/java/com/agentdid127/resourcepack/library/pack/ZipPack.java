package com.agentdid127.resourcepack.library.pack;

import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.Util;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;

import java.io.IOException;
import java.nio.file.Path;

public class ZipPack extends Pack {

    public ZipPack(Path path) {
        super(path);
    }


    @Override
    public ZipPack.Handler createHandler() {
        return new ZipPack.Handler(this);
    }


    @Override
    public String getFileName() {
        return path.getFileName().toString().substring(0, path.getFileName().toString().length() - 4);
    }

    public static class Handler extends Pack.Handler {

        public Handler(Pack pack) {
            super(pack);
        }

        public Path getConvertedZipPath() {
            return pack.getWorkingPath().getParent().resolve(pack.getWorkingPath().getFileName() + ".zip");
        }

        /**
         * Removes Existing Conversions and starts new one
         * @throws IOException Any issue with loading the pack, or deleting previous packs
         */
        @Override
        public void setup() throws IOException {
            if (pack.getWorkingPath().toFile().exists()) {
                PackConverter.log("  Deleting existing conversion");
                Util.deleteDirectoryAndContents(pack.getWorkingPath());
            }

            Path convertedZipPath = getConvertedZipPath();
            if (convertedZipPath.toFile().exists()) {
                PackConverter.log("  Deleting existing conversion zip");
                convertedZipPath.toFile().delete();
            }

            pack.getWorkingPath().toFile().mkdir();

            try {
                ZipFile zipFile = new ZipFile(pack.getOriginalPath().toFile());
                zipFile.extractAll(pack.getWorkingPath().toString());
            } catch (ZipException e) {
                Util.propagate(e);
            }

            bomRemover(pack.getWorkingPath());
            return;

        }

        /**
         * Runs after program is finished. Zips directory.
         * @throws IOException Any IO exception
         */
        @Override
        public void finish() throws IOException {
            try {
                PackConverter.log("  Zipping working directory");
                ZipFile zipFile = new ZipFile(getConvertedZipPath().toFile());
                ZipParameters parameters = new ZipParameters();
                parameters.setIncludeRootFolder(false);
                zipFile.createSplitZipFileFromFolder(pack.getWorkingPath().toFile(), parameters, false, 65536);
            } catch (ZipException e) {
                Util.propagate(e);
            }

            PackConverter.log("  Deleting working directory");
            Util.deleteDirectoryAndContents(pack.getWorkingPath());
        }

        @Override
        public String toString() {
            return "Handler{} " + super.toString();
        }
    }
}
