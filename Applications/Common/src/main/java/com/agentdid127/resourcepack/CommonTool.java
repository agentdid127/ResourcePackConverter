package com.agentdid127.resourcepack;

import com.agentdid127.converter.Converter;
import com.agentdid127.converter.Plugin;
import com.agentdid127.converter.core.PluginLoader;
import com.agentdid127.converter.iface.Application;
import com.agentdid127.converter.iface.IPluginLoader;
import com.agentdid127.converter.util.Logger;
import com.agentdid127.resourcepack.forwards.ForwardsPackConverter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.RPConverter;
import com.agentdid127.resourcepack.library.RPPlugin;
import com.agentdid127.resourcepack.library.RPPluginVersionSetter;
import com.agentdid127.resourcepack.library.Util;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import joptsimple.OptionSet;

import java.io.IOException;
import java.io.PrintStream;

public class CommonTool implements Application {

    IPluginLoader<RPPlugin> pluginLoader;

    public static CommonTool instance;

    public CommonTool() {
        CommonTool.instance = this;
    }


    public static void run(OptionSet optionSet, PrintStream out, PrintStream error) throws IOException {
        new CommonTool();
        if (optionSet.has(Options.HELP)) {
            Options.PARSER.printHelpOn(System.out);
            return;
        }

        String from = optionSet.valueOf(Options.FROM);
        String to = optionSet.valueOf(Options.TO);
        String light = optionSet.valueOf(Options.LIGHT);
        boolean minify = optionSet.has(Options.MINIFY);
        boolean unstable = optionSet.has(Options.UNSTABLE);
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.disableHtmlEscaping().create();

        Logger.setErrorStream(error);
        Logger.setStream(out);

        Path pluginsPath = Paths.get("./plugins/");
        if (!pluginsPath.toFile().exists()) {
            pluginsPath.toFile().mkdirs();
        }

        PluginLoader<RPPlugin> pluginLoader = new PluginLoader(pluginsPath.toFile(), RPPlugin.class,
            Arrays.asList("com.agentdid127.resourcepack.library", "com.agentdid127.converter",
                "com.agentdid127.resourcepack.forwards", "com.agentdid127.resourcepack.backwards"));
        instance.pluginLoader = pluginLoader;
        pluginLoader.loadPlugins();

        PackConverter packConverter;
        if (Util.getVersionProtocol(gson, from) > Util.getVersionProtocol(gson, to)) {
            Logger.error("Backwards Pack Conversion is temporarily disabled, as it is being updated to v3.0");
            return;
            // new BackwardsPackConverter(from, to, light, minify, optionSet.valueOf(Options.INPUT_DIR), true, out).runDir();
        } else {
            packConverter = new ForwardsPackConverter(from, to, light, minify, optionSet.valueOf(Options.INPUT_DIR),
                true, out, unstable);
        }

        for (RPPlugin value : pluginLoader.getPlugins().values()) {
            value.setApplication(instance);
            RPPluginVersionSetter.setData(value, from, to, packConverter);
            Logger.error(value.getFrom() + " " + value.getTo());
            Logger.error(value.getPackConverter());
            value.onInit();

            Logger.error(value.getRunners().size());

            for (RPConverter converter : value.getRunners()) {
                packConverter.registerConverter(converter);
            }
        }

        packConverter.runDir(optionSet.valueOf(Options.INPUT_DIR));

        for (RPPlugin value : pluginLoader.getPlugins().values()) {
            value.onUnload();
        }
    }

    @Override
    public IPluginLoader getPluginLoader() {
        return pluginLoader;
    }
}