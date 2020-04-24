package technology.agentdid127.resourcepack.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import technology.agentdid127.resourcepack.Converter;
import technology.agentdid127.resourcepack.PackConverter;
import technology.agentdid127.resourcepack.Util;
import technology.agentdid127.resourcepack.extra.PropertiesEx;
import technology.agentdid127.resourcepack.pack.Pack;

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
                        PropertiesEx prop2 = new PropertiesEx();
                        prop.load(input);

                        if (Double.parseDouble(from) <= 1.12 && (Double.parseDouble(version) == 1.13) ) {
                            JsonObject id = Util.readJsonResource(packConverter.getGson(), "/lang.json");

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
                        if (Double.parseDouble(version) > 1.13)
                        {
                            JsonObject id = Util.readJsonResource(packConverter.getGson(), "/lang1_14.json");

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



