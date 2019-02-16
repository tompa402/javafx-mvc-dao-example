package hr.java.vjezbe.model;

import java.math.BigDecimal;

public abstract class Senzor extends BazniEntitet {

    private String mjernaJedinica;
    private byte preciznost;
    private BigDecimal vrijednost;
    private RadSenzora radSenzora;
    private MjernaPostaja postaja;

    public Senzor() {
    }

    public Senzor(String mjernaJedinica, byte preciznost) {
        this.mjernaJedinica = mjernaJedinica;
        this.preciznost = preciznost;
    }

    public Senzor(String mjernaJedinica, byte preciznost, BigDecimal vrijednost, RadSenzora radSenzora, MjernaPostaja postaja) {
        this.mjernaJedinica = mjernaJedinica;
        this.preciznost = preciznost;
        this.vrijednost = vrijednost;
        this.radSenzora = radSenzora;
        this.postaja = postaja;
    }

    public Senzor(int id, String mjernaJedinica, byte preciznost, BigDecimal vrijednost, RadSenzora radSenzora, MjernaPostaja postaja) {
        this.setId(id);
        this.mjernaJedinica = mjernaJedinica;
        this.preciznost = preciznost;
        this.vrijednost = vrijednost;
        this.radSenzora = radSenzora;
        this.postaja = postaja;
    }

    public abstract String dohvatiPodatkeSenzora();

    public String getMjernaJedinica() {
        return mjernaJedinica;
    }

    public void setMjernaJedinica(String mjernaJedinica) {
        this.mjernaJedinica = mjernaJedinica;
    }

    public byte getPreciznost() {
        return preciznost;
    }

    public void setPreciznost(byte preciznost) {
        this.preciznost = preciznost;
    }

    public BigDecimal getVrijednost() {
        return vrijednost;
    }

    public void setVrijednost(BigDecimal vrijednost) {
        this.vrijednost = vrijednost;
    }

    public RadSenzora getRadSenzora() {
        return radSenzora;
    }

    public void setRadSenzora(RadSenzora radSenzora) {
        this.radSenzora = radSenzora;
    }

    public MjernaPostaja getPostaja() {
        return postaja;
    }

    public void setPostaja(MjernaPostaja postaja) {
        this.postaja = postaja;
    }
}
