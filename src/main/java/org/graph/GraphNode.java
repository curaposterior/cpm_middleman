package org.graph;

import java.util.Objects;

public class GraphNode {
    private int earliestOccurrence;
    private int latestOccurrence;
    private String name;


    public GraphNode(String name, int earliestOccurrence, int latestOccurrence){
        this.name = name;
        this.earliestOccurrence = earliestOccurrence;
        this.latestOccurrence = latestOccurrence;
    }

    public GraphNode(String name) {
        this.name = name;
        this.earliestOccurrence = 0;
        this.latestOccurrence = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEarliestOccurrence() {
        return earliestOccurrence;
    }

    public void setEarliestOccurrence(int earliestOccurrence) {
        this.earliestOccurrence = earliestOccurrence;
    }

    public int getLatestOccurrence() {
        return latestOccurrence;
    }

    public void setLatestOccurrence(int latestOccurrence) {
        this.latestOccurrence = latestOccurrence;
    }


    public int calculateTimeSupply(GraphNode node){
        return node.getLatestOccurrence()-node.getEarliestOccurrence();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        GraphNode other = (GraphNode) obj;
        return Objects.equals(name, other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name;
    }
}
