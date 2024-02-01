package com.agentdid127.resourcepack.library;

public class RPPluginVersionSetter {

  public static void setVersion(RPPlugin plugin, String from, String to) {
    plugin.from = from;
    plugin.to = to;
  }

  public static void setPackConverter(RPPlugin plugin, PackConverter packConverter) {
    plugin.packConverter = packConverter;
  }

}
