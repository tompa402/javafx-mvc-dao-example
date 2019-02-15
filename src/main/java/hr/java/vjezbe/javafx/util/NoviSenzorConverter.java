package hr.java.vjezbe.javafx.util;

import hr.java.vjezbe.javafx.model.Senzor;
import hr.java.vjezbe.javafx.model.SenzorTemperature;
import hr.java.vjezbe.javafx.model.SenzorVlage;
import javafx.util.StringConverter;

public class NoviSenzorConverter extends StringConverter<Senzor> {
    @Override
    public String toString(Senzor object) {
        if (object instanceof SenzorTemperature)
            return "Senzor temperature";
        else if(object instanceof SenzorVlage)
            return "Senzor vlage";

        return "Senzor tlaka";
    }

    @Override
    public Senzor fromString(String string) {
        return null;
    }
}
