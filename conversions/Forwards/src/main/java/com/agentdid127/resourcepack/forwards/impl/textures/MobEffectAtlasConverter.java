package com.agentdid127.resourcepack.forwards.impl.textures;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import com.agentdid127.resourcepack.forwards.impl.textures.slicing.Slice;
import com.agentdid127.resourcepack.forwards.impl.textures.slicing.Slicer;
import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.JsonUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class MobEffectAtlasConverter extends Converter {
	private int from;

	public MobEffectAtlasConverter(PackConverter packConverter, int from) {
		super(packConverter);
		this.from = from;
	}

	/**
	 * Slices the mob effect images from inventory.png for 1.14+
	 * 
	 * @param pack
	 * @throws IOException
	 */
	@Override
	public void convert(Pack pack) throws IOException {
		Path texturesPath = pack.getWorkingPath().resolve("assets/minecraft/textures".replace("/", File.separator));
		if (!texturesPath.toFile().exists())
			return;
		Path containerPath = texturesPath.resolve("gui/container".replace("/", File.separator));
		if (!containerPath.toFile().exists())
			return;
		Path inventoryPath = containerPath.resolve("inventory.png");
		if (!inventoryPath.toFile().exists())
			return;
		Gson gson = packConverter.getGson();
		JsonObject effectJson = JsonUtil.readJsonResource(gson, "/forwards/mob_effect.json", JsonObject.class);
		Slice slice = Slice.parse(effectJson);
		Slicer.runSlicer(gson, slice, texturesPath, SlicerConverter.PredicateRunnable.class, from, false);
	}
}