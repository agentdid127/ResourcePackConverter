package com.agentdid127.resourcepack.backwards;

import com.agentdid127.resourcepack.backwards.impl.*;
import com.agentdid127.resourcepack.backwards.impl.textures.*;
import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.Logger;
import com.agentdid127.resourcepack.library.utilities.Util;
import com.google.gson.GsonBuilder;

import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.stream.Stream;

public class BackwardsPackConverter extends PackConverter {
    Path INPUT_DIR;

    public BackwardsPackConverter(String from, String to, String light, boolean minify, Path input, boolean debug,
                                  PrintStream out) {
        GsonBuilder gsonBuilder = new GsonBuilder().disableHtmlEscaping().setLenient();
        if (!minify)
            gsonBuilder.setPrettyPrinting();
        gson = gsonBuilder.create();
        Logger.setDebug(debug);
        Logger.setStream(out);
        Logger.log("Converting packs from: " + from + " to " + to);
        this.INPUT_DIR = input;
        converterRunner(from, to, light);
    }

    private void converterRunner(String from, String to, String light) {
        int protocolFrom = Util.getVersionProtocol(gson, from);
        int protocolTo = Util.getVersionProtocol(gson, to);

        // This needs to be run first, other converters might reference
        // new directory names.
        this.registerConverter(new NameConverter(this, protocolFrom, protocolTo));
        this.registerConverter(new PackMetaConverter(this, protocolTo));

        this.registerConverter(new DeleteFileConverter(this, protocolFrom, protocolTo));

        // TODO: backwards TitleConverter for going from 1.20 to below

        if (protocolFrom >= Util.getVersionProtocol(gson, "1.19.4")
                && protocolTo < Util.getVersionProtocol(gson, "1.19.4"))
            this.registerConverter(new EnchantPathConverter(this));

        if (protocolFrom >= Util.getVersionProtocol(gson, "1.18")
                && protocolTo < Util.getVersionProtocol(gson, "1.18")) {
            this.registerConverter(new ParticleConverter(this));
            this.registerConverter(new InventoryConverter(this));
        }

        if (protocolFrom >= Util.getVersionProtocol(gson, "1.15")
                && protocolTo < Util.getVersionProtocol(gson, "1.15")) {
            this.registerConverter(new EnchantConverter(this));
            this.registerConverter(new ChestConverter(this));
        }

        if (protocolFrom >= Util.getVersionProtocol(gson, "1.14")
                && protocolTo < Util.getVersionProtocol(gson, "1.14"))
            this.registerConverter(new PaintingConverter(this));

        this.registerConverter(new ParticleTextureConverter(this, protocolFrom, protocolTo));

        if (protocolFrom >= Util.getVersionProtocol(gson, "1.13")
                && protocolTo < Util.getVersionProtocol(gson, "1.13")) {
            this.registerConverter(new LangConverter(this, from, to));
            // this.registerConverter(new SoundsConverter(this));
            // this.registerConverter(new AnimationConverter(this));
            this.registerConverter(new MapIconConverter(this));
            this.registerConverter(new MCPatcherConverter(this));
            this.registerConverter(new WaterConverter(this));
        }

        this.registerConverter(new BlockStateConverter(this, protocolFrom, protocolTo));
        this.registerConverter(new ModelConverter(this, light, protocolFrom, protocolTo));

        if (protocolFrom >= Util.getVersionProtocol(gson, "1.9")
                && protocolTo < Util.getVersionProtocol(gson, "1.9"))
            this.registerConverter(new CompassConverter(this, protocolTo));
    }

    public void runPack(Pack pack) {
        try {
            Logger.log("Converting " + pack);
            Logger.addTab();
            pack.getHandler().setup();
            Logger.log("Running Converters");
            for (Converter converter : converters.values()) {
                Logger.addTab();
                Logger.log("Running " + converter.getClass().getSimpleName());
                converter.convert(pack);
                Logger.subTab();
            }
            pack.getHandler().finish();
            Logger.subTab();
        } catch (Throwable t) {
            Logger.resetTab();
            Logger.log("Failed to convert!");
            Util.propagate(t);
        }
    }

    public void runDir() {
        try (Stream<Path> pathStream = Files.list(INPUT_DIR)) {
            try (Stream<Pack> packStream = pathStream.map(Pack::parse).filter(Objects::nonNull)) {
                packStream.forEach(this::runPack);
            } catch (Exception exception) {
                Util.propagate(exception);
            }
        } catch (Exception exception) {
            Util.propagate(exception);
        }
    }
}
