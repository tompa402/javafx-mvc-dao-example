package hr.java.vjezbe.data;

import hr.java.vjezbe.javafx.model.Drzava;

import java.util.Set;

public interface DrzavaService extends CrudService<Drzava, Integer> {
    Set<Drzava> findByName(String name);
}
