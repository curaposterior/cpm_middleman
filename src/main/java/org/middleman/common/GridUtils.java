package org.middleman.common;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.util.Objects;

public class GridUtils {
    public static void addRow(GridPane grid, String name, double... values) {
        int row = grid.getRowCount();
        var label = new Label(name);
        GridPane.setHalignment(label, HPos.CENTER);
        grid.add(label, 0, row);
        for (int i = 1; i < grid.getColumnCount(); i++) {
            String text = "";
            if (values.length >= i)
                text = String.valueOf(values[i-1]);
            var field = new TextField(text);
            field.setMaxWidth(80);
            GridPane.setMargin(field, new Insets(5));
            grid.add(field, i, row);
        }
    }

    public static void addColumn(GridPane grid, String name, double... values) {
        int col = grid.getColumnCount();
        var label = new Label(name);
        GridPane.setHalignment(label, HPos.CENTER);
        grid.add(label, col, 0);
        for (int i = 1; i < grid.getRowCount(); i++) {
            String text = "";
            if (values.length >= i)
                text = String.valueOf(values[i-1]);
            var field = new TextField(text);
            field.setMaxWidth(80);
            GridPane.setMargin(field, new Insets(5));
            grid.add(field, col, i);
        }
    }

    public static void removeRow(GridPane grid, String name) {
        for (Node node : grid.getChildren())
            if (GridPane.getColumnIndex(node) == 0 && node instanceof Label label)
                if (Objects.equals(label.getText(), name)) {
                    int col = GridPane.getRowIndex(label);
                    grid.getChildren().removeIf(c -> GridPane.getRowIndex(c) == col);
                    return;
                }
    }

    public static void removeColumn(GridPane grid, String name) {
        for (Node node : grid.getChildren())
            if (GridPane.getRowIndex(node) == 0 && node instanceof Label label)
                if (Objects.equals(label.getText(), name)) {
                    int col = GridPane.getColumnIndex(label);
                    grid.getChildren().removeIf(c -> GridPane.getColumnIndex(c) == col);
                    return;
                }
    }
}
