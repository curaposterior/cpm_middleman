package org.middleman.calculation;

import javafx.fxml.FXML;
import lombok.Setter;

import java.util.*;

public class ResultsController {
    @Setter
    private List<Supplier> suppliers;
    @Setter
    private List<Customer> customers;
    @Setter
    private double[][] transportCostsMatrix; // macierz z kosztami transportu

    private List<Route> routes;

    @FXML
    void initialize() {
        routes = new ArrayList<>();
    }

    public void calculate() {
        for (var row : transportCostsMatrix) {
            System.out.println(Arrays.toString(row));
        }

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

    }

    public void calculateCriticalVariables() {

    }

    public void calculateOptimalDeltas() {

    }
}

