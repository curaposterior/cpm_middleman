module org.cpm {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.jshell;
    requires static lombok;
    requires java.sql;


    opens org.cpm to javafx.fxml;
    opens org.middleman to javafx.fxml;
    exports org.cpm;
    exports org.graph;
    exports org.middleman;
    exports org.middleman.input;
    opens org.middleman.input to javafx.fxml;
    exports org.middleman.calculation;
    opens org.middleman.calculation to javafx.fxml;
    exports org.middleman.common;
    opens org.middleman.common to javafx.fxml;
}