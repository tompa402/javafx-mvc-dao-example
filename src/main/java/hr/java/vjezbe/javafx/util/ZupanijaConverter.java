package hr.java.vjezbe.javafx.util;

import hr.java.vjezbe.javafx.model.Zupanija;
import javafx.util.StringConverter;

public class ZupanijaConverter extends StringConverter<Zupanija> {
    @Override
    public String toString(Zupanija object) {
        return object.getNaziv();
    }

    @Override
    public Zupanija fromString(String string) {
        return null;
    }
}
