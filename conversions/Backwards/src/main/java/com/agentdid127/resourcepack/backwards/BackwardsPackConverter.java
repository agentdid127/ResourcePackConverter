package com.agentdid127.resourcepack.backwards;

import com.agentdid127.resourcepack.backwards.impl.*;
import com.agentdid127.resourcepack.backwards.impl.textures.*;
import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.Util;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class BackwardsPackConverter extends PackConverter {

    Path INPUT_DIR;

    public BackwardsPackConverter(String from, String to, String light, boolean minify, Path input, boolean debug, PrintStream out) {
        GsonBuilder gsonBuilder = new GsonBuilder().disableHtmlEscaping();
        if (!minify) gsonBuilder.setPrettyPrinting();
        gson = gsonBuilder.create();
        DEBUG = debug;
        PackConverter.out = out;
        this.INPUT_DIR = input;

        log(from);
        log(to);

        converterRunner(from, to, light);
        
    }
    
    protected void converterRunner(String from, String to, String light) {
        // this needs to be run first, other converters might reference new directory names
        this.registerConverter(new NameConverter(this, Util.getVersionProtocol(gson, from), Util.getVersionProtocol(gson, to)));
        this.registerConverter(new PackMetaConverter(this, Util.getVersionProtocol(gson, to)));

        if (Util.getVersionProtocol(gson, from) > Util.getVersionProtocol(gson, "1.18") && Util.getVersionProtocol(gson, to) <= Util.getVersionProtocol(gson, "1.18")) {
            this.registerConverter(new ParticleConverter(this));
            this.registerConverter(new InventoryConverter(this));
        }
        if (Util.getVersionProtocol(gson, from) >= Util.getVersionProtocol(gson, "1.13") && Util.getVersionProtocol(gson, to) <= Util.getVersionProtocol(gson, "1.14.4")) {
            this.registerConverter(new PaintingConverter(this));
        }
        if (Util.getVersionProtocol(gson, from) > Util.getVersionProtocol(gson, "1.15") && Util.getVersionProtocol(gson, to) <= Util.getVersionProtocol(gson, "1.15")) {
            this.registerConverter(new EnchantConverter(this));
            this.registerConverter(new ChestConverter(this));
        }
        this.registerConverter(new ParticleTextureConverter(this, Util.getVersionProtocol(gson, from), Util.getVersionProtocol(gson, to)));
        if (Util.getVersionProtocol(gson, to) <= Util.getVersionProtocol(gson, "1.13"))
            this.registerConverter(new LangConverter(this, from, to));
        if (Util.getVersionProtocol(gson, from) >= Util.getVersionProtocol(gson, "1.12.2") && Util.getVersionProtocol(gson, to) <= Util.getVersionProtocol(gson, "1.13")) {
            this.registerConverter(new BlockStateConverter(this));
            this.registerConverter(new AnimationConverter(this));
            this.registerConverter(new MapIconConverter(this));
            this.registerConverter(new MCPatcherConverter(this));
        }
        this.registerConverter(new ModelConverter(this, light, Util.getVersionProtocol(gson, to), Util.getVersionProtocol(gson, from)));
        if (Util.getVersionProtocol(gson, from) > Util.getVersionProtocol(gson, "1.9") && Util.getVersionProtocol(gson, to) <= Util.getVersionProtocol(gson, "1.9"))
            this.registerConverter(new CompassConverter(this, Util.getVersionProtocol(gson, from), Util.getVersionProtocol(gson, to)));

    }

    public void runPack(Pack pack) {
        try {
            log("Converting " + pack);

            pack.getHandler().setup();

            log("  Running Converters");
            for (Converter converter : converters.values()) {
                if (DEBUG)
                    log("    Running " + converter.getClass().getSimpleName());
                converter.convert(pack);
            }

            pack.getHandler().finish();
        } catch (Throwable t) {
            log("Failed to convert!");
            Util.propagate(t);
        }
    }

    public void runDir() throws IOException {
        Files.list(INPUT_DIR)
                .map(Pack::parse)
                .filter(Objects::nonNull)
                .forEach(pack -> {
                    runPack(pack);
                });
    }

}
