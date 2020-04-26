package technology.agentdid127.resourcepack.impl;

import com.google.gson.*;
import technology.agentdid127.resourcepack.Converter;
import technology.agentdid127.resourcepack.PackConverter;
import technology.agentdid127.resourcepack.Util;
import technology.agentdid127.resourcepack.pack.Pack;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;

public class ModelConverter extends Converter {

    private double version;
    protected String light = "none";
    public ModelConverter(PackConverter packConverter, String lightIn, String versionIn) {
        super(packConverter);
        light = lightIn;
        version = Double.parseDouble(versionIn);
    }

    /**
     * Runs findfiles with the directory Models
     * @param pack
     * @throws IOException
     */
    @Override
    public void convert(Pack pack) throws IOException {
        Path models = pack.getWorkingPath().resolve("assets" + File.separator + "minecraft" +File.separator + "models");
        if (!models.toFile().exists()) return;
        findFiles(models);
        //remapModelJson(models.resolve("item"));
        //remapModelJson(models.resolve("block"));
    }

    /**
     * Recursively finds files with Path path and runs remapModelJson
     * @param path
     * @throws IOException
     */
    protected void findFiles(Path path) throws IOException {
        File directory = new File(path.toString());
        File[] fList = directory.listFiles();
        for (File file : fList) {
            if (file.isDirectory()) {
                remapModelJson(Paths.get(file.getPath()));
                findFiles(Paths.get(file.getPath()));

            }
        }
    }

    /**
     * Updates model Json to newer versions
     * @param path
     * @throws IOException
     */
    protected void remapModelJson(Path path) throws IOException {

    if (!path.toFile().exists()) return;

    Files.list(path)
            .filter(path1 -> path1.toString().endsWith(".json"))
            .forEach(model -> {
                try {
                    JsonObject jsonObject = Util.readJson(packConverter.getGson(), model);

                    //GUI light system for 1.15.2
                    if (!light.equals("none") && (light.equals("front") || light.equals("side"))) jsonObject.addProperty("gui_light", light);
                    // minify the json so we can replace spaces in paths easily
                    // TODO Improvement: handle this in a cleaner way?
                    String content = jsonObject.toString();
                    content = content.replaceAll("items/", "item/");
                    content = content.replaceAll("blocks/", "block/");
                    content = content.replaceAll(" ", "_");

                    Files.write(model, Collections.singleton(content), Charset.forName("UTF-8"));

                    // handle the remapping of textures, for models that use default texture names
                    jsonObject = Util.readJson(packConverter.getGson(), model);
                    if (jsonObject.has("textures")) {
                        NameConverter nameConverter = packConverter.getConverter(NameConverter.class);

                        JsonObject textureObject = jsonObject.getAsJsonObject("textures");
                        for (Map.Entry<String, JsonElement> entry : textureObject.entrySet()) {
                            String value = entry.getValue().getAsString();
                            if (version > 1.13) {
                                if (value.startsWith("block/")) {
                                    textureObject.addProperty(entry.getKey(), "block/" + nameConverter.getNewBlockMapping().remap(value.substring("block/".length())));
                                }
                            }
                            if (version >= 1.13) {
                                if (value.startsWith("block/")) {
                                    textureObject.addProperty(entry.getKey(), "block/" + nameConverter.getBlockMapping().remap(value.substring("block/".length())).toLowerCase().replaceAll("[()]", ""));
                                } else if (value.startsWith("item/")) {
                                    textureObject.addProperty(entry.getKey(), "item/" + nameConverter.getItemMapping().remap(value.substring("item/".length())).toLowerCase().replaceAll("[()]", ""));
                                }
                                else textureObject.addProperty(entry.getKey(), entry.getValue().getAsString().toLowerCase().replaceAll("[()]", ""));
                            }
                        }
                    }
                    if (jsonObject.has("overrides")) {
                        for(Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {

                            if (entry.getKey().equals("overrides")) {
                                JsonArray overrides = jsonObject.get("overrides").getAsJsonArray();
                                JsonArray overrides2 = new JsonArray();
                                for (int i = 0; i < overrides.size(); i++) {
                                    JsonObject object = overrides.get(i).getAsJsonObject();
                                    for (Map.Entry<String, JsonElement> json : object.entrySet()) {
                                        if (json.getKey().equals("model"))
                                            object.addProperty(json.getKey(), json.getValue().getAsString().replaceAll("[()]", ""));
                                        else object.add(json.getKey(), json.getValue());
                                    }
                                    overrides2.add(object);
                                }
                            jsonObject.add(entry.getKey(), overrides2);
                            }

                        }

                    }
                    if (jsonObject.has("parent")) {
                        for(Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                            if(entry.getKey().equals("parent")) jsonObject.addProperty(entry.getKey(), entry.getValue().getAsString().toLowerCase());
                        }

                    }
                    System.out.println("Updating Model: " + model.getFileName());
                    Files.write(model, Collections.singleton(packConverter.getGson().toJson(jsonObject)), Charset.forName("UTF-8"));
                } catch (IOException e) {
                    throw Util.propagate(e);
                }
            });
}
}
