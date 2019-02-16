package hr.java.vjezbe.javafx.model;

import javafx.beans.property.*;

import java.math.BigDecimal;

public abstract class Senzor extends BazniEntitet {

    private StringProperty mjernaJedinica;
    private ObjectProperty<Byte> preciznost;
    private ObjectProperty<BigDecimal> vrijednost;
    private ObjectProperty<RadSenzora> radSenzora;
    private ObjectProperty<MjernaPostaja> postaja;

    public Senzor(String mjernaJedinica, Byte preciznost, BigDecimal vrijednost, RadSenzora radSenzora) {
        this.mjernaJedinica = new SimpleStringProperty(mjernaJedinica);
        this.preciznost = new SimpleObjectProperty<>(preciznost);
        this.vrijednost = new SimpleObjectProperty<>(vrijednost);
        this.radSenzora = new SimpleObjectProperty<>(radSenzora);
    }

    public Senzor(Integer id, String mjernaJedinica, byte preciznost, BigDecimal vrijednost, RadSenzora radSenzora, MjernaPostaja postaja) {
        super(new SimpleIntegerProperty(id));
        this.mjernaJedinica = new SimpleStringProperty(mjernaJedinica);
        this.preciznost = new SimpleObjectProperty<>(preciznost);
        this.vrijednost = new SimpleObjectProperty<>(vrijednost);
        this.radSenzora = new SimpleObjectProperty<>(radSenzora);
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

    public Byte getPreciznost() {
        return preciznost.get();
    }

    public ObjectProperty<Byte> preciznostProperty() {
        return preciznost;
    }

    public void setPreciznost(Byte preciznost) {
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
