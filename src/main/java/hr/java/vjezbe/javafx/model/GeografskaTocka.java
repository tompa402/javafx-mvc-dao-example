package hr.java.vjezbe.javafx.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.math.BigDecimal;

/**
 * Predstavlja entitet geografske točke koja je definiran koordinatama X i Y
 *
 * @author Tomislav
 */
public class GeografskaTocka extends BazniEntitet {

    private ObjectProperty<BigDecimal> geoX;
    private ObjectProperty<BigDecimal> geoY;

    /**
     * Inicijalizira podatak o kooridanti X i Y
     *
     * @param geoX podatak o koordinati X
     * @param geoY podatak o koordinati Y
     */
    public GeografskaTocka(BigDecimal geoX, BigDecimal geoY) {
        this.geoX = new SimpleObjectProperty<>(geoX);
        this.geoY = new SimpleObjectProperty<>(geoY);
    }

    public BigDecimal getGeoX() {
        return geoX.get();
    }

    public ObjectProperty<BigDecimal> geoXProperty() {
        return geoX;
    }

    public void setGeoX(BigDecimal geoX) {
        this.geoX.set(geoX);
    }

    public BigDecimal getGeoY() {
        return geoY.get();
    }

    public ObjectProperty<BigDecimal> geoYProperty() {
        return geoY;
    }

    public void setGeoY(BigDecimal geoY) {
        this.geoY.set(geoY);
    }
}
