package hr.java.vjezbe.javafx.model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Predstavlja entitet države koja je definirana nazivom i površinom.
 *
 * @author Tomislav
 */
public class Drzava extends BazniEntitet {

    private StringProperty naziv;
    private ObjectProperty<BigDecimal> povrsina;
    private ListProperty<Zupanija> zupanije;

    /**
     * Inicijalizira podatake o novo kreiranoj državi.
     */
    public Drzava() {
        this(0, null, BigDecimal.ZERO);
    }

    /**
     * Inicijalizira podatake o identifikatoru, nazivu i površini države.
     *
     * @param id       podatak o identifikatoru države
     * @param naziv    podatak o nazivu države
     * @param povrsina podatak o pvršini države
     */
    public Drzava(Integer id, String naziv, BigDecimal povrsina) {
        super(new SimpleIntegerProperty(id));
        this.naziv = new SimpleStringProperty(naziv);
        this.povrsina = new SimpleObjectProperty<>(povrsina);
        ObservableList<Zupanija> observableList = FXCollections.observableArrayList(new ArrayList<>());
        this.zupanije = new SimpleListProperty<>(observableList);
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

    public BigDecimal getPovrsina() {
        return povrsina.get();
    }

    public ObjectProperty<BigDecimal> povrsinaProperty() {
        return povrsina;
    }

    public void setPovrsina(BigDecimal povrsina) {
        this.povrsina.set(povrsina);
    }

    public ObservableList<Zupanija> getZupanije() {
        return zupanije.get();
    }

    public ListProperty<Zupanija> zupanijeProperty() {
        return zupanije;
    }

    public void setZupanije(ObservableList<Zupanija> zupanije) {
        this.zupanije.set(zupanije);
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
