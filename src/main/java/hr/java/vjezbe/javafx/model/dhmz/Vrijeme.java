package hr.java.vjezbe.javafx.model.dhmz;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "Hrvatska")
@XmlAccessorType(XmlAccessType.FIELD)
public class Vrijeme {

    @XmlElement(name = "DatumTermin")
    private DatumTermin datumTermin;
    @XmlElement(name = "Grad")
    private List<Grad> grad;

    public DatumTermin getDatumTermin() {
        return datumTermin;
    }

    public void setDatumTermin(DatumTermin datumTermin) {
        this.datumTermin = datumTermin;
    }

    public List<Grad> getGrad() {
        return grad;
    }

    public void setGrad(List<Grad> grad) {
        this.grad = grad;
    }
}
