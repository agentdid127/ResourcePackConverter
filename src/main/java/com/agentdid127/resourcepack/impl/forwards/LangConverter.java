package com.agentdid127.resourcepack.impl.forwards;

import com.agentdid127.resourcepack.Converter;
import com.agentdid127.resourcepack.PackConverter;
import com.agentdid127.resourcepack.Util;
import com.agentdid127.resourcepack.utilities.PropertiesEx;
import com.agentdid127.resourcepack.pack.Pack;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
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
     * @param pack
     * @throws IOException
     */
    @Override
    public void convert(Pack pack) throws IOException {
        Path path = pack.getWorkingPath().resolve("assets" + File.separator + "minecraft" + File.separator + "lang");
        if (!path.toFile().exists()) return;
        ArrayList<String> models = new ArrayList<String>();

        Files.list(path)
                .filter(path1 -> path1.toString().endsWith(".lang"))
                .forEach(model -> {
                    JsonObject out = new JsonObject();
                    try(
                            InputStream input = new FileInputStream(model.toString())) {
                        PropertiesEx prop = new PropertiesEx();
                        prop.load(input);

                        if (Util.getVersionProtocol(packConverter.getGson(), from) <= Util.getVersionProtocol(packConverter.getGson(), "1.12") && ((Util.getVersionProtocol(packConverter.getGson(), version) >= Util.getVersionProtocol(packConverter.getGson(), "1.13")) && (Util.getVersionProtocol(packConverter.getGson(), version) <= Util.getVersionProtocol(packConverter.getGson(), "1.13.2")))) {
                            JsonObject id = Util.readJsonResource(packConverter.getGson(), "/forwards/lang.json").getAsJsonObject("1_13");

                                Enumeration<String> enums = (Enumeration<String>) prop.propertyNames();
                                while (enums.hasMoreElements()) {
                                    String key = enums.nextElement();
                                    String value = prop.getProperty(key);
                                    for (Map.Entry<String, JsonElement> id2 : id.entrySet()) {
                                        if (key.equals(id2.getKey())) {
                                            out.addProperty(id2.getValue().getAsString(), value);
                                        } else out.addProperty(key, value);
                                    }
                                }


                                //Saves File


                        }
                        if (Util.getVersionProtocol(packConverter.getGson(), version) > Util.getVersionProtocol(packConverter.getGson(), "1.14"))
                        {
                            JsonObject id = Util.readJsonResource(packConverter.getGson(), "/forwards/lang.json").getAsJsonObject("1_14");

                                Enumeration<String> enums = (Enumeration<String>) prop.propertyNames();
                                while (enums.hasMoreElements()) {
                                    String key = enums.nextElement();
                                    String value = prop.getProperty(key);
                                    for (Map.Entry<String, JsonElement> id2 : id.entrySet()) {
                                        if (key.equals(id2.getKey())) {
                                            out.addProperty(id2.getValue().getAsString(), value);
                                        } else out.addProperty(key, value);
                                    }
                                }


                                //Saves File



                        }

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        int modelNoLang = model.getFileName().toString().indexOf(".lang");
                        String file2 = model.getFileName().toString().substring(0 ,modelNoLang);
                        System.out.println("Saving: " + file2 + ".json");
                        Files.write(pack.getWorkingPath().resolve("assets" + File.separator + "minecraft" + File.separator + "lang" + File.separator + file2 + ".json"), Collections.singleton(packConverter.getGson().toJson(out)), Charset.forName("UTF-8"));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    models.add(model.getFileName().toString());
                });
        for(int i = 0; i < models.size(); i++) {
            System.out.println("Deleting: " + pack.getWorkingPath().resolve("assets" + File.separator + "minecraft" + File.separator + "lang" + File.separator + models.get(i)));
            Files.delete(pack.getWorkingPath().resolve("assets" + File.separator + "minecraft" + File.separator + "lang" + File.separator + models.get(i)));
        }
    }

    }



