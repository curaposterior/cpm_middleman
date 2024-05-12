package org.cpm;

import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;
import org.graph.GraphNode;

import java.util.ArrayList;
import java.util.List;

public class DiagramNode extends Pane {
    private final static Color PRIMARY_COLOR = Color.RED.deriveColor(0, 1, 1, 0.2);
    private final static Color CRITICAL_COLOR = Color.GOLD.deriveColor(0, 1, 1, 0.4);
    private final static double DEFAULT_RADIUS = 50;

    private final double radius;
    private Circle background;

    private final Label name;
    private final Label earlyStart;
    private final Label earlyFinish;
    private final Label timeMargin;
    private boolean toggleVisited;

    private List<DiagramEdge> edges;

    public DiagramNode(GraphNode node) {
        if (node == null)
            node = new GraphNode("");

        this.toggleVisited = false;

        this.radius = DEFAULT_RADIUS;
        background = new Circle(radius, PRIMARY_COLOR);
        background.setStroke(Color.BLACK);
        background.setStrokeWidth(1);

        Line line1 = new Line(-radius, 0, radius, 0);
        line1.getTransforms().add(new Rotate(45));
        line1.setOpacity(0.4);
        Line line2 = new Line(-radius, 0, radius, 0);
        line2.getTransforms().add(new Rotate(-45));
        line2.setOpacity(0.4);

        this.name = new Label(node.getName());
        name.setLayoutY(-radius);
        name.setMinHeight(radius);
        name.setLayoutX(-radius / 2);
        name.setMinWidth(radius);
        name.setAlignment(Pos.CENTER);

        int es = node.getEarliestOccurrence();
        this.earlyStart = new Label(String.valueOf(es));
        earlyStart.setLayoutY(-radius / 2);
        earlyStart.setMinHeight(radius);
        earlyStart.setLayoutX(-radius);
        earlyStart.setMinWidth(radius);
        earlyStart.setAlignment(Pos.CENTER);

        int lf = node.getLatestOccurrence();
        this.earlyFinish = new Label(String.valueOf(lf));
        earlyFinish.setLayoutY(-radius / 2);
        earlyFinish.setMinHeight(radius);
        earlyFinish.setMinWidth(radius);
        earlyFinish.setAlignment(Pos.CENTER);

        this.timeMargin = new Label(String.valueOf(lf - es));
        timeMargin.setMinHeight(radius);
        timeMargin.setLayoutX(-radius / 2);
        timeMargin.setMinWidth(radius);
        timeMargin.setAlignment(Pos.CENTER);

        getChildren().addAll(background, line1, line2, name, earlyStart, earlyFinish, timeMargin);

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

    public void toggle() {
        background.setFill(background.getFill() == PRIMARY_COLOR ? CRITICAL_COLOR : PRIMARY_COLOR);
    }

    public double getRadius() {
        return radius;
    }

    public String getName() {
        return name.getText();
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

    public void setEarlyStart(int earlyStart) {
        this.earlyStart.setText(String.valueOf(earlyStart));
    }

    public void setEarlyFinish(int earlyFinish) {
        this.earlyFinish.setText(String.valueOf(earlyFinish));
    }

    public void setTimeMargin(int timeMargin) {
        this.timeMargin.setText(String.valueOf(timeMargin));
    }

    public Integer getEarlyStart() {
        return Integer.valueOf(earlyStart.getText());
    }

    public Integer getEarlyFinish() {
        return Integer.valueOf(earlyFinish.getText());
    }

    public Integer getTimeMargin() {
        return Integer.valueOf(timeMargin.getText());
    }

    public boolean isToggleVisited() {
        return toggleVisited;
    }

    public void setToggleVisited(boolean toggleVisited) {
        this.toggleVisited = toggleVisited;
    }
}
