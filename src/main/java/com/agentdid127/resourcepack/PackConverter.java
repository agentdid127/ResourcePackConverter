package com.agentdid127.resourcepack;

import com.agentdid127.resourcepack.impl.forwards.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import com.agentdid127.resourcepack.utilities.BomDetector;
import com.agentdid127.resourcepack.pack.Pack;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import static com.agentdid127.resourcepack.Util.getVersionProtocol;

public class PackConverter {

    public static boolean DEBUG = false;

    protected final OptionSet optionSet;
    protected Gson gson;

    protected boolean doRun = true;

    protected final Map<Class<? extends Converter>, Converter> converters = new LinkedHashMap<>();

    /**
     * Starts New Conversion
     * @param optionSet options
     */
    public PackConverter(OptionSet optionSet, boolean debug) {
        this.optionSet = optionSet;
        this.DEBUG = debug;
        GsonBuilder gsonBuilder = new GsonBuilder();
        if (!this.optionSet.has(Options.MINIFY)) gsonBuilder.setPrettyPrinting();
        this.gson = gsonBuilder.disableHtmlEscaping().create();
        String light = this.optionSet.valueOf(Options.LIGHT);
        System.out.println(this.optionSet.valueOf(Options.FROM));
        System.out.println(this.optionSet.valueOf(Options.TO));
        String from = this.optionSet.valueOf(Options.FROM);
        String to = this.optionSet.valueOf(Options.TO);

        converterRunner(from, to, light);
    }

    public PackConverter(String from, String to, String light, boolean minify) {
        this.optionSet = Options.PARSER.parse("");
        GsonBuilder gsonBuilder = new GsonBuilder();
        if (!minify) gsonBuilder.setPrettyPrinting();
        this.gson = gsonBuilder.disableHtmlEscaping().create();
        System.out.println(from);
        System.out.println(to);

        converterRunner(from, to, light);
    }


    protected void converterRunner(String from, String to, String light) {

        if (!(getVersionProtocol(this.gson, from) > getVersionProtocol(this.gson, to))) {

        }
        else {
            System.out.println("Sorry! You can't convert to a lower version at the moment! We plan on supporting this in the future, so stay tuned.");
            this.doRun = false;
        }
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

        if (doRun)
        Files.list(optionSet.valueOf(Options.INPUT_DIR))
                .map(Pack::parse)
                .filter(Objects::nonNull)
                .forEach(pack -> {
                    try {
                        System.out.println("Converting " + pack);

                        pack.getHandler().setup();

                        BomDetector bom = new BomDetector(
                                pack.getWorkingPath().toString(),
                                ".txt", ".json", ".mcmeta", ".properties", ".lang"
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
     * @return Gson Object of PackConverter Class
     */
    public Gson getGson() {
        return gson;
    }
}
