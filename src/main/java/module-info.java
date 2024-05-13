module org.cpm {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.jshell;


    opens org.cpm to javafx.fxml;
    exports org.cpm;
    exports org.graph;
}