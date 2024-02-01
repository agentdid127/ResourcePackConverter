package com.agentdid127.resourcepack.library;

import com.agentdid127.converter.util.Logger;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public abstract class PackConverter {
    protected final Map<Class<? extends RPConverter>, RPConverter> converters = new LinkedHashMap<>();
    protected Gson gson;
    public static boolean DEBUG = true;

    /**
     * Registers Converter.
     * 
     * @param converter
     */
    public void registerConverter(RPConverter converter) {
        converters.put(converter.getClass(), converter);
    }

    /**
     * @param clazz
     * @param <T>
     * @return
     */
    public <T extends RPConverter> T getConverter(Class<T> clazz) {
        return (T) converters.get(clazz);
    }

    /**
     * Gson
     * 
     * @return Gson Object of PackConverter Class
     */
    public Gson getGson() {
        return gson;
    }

    public void runPack(Pack pack) {
        try {
            Logger.log("Converting " + pack);
            pack.getHandler().setup();
            Logger.log("  Running Converters");
            for (int i = 0; i < 100; i++) {
                for (RPConverter converter : converters.values()) {
                    if (converter.getPriority() == i) {
                        if (DEBUG)
                            Logger.log("    Running " + converter.getClass().getSimpleName());
                        converter.convert(pack);
                    }
                }
            }
            pack.getHandler().finish();
        } catch (Throwable t) {
            Logger.log("Failed to convert!");
            Util.propagate(t);
        }
    }

    public void runDir(Path inputDir) throws IOException {
        Files.list(inputDir)
            .map(Pack::parse)
            .filter(Objects::nonNull)
            .forEach(pack -> runPack(pack));
    }
}
