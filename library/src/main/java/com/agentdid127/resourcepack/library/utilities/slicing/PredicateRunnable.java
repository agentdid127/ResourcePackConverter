package com.agentdid127.resourcepack.library.utilities.slicing;

import com.google.gson.JsonObject;

public interface PredicateRunnable {
    boolean run(int from, JsonObject predicate);
}
