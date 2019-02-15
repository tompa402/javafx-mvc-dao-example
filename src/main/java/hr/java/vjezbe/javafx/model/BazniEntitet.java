package hr.java.vjezbe.javafx.model;

import javafx.beans.property.IntegerProperty;

public abstract class BazniEntitet {

    private IntegerProperty id;

    public BazniEntitet() {
        this(null);
    }

    public BazniEntitet(IntegerProperty id) {
        this.id = id;
    }

    public Integer getId() {
        return id.get();
    }

    public void setId(Integer id) {
        this.id.setValue(id);
    }

    public IntegerProperty idProperty(){
        return id;
    }
}
