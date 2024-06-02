package org.middleman.calculation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
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

    @Override
    public String toString() {
        return String.valueOf(totalRevenue);
    }
}
