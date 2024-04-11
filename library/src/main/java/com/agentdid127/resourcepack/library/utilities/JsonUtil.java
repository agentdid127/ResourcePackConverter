package com.agentdid127.resourcepack.library.utilities;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

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
		Files.write(out, Collections.singleton(gson.toJson(json)), Charset.forName("UTF-8"));
	}
}
