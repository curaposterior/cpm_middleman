package org.middleman.calculation;

import javafx.fxml.FXML;
import lombok.Setter;

import java.sql.Array;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ResultsController {
    @Setter
    private List<Supplier> suppliers;
    @Setter
    private List<Customer> customers;
    @Setter
    private double[][] transportCostsMatrix; // macierz z kosztami transportu

    private double[][] totalRevenueMatrix;
    private double[][] transportMatrix;
    private double[][] helperMatrix;

//    private List<List<Double>> totalRevenueMatrix = new ArrayList<>(); // macierz zyskow jednostkowych
//    private List<List<Double>> transportMatrix = new ArrayList<>();
//
//    private List<List<Double>> helperMatrix = new ArrayList<>();
//
    private List<Double> supplyList = new ArrayList<>();
    private List<Double> supplyListCost = new ArrayList<>();

    private List<Double> demandList = new ArrayList<>();
    private List<Double> demandListCost = new ArrayList<>();

    public static int totalRevenue; // pC - przychody calkowite

    // te 4 wartości to wynik
    public static int totalProfit; // zC = pC - Kc
    public static int totalCost; // Koszt całkowity = KT + KZ
    public static int costsOfTransportation; // KT - koszty transportu
    public static int purchaseCosts; // KZ - koszty zakupu

    // zmienne kryterialne
    public int[] alpha;
    public int[] beta;
    public double supply = 0;
    public double demand = 0;

    public int numberOfSuppliers;
    public int numberOfRecipients;
    public int numberOfSuppliersWithFictional;
    public int numberOfRecipientsWithFunctional;
    public boolean ozt = false;
    @FXML
    void initialize() {

    }

    public void calculate() {
        // TODO: jak ich posortować?
        System.out.println(suppliers);
        System.out.println(customers);

        for (var row : transportCostsMatrix) {
            System.out.println(Arrays.toString(row));
        }

        this.numberOfRecipients = this.customers.size();
        this.numberOfSuppliers = this.suppliers.size();
        this.numberOfSuppliersWithFictional = this.numberOfSuppliers + 1;
        this.numberOfRecipientsWithFunctional = this.numberOfRecipients + 1;

        for (var elem: suppliers) {
            this.supplyList.add(elem.getSupply());
            this.supplyListCost.add(elem.getCost());
        }
        for (var elem: customers) {
            this.demandList.add(elem.getDemand());
            this.demandListCost.add(elem.getPrice());
        }
        for (var row : customers) {
            demand += row.getDemand();
        }
        for (var row : suppliers) {
            supply += row.getSupply();
        }
        if (supply > demand || supply < demand) {
            this.totalRevenueMatrix = new double[numberOfSuppliers+1][numberOfRecipients+1];
            this.transportMatrix = new double[numberOfSuppliers+1][numberOfRecipients+1];
            this.helperMatrix = new double[numberOfSuppliers+1][numberOfRecipients+1];
            this.ozt = true;

        }
        else if (supply == demand) {
            this.totalRevenueMatrix = new double[numberOfSuppliers][numberOfRecipients];
            this.transportMatrix = new double[numberOfSuppliers][numberOfRecipients];
            this.helperMatrix = new double[numberOfSuppliers][numberOfRecipients];
        }

        for (int i = 0; i < this.numberOfSuppliersWithFictional; i++) {
            for (int j = 0; j < this.numberOfRecipientsWithFunctional; j++) {
                this.totalRevenueMatrix[i][j] = 0.0;
                this.helperMatrix[i][j] = Double.NaN;
                this.transportMatrix[i][j] = 0.0;
            }
            System.out.println();
        }

        // TODO: algos - middleman

        // sprawdzenie czy ZZT czy OZT
        int newNumberOfSuppliers = this.numberOfSuppliers + 1;
        int newNumberOfCustomers = this.numberOfRecipients + 1;

        this.calculateZyskiCalkowite();
        this.calculateMaxMatrixElementMethod();
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
    public void calculateZyskiCalkowite() {
        for (int i = 0; i < this.numberOfSuppliers; i++) {
            for (int j = 0; j < this.numberOfRecipients; j++) {
                totalRevenueMatrix[i][j] = this.demandListCost.get(j) - (this.supplyListCost.get(i) + this.transportCostsMatrix[i][j]);
                System.out.print(totalRevenueMatrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void calculateMaxMatrixElementMethod() {
        Map<Pair, Double> matrixMap = new HashMap<>();
        Map<Pair, Double> matrixMapFictional = new HashMap<>();
        for (int i = 0; i < totalRevenueMatrix.length; i++) {
            for (int j = 0; j < totalRevenueMatrix[i].length; j++) {
                if (i == totalRevenueMatrix.length - 1 || j == totalRevenueMatrix[i].length - 1) {
                    matrixMapFictional.put(new Pair(i, j), totalRevenueMatrix[i][j]);
                }
                else {
                    matrixMap.put(new Pair(i, j), totalRevenueMatrix[i][j]);
                }



            }
        }
        System.out.println("Suplly all: " + this.supply + ", demand all: " + this.demand);
//        this.supplyList.add(0.0);
//        this.demandList.add(0.0);
//        this.supplyListCost.add(0.0);
//        this.demandListCost.add(0.0);
//        System.out.println(matrixMap);
        System.out.println("Before");
        System.out.println(this.supplyList.toString());
        System.out.println(this.demandList.toString());
        System.out.println("###");
        while (!matrixMap.isEmpty()) {
            if (this.supplyList.stream().allMatch(num -> num == 0.0) ||
                    this.demandList.stream().allMatch(num -> num == 0.0)) {
                break;
            }

            Map.Entry<Pair, Double> maxEntry = Collections.max(matrixMap.entrySet(), Map.Entry.comparingByValue());
            Pair maxKey = maxEntry.getKey();
            Double maxValue = maxEntry.getValue();

            //cielsko sprawdzania
            // supplyList, demandList
            if (supplyList.get(maxKey.getRow()) == 0.0 ||
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
                System.out.println("Substracting: " + "(" + maxKey.getRow() + "," + maxKey.getCol() + ") - ");
                System.out.println(this.supplyList.toString());
                System.out.println(this.demandList.toString());
            }
            else {
                tempSubstraction = this.demandList.get(maxKey.getCol()) - tempSupply;
                this.supplyList.set(maxKey.getRow(), 0.0);
                this.demandList.set(maxKey.getCol(), tempSubstraction);
                helperMatrix[maxKey.getRow()][maxKey.getCol()] = tempSupply;

            }
            matrixMap.remove(maxKey);
        }

//        this.supplyList.add(this.demand);
//        this.demandList.add(this.supply);
//        this.supplyListCost.add(0.0);
//        this.demandListCost.add(0.0);
//        while (!matrixMapFictional.isEmpty()) { // handle fictional
//            Map.Entry<Pair, Double> maxEntry = Collections.max(matrixMapFictional.entrySet(), Map.Entry.comparingByValue());
//            Pair maxKey = maxEntry.getKey();
//            Double maxValue = maxEntry.getValue();
//
//            if (supplyList.get(maxKey.getRow()) == 0.0 ||
//                    demandList.get(maxKey.getRow()) == 0.0) {
//                matrixMap.remove(maxKey);
//                continue;
//            }
//            double tempSupply = supplyList.get(maxKey.getRow());
//            double tempDemand = demandList.get(maxKey.getCol());
//            double tempSubstraction = 0.0;
//            if (tempSupply >= tempDemand) {
//                tempSubstraction = this.supplyList.get(maxKey.getRow()) - tempDemand;
//                this.supplyList.set(maxKey.getRow(), tempSubstraction);
//                this.demandList.set(maxKey.getCol(), 0.0);
//                helperMatrix[maxKey.getRow()][maxKey.getCol()] = tempDemand;
//            }
//            else {
//                tempSubstraction = this.demandList.get(maxKey.getCol()) - tempSupply;
//                this.supplyList.set(maxKey.getRow(), 0.0);
//                this.demandList.set(maxKey.getCol(), tempSubstraction);
//                helperMatrix[maxKey.getRow()][maxKey.getCol()] = tempSupply;
//            }
//            matrixMap.remove(maxKey);
//        }

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

