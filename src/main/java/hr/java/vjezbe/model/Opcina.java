package hr.java.vjezbe.model;

import java.util.ArrayList;
import java.util.List;

public class Opcina extends BazniEntitet{

    private String naziv;
    private List<Mjesto> mjesta;

    public Opcina(Integer id, String naziv) {
        super.setId(id);
        this.naziv = naziv;
        this.mjesta = new ArrayList<>();
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public List<Mjesto> getMjesta() {
        return mjesta;
    }

    public void setMjesta(List<Mjesto> mjesta) {
        this.mjesta = mjesta;
    }
}
