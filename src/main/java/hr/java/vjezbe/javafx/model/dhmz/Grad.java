package hr.java.vjezbe.javafx.model.dhmz;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

@XmlRootElement(name = "Grad")
@XmlAccessorType(XmlAccessType.FIELD)
public class Grad {

    @XmlElement(name = "GradIme")
    private String naziv;
    @XmlElement(name = "Lat")
    private BigDecimal lat;
    @XmlElement(name = "Lon")
    private BigDecimal lon;
    @XmlElement(name = "Podatci")
    private Podatci podatci;

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    public BigDecimal getLon() {
        return lon;
    }

    public void setLon(BigDecimal lon) {
        this.lon = lon;
    }

    public Podatci getPodatci() {
        return podatci;
    }

    public void setPodatci(Podatci podatci) {
        this.podatci = podatci;
    }
}
