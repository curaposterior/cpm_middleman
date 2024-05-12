package org.cpm;

import javafx.geometry.Point2D;
import org.graph.GraphEdge;
import org.graph.GraphNode;

import java.util.ArrayList;
import java.util.List;

public class DiagramBuilder {
    private static final double SPACING = 100.;

    public static void build(List<GraphNode> graphNodes, List<GraphEdge> graphEdges, List<DiagramNode> diagramNodes, List<DiagramEdge> diagramEdges, Point2D start) {
        for (GraphNode node : graphNodes)
            diagramNodes.add(new DiagramNode(node));

        for (GraphEdge edge : graphEdges) {
            DiagramNode source = null;
            DiagramNode destination = null;

            for (DiagramNode node : diagramNodes) {
                if (source != null && destination != null)
                    break;
                if (node.getName().equals(edge.getSource().getName()))
                    source = node;
                else if (node.getName().equals(edge.getDestination().getName()))
                    destination = node;
            }

            DiagramEdge diagramEdge = new DiagramEdge(source, destination, edge.getName(), edge.getWeight());
            assert source != null;
            source.addEdge(diagramEdge);
            assert destination != null;
            destination.addEdge(diagramEdge);
            diagramEdges.add(diagramEdge);
        }

        DiagramNode startNode = diagramNodes.getFirst();

        List<DiagramNode> currentLevel = new ArrayList<>();
        currentLevel.add(startNode);

        List<List<DiagramNode>> levels = new ArrayList<>();
        List<DiagramNode> checked = new ArrayList<>();

        while (!currentLevel.isEmpty()) {
            List<DiagramNode> nextLevel = new ArrayList<>();

            for (DiagramNode node : currentLevel) {
                if (node == null) continue;
                for (DiagramEdge edge : node.getEdges())
                    if (node == edge.getSource()) {
                        var dest = edge.getDestination();
                        if (nextLevel.contains(dest)) continue;
                        nextLevel.add(edge.getDestination());
                    }
                checked.add(node);
            }

            for (DiagramNode node : nextLevel) {
                for (DiagramEdge edge : node.getEdges())
                    if (node == edge.getDestination() && !checked.contains(edge.getSource())) {
                        nextLevel.set(nextLevel.indexOf(node), null);
                        break;
                    }
            }

            levels.add(currentLevel);
            currentLevel = nextLevel;
        }

        final double axisY = start.getY();
        double axisX = start.getX();
        for (List<DiagramNode> level : levels) {
            double y = axisY - (level.size() - 1) * SPACING;

            for (var node : level) {
                if (node == null) {
                    y += SPACING * 2;
                    continue;
                }
                node.setLayoutX(axisX);
                node.setLayoutY(y);
                y += SPACING * 2;
            }

            axisX += SPACING * 2;
        }
    }
}
