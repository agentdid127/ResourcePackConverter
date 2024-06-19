package com.agentdid127.resourcepack.forwards.impl.textures;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.ImageConverter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class WaterConverter extends Converter {
	public WaterConverter(PackConverter packConverter) {
		super(packConverter);
	}

	@Override
	public void convert(Pack pack) throws IOException {
		Path blockFolder = pack.getWorkingPath()
				.resolve("assets/minecraft/textures/block".replace("/", File.separator));
		if (!blockFolder.toFile().exists())
			return;
		grayscale(blockFolder.resolve("water_flow.png"), 16, 512);
		grayscale(blockFolder.resolve("water_still.png"), 16, 512);
		grayscale(blockFolder.resolve("water_overlay.png"), 16, 16);
	}

	private void grayscale(Path path, int w, int h) throws IOException {
		if (!path.toFile().exists())
			return;
		ImageConverter imageConverter = new ImageConverter(w, h, path);
		if (!imageConverter.fileIsPowerOfTwo())
			return;
		imageConverter.newImage(w, h);
		imageConverter.grayscale();
		imageConverter.store();
	}
}
