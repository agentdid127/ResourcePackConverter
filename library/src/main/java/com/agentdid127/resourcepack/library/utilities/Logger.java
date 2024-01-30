package com.agentdid127.resourcepack.library.utilities;

import java.io.PrintStream;

public class Logger {
    private static PrintStream stream;

    public static void setStream(PrintStream stream) {
        Logger.stream = stream;
    }

    public static void log(String message) {
        stream.println(message);
    }

    public static void log(Object thing) {
        stream.println(String.valueOf(thing));
    }
}
