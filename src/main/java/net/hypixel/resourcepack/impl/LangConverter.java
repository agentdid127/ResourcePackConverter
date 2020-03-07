package net.hypixel.resourcepack.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.hypixel.resourcepack.Converter;
import net.hypixel.resourcepack.PackConverter;
import net.hypixel.resourcepack.Util;
import net.hypixel.resourcepack.extra.PropertiesEx;
import net.hypixel.resourcepack.pack.Pack;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.Map;

public class LangConverter extends Converter {
    public LangConverter(PackConverter packConverter) {
        super(packConverter);
    }

    @Override

    public void convert(Pack pack) throws IOException {
        Path path = pack.getWorkingPath().resolve("assets" + File.separator + "minecraft" + File.separator + "lang");
        if (!path.toFile().exists()) return;

        Files.list(path)
                .filter(path1 -> path1.toString().endsWith(".lang"))
                .forEach(model -> {
                    try (InputStream input = new FileInputStream(model.toString())) {
                        PropertiesEx prop = new PropertiesEx();
                        PropertiesEx prop2 = new PropertiesEx();
                        prop.load(input);
                        JsonObject id = Util.readJsonResource(packConverter.getGson(), "/lang.json");
                        try (OutputStream output = new FileOutputStream(model.toString())) {
                        Enumeration<String> enums = (Enumeration<String>) prop.propertyNames();
                        while (enums.hasMoreElements()) {
                            String key = enums.nextElement();
                            String value = prop.getProperty(key);
                            for (Map.Entry<String, JsonElement> id2 : id.entrySet()) {
                                if (key.equals(id2.getKey())) {
                                    prop2.setProperty(id2.getValue().getAsString(), value);
                                } else prop2.setProperty(key, value);
                            }
                        }



                            //Saves File
                            prop2.store(output, "");
                        } catch (IOException io) {
                            io.printStackTrace();
                        }


                    } catch (IOException e) {
                        throw Util.propagate(e);
                    }
                });
    }


        }

