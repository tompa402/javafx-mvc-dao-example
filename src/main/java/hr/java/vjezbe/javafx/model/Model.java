package hr.java.vjezbe.javafx.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Model {
    private BooleanProperty toggleValue;

    public Model(boolean toggleValue) {
        this.toggleValue = new SimpleBooleanProperty(toggleValue);
    }

    public boolean getToggleValue() {
        return toggleValue.get();
    }

    public BooleanProperty toggleValueProperty() {
        return toggleValue;
    }

    public void setToggleValue(boolean toggleValue) {
        this.toggleValue.set(toggleValue);
    }
}
