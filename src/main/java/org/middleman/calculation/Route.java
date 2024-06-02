package org.middleman.calculation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@ToString
public class Route {
    private final Supplier supplier;
    private final Customer customer;
    private final double transportCost;

    @Getter
    private double totalRevenue;
    @Getter
    private double units;

    public void calculateTotalRevenue() {
        totalRevenue = 0;
        if (supplier.isFictional() || customer.isFictional())
            return;
        totalRevenue = customer.getPrice() - (supplier.getCost() + transportCost);
    }

    public boolean isFull() {
        return supplier.getCurrentSupply() == 0 || customer.getCurrentDemand() == 0;
    }

    public void assignUnits() {
        units = Math.min(supplier.getCurrentSupply(), customer.getCurrentDemand());
    }

    public boolean isReal() {
        return !supplier.isFictional() && !customer.isFictional();
    }
}
