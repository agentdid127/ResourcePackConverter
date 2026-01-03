package com.agentdid127.resourcepack;

import com.agentdid127.resourcepack.backwards.BackwardsPackConverter;
import com.agentdid127.resourcepack.forwards.ForwardsPackConverter;
import com.agentdid127.resourcepack.library.utilities.Util;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.Strictness;
import joptsimple.OptionSet;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;

public class Main {
    /**
     * Main class. Runs program
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        OptionSet optionSet = Options.PARSER.parse(args);
        if (optionSet.has(Options.HELP)) {
            Options.PARSER.printHelpOn(System.out);
            return;
        }

        Path inputPath = optionSet.valueOf(Options.INPUT_DIR);
        String light = optionSet.valueOf(Options.LIGHT);
        boolean minify = optionSet.has(Options.MINIFY);
        boolean debug = optionSet.valueOf(Options.DEBUG);
        PrintStream out = System.out;

        GsonBuilder gsonBuilder = new GsonBuilder().disableHtmlEscaping();
        if (!minify) {
            gsonBuilder.setPrettyPrinting();
        }
        Gson gson = gsonBuilder.disableHtmlEscaping().create();

        int from = Util.getVersionProtocol(gson, optionSet.valueOf(Options.FROM));
        int to = Util.getVersionProtocol(gson, optionSet.valueOf(Options.TO));
        if (from < to) {
            new ForwardsPackConverter(gson, from, to, light, inputPath, debug, out).runDir();
        } else {
            new BackwardsPackConverter(gson, from, to, inputPath, debug, out).runDir();
        }
    }
}