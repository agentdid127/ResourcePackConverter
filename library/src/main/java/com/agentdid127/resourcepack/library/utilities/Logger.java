package com.agentdid127.resourcepack.library.utilities;

import java.io.PrintStream;

public class Logger {
    private static PrintStream stream;
    private static boolean debug;

    private static int tabs = 0;

    public static void addTab() {
        tabs++;
    }

    public static void subTab() {
        tabs--;
        if (tabs < 0) {
            tabs = 0;
        }
    }

    public static void resetTab() {
        tabs = 0;
    }

    private static String getTabs() {
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < tabs; i++) {
            out.append("  ");
        }
        return out.toString();
    }

    public static void setStream(PrintStream stream) {
        Logger.stream = stream;
    }

    public static void setDebug(boolean debug) {
        Logger.debug = debug;
    }

    public static void debug(String message) {
        if (debug) {
            log(message);
        }
    }

    public static void log(String message) {
        stream.println(getTabs() + message);
    }

    public static void error(String message) {
        log(message);
    }

    private static void debug(Object thing) {
        debug(thing.toString());
    }

    public static void log(Object thing) {
        String tabs = getTabs();
        stream.println(tabs + String.valueOf(thing).replaceAll("\n", "\n" + tabs));
    }
}
