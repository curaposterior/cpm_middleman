package org.middleman.calculation;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import lombok.Setter;
import org.middleman.common.GridUtils;

import java.util.*;

public class ResultsController {
    @Setter
    private List<Supplier> suppliers;
    @Setter
    private List<Customer> customers;
    @Setter
    private double[][] transportCostsMatrix; // macierz z kosztami transportu

    private List<Route> routes;

    private double totalTransportationCost = 0.0;
    private double totalPurchaseCost = 0.0;
    private double totalRevenue = 0.0;
    private double totalProfit = 0.0;

    @FXML
    GridPane unitProfit;
    @FXML
    GridPane optimalTransport;
    @FXML
    TextField costTextField;
    @FXML
    TextField incomeTextField;
    @FXML
    TextField profitTextField;

    @FXML
    void initialize() {
        routes = new ArrayList<>();
    }

    public void calculate() {
        double demand = 0;
        double supply = 0;
        for (var customer : customers) {
            demand += customer.getDemand();
        }
        for (var supplier : suppliers) {
            supply += supplier.getSupply();
        }

        if (demand != supply) {
            suppliers.add(new Supplier("SF", demand, 0, true));
            customers.add(new Customer("CF", supply, 0, true));
        }

        for (int i = 0; i < suppliers.size(); i++)
            for (int j = 0; j < customers.size(); j++) {
                Supplier supplier = suppliers.get(i);
                Customer customer = customers.get(j);
                Route route = new Route(supplier, customer, transportCostsMatrix[i][j]);
                routes.add(route);
                supplier.addRoute(route);
                customer.addRoute(route);
            }

        calculateTotalRevenues();
        calculateMaxMatrixElementMethod();

        while (true) {
            calculateAlphasBetas();
            calculateDeltas();
            boolean flag = false;
            for (var route : routes) {
                double delta = route.getDelta();
                if (!Double.isNaN(delta) && delta > 0) {
                    flag = true;
                    break;
                }
            }
            if (!flag || !optimizeCosts())
                break;
        }

        calculateFinalVariables();

        for (var supplier : suppliers) {
            if (supplier.isFictional())
                continue;
            GridUtils.addRow(unitProfit, supplier.getName());
            GridUtils.addRow(optimalTransport, supplier.getName());
        }

        for (var customer : customers) {
            if (customer.isFictional())
                continue;
            GridUtils.addColumn(unitProfit, customer.getName(),
                    customer.getRoutes().stream()
                            .filter(route -> !route.getSupplier().isFictional())
                            .mapToDouble(Route::getTotalRevenue)
                            .toArray()
            );
            GridUtils.addColumn(optimalTransport, customer.getName(),
                    customer.getRoutes().stream()
                            .filter(route -> !route.getSupplier().isFictional())
                            .mapToDouble(Route::getUnits)
                            .toArray()
            );
        }

        costTextField.setText(String.valueOf(this.totalTransportationCost + this.totalPurchaseCost));
        incomeTextField.setText(String.valueOf(this.totalRevenue));
        profitTextField.setText(String.valueOf(this.totalProfit));
    }

    public void calculateTotalRevenues() {
        for (var route : routes)
            route.calculateTotalRevenue();
    }

    public void calculateMaxMatrixElementMethod() {
        var routesSorted = routes.stream()
                .sorted(Comparator.comparingDouble(Route::getTotalRevenue))
                .toList()
                .reversed();

        while (!routesSorted.isEmpty()) {
            Route route = routesSorted.stream()
                    .filter(Route::isReal)
                    .findFirst()
                    .orElse(null);

            if (route == null)
                route = routesSorted.getFirst();

            route.assignUnits();

            routesSorted = routesSorted.stream()
                    .filter(r -> !r.isFull())
                    .toList();
        }
    }

    public void calculateAlphasBetas() {
        for (var supplier : suppliers)
            supplier.setAlpha(null);
        for (var customer : customers)
            customer.setBeta(null);

        for (var supplier : suppliers)
            if (supplier.isFictional()) {
                supplier.setAlpha(0.);
                supplier.propagateAlpha();
            }
    }

    public void calculateDeltas() {
        for (var route : routes)
            route.assignDelta();
    }

    public boolean optimizeCosts() {
        Route maxDeltaRoute = routes.stream()
                .filter(route -> !Double.isNaN(route.getDelta()))
                .sorted(Comparator.comparingDouble(Route::getDelta))
                .toList()
                .reversed()
                .getFirst();

        Supplier supplier1 = maxDeltaRoute.getSupplier();
        for (var route1 : supplier1.getRoutes()) {
            if (!Double.isNaN(route1.getDelta()))
                continue;
            Customer customer1 = route1.getCustomer();
            for (var route2 : customer1.getRoutes()) {
                if (route2 == route1 || !Double.isNaN(route2.getDelta()))
                    continue;
                Supplier supplier2 = route2.getSupplier();
                for (var route3 : supplier2.getRoutes()) {
                    if (route3 == route2 || !Double.isNaN(route3.getDelta()))
                        continue;
                    Customer customer2 = route3.getCustomer();
                    for (var route4 : customer2.getRoutes()) {
                        if (route4 != maxDeltaRoute)
                            continue;
                        double units = Math.min(route1.getUnits(), route2.getUnits());
                        units = Math.min(units, route3.getUnits());
                        if (units == 0)
                            continue;
                        maxDeltaRoute.assignUnits(units);
                        route1.assignUnits(-units);
                        route2.assignUnits(units);
                        route3.assignUnits(-units);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void calculateFinalVariables() {
        for (var route: routes) {
            if (route.isReal() && route.getUnits() > 0) {
                this.totalProfit += (route.getUnits()*route.getTotalRevenue());
                this.totalTransportationCost += (route.getUnits()*route.getTransportCost());
                this.totalPurchaseCost += (route.getUnits()*route.getSupplier().getCost());
                this.totalRevenue += (route.getUnits()*route.getCustomer().getPrice());
            }
        }
    }
}

