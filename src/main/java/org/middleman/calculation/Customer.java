package org.middleman.calculation;

import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class Customer {
    private String name;
    private double demand;
    private double price;
}
