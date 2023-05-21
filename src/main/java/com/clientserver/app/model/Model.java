package com.clientserver.app.model;

import com.clientserver.app.graphobj.GraphCircle;
import com.clientserver.app.graphobj.GraphImage;
import com.clientserver.app.graphobj.GraphObject;
import com.clientserver.app.graphobj.GraphStar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Model {
    private final Gson gson;
    private List<GraphObject> graphObjects = new ArrayList<>();

    public Model() {
        GsonBuilder gb = new GsonBuilder();
        gb.registerTypeAdapter(graphObjects.getClass(), new CustomSerializer());
        gb.registerTypeAdapter(graphObjects.getClass(), new CustomDeserializer());
        gson = gb.create();
    }

    public List<GraphObject> fromJsonList(String json) {
        List<GraphObject> objects = gson.fromJson(json, graphObjects.getClass());
        try {
            for (GraphObject object : objects) {
                object.init();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return objects;
    }

    public String toJsonList(List<GraphObject> graphObjectList) {
        return gson.toJson(graphObjectList);
    }

    public List<GraphObject> getGraphObjects() {
        return graphObjects;
    }

    public void addGrObject(GraphObject go) {
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

        String json = gson.toJson(graphObjects);
        System.out.println(json);
        osw.write(json);
        osw.flush();
        osw.close();
    }
}

