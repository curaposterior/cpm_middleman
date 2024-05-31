package org.middleman.calculation;

import javafx.fxml.FXML;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

public class ResultsController {
    @Setter
    private List<Supplier> suppliers;
    @Setter
    private List<Customer> customers;
    @Setter
    private double[][] transportCosts;
    @FXML
    void initialize() {

    }

    public void calculate() {
        System.out.println(suppliers);
        System.out.println(customers);
        for (var row : transportCosts) {
            System.out.println(Arrays.toString(row));
        }
        // tu piszesz swój kod Johnny
    }
}
