package net.hypixel.resourcepack;

import joptsimple.OptionSet;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        OptionSet optionSet = Options.PARSER.parse(args);
        if (optionSet.has(Options.HELP)) {
            Options.PARSER.printHelpOn(System.out);
            return;
        }

        new PackConverter(optionSet).run();


    }

}