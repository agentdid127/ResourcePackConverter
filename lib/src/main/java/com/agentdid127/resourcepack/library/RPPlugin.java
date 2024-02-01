package com.agentdid127.resourcepack.library;

import com.agentdid127.converter.Plugin;
import com.google.gson.Gson;

public abstract class RPPlugin extends Plugin {

  String from;
  String to;
  Gson gson;

  PackConverter packConverter;

  public RPPlugin(String name) {
    super(name);
  }

  public String getFrom() {
    return from;
  }

  public String getTo() {
    return to;
  }

  public PackConverter getPackConverter() {
    return packConverter;
  }
}
