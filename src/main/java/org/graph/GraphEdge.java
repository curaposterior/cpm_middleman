    package org.graph;

    public class GraphEdge {
        private  String name;
        private GraphNode source;
        private GraphNode destination;
        private int weight;

        public GraphEdge(String name, GraphNode source, GraphNode destination, int weight){
            this.name = name;
            this.source = source;
            this.destination = destination;
            this.weight = weight;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public GraphNode getSource() {
            return source;
        }

        public void setSource(GraphNode source) {
            this.source = source;
        }

        public GraphNode getDestination() {
            return destination;
        }

        public void setDestination(GraphNode destination) {
            this.destination = destination;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        @Override
        public String toString(){
            return "(" + source.getName() + " -> " + destination.getName() + ", Weight: " + weight + ")";
        }
    }
