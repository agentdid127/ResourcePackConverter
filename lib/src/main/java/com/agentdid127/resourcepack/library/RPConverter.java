package com.agentdid127.resourcepack.library;

import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.converter.Converter;
import java.io.IOException;

public abstract class RPConverter extends Converter {
    
    protected PackConverter packConverter;
    protected Pack pack;

    private boolean unstable;
    
    public RPConverter(PackConverter packConverter, String name, int priority) {
	super(name, priority);
        this.packConverter = packConverter;
	      this.pack = null;
        this.unstable = false;
    }

  public RPConverter(PackConverter packConverter, String name, int priority, boolean unstable) {
    super(name, priority);
    this.packConverter = packConverter;
    this.pack = null;
    this.unstable = unstable;
  }

    public void convert(Pack pack) throws IOException {
	    this.pack = pack;
	    convert();
    }

    public boolean isUnstable() {
      return unstable;
    }
}
