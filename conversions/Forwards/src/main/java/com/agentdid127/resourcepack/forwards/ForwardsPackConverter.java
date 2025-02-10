package com.agentdid127.resourcepack.forwards;

import com.agentdid127.resourcepack.forwards.impl.other.*;
import com.agentdid127.resourcepack.forwards.impl.other.textures.InventoryConverter;
import com.agentdid127.resourcepack.forwards.impl.other.textures.ParticlesSlicerConverter;
import com.agentdid127.resourcepack.forwards.impl.v1_14.textures.MobEffectAtlasSlicerConverter1_14;
import com.agentdid127.resourcepack.forwards.impl.v1_14.textures.PaintingSlicerConverter1_14_4;
import com.agentdid127.resourcepack.forwards.impl.v1_9.textures.OffHandCreator1_9;
import com.agentdid127.resourcepack.forwards.impl.v1_19.textures.WidgetSlidersCreator1_19_4;
import com.agentdid127.resourcepack.forwards.impl.v1_13.AnimationConverter1_13;
import com.agentdid127.resourcepack.forwards.impl.v1_13.LangConverter1_13;
import com.agentdid127.resourcepack.forwards.impl.v1_13.MCPatcherConverter1_13;
import com.agentdid127.resourcepack.forwards.impl.v1_13.SoundsConverter1_13;
import com.agentdid127.resourcepack.forwards.impl.v1_13.textures.MapIconConverter1_13;
import com.agentdid127.resourcepack.forwards.impl.v1_13.textures.WaterConverter1_13;
import com.agentdid127.resourcepack.forwards.impl.v1_15.textures.ChestConverter1_15;
import com.agentdid127.resourcepack.forwards.impl.v1_15.textures.EnchantConverter1_15;
import com.agentdid127.resourcepack.forwards.impl.v1_19.AtlasConverter1_19_3;
import com.agentdid127.resourcepack.forwards.impl.v1_19.EnchantPathConverter1_19_4;
import com.agentdid127.resourcepack.forwards.impl.v1_19.textures.CreativeTabsConverter1_19_3;
import com.agentdid127.resourcepack.forwards.impl.v1_20.ImageFormatConverter1_20_3;
import com.agentdid127.resourcepack.forwards.impl.v1_20.textures.GuiSlicerConverter1_20_2;
import com.agentdid127.resourcepack.forwards.impl.v1_20.textures.MapIconSlicerConverter1_20_5;
import com.agentdid127.resourcepack.forwards.impl.v1_20.textures.TitleConverter1_20;
import com.agentdid127.resourcepack.forwards.impl.v1_21.textures.ArmorMoverConverter1_21_2;
import com.agentdid127.resourcepack.forwards.impl.v1_21.textures.PostEffectShadersConverter1_21_2;
import com.agentdid127.resourcepack.forwards.impl.v1_9.textures.CompassConverter1_9;
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
        this.registerConverter(new OffHandCreator1_9(this));
        this.registerConverter(new RemoveSpacesConverter1_11(this));
        this.registerConverter(new ModelConverter(this, light, from, to));
        this.registerConverter(new SoundsConverter1_13(this));
        this.registerConverter(new AnimationConverter1_13(this));
        this.registerConverter(new MapIconConverter1_13(this));
        this.registerConverter(new MCPatcherConverter1_13(this));
        this.registerConverter(new WaterConverter1_13(this));
        this.registerConverter(new BlockStateConverter(this, from, to));
        this.registerConverter(new LangConverter1_13(this, from, to));
        this.registerConverter(new ParticlesSlicerConverter(this, from, to));
        this.registerConverter(new PaintingSlicerConverter1_14_4(this));
        this.registerConverter(new MobEffectAtlasSlicerConverter1_14(this, from));
        this.registerConverter(new ChestConverter1_15(this));
        this.registerConverter(new EnchantConverter1_15(this));
        this.registerConverter(new ParticleConverter1_18(this));
        this.registerConverter(new InventoryConverter(this));
        this.registerConverter(new AtlasConverter1_19_3(this));
        this.registerConverter(new CreativeTabsConverter1_19_3(this));
        this.registerConverter(new EnchantPathConverter1_19_4(this));
        this.registerConverter(new WidgetSlidersCreator1_19_4(this));
        this.registerConverter(new TitleConverter1_20(this));
        this.registerConverter(new GuiSlicerConverter1_20_2(this, from));
        this.registerConverter(new ImageFormatConverter1_20_3(this));
        this.registerConverter(new MapIconSlicerConverter1_20_5(this, from));
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
