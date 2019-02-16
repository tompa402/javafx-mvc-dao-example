package hr.java.vjezbe.model;

import java.math.BigDecimal;

/**
 * Predstavlja entitet Senzora tlaka koji je definiran vrijednošću senzora
 *
 * @author Tomislav
 */
public class SenzorTlaka extends Senzor {

    public SenzorTlaka(){
        super("°C", (byte) 2);
    }

    public SenzorTlaka(BigDecimal vrijednost, RadSenzora radSenzora) {
        super("°C", (byte) 2, vrijednost, radSenzora);
    }

    public SenzorTlaka(BigDecimal vrijednost, RadSenzora radSenzora, MjernaPostaja postaja) {
        super("hPa", (byte) 0.2, vrijednost, radSenzora, postaja);
    }

    public SenzorTlaka(Integer id, BigDecimal vrijednost, RadSenzora radSenzora, MjernaPostaja postaja) {
        super(id, "hPa", (byte) 0.2, vrijednost, radSenzora, postaja);
    }

    public String dohvatiPodatkeSenzora() {
        return "Vrijednost: " + getVrijednost() + " " + getMjernaJedinica() + " tlaka zraka, Način rada: " + getRadSenzora();
    }
}
