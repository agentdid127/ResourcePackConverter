package com.agentdid127.resourcepack.backwards.impl;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.Logger;
import com.agentdid127.resourcepack.library.utilities.PropertiesEx;
import com.agentdid127.resourcepack.library.utilities.Util;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
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
        Path langPath = pack.getWorkingPath()
                .resolve("assets/minecraft/lang".replace("/", File.separator));
        if (!langPath.toFile().exists())
            return;
        ArrayList<String> models = new ArrayList<String>();
        Files.list(langPath)
                .filter(path1 -> path1.toString().endsWith(".json"))
                .forEach(model -> {
                    PropertiesEx out = new PropertiesEx();
                    try (InputStream input = new FileInputStream(model.toString())) {
                        JsonObject object = Util.readJson(packConverter.getGson(), model, JsonObject.class);

                        if (Util.getVersionProtocol(packConverter.getGson(), from) > Util
                                .getVersionProtocol(packConverter.getGson(), "1.12")
                                && ((Util.getVersionProtocol(packConverter.getGson(), version) < Util
                                        .getVersionProtocol(packConverter.getGson(), "1.13"))
                                        && (Util.getVersionProtocol(packConverter.getGson(), version) > Util
                                                .getVersionProtocol(packConverter.getGson(), "1.13.2")))) {
                            JsonObject id = Util.readJsonResource(packConverter.getGson(), "/backwards/lang.json")
                                    .getAsJsonObject("1_13");
                            object.keySet().forEach(key -> {
                                String value = object.get(key).getAsString();
                                for (Map.Entry<String, JsonElement> id2 : id.entrySet()) {
                                    if (key.equals(id2.getKey())) {
                                        out.setProperty(id2.getValue().getAsString(), value);
                                    }
                                }
                            });
                        }

                        if (Util.getVersionProtocol(packConverter.getGson(), version) <= Util
                                .getVersionProtocol(packConverter.getGson(), "1.14")) {
                            JsonObject id = Util.readJsonResource(packConverter.getGson(), "/backwards/lang.json")
                                    .getAsJsonObject("1_14");
                            object.keySet().forEach(key -> {
                                String value = object.get(key).getAsString();
                                for (Map.Entry<String, JsonElement> id2 : id.entrySet())
                                    if (key.equals(id2.getKey()))
                                        out.setProperty(id2.getValue().getAsString(), value);
                            });
                        }

                        input.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        int modelNoJson = model.getFileName().toString().indexOf(".json");
                        String file2 = model.getFileName().toString().substring(0, modelNoJson);
                        Logger.log("Saving: " + file2 + ".lang");
                        out.store(
                                new FileOutputStream(
                                        pack.getWorkingPath()
                                                .resolve(("assets/minecraft/lang/" + file2 + ".lang").replace("/",
                                                        File.separator))
                                                .toFile()),
                                "");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    models.add(model.getFileName().toString());
                });
        for (int i = 0; i < models.size(); i++) {
            Path langFilePath = pack.getWorkingPath()
                    .resolve(("assets/minecraft/lang/" + models.get(i)).replace("/", File.separator));
            Logger.log("Deleting: " + langFilePath);
            Files.delete(langFilePath);
        }
    }
}
