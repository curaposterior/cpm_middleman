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

//    private double[][] totalRevenueMatrix;
//    private double[][] transportMatrix;
//    private double[][] helperMatrix; // krok 3.
//
////    private List<List<Double>> totalRevenueMatrix = new ArrayList<>(); // macierz zyskow jednostkowych
////    private List<List<Double>> transportMatrix = new ArrayList<>();
////
////    private List<List<Double>> helperMatrix = new ArrayList<>();
////
//    private List<Double> supplyList = new ArrayList<>();
//    private List<Double> supplyListCost = new ArrayList<>();
//
//    private List<Double> demandList = new ArrayList<>();
//    private List<Double> demandListCost = new ArrayList<>();
//
//    public static int totalRevenue; // pC - przychody calkowite
//
//    // te 4 wartości to wynik
//    public static int totalProfit; // zC = pC - Kc
//    public static int totalCost; // Koszt całkowity = KT + KZ
//    public static int costsOfTransportation; // KT - koszty transportu
//    public static int purchaseCosts; // KZ - koszty zakupu
//
//    // zmienne kryterialne
//    public int[] alpha;
//    public int[] beta;
//    public double supply = 0;
//    public double demand = 0;
//
//    public int numberOfSuppliers;
//    public int numberOfRecipients;
//    public int numberOfSuppliersWithFictional;
//    public int numberOfRecipientsWithFunctional;
//    public boolean ozt = false;

    @FXML
    void initialize() {
        routes = new ArrayList<>();
    }

    public void calculate() {
        System.out.println(suppliers);
        System.out.println(customers);

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
            double val = Math.abs(demand - supply);
            suppliers.add(new Supplier("SF", val, 0, true));
            customers.add(new Customer("CF", val, 0, true));
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

        // TODO: algos - middleman

        calculateTotalRevenues();
        calculateMaxMatrixElementMethod();
//        if (supply > demand) {
//            // OZT
//            for (int i = 0; i < this.numberOfSuppliers; i++) {
//                for (int j = 0; j < this.numberOfRecipients; j++) {
//                    totalRevenueMatrix[i][j] = this.demandListCost.get(j) - (this.supplyListCost.get(i) + this.transportCostsMatrix[i][j]);
//                    System.out.print(totalRevenueMatrix[i][j] + " ");
//                }
//                System.out.println();
//            }
//
//        }
//        else if (supply == demand) {
//            // ZZT
//            int newNumberOfSuppliers = this.numberOfSuppliers + 1;
//            int newNumberOfCustomers = this.numberOfRecipients + 1;
//
//            for (int i = 0; i < this.numberOfSuppliers; i++) {
//                for (int j = 0; j < this.numberOfRecipients; j++) {
//                    totalRevenueMatrix[i][j] = this.demandListCost.get(j) - (this.supplyListCost.get(i) + this.transportCostsMatrix[i][j]);
//                    System.out.print(totalRevenueMatrix[i][j] + " ");
//                }
//                System.out.println();
//            }
//            // 1. zysk calkowity
//
//
//        }
//        else {
//            throw new IllegalArgumentException("Cannot solve the problem.");
//        }


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
        System.out.println(routesSorted);

        while (!routesSorted.isEmpty()) {
            routesSorted = routesSorted.stream()
                    .filter(route -> route)
            if (this.supplyList.stream().allMatch(num -> num == 0.0) ||
                    this.demandList.stream().allMatch(num -> num == 0.0)) {
                break;
            }

            Map.Entry<Pair, Double> maxEntry = Collections.max(matrixMap.entrySet(), Map.Entry.comparingByValue());
            Pair maxKey = maxEntry.getKey();
            Double maxValue = maxEntry.getValue();

            //cielsko sprawdzania
            // supplyList, demandList
            if (supplyList.get(maxKey.getRow()) == 0.0 &&
                    demandList.get(maxKey.getRow()) == 0.0) {
                matrixMap.remove(maxKey);
                System.out.println("Removing key " + maxKey + " " + maxValue);
                continue;
            }

            double tempSupply = supplyList.get(maxKey.getRow());
            double tempDemand = demandList.get(maxKey.getCol());
            double tempSubstraction = 0.0;
            if (tempSupply >= tempDemand) {
                tempSubstraction = this.supplyList.get(maxKey.getRow()) - tempDemand;
                this.supplyList.set(maxKey.getRow(), tempSubstraction);
                this.demandList.set(maxKey.getCol(), 0.0);
                helperMatrix[maxKey.getRow()][maxKey.getCol()] = tempDemand;
            }
            else {
                tempSubstraction = this.demandList.get(maxKey.getCol()) - tempSupply;
                this.supplyList.set(maxKey.getRow(), 0.0);
                this.demandList.set(maxKey.getCol(), tempSubstraction);
                helperMatrix[maxKey.getRow()][maxKey.getCol()] = tempSupply;
            }
            matrixMap.remove(maxKey);
        }

        this.supplyList.add(this.demand);
        this.demandList.add(this.supply);
        this.supplyListCost.add(0.0);
        this.demandListCost.add(0.0);
        while (!matrixMapFictional.isEmpty()) { // handle fictional
            Map.Entry<Pair, Double> maxEntry = Collections.max(matrixMapFictional.entrySet(), Map.Entry.comparingByValue());
            Pair maxKey = maxEntry.getKey();
            Double maxValue = maxEntry.getValue();

            if (supplyList.get(maxKey.getRow()) == 0.0 ||
                    demandList.get(maxKey.getCol()) == 0.0) {
                matrixMapFictional.remove(maxKey);
                System.out.println("Removing key " + maxKey + " " + maxValue);
                continue;
            }

            double tempSupply = supplyList.get(maxKey.getRow());
            double tempDemand = demandList.get(maxKey.getCol());
            double tempSubstraction = 0.0;
            if (tempSupply >= tempDemand) {
                tempSubstraction = this.supplyList.get(maxKey.getRow()) - tempDemand;
                this.supplyList.set(maxKey.getRow(), tempSubstraction);
                this.demandList.set(maxKey.getCol(), 0.0);
                helperMatrix[maxKey.getRow()][maxKey.getCol()] = tempDemand;
            }
            else {
                tempSubstraction = this.demandList.get(maxKey.getCol()) - tempSupply;
                this.supplyList.set(maxKey.getRow(), 0.0);
                this.demandList.set(maxKey.getCol(), tempSubstraction);
                helperMatrix[maxKey.getRow()][maxKey.getCol()] = tempSupply;
            }
            matrixMap.remove(maxKey);
        }

        System.out.println("\n\nKROK NR 3");
        for (int i = 0; i < helperMatrix.length; i++) {
            for (int j = 0; j < helperMatrix[0].length; j++) {
                System.out.print(helperMatrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void calculateAlphasBetas() {

    }

    public void calculateCriticalVariables() {

    }

    public void calculateOptimalDeltas() {

    }
}

