module org.neelesh.demo {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires com.almasb.fxgl.all;
    requires RubiksCubeSim;
    requires com.fasterxml.jackson.databind;
    requires annotations;
    requires java.desktop;
    requires com.google.gson;

    opens org.neelesh.demo to javafx.fxml;
    exports org.neelesh.demo;
}