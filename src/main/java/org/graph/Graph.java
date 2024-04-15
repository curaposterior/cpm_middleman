package org.graph;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    private ArrayList<GraphNode> nodes;
    private ArrayList<GraphEdge> edges;

    public Graph() {
        nodes = new ArrayList<>();
        edges = new ArrayList<>();
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

    public List<List<GraphNode>> findAllShortestPaths(GraphNode start, GraphNode end) {
        List<List<GraphNode>> allShortestPaths = new ArrayList<>();
        List<GraphNode> shortestPath = new ArrayList<>();
        findShortestPaths(start, end, shortestPath, allShortestPaths);
        return allShortestPaths;
    }

    private void findShortestPaths(GraphNode current, GraphNode end, List<GraphNode> path, List<List<GraphNode>> allPaths) {
        path.add(current);
        if (current.equals(end)) {
            allPaths.add(new ArrayList<>(path));
        } else {
            for (GraphEdge edge : getAdjacentEdges(current)) {
                if (!path.contains(edge.getDestination())) {
                    findShortestPaths(edge.getDestination(), end, path, allPaths);
                }
            }
        }
        path.remove(path.size() - 1);
    }

    public List<List<GraphEdge>> findAllShortestEdges(GraphNode start, GraphNode end) {
        List<List<GraphEdge>> allShortestPaths = new ArrayList<>();
        List<GraphEdge> shortestPath = new ArrayList<>();
        findShortestEdges(start, end, shortestPath, allShortestPaths);
        return allShortestPaths;
    }

    private void findShortestEdges(GraphNode current, GraphNode end, List<GraphEdge> path, List<List<GraphEdge>> allPaths) {
        for (GraphEdge edge : getAdjacentEdges(current)) {
            if (!path.contains(edge)) {
                List<GraphEdge> newPath = new ArrayList<>(path);
                newPath.add(edge);
                if (edge.getDestination().equals(end)) {
                    allPaths.add(newPath);
                } else {
                    findShortestEdges(edge.getDestination(), end, newPath, allPaths);
                }
            }
        }
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
