package hr.java.vjezbe.javafx.model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

/**
 * Predstavlja entitet mjerne postaje koji je definiran nazivom, mjestom i koordinatama gdje se nalazi te pripadajucim
 * senzorima
 *
 * @author Tomislav
 */
public class MjernaPostaja extends BazniEntitet {

    private StringProperty naziv;
    private ObjectProperty<Mjesto> mjesto;
    private ObjectProperty<GeografskaTocka> geografskaTocka;
    private ListProperty<Senzor> senzori;

    public MjernaPostaja() {
        this(0, null, null, null);
    }

    /**
     * Inicijalizira podatak o identifikatoru nazivu, mjestu te geografskoj točci mjerne postaje
     *
     * @param id              podatak o identifikatoru postaje
     * @param naziv           podatak o nazivu
     * @param mjesto          podatak o mjestu
     * @param geografskaTocka podatak o koordinatama
     */
    public MjernaPostaja(Integer id, String naziv, Mjesto mjesto, GeografskaTocka geografskaTocka) {
        super(new SimpleIntegerProperty(id));
        this.naziv = new SimpleStringProperty(naziv);
        this.mjesto = new SimpleObjectProperty<>(mjesto);
        this.geografskaTocka = new SimpleObjectProperty<>(geografskaTocka);
        ObservableList<Senzor> observableList = FXCollections.observableArrayList(new ArrayList<>());
        this.senzori = new SimpleListProperty<>(observableList);
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

    public Mjesto getMjesto() {
        return mjesto.get();
    }

    public ObjectProperty<Mjesto> mjestoProperty() {
        return mjesto;
    }

    public void setMjesto(Mjesto mjesto) {
        this.mjesto.set(mjesto);
    }

    public GeografskaTocka getGeografskaTocka() {
        return geografskaTocka.get();
    }

    public ObjectProperty<GeografskaTocka> geografskaTockaProperty() {
        return geografskaTocka;
    }

    public void setGeografskaTocka(GeografskaTocka geografskaTocka) {
        this.geografskaTocka.set(geografskaTocka);
    }

    public ObservableList<Senzor> getSenzori() {
        return senzori.get();
    }

    public ListProperty<Senzor> senzoriProperty() {
        return senzori;
    }

    public void setSenzori(ObservableList<Senzor> senzori) {
        this.senzori.set(senzori);
    }
}
