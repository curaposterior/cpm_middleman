package org.cpm;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;

public class DiagramEdge extends Pane {
    private final static Color PRIMARY_COLOR = Color.BLACK;
    private final static Color CRITICAL_COLOR = Color.ORANGE;

    private final DiagramNode source;
    private final DiagramNode destination;
    private final String edgeName;
    private final int weight;

    private Line line;
    private Polygon arrowHead;
    private Color currentColor;

    public DiagramEdge(DiagramNode source, DiagramNode destination, String edgeName, int weight) {
        this.source = source;
        this.destination = destination;
        this.edgeName = edgeName;
        this.weight = weight;
        currentColor = PRIMARY_COLOR;
    }

    public void render() {
        getChildren().clear();
        getTransforms().clear();

        double distanceX = destination.getLayoutX() - source.getLayoutX();
        double distanceY = destination.getLayoutY() - source.getLayoutY();
        double distance = Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2));
        double angle = Math.toDegrees(Math.atan(distanceY / distanceX));
        angle += distanceX < 0 ? 180 : 0;
        setLayoutX(source.getLayoutX());
        setLayoutY(source.getLayoutY());

        double x1 = source.getRadius();
        double y1 = 0;

        double x2 = distance - destination.getRadius();
        double y2 = 0;

        line = new Line(x1, y1, x2, y2);
        line.setStroke(currentColor);

        double[] vertices = new double[] {
                0., 0.,
                -20., 6.,
                -20., -6.
        };
        arrowHead = new Polygon(vertices);
        arrowHead.setLayoutX(x2);
        arrowHead.setLayoutY(y2);
        arrowHead.setFill(currentColor);

        Label label = new Label(edgeName + "[" + weight + "]");
        label.setLayoutX(x1);
        label.setMinWidth(Math.abs(x2 - x1));
        label.setLayoutY(-20);
        label.setMinHeight(10);
        label.setAlignment(Pos.TOP_CENTER);

        getChildren().addAll(line, arrowHead, label);
        getTransforms().add(new Rotate(angle));
    }

    public void toggle() {
        currentColor = currentColor == PRIMARY_COLOR ? CRITICAL_COLOR : PRIMARY_COLOR;
    }

    public DiagramNode getSource() {
        return source;
    }

    public DiagramNode getDestination() {
        return destination;
    }

    public DiagramNode getOther(DiagramNode node) {
        if (node.equals(source))
            return destination;
        if (node.equals(destination))
            return source;
        return node;
    }
}
