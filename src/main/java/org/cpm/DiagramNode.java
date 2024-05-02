package org.cpm;

import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;

import java.util.ArrayList;
import java.util.List;

public class DiagramNode extends Pane {
    private final static Color primaryColor = Color.RED.deriveColor(0, 1, 1, 0.2);
    private final static Color criticalColor = Color.YELLOW.deriveColor(0, 1, 1, 0.2);
    private double radius;

    private Label id;
    private Label earlyStart;
    private Label lateFinish;
    private Label mean;

    private List<DiagramEdge> edges;

    public DiagramNode(double centerX, double centerY, double radius) {
        setLayoutX(centerX);
        setLayoutY(centerY);
        this.radius = radius;

        Circle circle = new Circle(radius, primaryColor);
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(1);

        Line line1 = new Line(-radius, 0, radius, 0);
        line1.getTransforms().add(new Rotate(45));
        line1.setOpacity(0.4);
        Line line2 = new Line(-radius, 0, radius, 0);
        line2.getTransforms().add(new Rotate(-45));
        line2.setOpacity(0.4);

        this.id = new Label("id");
        id.setLayoutY(-radius);
        id.setMinHeight(radius);
        id.setLayoutX(-radius / 2);
        id.setMinWidth(radius);
        id.setAlignment(Pos.CENTER);

        this.earlyStart = new Label("es");
        earlyStart.setLayoutY(-radius / 2);
        earlyStart.setMinHeight(radius);
        earlyStart.setLayoutX(-radius);
        earlyStart.setMinWidth(radius);
        earlyStart.setAlignment(Pos.CENTER);

        this.lateFinish = new Label("lf");
        lateFinish.setLayoutY(-radius / 2);
        lateFinish.setMinHeight(radius);
        lateFinish.setMinWidth(radius);
        lateFinish.setAlignment(Pos.CENTER);

        this.mean = new Label("m");
        mean.setMinHeight(radius);
        mean.setLayoutX(-radius / 2);
        mean.setMinWidth(radius);
        mean.setAlignment(Pos.CENTER);

        getChildren().addAll(circle, line1, line2, id, earlyStart, lateFinish, mean);

        edges = new ArrayList<>();
    }

    @Override
    public boolean contains(double v, double v1) {
        return Math.pow(v, 2) + Math.pow(v1, 2) < Math.pow(radius, 2);
    }

    @Override
    public boolean contains(Point2D point2D) {
        return contains(point2D.getX(), point2D.getY());
    }

    public double getRadius() {
        return radius;
    }

    public void addEdge(DiagramEdge edge) {
        edges.add(edge);
    }

    public void removeEdge(DiagramEdge edge) {
        edges.remove(edge);
    }

    public void setEdges(List<DiagramEdge> edges) {
        this.edges = edges;
    }

    public List<DiagramEdge> getEdges() {
        return edges;
    }
}
