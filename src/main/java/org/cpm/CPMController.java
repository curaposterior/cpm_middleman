package org.cpm;


import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class CPMController {
    @FXML
    Pane diagramPane;

    @FXML
    public void initialize() {
        Rectangle clip = new Rectangle(1000, 750);
        clip.setArcHeight(20);
        clip.setArcWidth(20);

        DiagramNode node1 = new DiagramNode(50, 375, 50);
        DiagramNode node2 = new DiagramNode(400, 200, 50);

        DiagramEdge edge = new DiagramEdge(node1, node2);

        diagramPane.setClip(clip);
        diagramPane.getChildren().addAll(node1, node2, edge);
    }
}
