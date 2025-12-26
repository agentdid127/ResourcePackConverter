package com.agentdid127.resourcepack.forwards.impl.v1_21_11.textures;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.FileUtil;
import com.agentdid127.resourcepack.library.utilities.JsonUtil;
import com.agentdid127.resourcepack.library.utilities.Util;
import com.agentdid127.resourcepack.library.utilities.slicing.Slice;
import com.agentdid127.resourcepack.library.utilities.slicing.Slicer;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class CelestialSlicerUpdater1_21_11 extends Converter {
	public CelestialSlicerUpdater1_21_11(PackConverter packConverter) {
		super(packConverter);
	}

	@Override
	public boolean shouldConvert(Gson gson, int from, int to) {
		return from <= Util.getVersionProtocol(gson, "1.21.10") && to >= Util.getVersionProtocol(gson, "1.21.11");
	}

	@Override
	public void convert(Pack pack) throws IOException {
		final Path environmentPath = pack.getWorkingPath().resolve("assets/minecraft/textures/environment".replace("/", File.separator));
		if (!environmentPath.toFile().exists()) return;

		final Path celestialFolder = environmentPath.resolve("celestial");
		FileUtil.moveIfExists(environmentPath.resolve("sun.png"), celestialFolder.resolve("sun.png"));
		FileUtil.moveIfExists(environmentPath.resolve("sun.png.mcmeta"), celestialFolder.resolve("sun.png.mcmeta"));
		FileUtil.moveIfExists(environmentPath.resolve("end_flash.png"), celestialFolder.resolve("end_flash.png"));
		FileUtil.moveIfExists(environmentPath.resolve("end_flash.png.mcmeta"), celestialFolder.resolve("end_flash.png.mcmeta"));

		// Slice Moon Phases
		final Path moonPhases = environmentPath.resolve("moon_phases.png");
		if (!moonPhases.toFile().exists()) return;

		// TODO: Metadata
		final Gson gson = this.packConverter.getGson();
		final JsonObject moonPhasesJson = JsonUtil.readJsonResource(gson, "/forwards/moon_phases.json", JsonObject.class);
		if (moonPhasesJson != null) {
			Slicer.run(gson, Slice.parse(moonPhasesJson), environmentPath, (from, predicate) -> true, -1, false);
		}
	}
}
