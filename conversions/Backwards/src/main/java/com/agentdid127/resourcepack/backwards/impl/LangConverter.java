package com.agentdid127.resourcepack.backwards.impl;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.JsonUtil;
import com.agentdid127.resourcepack.library.utilities.Logger;
import com.agentdid127.resourcepack.library.utilities.PropertiesEx;
import com.agentdid127.resourcepack.library.utilities.Util;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Stream;

public class LangConverter extends Converter {
    private final int from;
    private final int to;

    public LangConverter(PackConverter packConverter, int from, int to) {
        super(packConverter);
        this.from = from;
        this.to = to;
    }

    @Override
    public boolean shouldConvert(Gson gson, int from, int to) {
        return from >= Util.getVersionProtocol(gson, "1.13") && to <= Util.getVersionProtocol(gson, "1.12.2");
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

        ArrayList<String> models = new ArrayList<>();
        try (Stream<Path> pathStream = Files.list(langPath).filter(path1 -> path1.toString().endsWith(".json"))) {
            pathStream.forEach(model -> {
                PropertiesEx out = new PropertiesEx();
                try {
                    JsonObject object = JsonUtil.readJson(packConverter.getGson(), model, JsonObject.class);
                    if (from > Util.getVersionProtocol(packConverter.getGson(), "1.12")
                            && ((to < Util.getVersionProtocol(packConverter.getGson(), "1.13"))
                            && (to > Util.getVersionProtocol(packConverter.getGson(), "1.13.2")))) {
                        JsonObject id = JsonUtil.readJsonResource(packConverter.getGson(), "/backwards/lang.json").getAsJsonObject("1_13");
                        if (object != null) {
                            object.keySet().forEach(key -> {
                                String value = object.get(key).getAsString();
                                for (Map.Entry<String, JsonElement> id2 : id.entrySet()) {
                                    if (key.equals(id2.getKey())) {
                                        out.setProperty(id2.getValue().getAsString(), value);
                                    }
                                }
                            });
                        }
                    }

                    if (to <= Util.getVersionProtocol(packConverter.getGson(), "1.14")) {
                        JsonObject id = JsonUtil.readJsonResource(packConverter.getGson(), "/backwards/lang.json").getAsJsonObject("1_14");
                        if (object != null) {
                            object.keySet().forEach(key -> {
                                String value = object.get(key).getAsString();
                                for (Map.Entry<String, JsonElement> id2 : id.entrySet())
                                    if (key.equals(id2.getKey()))
                                        out.setProperty(id2.getValue().getAsString(), value);
                            });
                        }
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    int modelNoJson = model.getFileName().toString().indexOf(".json");
                    String file2 = model.getFileName().toString().substring(0, modelNoJson);
                    Logger.debug("Saving: " + file2 + ".lang");
                    out.store(Files.newOutputStream(pack.getWorkingPath().resolve(("assets/minecraft/lang/" + file2 + ".lang").replace("/", File.separator)).toFile().toPath()), "");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                models.add(model.getFileName().toString());
            });
        }

        for (String model : models) {
            Path langFilePath = pack.getWorkingPath().resolve(("assets/minecraft/lang/" + model).replace("/", File.separator));
            Logger.debug("Deleting: " + langFilePath);
            Files.delete(langFilePath);
        }
    }
}
