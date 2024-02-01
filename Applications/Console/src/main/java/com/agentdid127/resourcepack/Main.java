package com.agentdid127.resourcepack;

import com.agentdid127.converter.Plugin;
import java.io.PrintStream;
import joptsimple.OptionSet;
import java.io.IOException;

public class Main {
    /**
     * Main class. Runs program
     * 
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        OptionSet optionSet = Options.PARSER.parse(args);
        try {
            CommonTool.run(optionSet, System.out, System.err);
        } catch (Exception e) {
            e.printStackTrace();
            for (Plugin value : CommonTool.instance.pluginLoader.getPlugins().values()) {
                value.onUnload();
            }
        }
    }
}