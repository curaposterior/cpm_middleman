package org.cpm;

import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import org.graph.GraphEdge;
import org.graph.GraphNode;
import java.util.ArrayList;

import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

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

    private List<GraphEdge> graphEdges = new ArrayList<>();
    private List<GraphNode> graphNodes = new ArrayList<>();

    @FXML
    void readFromFile(ActionEvent event) {
    }

    @FXML
    void saveToFile(ActionEvent event) {
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

        GraphEdge edge = new GraphEdge(name, sourceNode, destinationNode, Integer.parseInt(weight));

        tableView.getItems().add(edge);

        graphEdges.add(edge);

        edgeColumn_name.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getName()));
        edgeColumn_weight.setCellValueFactory(cellData -> new ReadOnlyIntegerWrapper(cellData.getValue().getWeight()));
        edgeColumn_source.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getSource().toString()));
        edgeColumn_destination.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getDestination().toString()));

        activityNameTextField.clear();
        durationTextField.clear();
        eventSequenceTextField.clear();
        eventSequenceTextField1.clear();

        System.out.println(graphEdges);
        System.out.println(graphNodes);
    }

    @FXML
    private void initialize() {
//        edgeColumn_name.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getName()));
//        edgeColumn_weight.setCellValueFactory(cellData -> new ReadOnlyIntegerWrapper(cellData.getValue().getWeight()));
//        edgeColumn_source.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getSource().toString()));
//        edgeColumn_destination.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getDestination().toString()));
    }


}
