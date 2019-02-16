package hr.java.vjezbe.model;

import hr.java.vjezbe.iznimke.NiskaTemperaturaException;
import hr.java.vjezbe.iznimke.VisokaTemperaturaException;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Predstavlja entitet Senzora temperature koji je definiran nazivom komponente i vrijednošću senzora
 *
 * @author Tomislav
 */
public class SenzorTemperature extends Senzor {

    private String naziv;

    public SenzorTemperature(){
        super("°C", (byte) 2);
    }

    public SenzorTemperature(String naziv, BigDecimal vrijednost, RadSenzora radSenzora, MjernaPostaja postaja) {
        super("°C", (byte) 2, vrijednost, radSenzora, postaja);
        this.naziv = naziv;
    }

    public SenzorTemperature(Integer id, String naziv, BigDecimal vrijednost, RadSenzora radSenzora, MjernaPostaja postaja) {
        super(id, "°C", (byte) 2, vrijednost, radSenzora, postaja);
        this.naziv = naziv;
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
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }
}
