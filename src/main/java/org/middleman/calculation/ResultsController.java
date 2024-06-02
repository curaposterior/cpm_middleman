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
        this.alpha = new double[this.numberOfSuppliersWithFictional];
        this.beta = new double[this.numberOfRecipientsWithFunctional];

        Arrays.fill(this.alpha, Double.NaN);
        Arrays.fill(this.beta, Double.NaN);

        this.alpha[0] = 0.0;

        for (int nrRow = 0; nrRow < this.helperMatrix.length; nrRow++) {
            for (int nrCol = 0; nrCol < this.helperMatrix[nrRow].length; nrCol++) {
                for (int nrItem = nrRow; nrItem < this.helperMatrix.length; nrItem++) {
                    if (this.helperMatrix[nrItem][nrCol] != 0.0 && !Double.isNaN(this.helperMatrix[nrItem][nrCol])) {
                        if (Double.isNaN(alpha[nrItem]) && !Double.isNaN(beta[nrCol])) {
                            alpha[nrItem] = this.totalRevenueMatrix[nrItem][nrCol] - beta[nrCol];
                        }

                        if (Double.isNaN(beta[nrCol]) &&  !Double.isNaN(alpha[nrItem])) {
                            beta[nrCol] = this.totalRevenueMatrix[nrItem][nrCol] - alpha[nrItem];
                        }
                    }
                }
            }
        }

        System.out.println("ALFY I BETY");
        for (int i = 0; i < alpha.length; i++) {
            System.out.print(alpha[i] + " ");
        }
        System.out.println();
        for (int i = 0; i < beta.length; i++) {
            System.out.print(beta[i] + " ");
        }
        System.out.println();
    }

    public void calculateDeltas() {
        this.deltasMap = new HashMap<>();

        for (int i = 0; i < helperMatrix.length; i++) {
            for (int j = 0; j < helperMatrix[0].length; j++) {
                if (helperMatrix[i][j] == 0.0 || Double.isNaN(helperMatrix[i][j])) {
                    double delta = this.totalRevenueMatrix[i][j] - alpha[i] - beta[j];
                    System.out.println("Delta: " + "("+ i + "," + j + ")" + " - " + delta);
                    this.deltasMap.put(new Pair(i, j), delta);
                }
            }
        }
    }

    public void optimizeCosts() {

    }
}

