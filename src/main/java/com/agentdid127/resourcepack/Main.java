package com.agentdid127.resourcepack;

import joptsimple.OptionSet;

import java.io.IOException;

public class Main {

    /**
     * Main class. Runs program
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        OptionSet optionSet = Options.PARSER.parse(args);
        if (optionSet.has(Options.HELP)) {
            Options.PARSER.printHelpOn(System.out);
            return;
        }

        new PackConverter(optionSet, true).run();


    }



}