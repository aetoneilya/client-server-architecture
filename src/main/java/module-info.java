module com.clientserverarchitecture.clientserver {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens com.clientserverarchitecture.clientserver to javafx.fxml;
    exports com.clientserverarchitecture.clientserver;
}