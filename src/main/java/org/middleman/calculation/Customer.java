package org.middleman.calculation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@ToString
@Getter
public class Customer {
    private final String name;
    private final double demand;
    private final double price;
    private final boolean isFictional;
    @ToString.Exclude
    private final List<Route> routes = new ArrayList<>();
    @Setter
    private Double beta;

    public void addRoute(Route route) {
        routes.add(route);
    }

    @ToString.Include
    public double getCurrentDemand() {
        double val = demand;
        for (var route : routes)
            val -= route.getUnits();
        return val;
    }

    public void propagateBeta() {
        for (var route : routes)
            if (route.getUnits() > 0) {
                Supplier supplier = route.getSupplier();
                Double alpha = supplier.getAlpha();
                if (alpha == null) {
                    supplier.setAlpha(route.getTotalRevenue() - beta);
                    supplier.propagateAlpha();
                }
            }
    }
}
