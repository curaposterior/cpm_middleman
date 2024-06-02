package org.middleman.input;

import javafx.beans.property.StringProperty;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.middleman.*;
import org.middleman.calculation.Customer;
import org.middleman.calculation.ResultsController;
import org.middleman.calculation.Supplier;
import org.middleman.common.AlertProducer;
import org.middleman.common.GridUtils;

import java.io.IOException;
import java.util.*;

public class MiddlemanController {
    @FXML
    TableView<SupplierEntity> suppliersTableView;
    @FXML
    HBox suppliersInputFields;
    @FXML
    TableView<CustomerEntity> customersTableView;
    @FXML
    HBox customersInputFields;
    @FXML
    GridPane transportCosts;
    public static final int nameLengthConstant = 19;

    @FXML
    void initialize() {
        var supplierTableAdjuster = new TableAdjuster<SupplierEntity>();
        supplierTableAdjuster.adjust("Suppliers", suppliersTableView, suppliersInputFields);
        suppliersTableView.getItems().addListener((ListChangeListener<? super SupplierEntity>) change -> {
            change.next();
            if (change.getRemovedSize() > 0) {
                SupplierEntity removed = change.getRemoved().getFirst();
                GridUtils.removeRow(transportCosts, removed.getName());
                return;
            }
            SupplierEntity supplier = change.getAddedSubList().getFirst();
            int row = transportCosts.getRowCount();
            supplier.setGridIndex(row);
            GridUtils.addRow(transportCosts, supplier.getName());
            transportCosts.getScene().getWindow().sizeToScene();
        });

        var customerTableAdjuster = new TableAdjuster<CustomerEntity>();
        customerTableAdjuster.adjust("Customers", customersTableView, customersInputFields);
        customersTableView.getItems().addListener((ListChangeListener<? super CustomerEntity>) change -> {
            change.next();
            if (change.getRemovedSize() > 0) {
                CustomerEntity removed = change.getRemoved().getFirst();
                GridUtils.removeColumn(transportCosts, removed.getName());
                return;
            }
            CustomerEntity customer = change.getAddedSubList().getFirst();
            int col = transportCosts.getColumnCount();
            customer.setGridIndex(col);
            GridUtils.addColumn(transportCosts, customer.getName());
            transportCosts.getScene().getWindow().sizeToScene();
        });
    }

    @FXML
    void onCalculateButtonClick() throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(MiddlemanApplication.class.getResource("results.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.setTitle("Results");
        stage.sizeToScene();
        stage.centerOnScreen();
        stage.show();
        var controller = (ResultsController) loader.getController();

        List<Supplier> suppliers = new ArrayList<>();
        Map<Integer, Integer> supIndexes = new HashMap<>();
        for (var sup : suppliersTableView.getItems()) {
            supIndexes.put(sup.getGridIndex(), suppliers.size());
            double supply = Double.parseDouble(sup.getSupply());
            double cost = Double.parseDouble(sup.getCost());
            suppliers.add(new Supplier(sup.getName(), supply, cost, false));
        }

        List<Customer> customers = new ArrayList<>();
        Map<Integer, Integer> cusIndexes = new HashMap<>();
        for (var cus : customersTableView.getItems()) {
            cusIndexes.put(cus.getGridIndex(), customers.size());
            double demand = Double.parseDouble(cus.getDemand());
            double price = Double.parseDouble(cus.getPrice());
            customers.add(new Customer(cus.getName(), demand, price, false));
        }

        double[][] costs = new double[suppliers.size() + 1][customers.size() + 1];
        for (Node node : transportCosts.getChildren()) {
            int row = GridPane.getRowIndex(node);
            int col = GridPane.getColumnIndex(node);
            if (supIndexes.containsKey(row) && cusIndexes.containsKey(col))
                costs[supIndexes.get(row)][cusIndexes.get(col)] = Double.parseDouble(((TextField) node).getText());
        }

        controller.setSuppliers(suppliers);
        controller.setCustomers(customers);
        controller.setTransportCostsMatrix(costs);
        controller.calculate();
    }

    @FXML
    void onAddSupplierButtonAction() {
        StringProperty name = null;
        StringProperty supply = null;
        StringProperty cost = null;
        for (var field : suppliersInputFields.getChildren()) {
            if (field instanceof TextField) {
                switch (field.getId()) {
                    case "Name" -> name = ((TextField) field).textProperty();
                    case "Supply" -> supply = ((TextField) field).textProperty();
                    case "Cost" -> cost = ((TextField) field).textProperty();
                }
            }
        }
        assert name != null;
        assert supply != null;
        assert cost != null;
        if (name.get().isEmpty()) {
            AlertProducer.alert(Alert.AlertType.INFORMATION,
                    "Information",
                    "Input name",
                    "");
            return;
        }
        if (name.get().length() > nameLengthConstant) {
            AlertProducer.alert(Alert.AlertType.INFORMATION,
                    "Information",
                    "Name is too long",
                    "");
            return;
        }
        if (supply.get().isEmpty()) {
            AlertProducer.alert(Alert.AlertType.INFORMATION,
                    "Information",
                    "Input supply",
                    "");
            return;
        }
        if (cost.get().isEmpty()) {
            AlertProducer.alert(Alert.AlertType.INFORMATION,
                    "Information",
                    "Input cost",
                    "");
            return;
        }
        if (!supply.get().matches("^[0-9]*(\\.[0-9]*)?$")) {
            AlertProducer.alert(Alert.AlertType.WARNING,
                    "Warning!",
                    "Supply must be a positive number.",
                    "");
            return;
        }
        if (!cost.get().matches("^[0-9]*(\\.[0-9]*)?$")) {
            AlertProducer.alert(Alert.AlertType.WARNING,
                    "Warning!",
                    "Cost must be a positive number.",
                    "");
            return;
        }
        for (var supplier : suppliersTableView.getItems())
            if (supplier.getName().equals(name.get())) {
                AlertProducer.alert(Alert.AlertType.WARNING,
                        "Warning!",
                        "Supplier named '" + name.get() + "' already exists.",
                        "Use different name.");
                return;
            }
        suppliersTableView.getItems().add(new SupplierEntity(name.get(), supply.get(), cost.get()));
        name.set("");
        supply.set("");
        cost.set("");
    }


    @FXML
    void onAddCustomerButtonAction() {
        StringProperty name = null;
        StringProperty demand = null;
        StringProperty price = null;
        for (var field : customersInputFields.getChildren()) {
            if (field instanceof TextField) {
                switch (field.getId()) {
                    case "Name" -> name = ((TextField) field).textProperty();
                    case "Demand" -> demand = ((TextField) field).textProperty();
                    case "Price" -> price = ((TextField) field).textProperty();
                }
            }
        }
        assert name != null;
        assert demand != null;
        assert price != null;
        if (name.get().isEmpty()) {
            AlertProducer.alert(Alert.AlertType.INFORMATION,
                    "Information",
                    "Input name",
                    "");
            return;
        }
        if (name.get().length() > nameLengthConstant) {
            AlertProducer.alert(Alert.AlertType.INFORMATION,
                    "Information",
                    "Name is too long",
                    "");
            return;
        }
        if (demand.get().isEmpty()) {
            AlertProducer.alert(Alert.AlertType.INFORMATION,
                    "Information",
                    "Input demand",
                    "");
            return;
        }
        if (price.get().isEmpty()) {
            AlertProducer.alert(Alert.AlertType.INFORMATION,
                    "Information",
                    "Input price",
                    "");
            return;
        }
        if (!demand.get().matches("^[0-9]*(\\.[0-9]*)?$")) {
            AlertProducer.alert(Alert.AlertType.WARNING,
                    "Warning!",
                    "Demand must be a positive number.",
                    "");
            return;
        }
        if (!price.get().matches("^[0-9]*(\\.[0-9]*)?$")) {
            AlertProducer.alert(Alert.AlertType.WARNING,
                    "Warning!",
                    "Price must be a positive number.",
                    "");
            return;
        }
        for (var customer : customersTableView.getItems())
            if (customer.getName().equals(name.get())) {
                AlertProducer.alert(Alert.AlertType.WARNING,
                        "Warning!",
                        "Customer named '" + name.get() + "' already exists.",
                        "Use different name.");
                return;
            }
        customersTableView.getItems().add(new CustomerEntity(name.get(), demand.get(), price.get()));
        name.set("");
        demand.set("");
        price.set("");
    }
}
