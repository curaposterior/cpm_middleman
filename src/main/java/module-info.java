module org.cpm {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.cpm to javafx.fxml;
    exports org.cpm;
    exports org.graph;
}