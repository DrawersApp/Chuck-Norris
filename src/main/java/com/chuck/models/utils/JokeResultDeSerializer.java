package com.chuck.models.utils;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by nishant.pathak on 15/05/16.
 */

public class JokeResultDeSerializer<T> implements JsonDeserializer<List<T>> {
    @Override
    public List<T> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        JsonElement results = json.getAsJsonObject().get("value")
                .getAsJsonArray();

        return new Gson().fromJson(results, typeOfT);
    }
}
