package com.agentdid127.resourcepack.library.utilities;

import com.agentdid127.resourcepack.library.PackConverter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

public class JsonUtil {
	public static JsonArray add(JsonArray lhs, JsonArray rhs) {
		return add(lhs, rhs, (byte) 1);
	}

	public static JsonArray add(JsonArray lhs, JsonArray rhs, byte sign) {
		JsonArray newArray = new JsonArray();
		for (int i = 0; i < 3; i++)
			newArray.add(add(lhs, rhs, i, sign));
		return newArray;
	}

	public static float add(JsonArray lhs, JsonArray rhs, int i, byte sign) {
		return lhs.get(i).getAsFloat() + sign * rhs.get(i).getAsFloat();
	}

	public static float multiply(JsonArray lhs, JsonArray rhs, int i) {
		return lhs.get(i).getAsFloat() * rhs.get(i).getAsFloat();
	}

	public static float divide(JsonArray lhs, JsonArray rhs, int i) {
		return lhs.get(i).getAsFloat() / rhs.get(i).getAsFloat();
	}

	public static JsonArray subtract(JsonArray lhs, JsonArray rhs) {
		return add(lhs, rhs, (byte) -1);
	}

	public static JsonArray multiply(JsonArray lhs, JsonArray rhs) {
		JsonArray newArray = new JsonArray();
		for (int i = 0; i < 3; i++)
			newArray.add(multiply(lhs, rhs, i));
		return newArray;
	}

	public static JsonArray divide(JsonArray lhs, JsonArray rhs) {
		JsonArray newArray = new JsonArray();
		for (int i = 0; i < 3; i++)
			newArray.add(divide(lhs, rhs, i));
		return newArray;
	}

	public static JsonArray asArray(Gson gson, String raw) {
		return gson.fromJson(raw, JsonArray.class);
	}

	public static void writeJson(Gson gson, Path out, JsonElement json) throws IOException {
		Files.write(out, Collections.singleton(gson.toJson(json)), StandardCharsets.UTF_8);
	}

	public static boolean isJson(Gson gson, String Json) {
		try {
			gson.fromJson(Json, Object.class);
			return true;
		} catch (com.google.gson.JsonSyntaxException ex) {
			ex.printStackTrace();
			return false;
		}
	}

	public static <T> T readJson(Gson gson, Path path, Class<T> clazz) throws IOException {
		String json = Util.readFromFile(path);
		if (!isJson(gson, json))
			return null;
		JsonReader reader = new JsonReader(new StringReader(json));
		reader.setLenient(true);
		return gson.fromJson(reader, clazz);
	}

	public static JsonObject readJson(Gson gson, Path path) throws IOException {
		return readJson(gson, path, JsonObject.class);
	}

	public static <T> T readJsonResource(Gson gson, String path, Class<T> clazz) {
		try (InputStream stream = PackConverter.class.getResourceAsStream(path)) {
			if (stream == null)
				return null;
			try (InputStreamReader streamReader = new InputStreamReader(stream)) {
				return gson.fromJson(streamReader, clazz);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static JsonObject readJsonResource(Gson gson, String path) {
		return readJsonResource(gson, path, JsonObject.class);
	}
}
