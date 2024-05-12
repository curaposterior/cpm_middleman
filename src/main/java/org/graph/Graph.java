package org.graph;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    private List<GraphNode> nodes;
    private List<GraphEdge> edges;

    public Graph() {
        nodes = new ArrayList<>();
        edges = new ArrayList<>();
    }

    public Graph(List<GraphNode> nodes, List<GraphEdge> edges) {
        this.nodes = nodes;
        this.edges = edges;
    }

    public void addNode(GraphNode node) {
        nodes.add(node);
    }

    public void addEdge(GraphEdge edge){
        edges.add(edge);
    }

    public List<GraphNode> getNodes() {
        return nodes;
    }

    public List<GraphEdge> getEdges() {
        return edges;
    }

    public List<GraphEdge> getAdjacentEdges(GraphNode node) {
        List<GraphEdge> adjacentEdges = new ArrayList<>();
        for (GraphEdge edge : edges) {
            if (edge.getSource().equals(node)) {
                adjacentEdges.add(edge);
            }
        }
        return adjacentEdges;
    }

    public List<List<GraphNode>> findLongestPaths(GraphNode start, GraphNode end) {
        List<List<GraphNode>> longestPaths = new ArrayList<>();
        List<GraphNode> currentPath = new ArrayList<>();
        int[] maxLength = new int[]{0};
        findLongestPathsDFS(start, end, currentPath, 0, maxLength, longestPaths);
        return longestPaths;
    }

    private void findLongestPathsDFS(GraphNode current, GraphNode end, List<GraphNode> currentPath, int currentLength, int[] maxLength, List<List<GraphNode>> longestPaths) {
        currentPath.add(current);
        if (current.equals(end) && currentLength >= maxLength[0]) {
            if (currentLength > maxLength[0]) {
                maxLength[0] = currentLength;
                longestPaths.clear();
            }
            longestPaths.add(new ArrayList<>(currentPath));
        } else {
            for (GraphEdge edge : getAdjacentEdges(current)) {
                if (!currentPath.contains(edge.getDestination())) {
                    findLongestPathsDFS(edge.getDestination(), end, currentPath, currentLength + edge.getWeight(), maxLength, longestPaths);
                }
            }
        }
        currentPath.remove(current);
    }

    public Graph buildGraph(ArrayList<GraphNode> nodes, ArrayList<GraphEdge> edges){
        Graph graph = new Graph();
        for (GraphNode node : nodes){
            graph.addNode(node);
        }
        for (GraphEdge edge : edges){
            graph.addEdge(edge);
        }
        return graph;
    }
}
