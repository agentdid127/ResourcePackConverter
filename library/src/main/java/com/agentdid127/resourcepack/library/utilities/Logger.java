package com.agentdid127.resourcepack.library.utilities;

import java.io.PrintStream;

public class Logger {
    public static PrintStream out;

    public static void log(String message) {
        out.println(message);
    }

    public static void log(Object thing) {
        System.out.println(String.valueOf(thing));
    }
}
