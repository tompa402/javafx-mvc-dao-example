package hr.java.vjezbe.javafx.util;

import hr.java.vjezbe.javafx.model.MjernaPostaja;
import hr.java.vjezbe.javafx.model.RadioSondaznaMjernaPostaja;
import javafx.util.StringConverter;

public class NovaPostajaConverter extends StringConverter<MjernaPostaja> {
    @Override
    public String toString(MjernaPostaja object) {
        if (object instanceof RadioSondaznaMjernaPostaja)
            return "Radio sondažna postaja";

        return "Mjerna postaja";
    }

    @Override
    public MjernaPostaja fromString(String string) {
        return null;
    }
}
