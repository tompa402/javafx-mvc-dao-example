package hr.java.vjezbe.model;

import java.math.BigDecimal;

/**
 * Predstavlja entitet Senzora vlage koji je definiran vrijednošću senzora
 *
 * @author Tomislav
 */
public class SenzorVlage extends Senzor {

    public SenzorVlage(){
        super("°C", (byte) 2);
    }

    public SenzorVlage(BigDecimal vrijednost, RadSenzora radSenzora, MjernaPostaja postaja) {
        super("%", (byte) 10, vrijednost, radSenzora,postaja);
    }

    public SenzorVlage(Integer id, BigDecimal vrijednost, RadSenzora radSenzora, MjernaPostaja postaja) {
        super(id, "%", (byte) 10, vrijednost, radSenzora, postaja);
    }

    public String dohvatiPodatkeSenzora() {
        return "Vrijednost: " + getVrijednost() + " " + getMjernaJedinica() + " vlage zraka, Način rada: " + getRadSenzora().name();
    }
}
