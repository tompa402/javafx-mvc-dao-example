package hr.java.vjezbe.javafx.util;

import hr.java.vjezbe.javafx.model.MjernaPostaja;
import javafx.util.StringConverter;

public class PostajaConverter extends StringConverter<MjernaPostaja> {
    @Override
    public String toString(MjernaPostaja object) {
        return object.getNaziv();
    }

    @Override
    public MjernaPostaja fromString(String string) {
        return null;
    }
}
