package hr.java.vjezbe.javafx.util;

import hr.java.vjezbe.javafx.model.Drzava;
import javafx.util.StringConverter;

public class DrzavaConverter extends StringConverter<Drzava> {
    @Override
    public String toString(Drzava object) {
        return object.getNaziv();
    }

    @Override
    public Drzava fromString(String string) {
        return null;
    }
}
