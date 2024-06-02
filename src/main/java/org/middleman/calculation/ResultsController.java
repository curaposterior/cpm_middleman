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
            if (!flag)
                break;
            optimizeCosts();
        }
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
        for (var suplier : suppliers)
            suplier.setAlpha(null);
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

    public void optimizeCosts() {
        Route maxDeltaRoute = routes.stream()
                .filter(route -> !Double.isNaN(route.getDelta()))
                .sorted(Comparator.comparingDouble(Route::getDelta))
                .toList()
                .reversed()
                .getFirst();

        Supplier supplier1 = maxDeltaRoute.getSupplier();
        for (var route1 : supplier1.getRoutes()) {
            if (!Double.isNaN(route1.getDelta())) {
                Customer customer1 = route1.getCustomer();
                for (var route2 : customer1.getRoutes()) {
                    if (route2 == route1)
                        continue;
                    Supplier supplier2 = route2.getSupplier();
                    for (var route3 : supplier2.getRoutes()) {
                        if (route3 == route2)
                            continue;
                        Customer customer2 = route3.getCustomer();
                        for (var route4 : customer2.getRoutes())
                            if (route4 == maxDeltaRoute) {
                                double units = Math.min(route1.getUnits(), route2.getUnits());
                                units = Math.min(units, route3.getUnits());
                                maxDeltaRoute.assignUnits(units);
                                route1.assignUnits(-units);
                                route2.assignUnits(units);
                                route3.assignUnits(-units);
                            }
                    }
                }
            }
        }
    }
}

