package hr.java.vjezbe.javafx.model;

import hr.java.vjezbe.javafx.iznimke.NiskaTemperaturaException;
import hr.java.vjezbe.javafx.iznimke.VisokaTemperaturaException;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Predstavlja entitet Senzora temperature koji je definiran nazivom komponente i vrijednošću senzora
 *
 * @author Tomislav
 */
public class SenzorTemperature extends Senzor {

    private StringProperty naziv;

    public SenzorTemperature() {
        this(0, null, null, null, null, null);
    }

    public SenzorTemperature(Integer id, String naziv, BigDecimal vrijednost, RadSenzora radSenzora, Boolean active, MjernaPostaja postaja) {
        super(id, "°C", 2.0, vrijednost, radSenzora, active, postaja);
        this.naziv = new SimpleStringProperty(naziv);
    }

    public String dohvatiPodatkeSenzora() {
        return "Komponenta: " + naziv + ", vrijednost: " + getVrijednost() + " " + getMjernaJedinica() + ", Način rada: " + getRadSenzora();
    }

    public void generirajVrijednost() throws VisokaTemperaturaException, NiskaTemperaturaException {
        int randomNum = ThreadLocalRandom.current().nextInt(-50, 51);
        setVrijednost(new BigDecimal(randomNum));

        if (getVrijednost().compareTo(new BigDecimal("40")) > 0) {
            throw new VisokaTemperaturaException("Temperatura od " + getVrijednost() + getMjernaJedinica() + " je previsoka");
        } else if (getVrijednost().compareTo(new BigDecimal("-10")) < 0) {
            throw new NiskaTemperaturaException("Temperatura od " + getVrijednost() + getMjernaJedinica() + " je preniska");
        }
    }

    public String getNaziv() {
        return naziv.get();
    }

    public StringProperty nazivProperty() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv.set(naziv);
    }
}
