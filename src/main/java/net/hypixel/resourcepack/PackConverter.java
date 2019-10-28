package net.hypixel.resourcepack;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import joptsimple.OptionSet;
import net.hypixel.resourcepack.impl.*;
import net.hypixel.resourcepack.pack.Pack;

import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class PackConverter {

    public static final boolean DEBUG = true;

    protected final OptionSet optionSet;
    protected final Gson gson;

    protected final Map<Class<? extends Converter>, Converter> converters = new LinkedHashMap<>();

    public PackConverter(OptionSet optionSet) {
        this.optionSet = optionSet;

        GsonBuilder gsonBuilder = new GsonBuilder();
        if (!this.optionSet.has(Options.MINIFY)) gsonBuilder.setPrettyPrinting();
        this.gson = gsonBuilder.create();

        // this needs to be run first, other converters might reference new directory names
        this.registerConverter(new NameConverter(this));

        this.registerConverter(new PackMetaConverter(this));

        this.registerConverter(new ModelConverter(this));
        this.registerConverter(new SpacesConverter(this));
        this.registerConverter(new SoundsConverter(this));
        this.registerConverter(new ParticleConverter(this));
        this.registerConverter(new BlockStateConverter(this));
        this.registerConverter(new AnimationConverter(this));
        this.registerConverter(new MapIconConverter(this));

        this.registerConverter(new MCPatcherConverter(this));
    }

    public void registerConverter(Converter converter) {
        converters.put(converter.getClass(), converter);
    }

    public <T extends Converter> T getConverter(Class<T> clazz) {
        //noinspection unchecked
        return (T) converters.get(clazz);
    }

    public void run() throws IOException {
        Files.list(optionSet.valueOf(Options.INPUT_DIR))
                .map(Pack::parse)
                .filter(Objects::nonNull)
                .forEach(pack -> {
                    try {
                        System.out.println("Converting " + pack);

                        pack.getHandler().setup();

                        System.out.println("  Running Converters");
                        for (Converter converter : converters.values()) {
                            if (PackConverter.DEBUG) System.out.println("    Running " + converter.getClass().getSimpleName());
                            converter.convert(pack);
                        }

                        pack.getHandler().finish();
                    } catch (Throwable t) {
                        System.err.println("Failed to convert!");
                        Util.propagate(t);
                    }
                });
    }

    public Gson getGson() {
        return gson;
    }

    @Override
    public String toString() {
        return "PackConverter{" +
                "optionSet=" + optionSet +
                ", converters=" + converters +
                '}';
    }
}