package com.agentdid127.resourcepack.impl.forwards;

import com.agentdid127.resourcepack.Converter;
import com.agentdid127.resourcepack.PackConverter;
import com.agentdid127.resourcepack.Util;
import com.agentdid127.resourcepack.pack.Pack;
import com.google.gson.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;

public class ModelConverter extends Converter {

    private int version;
    private int from;
    protected String light = "none";
    public ModelConverter(PackConverter packConverter, String lightIn, int versionIn, int fromIn) {
        super(packConverter);
        light = lightIn;
        version = versionIn;
        from = fromIn;
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
                    JsonObject jsonObject;
                    if (Util.readJson(packConverter.getGson(), model) != null && Util.readJson(packConverter.getGson(), model).isJsonObject())
                    jsonObject = Util.readJson(packConverter.getGson(), model);
                    else {
                        System.out.println("Could not convert model: " + model.getFileName());
                        if (Util.readJson(packConverter.getGson(), model) == null) System.out.println("Check for Syntax Errors in file.");
                        else System.out.println("File is not JSON Object.");
                        return;
                    }

                    //GUI light system for 1.15.2
                    if (!light.equals("none") && (light.equals("front") || light.equals("side"))) jsonObject.addProperty("gui_light", light);
                    // minify the json so we can replace spaces in paths easily
                    // TODO Improvement: handle this in a cleaner way?
                    String content = jsonObject.toString();
                    content = content.replaceAll("items/", "item/");
                    content = content.replaceAll("blocks/", "block/");
                    content = content.replaceAll(" ", "_");

                    Files.write(model, Collections.singleton(content), StandardCharsets.UTF_8);

                    // handle the remapping of textures, for models that use default texture names
                    jsonObject = Util.readJson(packConverter.getGson(), model);
                    if (jsonObject.has("textures") && jsonObject.get("textures").isJsonObject()) {
                        NameConverter nameConverter = packConverter.getConverter(NameConverter.class);

                        JsonObject textureObject = jsonObject.getAsJsonObject("textures");
                        for (Map.Entry<String, JsonElement> entry : textureObject.entrySet()) {
                            String value = entry.getValue().getAsString();


                            //1.13 Mappings
                            if (version >= Util.getVersionProtocol(packConverter.getGson(), "1.13")) {
                                if (value.startsWith("block/")) {
                                    textureObject.addProperty(entry.getKey(), "block/" + nameConverter.getBlockMapping().remap(value.substring("block/".length())).toLowerCase().replaceAll("[()]", ""));
                                } else if (value.startsWith("item/")) {
                                    textureObject.addProperty(entry.getKey(), "item/" + nameConverter.getItemMapping().remap(value.substring("item/".length())).toLowerCase().replaceAll("[()]", ""));
                                }
                                else textureObject.addProperty(entry.getKey(), entry.getValue().getAsString().toLowerCase().replaceAll("[()]", ""));
                            }

                            //1.14 Mappings
                            if (version >= Util.getVersionProtocol(packConverter.getGson(), "1.14")) {
                                if (value.startsWith("block/")) {
                                    textureObject.addProperty(entry.getKey(), "block/" + nameConverter.getNewBlockMapping().remap(value.substring("block/".length())));
                                }
                            }

                            //1.17 Mappings
                            if (version >= Util.getVersionProtocol(packConverter.getGson(), "1.17")) {
                                if (value.startsWith("block/")) {
                                    textureObject.addProperty(entry.getKey(), "block/" + nameConverter.getBlockMapping17().remap(value.substring("block/".length())).toLowerCase().replaceAll("[()]", ""));
                                } else if (value.startsWith("item/")) {
                                    textureObject.addProperty(entry.getKey(), "item/" + nameConverter.getItemMapping17().remap(value.substring("item/".length())).toLowerCase().replaceAll("[()]", ""));
                                }
                                else textureObject.addProperty(entry.getKey(), entry.getValue().getAsString().toLowerCase().replaceAll("[()]", ""));
                            }


                            //Dyes
                            if (value.startsWith("item/") && value.contains("dye")) {
                                if (version > Util.getVersionProtocol(packConverter.getGson(), "1.13")) {
                                    textureObject.addProperty(entry.getKey(), "item/" + nameConverter.getNewItemMapping().remap(value.substring("item/".length())));
                                }
                            }
                        }
                    }
                    //fix display settings for packs from 1.8
                    if (jsonObject.has("display") && from == Util.getVersionProtocol(packConverter.getGson(), "1.8")) {
                        JsonObject display = jsonObject.getAsJsonObject("display");
                        if (display.has("firstperson")){
                            display.add("firstperson_righthand", display.get("firstperson"));
                            display.remove("firstperson");

                        }
                        if (display.has("thirdperson")) {
                            display.add("thirdperson_righthand", display.get("thirdperson"));
                            display.remove("thirdperson");
                        }
                        jsonObject.remove("display");
                        jsonObject.add("display", display);
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

                    //Parent Stuff
                    if (jsonObject.has("parent")) {
                        //Change parent to lowercase
                        for(Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                            if (entry.getKey().equals("parent")) {
                                String parent = entry.getValue().getAsString().toLowerCase();



                                parent = parent.replace(" ", "_");
                                //Get block/item parents renamed
                                if (from < Util.getVersionProtocol(packConverter.getGson(), "1.13") && version >= Util.getVersionProtocol(packConverter.getGson(), "1.13")) {
                                    if (parent.startsWith("block/")) {
                                        parent = setParent("block/", "/forwards/blocks.json", parent, "1_13");
                                    }
                                    if (parent.startsWith("item/")) {
                                       parent = setParent("item/", "/forwards/items.json", parent, "1_13");
                                    }
                                }
                                if (from < Util.getVersionProtocol(packConverter.getGson(), "1.14") && version >= Util.getVersionProtocol(packConverter.getGson(), "1.14")) {
                                    if (parent.startsWith("block/")) {
                                        parent = setParent("block/", "/forwards/blocks.json", parent, "1_14");
                                    }
                                    if (parent.startsWith("item/")) {
                                        parent = setParent("item/", "/forwards/items.json", parent, "1_14");
                                    }
                                }
                                if (from < Util.getVersionProtocol(packConverter.getGson(), "1.17") && version >= Util.getVersionProtocol(packConverter.getGson(), "1.14")) {
                                    if (parent.startsWith("block/")) {
                                        parent = setParent("block/", "/forwards/blocks.json", parent, "1_17");
                                    }
                                    if (parent.startsWith("item/")) {
                                        parent = setParent("item/", "/forwards/items.json", parent, "1_17");
                                    }
                                }
                                jsonObject.addProperty(entry.getKey(), parent);
                            }
                        }
                    }
                    if (!Util.readJson(packConverter.getGson(), model).equals(jsonObject) && packConverter.DEBUG) System.out.println("Updating Model: " + model.getFileName());
                    Files.write(model, Collections.singleton(packConverter.getGson().toJson(jsonObject)), Charset.forName("UTF-8"));
                } catch (IOException e) {
                    throw Util.propagate(e);
                }
            });
}

    /**
     * Gets parent object and sets a new one
     * @param prefix prefix of file path
     * @param path File path of json control
     * @param parent Parent String
     * @return New string with changed parent.
     */
    protected String setParent(String prefix, String path, String parent, String item)
{
    String parent2 = parent.replace(prefix, "");
    JsonObject file = Util.readJsonResource(packConverter.getGson(), path).getAsJsonObject(item);
    if (file != null) {
        if (file.has(parent2))
        return prefix + file.get(parent2).getAsString();
        else return parent;
    }
    else{
        System.out.println("Prefix Failed on: " + parent);
        return "";
    }
}
}
