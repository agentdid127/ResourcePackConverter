package com.agentdid127.resourcepack.library.utilities.slicing;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public interface PredicateRunnable {
    boolean run(Gson gson, int from, JsonObject predicate);
}
