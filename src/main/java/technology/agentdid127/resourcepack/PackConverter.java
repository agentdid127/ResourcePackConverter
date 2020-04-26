package technology.agentdid127.resourcepack;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import joptsimple.OptionSet;
import technology.agentdid127.resourcepack.extra.BomDetector;
import technology.agentdid127.resourcepack.pack.Pack;
import technology.agentdid127.resourcepack.impl.*;

import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class PackConverter {

    public static final boolean DEBUG = true;

    protected final OptionSet optionSet;
    protected Gson gson;

    protected final Map<Class<? extends Converter>, Converter> converters = new LinkedHashMap<>();

    /**
     * Starts New Conversion
     * @param optionSet
     */
    public PackConverter(OptionSet optionSet) {
        this.optionSet = optionSet;

        GsonBuilder gsonBuilder = new GsonBuilder();
        if (!this.optionSet.has(Options.MINIFY)) gsonBuilder.setPrettyPrinting();
        this.gson = gsonBuilder.disableHtmlEscaping().create();
        String from = "";
        String to = "";
        String light = "";
        light = this.optionSet.valueOf(Options.LIGHT);
        System.out.println(this.optionSet.valueOf(Options.FROM));
        System.out.println(this.optionSet.valueOf(Options.TO));
        from = this.optionSet.valueOf(Options.FROM);
        to = this.optionSet.valueOf(Options.TO);

        // this needs to be run first, other converters might reference new directory names
            this.registerConverter(new NameConverter(this, to, from));
        if (Double.parseDouble(to) >= 1.15){
            this.registerConverter(new PackMetaConverter(this, to));
        }
        else if (to.equals("1.14")){
            this.registerConverter(new PackMetaConverter(this, to));
        }
        else this.registerConverter(new PackMetaConverter(this, "1.13"));

        if (Double.parseDouble(from) < 1.11 && Double.parseDouble(to) >= 1.11)
            this.registerConverter(new SpacesConverter(this));
        if (Double.parseDouble(from) <= 1.12 && Double.parseDouble(to) >= 1.13) {
            this.registerConverter(new ModelConverter(this, light, to));
            this.registerConverter(new SoundsConverter(this));
            this.registerConverter(new ParticleConverter(this));
            this.registerConverter(new BlockStateConverter(this));
            this.registerConverter(new AnimationConverter(this));
            this.registerConverter(new MapIconConverter(this));
            this.registerConverter(new MCPatcherConverter(this));
        }

        if (Double.parseDouble(to) >= 1.13)
            this.registerConverter(new LangConverter(this, from, to));
        if (Double.parseDouble(from) < 1.15 && Double.parseDouble(to) >= 1.15) this.registerConverter(new ChestConverter(this));
        if (Double.parseDouble(from) <= 1.13 && Double.parseDouble(to) >= 1.14) this.registerConverter(new PaintingConverter(this));
    }


    /**
     * Registers Converter.
     * @param converter
     */
    public void registerConverter(Converter converter) {
        converters.put(converter.getClass(), converter);
    }

    /**
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T extends Converter> T getConverter(Class<T> clazz) {
        //noinspection unchecked
        return (T) converters.get(clazz);
    }


    /**
     * Actually runs the thing
     * @throws IOException
     */
    public void run() throws IOException {
        Files.list(optionSet.valueOf(Options.INPUT_DIR))
                .map(Pack::parse)
                .filter(Objects::nonNull)
                .forEach(pack -> {
                    try {
                        System.out.println("Converting " + pack);

                        pack.getHandler().setup();

                        BomDetector bom = new BomDetector(
                                pack.getWorkingPath().toString(),
                                ".txt", ".json", ".mcmeta", ".properties"
                        );

                        int count = 0;
                        for(String file : bom.findBOMs()){
                            count++;
                        }
                        if (count > 0){
                            System.out.println("Removing BOMs from " + count + " files.");
                        } bom.removeBOMs();

                        System.out.println("  Running Converters");
                        for (Converter converter : converters.values()) {
                            if (PackConverter.DEBUG)
                                System.out.println("    Running " + converter.getClass().getSimpleName());
                            converter.convert(pack);
                        }

                        pack.getHandler().finish();
                    } catch (Throwable t) {
                        System.err.println("Failed to convert!");
                        Util.propagate(t);
                    }
                });
    }


    /**
     * Gson
     * @return
     */
    public Gson getGson() {
        return gson;
    }
}