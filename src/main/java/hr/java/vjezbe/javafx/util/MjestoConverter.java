package hr.java.vjezbe.javafx.util;

import hr.java.vjezbe.javafx.model.Mjesto;
import javafx.util.StringConverter;

public class MjestoConverter extends StringConverter<Mjesto> {
    @Override
    public String toString(Mjesto object) {
        return object.getNaziv();
    }

    @Override
    public Mjesto fromString(String string) {
        return null;
    }
}
