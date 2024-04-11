package com.agentdid127.resourcepack.backwards.impl;

import com.agentdid127.resourcepack.library.Converter;
import com.agentdid127.resourcepack.library.PackConverter;
import com.agentdid127.resourcepack.library.pack.Pack;
import com.agentdid127.resourcepack.library.utilities.JsonUtil;
import com.agentdid127.resourcepack.library.utilities.Logger;
import com.agentdid127.resourcepack.library.utilities.Util;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ModelConverter extends Converter {
	private int version;
	private int from;
	protected String light = "none";

	protected Path models;

	public ModelConverter(PackConverter packConverter, String lightIn, int fromIn, int versionIn) {
		super(packConverter);
		light = lightIn;
		from = fromIn;
		version = versionIn;
	}

	/**
	 * Runs findfiles with the directory Models
	 * 
	 * @param pack
	 * @throws IOException
	 */
	@Override
	public void convert(Pack pack) throws IOException {
		models = pack.getWorkingPath()
				.resolve("assets" + File.separator + "minecraft" + File.separator + "models");
		if (!models.toFile().exists())
			return;
		findFiles(models);
	}

	/**
	 * Recursively finds files with Path path and runs remapModelJson
	 * 
	 * @param path
	 * @throws IOException
	 */
	protected void findFiles(Path path) throws IOException {
		File directory = path.toFile();
		for (File file : directory.listFiles()) {
			if (file.isDirectory())
				continue;
			remapModelJson(file.toPath());
			findFiles(file.toPath());
		}
	}

	/**
	 * Updates model Json to newer versions
	 * 
	 * @param path
	 * @throws IOException
	 */
	protected void remapModelJson(Path path) throws IOException {
		if (!path.toFile().exists())
			return;
		Files.list(path)
				.filter(path1 -> path1.toString().endsWith(".json"))
				.forEach(model -> {
					try {
						JsonObject jsonObject;
						if (Util.readJson(packConverter.getGson(), model) != null
								&& Util.readJson(packConverter.getGson(), model).isJsonObject())
							jsonObject = Util.readJson(packConverter.getGson(), model);
						else {
							if (PackConverter.DEBUG) {
								Logger.log("Could not convert model: " + model.getFileName());
								if (Util.readJson(packConverter.getGson(), model) == null)
									Logger.log("Check for Syntax Errors in file.");
								else
									Logger.log("File is not JSON Object.");
							}
							return;
						}

						// Parent Stuff
						if (jsonObject.has("parent")) {
							// Change parent to lowercase
							for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
								if (entry.getKey().equals("parent")) {
									String parent = entry.getValue().getAsString().toLowerCase();
									jsonObject.addProperty(entry.getKey(), getParent(parent));
								}
							}
							if (from >= Util.getVersionProtocol(packConverter.getGson(), "1.9")
									&& version < Util.getVersionProtocol(packConverter.getGson(), "1.9")) {

								jsonObject = mergeParent(jsonObject, jsonObject.get("parent").getAsString());
							}

						}

						// GUI light system for 1.15.2
						if (version < Util.getVersionProtocol(packConverter.getGson(), "1.15.2")) {
							jsonObject.remove("gui_light");
						}
						// minify the json so we can replace spaces in paths easily
						// TODO Improvement: handle this in a cleaner way?

						// handle the remapping of textures, for models that use default texture names
						if (jsonObject.has("textures") && jsonObject.get("textures").isJsonObject()) {
							NameConverter nameConverter = packConverter.getConverter(NameConverter.class);
							JsonObject initialTextureObject = jsonObject.getAsJsonObject("textures");
							JsonObject textureObject = initialTextureObject.deepCopy();
							for (Map.Entry<String, JsonElement> entry : initialTextureObject.entrySet()) {
								String value = entry.getValue().getAsString();
								textureObject.remove(entry.getKey());

								if (version < Util.getVersionProtocol(packConverter.getGson(), "1.19.3")
										&& from >= Util.getVersionProtocol(packConverter.getGson(), "1.19.3")) {
									value = value.replaceAll("minecraft:", "");
								}
								// 1.19 Mappings
								if (version < Util.getVersionProtocol(packConverter.getGson(), "1.19"))
									if (value.startsWith("block/"))
										value = "block/" + nameConverter.getBlockMapping19()
												.remap(value.substring("block/".length())).toLowerCase()
												.replaceAll("[()]", "");
								value = value.toLowerCase().replaceAll("[()]", "");

								// 1.17 Mappings
								if (version < Util.getVersionProtocol(packConverter.getGson(), "1.17")) {
									if (value.startsWith("block/"))
										value = "block/" + nameConverter.getBlockMapping17()
												.remap(value.substring("block/".length())).toLowerCase()
												.replaceAll("[()]", "");
									else if (value.startsWith("item/"))
										value = "item/" + nameConverter.getItemMapping17()
												.remap(value.substring("item/".length())).toLowerCase()
												.replaceAll("[()]", "");
									value = value.toLowerCase().replaceAll("[()]", "");
								}

								// 1.14 Mappings
								if (version < Util.getVersionProtocol(packConverter.getGson(), "1.14"))
									if (value.startsWith("block/"))
										value = "block/"
												+ nameConverter.getNewBlockMapping()
														.remap(value.substring("block/".length()));

								// Dyes
								if (value.startsWith("item/") && value.contains("dye"))
									if (version < Util.getVersionProtocol(packConverter.getGson(), "1.14"))
										value = "item/"
												+ nameConverter.getNewItemMapping()
														.remap(value.substring("item/".length()));

								// 1.13 Mappings
								if (version < Util.getVersionProtocol(packConverter.getGson(), "1.13")) {
									if (value.startsWith("block/")) {
										List<String> same = Arrays.asList("snow");
										String remap = nameConverter.getBlockMapping()
												.remap(value.substring("block/".length())).toLowerCase()
												.replaceAll("[()]", "");
										for (String s : same) {
											if (s.equals(value.substring("block/".length()))) {
												remap = s;
											}
										}
										value = "blocks/" + remap;
									} else if (value.startsWith("item/"))
										value = "items/" + nameConverter.getItemMapping()
												.remap(value.substring("item/".length())).toLowerCase()
												.replaceAll("[()]", "");
									else
										value = value.toLowerCase().replaceAll("[()]", "");
								}

								if (!textureObject.has(entry.getKey()))
									textureObject.addProperty(entry.getKey(), value);
							}

							jsonObject.remove("textures");
							jsonObject.add("textures", textureObject);
						}

						// fix display settings for packs for 1.8
						if (jsonObject.has("display") && from > Util.getVersionProtocol(packConverter.getGson(), "1.8")
								&& version == Util.getVersionProtocol(packConverter.getGson(), "1.8")) {
							JsonElement display = jsonObject.remove("display");

							if (display.isJsonObject()) {
								jsonObject.add("display",
										updateDisplay(packConverter.getGson(), display.getAsJsonObject()));
							}
						}

						if (jsonObject.has("overrides")) {
							for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
								if (entry.getKey().equals("overrides")) {
									JsonArray overrides = jsonObject.get("overrides").getAsJsonArray();
									JsonArray overrides2 = new JsonArray();
									for (int i = 0; i < overrides.size(); i++) {
										JsonObject object = overrides.get(i).getAsJsonObject();
										for (Map.Entry<String, JsonElement> json : object.entrySet()) {
											if (json.getKey().equals("model"))
												object.addProperty(json.getKey(),
														json.getValue().getAsString().replaceAll("[()]", ""));
											else
												object.add(json.getKey(), json.getValue());
										}
										overrides2.add(object);
									}
									jsonObject.add(entry.getKey(), overrides2);
								}
							}
						}

						if (!Util.readJson(packConverter.getGson(), model).equals(jsonObject)) {
							if (PackConverter.DEBUG)
								Logger.log("Updating Model: " + model.getFileName());
							JsonUtil.writeJson(packConverter.getGson(), model, jsonObject);
						}
					} catch (IOException e) {
						throw Util.propagate(e);
					}
				});
	}

	private String getParent(String parent) {
		parent = parent.replace(" ", "_");

		// Get block/item parents renamed
		if (from >= Util.getVersionProtocol(packConverter.getGson(), "1.19.3")
				&& version < Util.getVersionProtocol(packConverter.getGson(), "1.19.3"))
			parent = parent.replaceAll("minecraft:", "");

		if (from >= Util.getVersionProtocol(packConverter.getGson(), "1.19")
				&& version < Util.getVersionProtocol(packConverter.getGson(), "1.19"))
			if (parent.startsWith("block/"))
				parent = setParent("block/", "/backwards/blocks.json", parent, "1_19");

		if (from >= Util.getVersionProtocol(packConverter.getGson(), "1.17")
				&& version < Util.getVersionProtocol(packConverter.getGson(), "1.17")) {
			if (parent.startsWith("block/"))
				parent = setParent("block/", "/backwards/blocks.json", parent, "1_17");
			if (parent.startsWith("item/"))
				parent = setParent("item/", "/backwards/items.json", parent, "1_17");
		}

		if (from >= Util.getVersionProtocol(packConverter.getGson(), "1.14")
				&& version < Util.getVersionProtocol(packConverter.getGson(), "1.14")) {
			if (parent.startsWith("block/"))
				parent = setParent("block/", "/backwards/blocks.json", parent, "1_14");
			if (parent.startsWith("item/"))
				parent = setParent("item/", "/backwards/items.json", parent, "1_14");
		}

		if (from >= Util.getVersionProtocol(packConverter.getGson(), "1.13")
				&& version < Util.getVersionProtocol(packConverter.getGson(), "1.13")) {

			if (parent.startsWith("block/"))
				parent = setParent("block/", "/backwards/blocks.json", parent, "1_13");
			if (parent.startsWith("item/"))
				parent = setParent("item/", "/backwards/items.json", parent, "1_13");

		}
		return parent;
	}

	protected JsonObject mergeParent(JsonObject current, String parent) {
		JsonObject jsonObject = current.deepCopy();

		if (parent == null || !models.resolve(parent + ".json").toFile().exists()) {
			return jsonObject;
		}
		if (!current.has("parent")) {
			return jsonObject;
		} else {
			Path parentPath = models.resolve(parent + ".json");

			try {

				JsonObject parentObj = Util.readJson(packConverter.getGson(), parentPath);
				if (parentObj != null) {
					jsonObject.remove("parent");
					if (parentObj.has("parent")) {
						String parentStr = parentObj.get("parent").getAsString();
						String parentVal = parentStr == null ? null : getParent(parentStr);
						jsonObject = mergeParent(parentObj, parentVal);
					}

					if (!jsonObject.has("elements") && current.has("elements")) {
						jsonObject.add("elements", current.get("elements"));
					}

					if (!jsonObject.has("elements") && parentObj.has("elements")) {
						jsonObject.add("elements", parentObj.get("elements"));
					}

					if (!jsonObject.has("display") && parentObj.has("display")) {
						jsonObject.add("display", parentObj.get("display"));
					}

					JsonObject textures = new JsonObject();
					if (jsonObject.has("textures")) {
						textures = jsonObject.remove("textures").getAsJsonObject();
					}

					if (current.has("textures")) {
						for (String s : current.get("textures").getAsJsonObject().keySet()) {
							if (!textures.has(s)) {
								textures.add(s, current.get("textures").getAsJsonObject().get(s));
							}
						}
					}

					if (parentObj.has("textures")) {
						JsonObject parentTextures = parentObj.get("textures").getAsJsonObject();

						for (String s : parentTextures.keySet()) {
							if (!textures.has(s)) {
								textures.add(s, parentTextures.get(s));

							}
						}
					}

					for (int i = 0; i < 5; i++) {
						JsonObject textures2 = new JsonObject();
						for (String s : textures.keySet()) {
							if (textures.get(s).getAsString().startsWith("#") &&
									textures.has(textures.get(s).getAsString().substring(1))) {
								textures2.add(s,
										textures.get(textures.get(s).getAsString().substring(1)));
							} else {
								textures2.add(s, textures.get(s));
							}
						}
						textures = textures2;
					}

					if (textures.keySet().size() > 0) {
						jsonObject.add("textures", textures);
					}
				}
			} catch (IOException e) {

			}

		}
		return jsonObject;

	}

	/**
	 * Gets parent object and sets a new one
	 * 
	 * @param prefix prefix of file path
	 * @param path   File path of json control
	 * @param parent Parent String
	 * @return New string with changed parent.
	 */
	protected String setParent(String prefix, String path, String parent, String item) {
		String parent2 = parent.replace(prefix, "");
		JsonObject file = Util.readJsonResource(packConverter.getGson(), path).getAsJsonObject(item);
		if (file == null) {
			Logger.log("Prefix Failed on: " + parent);
			return "";
		}
		return file.has(parent2) ? prefix + file.get(parent2).getAsString() : parent;
	}

	protected static JsonObject updateDisplay(Gson gson, JsonObject display) {
		JsonObject defaults = Util.readJsonResource(gson, "/backwards/display.json");
		if (display == null) {
			return defaults.deepCopy();
		}

		// First Person
		boolean found = false;
		JsonObject firstPerson = defaults.get("firstperson").getAsJsonObject().deepCopy();
		if (display.has("firstperson_righthand")) {
			firstPerson = updateDisplayFirstPerson(gson, display.remove("firstperson_righthand").getAsJsonObject());
			found = true;
		}
		if (display.has("firstperson_lefthand")) {
			JsonObject firstPersonLeft = display.remove("firstperson_lefthand").getAsJsonObject();
			if (!found) {
				firstPerson = updateDisplayFirstPerson(gson, getLeftHand(gson, firstPersonLeft));
			}
		}
		display.remove("firstperson");
		display.add("firstperson", firstPerson);

		// Third Person
		found = false;
		JsonObject thirdPerson = defaults.get("thirdperson").getAsJsonObject().deepCopy();
		if (display.has("thirdperson_righthand")) {
			thirdPerson = updateDisplayThirdPerson(gson, display.remove("thirdperson_righthand").getAsJsonObject());
			found = true;
		}
		if (display.has("thirdperson_lefthand")) {
			JsonObject thirdPersonLeft = display.remove("thirdperson_lefthand").getAsJsonObject();
			if (!found) {
				thirdPerson = updateDisplayThirdPerson(gson, getLeftHand(gson, thirdPersonLeft));
			}
		}
		display.remove("thirdperson");
		display.add("thirdperson", thirdPerson);

		if (display.has("ground")) {
			display.remove("ground");
		}

		if (display.has("head")) {
			display.remove("head");
		}

		return display;
	}

	private static JsonObject getLeftHand(Gson gson, JsonObject old) {
		JsonObject newObject = old.deepCopy();
		if (old.has("rotation")) {
			JsonArray oldRotation = newObject.remove("rotation").getAsJsonArray();
			JsonArray rotation = new JsonArray();
			rotation.add(oldRotation.get(0).getAsNumber());
			rotation.add(0 - oldRotation.get(1).getAsDouble());
			rotation.add(0 - oldRotation.get(2).getAsDouble());
			newObject.add("rotation",
					rotation);
		}

		return newObject;
	}

	private static JsonObject updateDisplayFirstPerson(Gson gson, JsonObject old) {
		JsonObject newObject = old.deepCopy();
		if (old.has("rotation")) {
			JsonArray rotation = newObject.remove("rotation").getAsJsonArray();
			newObject.add("rotation",
					JsonUtil.subtract(
							rotation,
							JsonUtil.asArray(gson, "[0, 45, 0]")));
		}

		if (old.has("translation")) {
			JsonArray translation = newObject.remove("translation").getAsJsonArray();
			newObject.add("translation",
					JsonUtil.add(
							JsonUtil.divide(
									JsonUtil.subtract(
											translation,
											JsonUtil.asArray(gson, "[1.13, 3.2, 1.13]")),
									JsonUtil.asArray(gson, "[0.4, 0.4, 0.4]")),
							JsonUtil.asArray(gson, "[0, 4, 2]")));

		}

		if (old.has("scale")) {
			JsonArray scale = newObject.remove("scale").getAsJsonArray();
			newObject.add("scale",
					JsonUtil.divide(
							scale,
							JsonUtil.asArray(gson, "[0.4, 0.4, 0.4]")));
		}

		return newObject;
	}

	private static JsonObject updateDisplayThirdPerson(Gson gson, JsonObject old) {
		JsonObject newObject = old.deepCopy();
		if (old.has("rotation")) {
			JsonArray rotation = newObject.remove("rotation").getAsJsonArray();
			newObject.add("rotation",
					JsonUtil.divide(
							JsonUtil.subtract(
									rotation,
									JsonUtil.asArray(gson, "[0, 0, 20]")),
							JsonUtil.asArray(gson, "[1, -1, -1]")));
		}

		if (old.has("translation")) {
			JsonArray translation = newObject.remove("translation").getAsJsonArray();
			newObject.add("translation",
					JsonUtil.divide(
							JsonUtil.subtract(
									translation,
									JsonUtil.asArray(gson, "[0, 2.75, -3]")),
							JsonUtil.asArray(gson, "[1, 1, -1]")));
		}

		// For keeping order
		if (old.has("scale")) {
			JsonArray scale = newObject.remove("scale").getAsJsonArray();
			newObject.add("scale", scale);
		}

		return newObject;
	}
}