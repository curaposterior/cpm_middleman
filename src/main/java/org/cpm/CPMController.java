package org.cpm;


import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;

import java.util.ArrayList;
import java.util.List;

public class CPMController {
    @FXML
    Pane diagramPane;
    @FXML
    Group contents;
    private Point2D lastTransformPoint;
    private Point2D moveOffset;
    private Node movedObject;

    @FXML
    public void initialize() {
        lastTransformPoint = null;
        contents.getTransforms().addAll(new Scale());

        diagramPane.setOnScroll(scrollEvent -> {
            Point2D local = contents.parentToLocal(scrollEvent.getX(), scrollEvent.getY());
            if (scrollEvent.getDeltaY() > 0) {
                Transform current = contents.getTransforms().getFirst();
                Scale scale = new Scale(1.1, 1.1, local.getX(), local.getY());
                contents.getTransforms().removeFirst();
                contents.getTransforms().add(current.createConcatenation(scale));
            } else if (scrollEvent.getDeltaY() < 0) {
                Transform current = contents.getTransforms().getFirst();
                Scale scale = new Scale(0.91, 0.91, local.getX(), local.getY());
                contents.getTransforms().removeFirst();
                contents.getTransforms().add(current.createConcatenation(scale));
            }
        });
        diagramPane.setOnMousePressed(mouseEvent -> {
            if (mouseEvent.isMiddleButtonDown()) {
                lastTransformPoint = new Point2D(mouseEvent.getX(), mouseEvent.getY());
            } else if (mouseEvent.isPrimaryButtonDown()) {
                Point2D local = contents.parentToLocal(mouseEvent.getX(), mouseEvent.getY());
                if (contents.contains(local))
                    for (Node child : contents.getChildren()) {
                        if (!(child instanceof DiagramNode)) continue;
                        if (child.contains(child.parentToLocal(local))) {
                            moveOffset = new Point2D(local.getX() - child.getLayoutX(), local.getY() - child.getLayoutY());
                            movedObject = child;
                            break;
                        }
                    }
            }
        });
        diagramPane.setOnMouseDragged(mouseEvent -> {
            if (mouseEvent.isMiddleButtonDown()) {
                Transform current = contents.getTransforms().getFirst();
                Translate translate = new Translate(mouseEvent.getX() - lastTransformPoint.getX(), mouseEvent.getY() - lastTransformPoint.getY());
                lastTransformPoint = new Point2D(mouseEvent.getX(), mouseEvent.getY());
                contents.getTransforms().removeFirst();
                contents.getTransforms().add(translate.createConcatenation(current));
            } else if (mouseEvent.isPrimaryButtonDown()) {
                if (movedObject != null) {
                    Point2D local = contents.parentToLocal(mouseEvent.getX(), mouseEvent.getY());
                    movedObject.setLayoutX(local.getX() - moveOffset.getX());
                    movedObject.setLayoutY(local.getY() - moveOffset.getY());
                    List<DiagramEdge> newEdges = new ArrayList<>();
                    if (movedObject instanceof DiagramNode node) {
                        for (DiagramEdge edge : node.getEdges()) {
                            DiagramNode other = edge.getOther(node);
                            if (other == node) continue;
                            DiagramEdge replacement = new DiagramEdge(edge.getNode1(), edge.getNode2());
                            newEdges.add(replacement);
                            other.removeEdge(edge);
                            other.addEdge(replacement);
                            contents.getChildren().remove(edge);
                        }
                        node.setEdges(newEdges);
                        contents.getChildren().addAll(newEdges);
                    }
                }
            }
        });
        diagramPane.setOnMouseReleased(mouseEvent -> {
            if (!mouseEvent.isPrimaryButtonDown())
                movedObject = null;
        });
        Rectangle clip = new Rectangle(1000, 750);
        clip.setArcHeight(20);
        clip.setArcWidth(20);

        DiagramNode node1 = new DiagramNode(null);
        node1.setCenter(200, 375);
        DiagramNode node2 = new DiagramNode(null);
        node2.setCenter(400, 200);
        DiagramNode node3 = new DiagramNode(null);
        node3.setCenter(400, 400);

        DiagramEdge edge1 = new DiagramEdge(node1, node2);
        node1.addEdge(edge1);
        node2.addEdge(edge1);

        DiagramEdge edge2 = new DiagramEdge(node1, node3);
        node1.addEdge(edge2);
        node3.addEdge(edge2);

        diagramPane.setClip(clip);
        contents.getChildren().addAll(node1, node2, node3, edge1, edge2);
    }
}