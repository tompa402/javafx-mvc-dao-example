package hr.java.vjezbe.javafx.model;

import java.math.BigDecimal;

/**
 * Predstavlja entitet Senzora vlage koji je definiran vrijednošću senzora
 *
 * @author Tomislav
 */
public class SenzorVlage extends Senzor {

    public SenzorVlage(){
        this(0, null, null,null);
    }

    public SenzorVlage(BigDecimal vrijednost, RadSenzora radSenzora) {
        super("%", (byte) 10, vrijednost, radSenzora);
    }

    public SenzorVlage(Integer id, BigDecimal vrijednost, RadSenzora radSenzora, MjernaPostaja postaja) {
        super(id,"%", (byte) 10, vrijednost, radSenzora, postaja);
    }

    public String dohvatiPodatkeSenzora() {
        return "Vrijednost: " + getVrijednost() + " " + getMjernaJedinica() + " vlage zraka, Način rada: " + getRadSenzora().name();
    }
}
