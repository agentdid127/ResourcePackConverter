package com.agentdid127.resourcepack.backwards;

import com.agentdid127.resourcepack.backwards.impl.*;
import com.agentdid127.resourcepack.backwards.impl.textures.*;
import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.Logger;
import com.agentdid127.resourcepack.library.utilities.Util;
import com.google.gson.Gson;

import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.stream.Stream;

public class BackwardsPackConverter extends PackConverter {
    Path INPUT_DIR;
    private final int from;
    private final int to;

    public BackwardsPackConverter(Gson gson, int from, int to, Path input, boolean debug, PrintStream out) {
        this.gson = gson;
        Logger.setDebug(debug);
        Logger.setStream(out);
        Logger.log("Converting packs from: " + from + " to " + to);
        this.from = from;
        this.to = to;
        this.INPUT_DIR = input;
        this.setupConverters();
    }

    private void setupConverters() {
        this.registerConverter(new NameConverter(this, from, to)); // This needs to be run first, other converters might reference new directory names.
        this.registerConverter(new PackMetaConverter(this, to));
        this.registerConverter(new DeleteFileConverter(this, from, to));
        // TODO: backwards TitleConverter for going from 1.20 to below
        this.registerConverter(new EnchantPathConverter(this));
        this.registerConverter(new ParticleConverter(this));
        this.registerConverter(new InventoryConverter(this));
        this.registerConverter(new EnchantConverter(this));
        this.registerConverter(new ChestConverter(this));
        this.registerConverter(new PaintingConverter(this));
        this.registerConverter(new ParticleTextureConverter(this, from, to));
        this.registerConverter(new LangConverter(this, from, to));
//         this.registerConverter(new SoundsConverter(this)); //         return from >= Util.getVersionProtocol(gson, "1.13") && to <= Util.getVersionProtocol(gson, "1.12.2");
//         this.registerConverter(new AnimationConverter(this)); //         return from >= Util.getVersionProtocol(gson, "1.13") && to <= Util.getVersionProtocol(gson, "1.12.2");
        this.registerConverter(new MapIconConverter(this));
        this.registerConverter(new MCPatcherConverter(this));
        this.registerConverter(new WaterConverter(this));
        this.registerConverter(new BlockStateConverter(this, from, to));
        this.registerConverter(new ModelConverter(this, from, to));
        this.registerConverter(new CompassConverter(this, to));
    }

    public void runPack(Pack pack) {
        try {
            Logger.log("Converting " + pack);
            Logger.addTab();
            pack.getHandler().setup();
            Logger.log("Running Converters");
            for (Converter converter : converters.values()) {
                if (converter.shouldConvert(gson, from, to)) {
                    Logger.addTab();
                    Logger.log("Running " + converter.getClass().getSimpleName());
                    converter.convert(pack);
                    Logger.subTab();
                }
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
