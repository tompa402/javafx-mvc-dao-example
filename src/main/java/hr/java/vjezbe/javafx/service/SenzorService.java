package hr.java.vjezbe.javafx.service;

import hr.java.vjezbe.javafx.model.Senzor;

public interface SenzorService extends CrudService<Senzor, Long> {
    int getNegativeActiveSensors();
}
