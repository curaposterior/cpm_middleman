package org.graph;

public class GraphNode {
    private int earliestOccurrence;
    private int latestOccurrence;
    private String name;
    private int id;

    public GraphNode(String name, int id){
        this.name = name;
        this.id = id;
    }

    public GraphNode(String name, int id, int earliestOccurrence, int latestOccurrence){
        this.name = name;
        this.id = id;
        this.earliestOccurrence = earliestOccurrence;
        this.latestOccurrence = latestOccurrence;
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


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int calculateTimeSupply(GraphNode node){
        return node.getLatestOccurrence()-node.getEarliestOccurrence();
    }

    @Override
    public String toString() {
        return name;
    }
}
