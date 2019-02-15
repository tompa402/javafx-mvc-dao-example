package hr.java.vjezbe.javafx.model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

/**
 * Predstavlja entitet mjesta koji je definiran nazivom i pripadajućom županijom
 *
 * @author Tomislav
 */
public class Mjesto extends BazniEntitet {

    private StringProperty naziv;
    private ObjectProperty<Zupanija> zupanija;
    private ObjectProperty<VrstaMjesta> vrstaMjesta;
    private ListProperty<MjernaPostaja> mjernePostaje;

    public Mjesto() {
        this(0, null, null, null);
    }

    /**
     * Inicijalizira podatak o identifikatoru, nazivu, pripadajućoj županiji i vrsti mjesta
     *
     * @param id          podatak o identifikatoru
     * @param naziv       podatak o nazivu
     * @param zupanija    podatak o županiji
     * @param vrstaMjesta podatak o vrsti mjesta
     */
    public Mjesto(Integer id, String naziv, Zupanija zupanija, VrstaMjesta vrstaMjesta) {
        super(new SimpleIntegerProperty(id));
        this.naziv = new SimpleStringProperty(naziv);
        this.zupanija = new SimpleObjectProperty<>(zupanija);
        this.vrstaMjesta = new SimpleObjectProperty<>(vrstaMjesta);
        ObservableList<MjernaPostaja> observableList = FXCollections.observableArrayList(new ArrayList<>());
        this.mjernePostaje = new SimpleListProperty<>(observableList);
    }

    public String getNaziv() {
        return naziv.get();
    }

    public StringProperty nazivProperty() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv.set(naziv);
    }

    public Zupanija getZupanija() {
        return zupanija.get();
    }

    public ObjectProperty<Zupanija> zupanijaProperty() {
        return zupanija;
    }

    public void setZupanija(Zupanija zupanija) {
        this.zupanija.set(zupanija);
    }

    public VrstaMjesta getVrstaMjesta() {
        return vrstaMjesta.get();
    }

    public ObjectProperty<VrstaMjesta> vrstaMjestaProperty() {
        return vrstaMjesta;
    }

    public void setVrstaMjesta(VrstaMjesta vrstaMjesta) {
        this.vrstaMjesta.set(vrstaMjesta);
    }

    public ObservableList<MjernaPostaja> getMjernePostaje() {
        return mjernePostaje.get();
    }

    public ListProperty<MjernaPostaja> mjernePostajeProperty() {
        return mjernePostaje;
    }

    public void setMjernePostaje(ObservableList<MjernaPostaja> mjernePostaje) {
        this.mjernePostaje.set(mjernePostaje);
    }
}
