package com.agentdid127.resourcepack.library;

import com.agentdid127.resourcepack.library.pack.Pack;
import com.google.gson.Gson;

import java.io.IOException;

public abstract class Converter {
    protected PackConverter packConverter;

    public Converter(PackConverter packConverter) {
        this.packConverter = packConverter;
    }

    public abstract boolean shouldConvert(Gson gson, int from, int to);

    public abstract void convert(Pack pack) throws IOException;
}
