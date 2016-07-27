package io.coerce.commons.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonUtil {
    private static Gson gsonInstance;

    public static Gson getGsonInstance() {
        if(gsonInstance == null) {
            gsonInstance = new GsonBuilder().setExclusionStrategies(new AnnotationExclusionStrategy()).create();
        }

        return gsonInstance;
    }
}
