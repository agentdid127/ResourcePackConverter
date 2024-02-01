package com.agentdid127.resourcepack;

import com.agentdid127.converter.Plugin;
import com.agentdid127.resourcepack.library.RPPlugin;
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
            for (RPPlugin value : CommonTool.instance.pluginLoader.getPlugins().values()) {
                value.onUnload();
            }
        }
    }
}