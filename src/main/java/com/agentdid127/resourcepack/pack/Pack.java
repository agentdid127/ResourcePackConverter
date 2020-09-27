package com.agentdid127.resourcepack.pack;

import com.agentdid127.resourcepack.Util;

import java.io.IOException;
import java.nio.file.Path;

public class Pack {

    protected static final String CONVERTED_SUFFIX = "_converted";

    protected Path path;
    protected Handler handler;

    protected Pack(Path path) {
        this.path = path;
        this.handler = createHandler();
    }

    public Handler createHandler() {
        return new Handler(this);
    }

    /**
     * Checks the type of pack it is.
     * @param path
     * @return
     */
    public static Pack parse(Path path) {
        if (!path.toString().contains(CONVERTED_SUFFIX)) {
            if (path.toFile().isDirectory() && path.resolve("pack.mcmeta").toFile().exists()) {
                return new Pack(path);
            } else if (path.toString().endsWith(".zip")) {
                return new ZipPack(path);
            }
        }
        return null;
    }


    public Path getOriginalPath() {
        return path;
    }

    public Handler getHandler() {
        return handler;
    }

    public Path getWorkingPath() {
        return path.getParent().resolve(getFileName() + CONVERTED_SUFFIX);
    }

    public String getFileName() {
        return path.getFileName().toString();
    }

    @Override
    public String toString() {
        return "ResourcePack{" +
                "path=" + path +
                '}';
    }

    public static class Handler {

        protected Pack pack;

        public Handler(Pack pack) {
            this.pack = pack;
        }

        /**
         * Deletes existing conversions and sets up pack for conversion
         * @throws IOException
         */
        public void setup() throws IOException {
            if (pack.getWorkingPath().toFile().exists()) {
                System.out.println("  Deleting existing conversion");
                Util.deleteDirectoryAndContents(pack.getWorkingPath());
            }

            System.out.println("  Copying existing pack");
            Util.copyDir(pack.getOriginalPath(), pack.getWorkingPath());
        }

        public void finish() throws IOException {
        }

        @Override
        public String toString() {
            return "Handler{" +
                    "pack=" + pack +
                    '}';
        }
    }
}
