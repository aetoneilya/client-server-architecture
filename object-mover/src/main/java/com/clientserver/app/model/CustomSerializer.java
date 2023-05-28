package com.clientserver.app.model;

import com.clientserver.app.graphobj.GraphObject;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class CustomSerializer implements JsonSerializer<ArrayList<GraphObject>> {
    @Override
    public JsonElement serialize(ArrayList<GraphObject> src, Type typeOfSrc, JsonSerializationContext context) {
        if (src == null) return null;
        else {
            JsonArray ja = new JsonArray();

            for (GraphObject go : src) {
                JsonObject jo = new JsonObject();
                jo.addProperty("className", go.getClass().getName());
                jo.add("class", context.serialize(go, go.getClass()));
                ja.add(context.serialize(jo));
            }
            return ja;
        }
    }
}
