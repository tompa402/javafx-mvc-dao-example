package hr.java.vjezbe.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Predstavlja entitet države koja je definirana nazivom i površinom.
 *
 * @author Tomislav
 */
public class Drzava extends BazniEntitet {

    private String naziv;
    private BigDecimal povrsina;
    private List<Zupanija> zupanije;

    public Drzava() {
    }

    /**
     * Inicijalizira podatake o novo kreiranoj državi.
     */
    public Drzava(String naziv, BigDecimal povrsina) {
        this.naziv = naziv;
        this.povrsina = povrsina;
        this.zupanije = new ArrayList<>();
    }

    /**
     * Inicijalizira podatake o identifikatoru, nazivu i površini države.
     *
     * @param id       podatak o identifikatoru države
     * @param naziv    podatak o nazivu države
     * @param povrsina podatak o pvršini države
     */
    public Drzava(Integer id, String naziv, BigDecimal povrsina) {
        this.setId(id);
        this.naziv = naziv;
        this.povrsina = povrsina;
        this.zupanije = new ArrayList<>();
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public BigDecimal getPovrsina() {
        return povrsina;
    }

    public void setPovrsina(BigDecimal povrsina) {
        this.povrsina = povrsina;
    }

    public List<Zupanija> getZupanije() {
        return zupanije;
    }

    public void setZupanije(List<Zupanija> zupanije) {
        this.zupanije = zupanije;
    }

    @Override
    public String toString() {
        return "Drzava{" +
                "naziv=" + naziv +
                ", povrsina=" + povrsina +
                ", zupanije=" + zupanije +
                '}';
    }
}
