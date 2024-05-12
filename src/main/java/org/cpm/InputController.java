package org.cpm;

import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

import javafx.stage.Stage;
import org.graph.Graph;
import org.graph.GraphEdge;
import org.graph.GraphNode;

import java.io.IOException;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class InputController {

    @FXML
    public Button readFromFileButton;

    @FXML
    public Button saveToFileButton;

    @FXML
    public TableView<GraphEdge> tableView;

    @FXML
    public TableColumn<GraphEdge, String> edgeColumn_name;

    @FXML
    public TableColumn<GraphEdge, Number> edgeColumn_weight;

    @FXML
    public TableColumn<GraphEdge, String> edgeColumn_source;

    @FXML
    public TableColumn<GraphEdge, String> edgeColumn_destination;

    @FXML
    public TextField activityNameTextField;

    @FXML
    public TextField durationTextField;

    @FXML
    public TextField eventSequenceTextField;

    @FXML
    public TextField eventSequenceTextField1;

    @FXML
    public Button addRecordButton;

    private ObservableList<GraphEdge> graphEdges;
    private ObservableList<GraphNode> graphNodes;

    @FXML
    private void initialize() {
        edgeColumn_name.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getName()));
        edgeColumn_weight.setCellValueFactory(cellData -> new ReadOnlyIntegerWrapper(cellData.getValue().getWeight()));
        edgeColumn_source.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getSource().toString()));
        edgeColumn_destination.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getDestination().toString()));

        graphNodes = FXCollections.observableArrayList();
        graphEdges = FXCollections.observableArrayList();
    }

    @FXML
    void readFromFile(ActionEvent event) {
        ObservableList<GraphEdge> readEdges = FXCollections.observableArrayList();
        ObservableList<GraphNode> readNodes = FXCollections.observableArrayList();


        try (BufferedReader nodesReader = new BufferedReader(new FileReader("nodes.csv"));
             BufferedReader edgesReader = new BufferedReader(new FileReader("edges.csv"))) {

            String line;

            nodesReader.readLine();
            edgesReader.readLine();

            while ((line = nodesReader.readLine()) != null) {
                String[] parts = line.split(",");
                String nodeName = parts[0].trim();
                int earliestOccurrence = Integer.parseInt(parts[1].trim());
                int latestOccurrence = Integer.parseInt(parts[2].trim());
                GraphNode node = new GraphNode(nodeName, earliestOccurrence, latestOccurrence);
                if (!readNodes.contains(node)) {
                    readNodes.add(node);
                }
            }

            while ((line = edgesReader.readLine()) != null) {
                String[] parts = line.split(",");
                String edgeName = parts[0];
                String sourceName = parts[1];
                String destinationName = parts[2];
                int weight = Integer.parseInt(parts[3]);

                GraphNode sourceNode = new GraphNode(sourceName);
                GraphNode destinationNode = new GraphNode(destinationName);
                GraphEdge edge = new GraphEdge(edgeName, sourceNode, destinationNode, weight);
                if (!readNodes.contains(sourceNode)) {
                    readNodes.add(sourceNode);
                }
                if (!readNodes.contains(destinationNode)) {
                    readNodes.add(destinationNode);
                }
                if (!readEdges.contains(edge)) {
                    readEdges.add(edge);
                }
            }

            graphEdges = readEdges;
            graphNodes = readNodes;

            System.out.println(graphEdges);
            System.out.println(graphNodes);

            refreshTableView();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Informacja");
            alert.setHeaderText(null);
            alert.setContentText("Dane zostały wczytane z plików CSV.");
            alert.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd");
            alert.setHeaderText(null);
            alert.setContentText("Wystąpił błąd podczas wczytywania danych.");
            alert.showAndWait();
        }
    }

    private void refreshTableView() {
        tableView.getItems().clear();
        tableView.getItems().addAll(graphEdges);
    }

    @FXML
    void saveToFile(ActionEvent event) {
        try (FileWriter nodesWriter = new FileWriter("nodes.csv");
             FileWriter edgesWriter = new FileWriter("edges.csv")) {

            // Zapisywanie do pliku nodes.csv
            nodesWriter.write("Name,EarliestOccurrence,LatestOccurrence\n");
            for (GraphNode node : graphNodes) {
                nodesWriter.write(node.getName() + "," + String.valueOf(node.getEarliestOccurrence()) + "," + String.valueOf(node.getLatestOccurrence()) + "\n");
            }

            // Zapisywanie do pliku edges.csv
            edgesWriter.write("Name,Source,Destination,Weight\n");
            for (GraphEdge edge : graphEdges) {
                edgesWriter.write(edge.getName() + "," + edge.getSource().getName() + "," + edge.getDestination().getName() + "," + String.valueOf(edge.getWeight()) + "\n");
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Informacja");
            alert.setHeaderText(null);
            alert.setContentText("Dane zostały zapisane do plików CSV.");
            alert.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd");
            alert.setHeaderText(null);
            alert.setContentText("Wystąpił błąd podczas zapisywania danych.");
            alert.showAndWait();
        }
    }

    @FXML
    void addRecord(ActionEvent event) {
        String name = activityNameTextField.getText();
        String weight = durationTextField.getText();
        String sourceName = eventSequenceTextField.getText();
        String destinationName = eventSequenceTextField1.getText();

        GraphNode sourceNode = new GraphNode(sourceName);
        GraphNode destinationNode = new GraphNode(destinationName);

        if (!graphNodes.contains(sourceNode)) {
            graphNodes.add(sourceNode);
        }

        if (!graphNodes.contains(destinationNode)) {
            graphNodes.add(destinationNode);
        }

        boolean edgeExists = graphEdges.stream().anyMatch(e -> e.getName().equals(name));
        if (edgeExists) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd");
            alert.setHeaderText(null);
            alert.setContentText("Krawędź o nazwie " + name + " już istnieje.");
            alert.showAndWait();
            return;
        }

        GraphEdge edge = new GraphEdge(name, sourceNode, destinationNode, Integer.parseInt(weight));
        tableView.getItems().add(edge);
        graphEdges.add(edge);


        activityNameTextField.clear();
        durationTextField.clear();
        eventSequenceTextField.clear();
        eventSequenceTextField1.clear();

        System.out.println(graphEdges);
        System.out.println(graphNodes);
    }

    public List<GraphEdge> getAdjacentEdges(GraphNode node) {
        List<GraphEdge> adjacentEdges = new ArrayList<>();
        for (GraphEdge edge : graphEdges) {
            if (edge.getSource().equals(node)) {
                adjacentEdges.add(edge);
            }
        }
        return adjacentEdges;
    }

    @FXML
    void buildGraph(ActionEvent event) throws IOException {
        CPMApplication.load("cpm.fxml", null);
        ((Stage) tableView.getScene().getWindow()).close();
    }

    public List<GraphEdge> getGraphEdges() {
        return new ArrayList<>(graphEdges);
    }

    public List<GraphNode> getGraphNodes() {
        return new ArrayList<>(graphNodes);
    }
}