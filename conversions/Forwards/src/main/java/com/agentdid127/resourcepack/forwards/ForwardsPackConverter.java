package com.agentdid127.resourcepack.forwards;

import com.agentdid127.resourcepack.forwards.impl.*;
import com.agentdid127.resourcepack.forwards.impl.textures.*;
import com.agentdid127.resourcepack.forwards.impl.textures.creator.OffHandCreator;
import com.agentdid127.resourcepack.forwards.impl.textures.creator.WidgetSlidersCreator;
import com.agentdid127.resourcepack.forwards.impl.textures.slicer.*;
import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.Logger;
import com.agentdid127.resourcepack.library.utilities.Util;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.stream.Stream;

public class ForwardsPackConverter extends PackConverter {
    Path INPUT_DIR;
    private final int from;
    private final int to;

    public ForwardsPackConverter(Gson gson, int from, int to, String light, Path input, boolean debug,
                                 PrintStream out) {
        this.gson = gson;
        Logger.setDebug(debug);
        Logger.setStream(out);
        Logger.log("Converting packs from: " + from + " to " + to);
        this.from = from;
        this.to = to;
        this.INPUT_DIR = input;
        this.setupConverters(light);
    }

    private void setupConverters(String light) {
        this.registerConverter(new NameConverter(this, from, to)); // This needs to be run first, other converters might reference new directory names.
        this.registerConverter(new PackMetaConverter(this, from, to));
        this.registerConverter(new CompassConverter1_9(this, to));
        this.registerConverter(new OffHandCreator(this));
        this.registerConverter(new SpacesConverter(this));
        this.registerConverter(new ModelConverter(this, light, from, to));
        this.registerConverter(new SoundsConverter(this));
        this.registerConverter(new AnimationConverter(this));
        this.registerConverter(new MapIconConverter1_13(this));
        this.registerConverter(new MCPatcherConverter(this));
        this.registerConverter(new WaterConverter1_13(this));
        this.registerConverter(new BlockStateConverter(this, from, to));
        this.registerConverter(new LangConverter(this, from, to));
        this.registerConverter(new ParticlesSlicerConverter(this, from, to));
        this.registerConverter(new PaintingSlicerConverter(this));
        this.registerConverter(new MobEffectAtlasSlicerConverter(this, from));
        this.registerConverter(new ChestConverter1_15(this));
        this.registerConverter(new EnchantConverter1_15(this));
        this.registerConverter(new ParticleConverter(this));
        this.registerConverter(new InventoryConverter(this));
        this.registerConverter(new AtlasConverter(this));
        this.registerConverter(new CreativeTabsConverter1_19_3(this));
        this.registerConverter(new EnchantPathConverter(this));
        this.registerConverter(new WidgetSlidersCreator(this));
        this.registerConverter(new TitleConverter1_20(this));
        this.registerConverter(new GuiSlicerConverter(this, from));
        this.registerConverter(new ImageFormatConverter(this));
        this.registerConverter(new MapIconSlicerConverter(this, from));
        this.registerConverter(new ArmorMoverConverter1_21_2(this));
        this.registerConverter(new PostEffectShadersConverter1_21_2(this));
    }

    public void runPack(Pack pack) {
        try {
            Logger.log("Converting " + pack);
            pack.getHandler().setup();
            Logger.addTab();
            Logger.log("Running Converters");
            for (Converter converter : converters.values()) {
                if (converter.shouldConvert(gson, from, to)) {
                    Logger.addTab();
                    Logger.log("Running " + converter.getClass().getSimpleName());
                    converter.convert(pack);
                    Logger.subTab();
                }
            }
            Logger.subTab();
            pack.getHandler().finish();
        } catch (Throwable t) {
            Logger.resetTab();
            Logger.log("Failed to convert!");
            Util.propagate(t);
        }
    }

    public void runDir() throws IOException {
        try (Stream<Path> pathStream = Files.list(INPUT_DIR)) {
            try (Stream<Pack> packStream = pathStream.map(Pack::parse).filter(Objects::nonNull)) {
                packStream.forEach(this::runPack);
            }
        }
    }
}
