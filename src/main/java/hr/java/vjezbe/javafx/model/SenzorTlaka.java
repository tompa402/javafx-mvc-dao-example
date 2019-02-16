package hr.java.vjezbe.javafx.model;

import java.math.BigDecimal;

/**
 * Predstavlja entitet Senzora tlaka koji je definiran vrijednošću senzora
 *
 * @author Tomislav
 */
public class SenzorTlaka extends Senzor{

    public SenzorTlaka(){
        this(0, null, null,null);
    }

    public SenzorTlaka(BigDecimal vrijednost, RadSenzora radSenzora) {
        super("hPa", (byte) 0.2, vrijednost, radSenzora);
    }

    public SenzorTlaka(Integer id, BigDecimal vrijednost, RadSenzora radSenzora, MjernaPostaja postaja) {
        super(id,"hPa", (byte) 0.2, vrijednost, radSenzora, postaja);
    }

    public String dohvatiPodatkeSenzora() {
        return "Vrijednost: " + getVrijednost() + " " + getMjernaJedinica() + " tlaka zraka, Način rada: " + getRadSenzora();
    }
}
