package org.middleman.input;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

public class TableAdjuster<T> {
    public void adjust(String tableName, TableView<T> table, HBox inputs) {
        Node addButton = inputs.getChildren().removeFirst();
        var mainCol = new TableColumn<T, String>(tableName);

        String[] fields = null;
        if (tableName.equals("Suppliers"))
            fields = new String[]{"Name", "Supply", "Cost"};
        else if (tableName.equals("Customers"))
            fields = new String[]{"Name", "Demand", "Price"};

        assert fields != null;
        for (var field : fields) {
            var col = new TableColumn<T, String>(field);
            col.setCellValueFactory(new PropertyValueFactory<>(field));
            mainCol.getColumns().add(col);
            var textField = new TextField();
            textField.setId(field);
            textField.setPromptText(field);
            inputs.getChildren().add(textField);
        }

        var delButton = new Button("Del");
        delButton.setMinWidth(38);
        delButton.setOnMouseClicked(mouseEvent -> table.getItems().remove(table.getSelectionModel().getSelectedItem()));

        inputs.getChildren().addAll(addButton, delButton);
        table.getColumns().add(mainCol);
    }
}
