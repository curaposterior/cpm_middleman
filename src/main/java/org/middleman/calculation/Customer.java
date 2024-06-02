package org.middleman.calculation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
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
    private List<Route> routes;

    public void addRoute(Route route) {
        if (routes == null)
            routes = new ArrayList<>();
        routes.add(route);
    }

    public double getCurrentDemand() {
        double val = demand;
        for (var route : routes)
            val -= route.getUnits();
        return val;
    }
}
