package com.agentdid127.resourcepack;

import joptsimple.ArgumentAcceptingOptionSpec;
import joptsimple.OptionParser;
import joptsimple.OptionSpec;
import joptsimple.ValueConverter;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class Options {

    public static final OptionParser PARSER = new OptionParser();

    public static final OptionSpec<Void> HELP = PARSER.acceptsAll(Arrays.asList("?", "h", "help"), "Print this message.").forHelp();

    public static final OptionSpec<Path> INPUT_DIR = PARSER.acceptsAll(Arrays.asList("i", "input", "input-dir"), "Input directory for the packs")
            .withRequiredArg()
            .withValuesConvertedBy(new PathConverter())
            .defaultsTo(Paths.get("./"));

    public static final OptionSpec<Boolean> DEBUG = PARSER.accepts("debug", "Displays other output").withRequiredArg().ofType(Boolean.class).defaultsTo(true);

    public static final OptionSpec<String> TO = PARSER.accepts("to", "Updates to version").withRequiredArg().ofType(String.class).defaultsTo("1.13");

    public static final ArgumentAcceptingOptionSpec<String> FROM = PARSER.accepts("from", "Updates from version").withRequiredArg().ofType(String.class).defaultsTo("1.12");

    public static final ArgumentAcceptingOptionSpec<String> LIGHT = PARSER.accepts("light", "Updates from version").withRequiredArg().ofType(String.class).defaultsTo("none");

    public static final OptionSpec<Void> MINIFY = PARSER.accepts("minify", "Minify the json files.");

    public static class PathConverter implements ValueConverter<Path> {


        @Override
        public Path convert(String s) {
            return Paths.get(s);
        }

        @Override
        public Class<? extends Path> valueType() {
            return Path.class;
        }

        @Override
        public String valuePattern() {
            return "*";
        }
    }

}