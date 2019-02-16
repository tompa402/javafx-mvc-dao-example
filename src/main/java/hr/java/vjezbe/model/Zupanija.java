package hr.java.vjezbe.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Predstavlja entitet županije koji je definiran nazivom i državom
 *
 * @author Tomislav
 */
public class Zupanija extends BazniEntitet {

    private String naziv;
    private Drzava drzava;
    private List<Mjesto> mjesta;

    public Zupanija(String naziv, Drzava drzava) {
        this.naziv = naziv;
        this.drzava = drzava;
        this.mjesta = new ArrayList<>();
    }

    /**
     * Inicijalizira podatak o identifikatoru, nazivu i državi kojoj pripada
     *
     * @param id     podatak o identifikatoru
     * @param naziv  podatak o nazivu
     * @param drzava podatak o državi
     */
    public Zupanija(int id, String naziv, Drzava drzava) {
        this.setId(id);
        this.naziv = naziv;
        this.drzava = drzava;
        this.mjesta = new ArrayList<>();
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public Drzava getDrzava() {
        return drzava;
    }

    public void setDrzava(Drzava drzava) {
        this.drzava = drzava;
    }

    public List<Mjesto> getMjesta() {
        return mjesta;
    }

    public void setMjesta(List<Mjesto> mjesta) {
        this.mjesta = mjesta;
    }
}
