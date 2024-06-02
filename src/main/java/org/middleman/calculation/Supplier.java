package org.middleman.calculation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@ToString
@Getter
public class Supplier {
    private final String name;
    private final double supply;
    private final double cost;
    private final boolean isFictional;
    @ToString.Exclude
    private final List<Route> routes = new ArrayList<>();

    public void addRoute(Route route) {
        routes.add(route);
    }

    @ToString.Include
    public double getCurrentSupply() {
        double val = supply;
        for (var route : routes)
            val -= route.getUnits();
        return val;
    }
}
