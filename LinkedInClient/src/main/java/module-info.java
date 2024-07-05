module org.example.linkedinclient {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;
    requires com.fasterxml.jackson.databind;
    requires json;
    requires org.eclipse.jetty.util;
    requires de.jensd.fx.glyphs.fontawesome;

    opens org.example.linkedinclient to javafx.fxml;
    exports org.example.linkedinclient;
}