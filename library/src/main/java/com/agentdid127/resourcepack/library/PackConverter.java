package com.agentdid127.resourcepack.library;

import com.google.gson.Gson;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class PackConverter {
    protected final Map<Class<? extends Converter>, Converter> converters = new LinkedHashMap<>();
    protected Gson gson;

    /**
     * Registers Converter.
     * 
     * @param converter
     */
    public void registerConverter(Converter converter) {
        converters.put(converter.getClass(), converter);
    }

    /**
     * @param clazz
     * @param <T>
     * @return
     */
    public <T extends Converter> T getConverter(Class<T> clazz) {
        return (T) converters.get(clazz);
    }

    /**
     * Gson
     * 
     * @return Gson Object of PackConverter Class
     */
    public Gson getGson() {
        return gson;
    }
}
