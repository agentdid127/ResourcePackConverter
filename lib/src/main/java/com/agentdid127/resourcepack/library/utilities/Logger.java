package com.agentdid127.resourcepack.library.utilities;

import java.io.PrintStream;

public class Logger {
    private static PrintStream stream;
    private static PrintStream errorStream;
    
    public static void setStream(PrintStream stream) {
        Logger.stream = stream;
    }

    public static void setErrorStream(PrintStream stream) {
	Logger.errorStream = stream;
    }

    public static void log(String message) {
        stream.println(message);
    }

    public static void log(Object thing) {
        stream.println(String.valueOf(thing));
    }

    public static void error(String message) {
	errorStream.println(message);
    }

    public static void error(Object thing) {
	errorStream.println(String.valueOf(thing));
    }
}
