package hr.java.vjezbe.javafx.model;

import java.math.BigDecimal;

/**
 * Predstavlja entitet Senzora vlage koji je definiran vrijednošću senzora
 *
 * @author Tomislav
 */
public class SenzorVlage extends Senzor {

    public SenzorVlage() {
        this(0, null, null, null, null);
    }

    public SenzorVlage(Integer id, BigDecimal vrijednost, RadSenzora radSenzora, Boolean active, MjernaPostaja postaja) {
        super(id, "%", 10.0, vrijednost, radSenzora, active, postaja);
    }

    public String dohvatiPodatkeSenzora() {
        return "Vrijednost: " + getVrijednost() + " " + getMjernaJedinica() + " vlage zraka, Način rada: " + getRadSenzora().name();
    }
}
