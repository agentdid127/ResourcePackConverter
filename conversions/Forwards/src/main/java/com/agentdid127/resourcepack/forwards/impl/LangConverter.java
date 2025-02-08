package com.agentdid127.resourcepack.forwards.impl;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.JsonUtil;
import com.agentdid127.resourcepack.library.utilities.Logger;
import com.agentdid127.resourcepack.library.utilities.PropertiesEx;
import com.agentdid127.resourcepack.library.utilities.Util;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Map;
import java.util.stream.Stream;

public class LangConverter extends Converter {
    private final String from;
    private final String to;

    public LangConverter(PackConverter packConverter, String from, String to) {
        super(packConverter);
        this.from = from;
        this.to = to;
    }

    /**
     * Moves Lang (properties) to JSON
     *
     * @param pack
     * @throws IOException
     */
    @Override
    public void convert(Pack pack) throws IOException {
        Path langPath = pack.getWorkingPath().resolve("assets/minecraft/lang".replace("/", File.separator));
        if (!langPath.toFile().exists()) {
            return;
        }

        ArrayList<String> models = new ArrayList<String>();
        try (Stream<Path> pathStream = Files.list(langPath).filter(path1 -> path1.toString().endsWith(".lang"))) {
            pathStream.forEach(lang -> {
                JsonObject out = new JsonObject();
                try (InputStream input = Files.newInputStream(Paths.get(lang.toString()))) {
                    PropertiesEx prop = new PropertiesEx();
                    prop.load(input);
                    if (Util.getVersionProtocol(packConverter.getGson(), from) <= Util.getVersionProtocol(packConverter.getGson(), "1.12")
                            && ((Util.getVersionProtocol(packConverter.getGson(), to) >= Util.getVersionProtocol(packConverter.getGson(), "1.13"))
                            && (Util.getVersionProtocol(packConverter.getGson(), to) <= Util.getVersionProtocol(packConverter.getGson(), "1.13.2")))) {
                        JsonObject id = JsonUtil.readJsonResource(packConverter.getGson(), "/forwards/lang.json").getAsJsonObject("1_13");
                        Enumeration<String> enums = (Enumeration<String>) prop.propertyNames();
                        while (enums.hasMoreElements()) {
                            String key = enums.nextElement();
                            String value = prop.getProperty(key);
                            for (Map.Entry<String, JsonElement> id2 : id.entrySet()) {
                                if (key.equals(id2.getKey()))
                                    out.addProperty(id2.getValue().getAsString(), value);
                                else
                                    out.addProperty(key, value);
                            }
                        }
                    }

                    if (Util.getVersionProtocol(packConverter.getGson(), to) > Util.getVersionProtocol(packConverter.getGson(), "1.14")) {
                        JsonObject id = JsonUtil.readJsonResource(packConverter.getGson(), "/forwards/lang.json").getAsJsonObject("1_14");
                        Enumeration<String> enums = (Enumeration<String>) prop.propertyNames();
                        while (enums.hasMoreElements()) {
                            String key = enums.nextElement();
                            String value = prop.getProperty(key);
                            for (Map.Entry<String, JsonElement> id2 : id.entrySet()) {
                                if (key.equals(id2.getKey()))
                                    out.addProperty(id2.getValue().getAsString(), value);
                                else
                                    out.addProperty(key, value);
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    int modelNoLang = lang.getFileName().toString().indexOf(".lang");
                    String file2 = lang.getFileName().toString().substring(0, modelNoLang);
                    Logger.debug("Saving: " + file2 + ".json");
                    Path outLangPath = pack.getWorkingPath().resolve(("assets/minecraft/lang/" + file2 + ".json").replace("/", File.separator));
                    JsonUtil.writeJson(packConverter.getGson(), outLangPath, out);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                models.add(lang.getFileName().toString());
            });
        }

        for (String model : models) {
            Path langFilePath = pack.getWorkingPath().resolve(("assets/minecraft/lang/" + model).replace("/", File.separator));
            Logger.debug("Deleting: " + langFilePath);
            Files.delete(langFilePath);
        }
    }
}
