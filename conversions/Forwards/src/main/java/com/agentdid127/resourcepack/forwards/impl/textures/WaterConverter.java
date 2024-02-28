package com.agentdid127.resourcepack.forwards.impl.textures;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.ImageConverter;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class WaterConverter extends Converter {

  private Path blocks;
  public WaterConverter(PackConverter packConverter) {
    super(packConverter);
  }

  @Override
  public void convert(Pack pack) throws IOException {
    blocks = pack.getWorkingPath().resolve("assets" + File.separator + "minecraft" + File.separator + "textures" + File.separator + "blocks");

    colorize(32, 1024, blocks.resolve("water_flow.png"));
    colorize(32,1024, blocks.resolve("water_still.png"));
    colorize(32, 32, blocks.resolve("water_overlay.png"));

  }

  private void colorize(int w, int h, Path path) throws IOException {
    if (!path.toFile().exists()) return;

    ImageConverter imageConverter = new ImageConverter(w, h, path);
    if (!imageConverter.fileIsPowerOfTwo()) return;

    imageConverter.newImage(w, h);
    imageConverter.grayscale();
    imageConverter.store();
  }
}
