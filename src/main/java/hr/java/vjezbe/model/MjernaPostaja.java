package hr.java.vjezbe.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Predstavlja entitet mjerne postaje koji je definiran nazivom, mjestom i koordinatama gdje se nalazi te pripadajucim
 * senzorima
 *
 * @author Tomislav
 */
public class MjernaPostaja extends BazniEntitet {

    private String naziv;
    private Mjesto mjesto;
    private GeografskaTocka geografskaTocka;
    private List<Senzor> senzori = new ArrayList<>();

    public MjernaPostaja() {
    }

    public MjernaPostaja(String naziv, Mjesto mjesto, GeografskaTocka geografskaTocka) {
        this.naziv = naziv;
        this.mjesto = mjesto;
        this.geografskaTocka = geografskaTocka;
    }

    public MjernaPostaja(String naziv, Mjesto mjesto, GeografskaTocka geografskaTocka, List<Senzor> senzori) {
        this.naziv = naziv;
        this.mjesto = mjesto;
        this.geografskaTocka = geografskaTocka;
        this.senzori = senzori;
    }

    /**
     * Inicijalizira podatak o identifikatoru nazivu, mjestu te geografskoj točci mjerne postaje
     *
     * @param id              podatak o identifikatoru postaje
     * @param naziv           podatak o nazivu
     * @param mjesto          podatak o mjestu
     * @param geografskaTocka podatak o koordinatama
     */
    public MjernaPostaja(int id, String naziv, Mjesto mjesto, GeografskaTocka geografskaTocka, List<Senzor> senzori) {
        this.setId(id);
        this.naziv = naziv;
        this.mjesto = mjesto;
        this.geografskaTocka = geografskaTocka;
        this.senzori = senzori;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public Mjesto getMjesto() {
        return mjesto;
    }

    public void setMjesto(Mjesto mjesto) {
        this.mjesto = mjesto;
    }

    public GeografskaTocka getGeografskaTocka() {
        return geografskaTocka;
    }

    public void setGeografskaTocka(GeografskaTocka geografskaTocka) {
        this.geografskaTocka = geografskaTocka;
    }

    public List<Senzor> getSenzori() {
        return senzori;
    }

    public void setSenzori(List<Senzor> senzori) {
        this.senzori = senzori;
    }
}
