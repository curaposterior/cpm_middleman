package org.middleman.input;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.Setter;

public class CustomerEntity {
    private final StringProperty nameProperty;
    private final StringProperty demandProperty;
    private final StringProperty priceProperty;

    @Getter
    @Setter
    private int gridIndex;

    public CustomerEntity(String name, String demand, String cost) {
        this.nameProperty = new SimpleStringProperty(name);
        this.demandProperty = new SimpleStringProperty(demand);
        this.priceProperty = new SimpleStringProperty(cost);
    }

    public void setName(String value) {
        nameProperty.set(value);
    }

    public String getName() {
        return nameProperty.get();
    }

    public StringProperty nameProperty() {
        return nameProperty;
    }

    public void setDemand(String value) {
        demandProperty.set(value);
    }

    public String getDemand() {
        return demandProperty.get();
    }

    public StringProperty demandProperty() {
        return demandProperty;
    }

    public void setPrice(String value) {
        priceProperty.set(value);
    }

    public String getPrice() {
        return priceProperty.get();
    }

    public StringProperty priceProperty() {
        return priceProperty;
    }

}
