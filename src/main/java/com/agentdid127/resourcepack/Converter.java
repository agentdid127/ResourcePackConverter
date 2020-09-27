package com.agentdid127.resourcepack;

import com.agentdid127.resourcepack.pack.Pack;

import java.io.IOException;

public abstract class Converter {

    protected PackConverter packConverter;

    public Converter(PackConverter packConverter) {
        this.packConverter = packConverter;
    }

    public abstract void convert(Pack pack) throws IOException;

}