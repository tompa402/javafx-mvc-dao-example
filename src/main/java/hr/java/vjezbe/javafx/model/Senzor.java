package hr.java.vjezbe.javafx.model;

import javafx.beans.property.*;

import java.math.BigDecimal;

public abstract class Senzor extends BazniEntitet {

    private StringProperty mjernaJedinica;
    private ObjectProperty<Double> preciznost;
    private ObjectProperty<BigDecimal> vrijednost;
    private ObjectProperty<RadSenzora> radSenzora;
    private BooleanProperty active;
    private ObjectProperty<MjernaPostaja> postaja;

    public Senzor() {

    }

    public Senzor(Integer id, String mjernaJedinica, Double preciznost, BigDecimal vrijednost, RadSenzora radSenzora, Boolean active, MjernaPostaja postaja) {
        super(new SimpleIntegerProperty(id));
        this.mjernaJedinica = new SimpleStringProperty(mjernaJedinica);
        this.preciznost = new SimpleObjectProperty<>(preciznost);
        this.vrijednost = new SimpleObjectProperty<>(vrijednost);
        this.radSenzora = new SimpleObjectProperty<>(radSenzora);
        this.active = new SimpleBooleanProperty(active == null ? false : active);
        this.postaja = new SimpleObjectProperty<>(postaja);
    }

    public String getMjernaJedinica() {
        return mjernaJedinica.get();
    }

    public StringProperty mjernaJedinicaProperty() {
        return mjernaJedinica;
    }

    public void setMjernaJedinica(String mjernaJedinica) {
        this.mjernaJedinica.set(mjernaJedinica);
    }

    public Double getPreciznost() {
        return preciznost.get();
    }

    public ObjectProperty<Double> preciznostProperty() {
        return preciznost;
    }

    public void setPreciznost(Double preciznost) {
        this.preciznost.set(preciznost);
    }

    public BigDecimal getVrijednost() {
        return vrijednost.get();
    }

    public ObjectProperty<BigDecimal> vrijednostProperty() {
        return vrijednost;
    }

    public void setVrijednost(BigDecimal vrijednost) {
        this.vrijednost.set(vrijednost);
    }

    public RadSenzora getRadSenzora() {
        return radSenzora.get();
    }

    public ObjectProperty<RadSenzora> radSenzoraProperty() {
        return radSenzora;
    }

    public void setRadSenzora(RadSenzora radSenzora) {
        this.radSenzora.set(radSenzora);
    }

    public boolean getActive() {
        return active.get();
    }

    public BooleanProperty activeProperty() {
        return active;
    }

    public void setActive(boolean active) {
        this.active.set(active);
    }

    public MjernaPostaja getPostaja() {
        return postaja.get();
    }

    public ObjectProperty<MjernaPostaja> postajaProperty() {
        return postaja;
    }

    public void setPostaja(MjernaPostaja postaja) {
        this.postaja.set(postaja);
    }
}
