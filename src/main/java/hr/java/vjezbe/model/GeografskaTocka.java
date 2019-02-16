package hr.java.vjezbe.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.math.BigDecimal;

/**
 * Predstavlja entitet geografske točke koja je definiran koordinatama X i Y
 *
 * @author Tomislav
 */
public class GeografskaTocka extends BazniEntitet {

    private BigDecimal geoX;
    private BigDecimal geoY;

    public GeografskaTocka() {
    }

    /**
     * Inicijalizira podatak o kooridanti X i Y
     *
     * @param geoX podatak o koordinati X
     * @param geoY podatak o koordinati Y
     */
    public GeografskaTocka(BigDecimal geoX, BigDecimal geoY) {
        this.geoX = geoX;
        this.geoY = geoY;
    }

    public GeografskaTocka(int id, BigDecimal geoX, BigDecimal geoY) {
        this.setId(id);
        this.geoX = geoX;
        this.geoY = geoY;
    }

    public BigDecimal getGeoX() {
        return geoX;
    }

    public void setGeoX(BigDecimal geoX) {
        this.geoX = geoX;
    }

    public BigDecimal getGeoY() {
        return geoY;
    }

    public void setGeoY(BigDecimal geoY) {
        this.geoY = geoY;
    }
}
