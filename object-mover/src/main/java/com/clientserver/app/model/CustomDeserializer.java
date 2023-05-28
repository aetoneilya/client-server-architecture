package com.clientserver.app.model;

import com.clientserver.app.graphobj.GraphObject;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class CustomDeserializer implements JsonDeserializer<ArrayList<GraphObject>> {
    public ArrayList<GraphObject> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        ArrayList<GraphObject> list = new ArrayList<>();
        JsonArray ja = json.getAsJsonArray();

        for (JsonElement je : ja) {
            String className = je.getAsJsonObject().get("className").getAsString();

            try {
                list.add(context.deserialize(je.getAsJsonObject().get("class"), Class.forName(className)));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        return list;
    }
}
