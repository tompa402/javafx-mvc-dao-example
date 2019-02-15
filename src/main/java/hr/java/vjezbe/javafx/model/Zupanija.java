package hr.java.vjezbe.javafx.model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

/**
 * Predstavlja entitet županije koji je definiran nazivom i državom
 *
 * @author Tomislav
 */
public class Zupanija extends BazniEntitet {

    private StringProperty naziv;
    private ObjectProperty<Drzava> drzava;
    private ListProperty<Mjesto> mjesta;

    public Zupanija() {
        this(0, null, null);
    }

    /**
     * Inicijalizira podatak o identifikatoru, nazivu i državi kojoj pripada
     *
     * @param id     podatak o identifikatoru
     * @param naziv  podatak o nazivu
     * @param drzava podatak o državi
     */
    public Zupanija(Integer id, String naziv, Drzava drzava) {
        super(new SimpleIntegerProperty(id));
        this.naziv = new SimpleStringProperty(naziv);
        this.drzava = new SimpleObjectProperty<>(drzava);
        ObservableList<Mjesto> observableList = FXCollections.observableArrayList(new ArrayList<>());
        this.mjesta = new SimpleListProperty<>(observableList);
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

    public Drzava getDrzava() {
        return drzava.get();
    }

    public ObjectProperty<Drzava> drzavaProperty() {
        return drzava;
    }

    public void setDrzava(Drzava drzava) {
        this.drzava.set(drzava);
    }

    public ObservableList<Mjesto> getMjesta() {
        return mjesta.get();
    }

    public ListProperty<Mjesto> mjestaProperty() {
        return mjesta;
    }

    public void setMjesta(ObservableList<Mjesto> mjesta) {
        this.mjesta.set(mjesta);
    }
}
