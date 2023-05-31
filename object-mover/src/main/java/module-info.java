module com.clientserverarchitecture.clientserver {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires org.apache.commons.codec;
    requires org.apache.commons.io;
    requires com.google.gson;
    requires xstream;
    requires retrofit2;
    requires retrofit2.converter.scalars;

    opens com.clientserver.app to javafx.fxml;
    opens com.clientserver.app.graphobj;
    exports com.clientserver.app;
    exports com.clientserver.app.model;
    opens com.clientserver.app.model to javafx.fxml;

    exports com.clientserver.app.connection;
    exports com.clientserver.app.graphobj;
    exports com.clientserver.app.connection.tcp;
}