package org.cpm;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;

public class DiagramEdge extends Pane {
    private final static Color primaryColor = Color.BLACK;
    private final static Color criticalColor = Color.YELLOW;
    public DiagramEdge(DiagramNode node1, DiagramNode node2) {
        double distanceX = node2.getLayoutX() - node1.getLayoutX();
        double distanceY = node2.getLayoutY() - node1.getLayoutY();
        double distance = Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2));
        double angle = Math.toDegrees(Math.atan(distanceY / distanceX));
        setLayoutX(node1.getLayoutX());
        setLayoutY(node1.getLayoutY());

        double x1 = node1.getRadius();
        double y1 = 0;

        double x2 = distance - node2.getRadius();
        double y2 = 0;

        Line line = new Line(x1, y1, x2, y2);
        line.setStroke(primaryColor);

        double[] vertices = new double[] {
                0., 0.,
                -20., 6.,
                -20., -6.
        };
        Polygon arrowHead = new Polygon(vertices);
        arrowHead.setLayoutX(x2);
        arrowHead.setLayoutY(y2);
        arrowHead.setFill(primaryColor);

        getChildren().addAll(line, arrowHead);
        getTransforms().add(new Rotate(angle));
    }
}
