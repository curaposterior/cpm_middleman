package org.middleman.input;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.Setter;

public class SupplierEntity {
    private final StringProperty nameProperty;
    private final StringProperty supplyProperty;
    private final StringProperty costProperty;

    @Getter
    @Setter
    private int gridIndex;

    public SupplierEntity(String name, String supply, String price) {
        this.nameProperty = new SimpleStringProperty(name);
        this.supplyProperty = new SimpleStringProperty(supply);
        this.costProperty = new SimpleStringProperty(price);
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

    public void setSupply(String value) {
        supplyProperty.set(value);
    }

    public String getSupply() {
        return supplyProperty.get();
    }

    public StringProperty supplyProperty() {
        return supplyProperty;
    }

    public void setCost(String value) {
        costProperty.set(value);
    }

    public String getCost() {
        return costProperty.get();
    }

    public StringProperty costProperty() {
        return costProperty;
    }
}
