package hr.java.vjezbe.javafx.model;

import java.math.BigDecimal;

/**
 * Predstavlja entitet Senzora tlaka koji je definiran vrijednošću senzora
 *
 * @author Tomislav
 */
public class SenzorTlaka extends Senzor {

    public SenzorTlaka() {
        this(0, null, null, null, null);
    }

    public SenzorTlaka(Integer id, BigDecimal vrijednost, RadSenzora radSenzora, Boolean active, MjernaPostaja postaja) {
        super(id, "hPa", 0.2, vrijednost, radSenzora, active, postaja);
    }

    public String dohvatiPodatkeSenzora() {
        return "Vrijednost: " + getVrijednost() + " " + getMjernaJedinica() + " tlaka zraka, Način rada: " + getRadSenzora();
    }
}
