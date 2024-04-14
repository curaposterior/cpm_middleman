package org.cpm;


import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

public class CPMController {
    @FXML
    Pane diagramPane;

    @FXML
    public void initialize() {
        Shape clip = (Shape) diagramPane.getChildren().getFirst();
        Circle circle = new Circle(50, 500, 100, Color.BLUE);
        diagramPane.getChildren().add(circle);
//        clip.setVisible(false);
        diagramPane.setClip(clip);
    }
}
