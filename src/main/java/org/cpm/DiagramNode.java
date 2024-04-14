package org.cpm;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;

public class DiagramNode extends Pane {
    private final static Color primaryColor = Color.RED.deriveColor(0, 1, 1, 0.2);
    private final static Color criticalColor = Color.YELLOW.deriveColor(0, 1, 1, 0.2);
    private double radius;

    private Text id;
    private Text earlyStart;
    private Text lateFinish;
    private Text mean;

    public DiagramNode(double centerX, double centerY, double radius) {
        setLayoutX(centerX);
        setLayoutY(centerY);
        this.radius = radius;

        Circle circle = new Circle(radius, primaryColor);
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(1);

        Line line1 = new Line(-radius, 0, radius,0);
        line1.getTransforms().add(new Rotate(45));
        line1.setOpacity(0.4);
        Line line2 = new Line(-radius, 0, radius,0);
        line2.getTransforms().add(new Rotate(-45));
        line2.setOpacity(0.4);

        getChildren().addAll(circle, line1, line2);
    }

    public double getRadius() {
        return radius;
    }
}
