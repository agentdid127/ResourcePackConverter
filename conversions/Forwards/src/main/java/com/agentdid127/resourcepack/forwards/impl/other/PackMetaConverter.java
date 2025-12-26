package com.agentdid127.resourcepack.forwards.impl.other;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.JsonUtil;
import com.agentdid127.resourcepack.library.utilities.Util;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.nio.file.Path;

// Reference: https://minecraft.wiki/w/Pack_format
public class PackMetaConverter extends Converter {
	private final int from;
	private final int to;

	public PackMetaConverter(PackConverter packConverter, int from, int to) {
		super(packConverter);
		this.from = from;
		this.to = to;
	}

	@Override
	public boolean shouldConvert(Gson gson, int from, int to) {
		return true;
	}

	/**
	 * Converts MCMeta to newer version
	 *
	 * @param pack The input pack
	 * @throws IOException The IO Exception
	 */
	@Override
	public void convert(Pack pack) throws IOException {
		final Path file = pack.getWorkingPath().resolve("pack.mcmeta");
		if (!file.toFile().exists()) {
			return;
		}

		final Gson gson = packConverter.getGson();

		double packVersion = 4;
		JsonObject versionObj = Util.getVersionObjectByProtocol(packConverter.getGson(), to);
		if (versionObj != null) {
			packVersion = versionObj.get("pack_format").getAsDouble();
		}

		JsonObject json = JsonUtil.readJson(packConverter.getGson(), file);
		if (json == null) {
			json = new JsonObject();
		}

		{
			JsonObject meta = json.getAsJsonObject("meta");
			if (meta == null) {
				meta = new JsonObject();
			}

			meta.addProperty("game_version", Util.getVersionFromProtocol(packConverter.getGson(), to));
			json.add("meta", meta);
		}

		{
			JsonObject packObject = json.getAsJsonObject("pack");
			if (packObject == null) {
				packObject = new JsonObject();
			}

			final boolean isGT1_21_10 = to >= Util.getVersionProtocol(gson, "1.21.10");

			packObject.addProperty("pack_format", (int) packVersion);
			if (from <= Util.getVersionProtocol(gson, "1.20.1")
					&& to >= Util.getVersionProtocol(gson, "1.20.2")) {
				final JsonObject fromVersion = Util.getVersionObjectByProtocol(gson, from);
				final JsonObject toVersion = Util.getVersionObjectByProtocol(gson, to);

				final JsonObject supportedFormats = new JsonObject();
				if (isGT1_21_10) { // TODO: Simplify
					supportedFormats.addProperty("min_inclusive", fromVersion != null ? fromVersion.get("pack_format").getAsDouble() : packVersion);
					supportedFormats.addProperty("max_inclusive", toVersion != null ? toVersion.get("pack_format").getAsDouble() : packVersion);
				} else {
					supportedFormats.addProperty("min_inclusive", fromVersion != null ? fromVersion.get("pack_format").getAsInt() : (int) packVersion);
					supportedFormats.addProperty("max_inclusive", toVersion != null ? toVersion.get("pack_format").getAsInt() : (int) packVersion);
				}

				packObject.add("supported_formats", supportedFormats);
			}

			if (from <= Util.getVersionProtocol(gson, "1.21.8") && isGT1_21_10) {
				packObject.addProperty("pack_format", packVersion);
				if (packObject.has("supported_formats")) {
					JsonObject supportedFormats = packObject.remove("supported_formats").getAsJsonObject();
					packObject.addProperty("min_format", Math.max(supportedFormats.get("min_inclusive").getAsDouble(), 65.0));
					packObject.addProperty("max_format", supportedFormats.get("max_inclusive").getAsDouble());
				}

				// TODO
				// For overlay entries:
				//   - The formats field has been removed
				//     - If your pack includes any overlay range that includes a pack version with the previous format (data pack < 82, resource pack < 65), it is still required for all overlay definitions
				//     - Otherwise, it is not allowed and must be removed
				//   - Added required fields min_format and max_format with the same formats as the fields above with the same name for the pack section
			}

			json.add("pack", packObject);
		}

		JsonUtil.writeJson(gson, file, json);
	}
}
