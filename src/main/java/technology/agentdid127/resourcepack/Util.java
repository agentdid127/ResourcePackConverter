package technology.agentdid127.resourcepack;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

public final class Util {

    private Util() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }

    /**
     * Copies Directory
     * @param src directory Source
     * @param dest Directory Destination
     * @throws IOException
     */
    public static void copyDir(Path src, Path dest) throws IOException {
        Files.walk(src).forEach(path -> {
            try {
                Files.copy(path, dest.resolve(src.relativize(path)));
            } catch (Throwable e) {
                throw Util.propagate(e);
            }
        });
    }


    /**
     * Deletes full directory
     * @param dirPath Path of Directory to delete
     * @throws IOException
     */
    public static void deleteDirectoryAndContents(Path dirPath) throws IOException {
        if (!dirPath.toFile().exists()) return;

        //noinspection ResultOfMethodCallIgnored
        Files.walk(dirPath)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }

    /**
     * If file exists, return file with correct casing.
     * @param path
     * @return
     * @throws IOException
     */
    public static boolean fileExistsCorrectCasing(Path path) throws IOException {
        if (!path.toFile().exists()) return false;
        return path.toString().equals(path.toFile().getCanonicalPath());
    }

    /**
     * Reads Json
     * @param gson
     * @param path
     * @return
     */
    public static JsonObject readJsonResource(Gson gson, String path) {
        try (InputStream stream = PackConverter.class.getResourceAsStream(path)) {
            if (stream == null) return null;
            try (InputStreamReader streamReader = new InputStreamReader(stream)) {
                return gson.fromJson(streamReader, JsonObject.class);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Reads Image as BufferedImage
     * @param path
     * @return
     */
    public static BufferedImage readImageResource(String path) {
        try (InputStream stream = PackConverter.class.getResourceAsStream(path)) {
            if (stream == null) return null;
            return ImageIO.read(stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static JsonObject readJson(Gson gson, Path path) throws IOException {
        return Util.readJson(gson, path, JsonObject.class);
    }

    public static <T> T readJson(Gson gson, Path path, Class<T> clazz) throws IOException {
        return gson.fromJson(new JsonReader(new FileReader(path.toFile())), clazz);
    }

    /**
     * @return null if file doesn't exist, {@code true} if successfully renamed, {@code false} if failed
     */
    public static Boolean renameFile(Path file, String newName) {
        if (!file.toFile().exists()) return null;
        return file.toFile().renameTo(new File(file.getParent() + File.separator + newName));
    }

    /**
     * Takes dir2 and merges it into dir1
     * @param dir1
     * @param dir2
     */
    public static Boolean mergeDirectories(File dir1, File dir2) throws IOException {
        if (!dir1.exists() && !dir2.exists()) return null;
        String targetDirPath = dir1.getAbsolutePath();
        File[] files = dir2.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                System.out.println(dir1.getAbsolutePath() + File.separator + file.getName());
                File file3 = new File(dir1.getAbsolutePath() + File.separator + file.getName());
                file3.mkdirs();
                System.out.println("Created" + file3.getName());
                mergeDirectories(file3, file);
            }
            else {
                System.out.println(dir1.getAbsolutePath() + File.separator + file.getName());
                file.renameTo(new File(dir1.getAbsolutePath() + File.separator + file.getName()));
            }
        }
        if((dir2.list().length==0)) {
        Files.delete(dir2.toPath());
        }
        return true;
    }
    public static RuntimeException propagate(Throwable t) {
        throw new RuntimeException(t);
    }

}
