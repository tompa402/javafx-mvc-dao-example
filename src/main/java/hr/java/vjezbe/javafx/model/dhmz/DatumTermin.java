package hr.java.vjezbe.javafx.model.dhmz;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "DatumTermin")
@XmlAccessorType(XmlAccessType.FIELD)
public class DatumTermin {

    @XmlElement(name = "Datum")
    private String datum;
    @XmlElement(name = "Termin")
    private Integer termin;

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public Integer getTermin() {
        return termin;
    }

    public void setTermin(Integer termin) {
        this.termin = termin;
    }
}
