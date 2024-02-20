package com.agentdid127.resourcepack.forwards;

import com.agentdid127.resourcepack.forwards.impl.textures.*;
import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.Util;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.Logger;
import com.agentdid127.resourcepack.forwards.impl.*;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class ForwardsPackConverter extends PackConverter {
    Path INPUT_DIR;

    public ForwardsPackConverter(String from, String to, String light, boolean minify, Path input, boolean debug,
            PrintStream out) {
        GsonBuilder gsonBuilder = new GsonBuilder().disableHtmlEscaping();
        if (!minify)
            gsonBuilder.setPrettyPrinting();
        gson = gsonBuilder.create();
        DEBUG = debug;
        Logger.setStream(out);
        Logger.log(from);
        Logger.log(to);
        this.INPUT_DIR = input;
        converterRunner(from, to, light);
    }

    protected void converterRunner(String from, String to, String light) {
        int protocol_from = Util.getVersionProtocol(gson, from);
        int protocol_to = Util.getVersionProtocol(gson, to);

        // this needs to be run first, other converters might reference new directory
        // names
        this.registerConverter(
                new NameConverter(this, protocol_from, protocol_to));

        this.registerConverter(new PackMetaConverter(this, protocol_to));

        if (protocol_from < Util.getVersionProtocol(gson, "1.9")
                && protocol_to >= Util.getVersionProtocol(gson, "1.9"))
            this.registerConverter(
                    new CompassConverter(this, protocol_from, protocol_to));

        if (protocol_from < Util.getVersionProtocol(gson, "1.11")
                && protocol_to >= Util.getVersionProtocol(gson, "1.11"))
            this.registerConverter(new SpacesConverter(this));

        this.registerConverter(new ModelConverter(this, light, protocol_to, protocol_from));

        if (protocol_from <= Util.getVersionProtocol(gson, "1.12.2")
                && protocol_to >= Util.getVersionProtocol(gson, "1.13")) {
            this.registerConverter(new SoundsConverter(this));
            this.registerConverter(new AnimationConverter(this));
            this.registerConverter(new MapIconConverter(this));
            this.registerConverter(new MCPatcherConverter(this));
        }

        this.registerConverter(new BlockStateConverter(this, protocol_from, protocol_to));

        if (protocol_to >= Util.getVersionProtocol(gson, "1.13"))
            this.registerConverter(new LangConverter(this, from, to));

        this.registerConverter(new ParticleTextureConverter(this, protocol_from, protocol_to));
        this.registerConverter(new ChestConverter(this));

        if (protocol_from <= Util.getVersionProtocol(gson, "1.13")
                && protocol_to >= Util.getVersionProtocol(gson, "1.14.4"))
            this.registerConverter(new PaintingConverter(this));

        if (protocol_from <= Util.getVersionProtocol(gson, "1.13.2")
                && protocol_to >= Util.getVersionProtocol(gson, "1.14"))
            this.registerConverter(new MobEffectAtlasConverter(this));

        if (protocol_from < Util.getVersionProtocol(gson, "1.15")
                && protocol_to >= Util.getVersionProtocol(gson, "1.15"))
            this.registerConverter(new EnchantConverter(this));

        if (protocol_from < Util.getVersionProtocol(gson, "1.18")
                && protocol_to >= Util.getVersionProtocol(gson, "1.18"))
            this.registerConverter(new ParticleConverter(this));

        this.registerConverter(new InventoryConverter(this));

        if (protocol_from < Util.getVersionProtocol(gson, "1.19.3")
                && protocol_to >= Util.getVersionProtocol(gson, "1.19.3"))
            this.registerConverter(new AtlasConverter(this));

        if (protocol_from < Util.getVersionProtocol(gson, "1.19.4")
                && protocol_to >= Util.getVersionProtocol(gson, "1.19.4")) {
            this.registerConverter(new EnchantPathConverter(this));
            // this.registerConverter(new CreativeTabsUpdateConverter());
        }

        if (protocol_from <= Util.getVersionProtocol(gson, "1.19.4")
                && protocol_to >= Util.getVersionProtocol(gson, "1.20"))
            this.registerConverter(new TitleConverter(this));

        if (protocol_from <= Util.getVersionProtocol(gson, "1.20.1")
                && protocol_to >= Util.getVersionProtocol(gson, "1.20.2"))
            this.registerConverter(new SlicerConverter(this, protocol_from, protocol_to));
    }

    public void runPack(Pack pack) {
        try {
            Logger.log("Converting " + pack);
            pack.getHandler().setup();
            Logger.log("  Running Converters");
            for (Converter converter : converters.values()) {
                if (DEBUG)
                    Logger.log("    Running " + converter.getClass().getSimpleName());
                converter.convert(pack);
            }
            pack.getHandler().finish();
        } catch (Throwable t) {
            Logger.log("Failed to convert!");
            Util.propagate(t);
        }
    }

    public void runDir() throws IOException {
        Files.list(INPUT_DIR)
                .map(Pack::parse)
                .filter(Objects::nonNull)
                .forEach(pack -> runPack(pack));
    }
}
