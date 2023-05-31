package com.clientserver.app.graphobj;

public class GraphObjectFactory {
    public static GraphObject createGraphObject(GraphObjectsType type, double x, double y) throws Exception {
        GraphObject go = null;

        switch (type) {
            case CIRCLE -> go = new GraphCircle(x, y, 10);
            case IMAGE ->
                    go = new GraphImage(x, y, 150, 150, "C:\\code\\client-server-architecture\\object-mover\\src\\main\\resources\\com\\clientserver\\app\\pngegg.png");
            case STAR -> go = new GraphStar(x, y, 100, 6);
        }
        return go;
    }


}
