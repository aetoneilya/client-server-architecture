package com.clientserver.app;

import com.clientserver.app.graphobj.GraphCircle;
import com.clientserver.app.graphobj.GraphImage;
import com.clientserver.app.graphobj.GraphObject;
import com.clientserver.app.graphobj.GraphStar;
import com.google.gson.*;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Model {
    private List<GraphObject> graphObjects = new ArrayList<>();

    public List<GraphObject> getGraphObjects() {
        return graphObjects;
    }

    void addGrObject(GraphObject go) {
        graphObjects.add(go);
    }

    public void save(String path) throws IOException {
        OutputStream os = new FileOutputStream(path);
        String ext = FilenameUtils.getExtension(path);

        switch (ext) {
            case "txt" -> saveTxt(os);
            case "bin" -> saveBin(os);
            case "xml" -> saveXml(os);
            case "json" -> saveJson(os);
        }

        os.close();
    }

    public void load(String path) throws Exception {
        String ext = FilenameUtils.getExtension(path);
        InputStream is = new FileInputStream(path);
//        ObjectInputStream ois = new ObjectInputStream(fis);

        graphObjects.clear();
        switch (ext) {
            case "txt" -> loadTxt(is);
            case "bin" -> loadBin(is);
            case "xml" -> loadXml(is);
            case "json" -> loadJson(is);
        }

        is.close();

        for (GraphObject go : graphObjects)
            go.init();
    }

    private void loadXml(InputStream is) throws IOException {
        XStream xstream = new XStream();
        String xmlData = IOUtils.toString(is);

        xstream.allowTypesByWildcard(new String[]{"com.clientserver.app.**"});
        graphObjects = (List<GraphObject>) xstream.fromXML(xmlData);
    }

    private void loadJson(InputStream is) throws IOException {
        String json = IOUtils.toString(is);
        GsonBuilder gb = new GsonBuilder();

        gb.registerTypeAdapter(graphObjects.getClass(), new CustomDeserializer());
        Gson gson = gb.create();

        graphObjects = gson.fromJson(json, graphObjects.getClass());
    }

    private void loadBin(InputStream is) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(is);
        graphObjects = (List<GraphObject>) ois.readObject();
        ois.close();
    }

    private void loadTxt(InputStream is) throws Exception {
        Scanner scanner = new Scanner(is);

        int count = scanner.nextInt();

        for (int i = 0; i < count; i++) {
            scanner.nextLine();
            String className = scanner.nextLine();
            GraphObject go = null;
            switch (className) {
                case "GraphCircle" ->
                        go = new GraphCircle(scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble());
                case "GraphImage" ->
                        go = new GraphImage(scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble(), scanner.next());
                case "GraphStar" ->
                        go = new GraphStar(scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble(), scanner.nextInt());
            }
            graphObjects.add(go);
        }
    }

    private void saveTxt(OutputStream os) throws IOException {
        OutputStreamWriter osw = new OutputStreamWriter(os);
        osw.write(graphObjects.size() + "\n");
        for (GraphObject go : graphObjects)
            osw.write(go.toString());
        osw.flush();
        osw.close();
    }

    private void saveBin(OutputStream os) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(os);
        oos.writeObject(graphObjects);
        oos.flush();
        oos.close();
    }

    private void saveXml(OutputStream os) throws IOException {
        OutputStreamWriter osw = new OutputStreamWriter(os);
        XStream xstream = new XStream();
        String dataXml = xstream.toXML(graphObjects);
        osw.write(dataXml);
        osw.flush();
        osw.close();
    }

    private void saveJson(OutputStream os) throws IOException {
        OutputStreamWriter osw = new OutputStreamWriter(os);

        GsonBuilder gb = new GsonBuilder();

        gb.registerTypeAdapter(graphObjects.getClass(), new CustomSerializer());
        Gson gson = gb.create();

        String json = gson.toJson(graphObjects);
        System.out.println(json);
        osw.write(json);
        osw.flush();
        osw.close();
    }
}

class CustomSerializer implements JsonSerializer<ArrayList<GraphObject>> {
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

class CustomDeserializer implements JsonDeserializer<ArrayList<GraphObject>> {
    public ArrayList<GraphObject> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        ArrayList list = new ArrayList<GraphObject>();
        JsonArray ja = json.getAsJsonArray();

        for (JsonElement je : ja) {

            String className = je.getAsJsonObject().get("className").getAsString();
            Class c;
            try {
                c = Class.forName(className);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

            list.add(context.deserialize(je.getAsJsonObject().get("class"), c));
        }

        return list;
    }
}