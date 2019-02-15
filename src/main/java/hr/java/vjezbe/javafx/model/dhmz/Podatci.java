package hr.java.vjezbe.javafx.model.dhmz;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

@XmlRootElement(name = "Podatci")
@XmlAccessorType(XmlAccessType.FIELD)
public class Podatci {

    @XmlElement(name = "Temp")
    private BigDecimal temp;
    @XmlElement(name = "Vlaga")
    private String vlaga;
    @XmlElement(name = "Tlak")
    private String tlak;
    @XmlElement(name = "TlakTend")
    private String vlakTend;
    @XmlElement(name = "VjetarSmjer")
    private String vjetarSmjer;
    @XmlElement(name = "VjetarBrzina")
    private String vjetarBrzina;
    @XmlElement(name = "Vrijeme")
    private String vrijeme;
    @XmlElement(name = "VrijemeZnak")
    private String vrijemeZnak;

    public BigDecimal getTemp() {
        return temp;
    }

    public void setTemp(BigDecimal temp) {
        this.temp = temp;
    }

    public String getVlaga() {
        return vlaga;
    }

    public void setVlaga(String vlaga) {
        this.vlaga = vlaga;
    }

    public String getTlak() {
        return tlak;
    }

    public void setTlak(String tlak) {
        this.tlak = tlak;
    }

    public String getVlakTend() {
        return vlakTend;
    }

    public void setVlakTend(String vlakTend) {
        this.vlakTend = vlakTend;
    }

    public String getVjetarSmjer() {
        return vjetarSmjer;
    }

    public void setVjetarSmjer(String vjetarSmjer) {
        this.vjetarSmjer = vjetarSmjer;
    }

    public String getVjetarBrzina() {
        return vjetarBrzina;
    }

    public void setVjetarBrzina(String vjetarBrzina) {
        this.vjetarBrzina = vjetarBrzina;
    }

    public String getVrijeme() {
        return vrijeme;
    }

    public void setVrijeme(String vrijeme) {
        this.vrijeme = vrijeme;
    }

    public String getVrijemeZnak() {
        return vrijemeZnak;
    }

    public void setVrijemeZnak(String vrijemeZnak) {
        this.vrijemeZnak = vrijemeZnak;
    }
}
