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

    opens com.clientserver.app to javafx.fxml;
    opens com.clientserver.app.graphobj;
    exports com.clientserver.app;
}