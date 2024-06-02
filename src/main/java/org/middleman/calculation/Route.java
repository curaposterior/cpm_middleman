package org.middleman.calculation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@ToString
@Getter
public class Route {
    private final Supplier supplier;
    private final Customer customer;
    private final double transportCost;

    private double totalRevenue;
    private double units;
    private double delta;

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

    public void assignDelta() {
        delta = units == 0 ? totalRevenue - supplier.getAlpha() - customer.getBeta() : Double.NaN;
    }

    public boolean isReal() {
        return !supplier.isFictional() && !customer.isFictional();
    }
}
