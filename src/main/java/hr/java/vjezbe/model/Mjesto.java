package hr.java.vjezbe.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Predstavlja entitet mjesta koji je definiran nazivom i pripadajućom županijom
 *
 * @author Tomislav
 */
public class Mjesto extends BazniEntitet {

    private String naziv;
    private Zupanija zupanija;
    private VrstaMjesta vrstaMjesta;
    private List<MjernaPostaja> mjernePostaje;
    private Opcina opcina;

    public Mjesto() {
    }

    public Mjesto(String naziv, Zupanija zupanija, VrstaMjesta vrstaMjesta) {
        this.naziv = naziv;
        this.zupanija = zupanija;
        this.vrstaMjesta = vrstaMjesta;
        this.mjernePostaje = new ArrayList<>();
    }

    /**
     * Inicijalizira podatak o identifikatoru, nazivu, pripadajućoj županiji i vrsti mjesta
     *
     * @param id          podatak o identifikatoru
     * @param naziv       podatak o nazivu
     * @param zupanija    podatak o županiji
     * @param vrstaMjesta podatak o vrsti mjesta
     */
    public Mjesto(int id, String naziv, Zupanija zupanija, VrstaMjesta vrstaMjesta) {
        super.setId(id);
        this.naziv = naziv;
        this.zupanija = zupanija;
        this.vrstaMjesta = vrstaMjesta;
        this.mjernePostaje = new ArrayList<>();
    }

    public Mjesto(int id, String naziv, Zupanija zupanija, VrstaMjesta vrstaMjesta, Opcina opcina) {
        super.setId(id);
        this.naziv = naziv;
        this.zupanija = zupanija;
        this.vrstaMjesta = vrstaMjesta;
        this.mjernePostaje = new ArrayList<>();
        this.opcina = opcina;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public Zupanija getZupanija() {
        return zupanija;
    }

    public void setZupanija(Zupanija zupanija) {
        this.zupanija = zupanija;
    }

    public VrstaMjesta getVrstaMjesta() {
        return vrstaMjesta;
    }

    public void setVrstaMjesta(VrstaMjesta vrstaMjesta) {
        this.vrstaMjesta = vrstaMjesta;
    }

    public List<MjernaPostaja> getMjernePostaje() {
        return mjernePostaje;
    }

    public void setMjernePostaje(List<MjernaPostaja> mjernePostaje) {
        this.mjernePostaje = mjernePostaje;
    }

    public Opcina getOpcina() {
        return opcina;
    }

    public void setOpcina(Opcina opcina) {
        this.opcina = opcina;
    }

    @Override
    public String toString() {
        return "Mjesto{" +
                "naziv='" + naziv + '\'' +
                ", zupanija=" + zupanija +
                ", vrstaMjesta=" + vrstaMjesta +
                ", mjernePostaje=" + mjernePostaje +
                ", opcina=" + opcina +
                '}';
    }
}
