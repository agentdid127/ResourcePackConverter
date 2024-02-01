package com.agentdid127.resourcepack.library;

public class RPPluginVersionSetter {

  public static void setData(RPPlugin plugin, String from, String to, PackConverter pc) {
    plugin.setFrom(from);
    plugin.setTo(to);
    plugin.setPackConverter(pc);
  }


}
