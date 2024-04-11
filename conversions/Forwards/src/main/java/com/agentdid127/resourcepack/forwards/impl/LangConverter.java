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

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Map;

public class LangConverter extends Converter {
    private String version;
    private String from;

    public LangConverter(PackConverter packConverter, String fromIn, String versionIn) {
        super(packConverter);
        version = versionIn;
        from = fromIn;
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
        if (!langPath.toFile().exists())
            return;

        ArrayList<String> models = new ArrayList<String>();
        Files.list(langPath)
                .filter(path1 -> path1.toString().endsWith(".lang"))
                .forEach(model -> {
                    JsonObject out = new JsonObject();
                    try (InputStream input = new FileInputStream(model.toString())) {
                        PropertiesEx prop = new PropertiesEx();
                        prop.load(input);
                        if (Util.getVersionProtocol(packConverter.getGson(), from) <= Util
                                .getVersionProtocol(packConverter.getGson(), "1.12")
                                && ((Util.getVersionProtocol(packConverter.getGson(), version) >= Util
                                        .getVersionProtocol(packConverter.getGson(), "1.13"))
                                        && (Util.getVersionProtocol(packConverter.getGson(), version) <= Util
                                                .getVersionProtocol(packConverter.getGson(), "1.13.2")))) {
                            JsonObject id = Util.readJsonResource(packConverter.getGson(), "/forwards/lang.json")
                                    .getAsJsonObject("1_13");

                            @SuppressWarnings("unchecked")
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
                            // Saves File
                        }
                        if (Util.getVersionProtocol(packConverter.getGson(), version) > Util
                                .getVersionProtocol(packConverter.getGson(), "1.14")) {
                            JsonObject id = Util.readJsonResource(packConverter.getGson(), "/forwards/lang.json")
                                    .getAsJsonObject("1_14");

                            @SuppressWarnings("unchecked")
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
                            // Saves File
                        }
                        input.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        int modelNoLang = model.getFileName().toString().indexOf(".lang");
                        String file2 = model.getFileName().toString().substring(0, modelNoLang);
                        Logger.log("Saving: " + file2 + ".json");
                        Path outLangPath = pack.getWorkingPath()
                                .resolve(("assets/minecraft/lang/" + file2 + ".json").replace("/", File.separator));
                        JsonUtil.writeJson(packConverter.getGson(), outLangPath, out);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    models.add(model.getFileName().toString());
                });

        for (int i = 0; i < models.size(); i++) {
            Logger.log("Deleting: " + pack.getWorkingPath().resolve("assets" + File.separator + "minecraft"
                    + File.separator + "lang" + File.separator + models.get(i)));
            Files.delete(pack.getWorkingPath().resolve("assets" + File.separator + "minecraft" + File.separator + "lang"
                    + File.separator + models.get(i)));
        }
    }
}
