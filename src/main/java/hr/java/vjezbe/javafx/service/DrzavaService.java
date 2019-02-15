package hr.java.vjezbe.javafx.service;

import hr.java.vjezbe.javafx.model.Drzava;

import java.util.List;

public interface DrzavaService extends CrudService<Drzava, Long> {
    List<Drzava> findByName(String name);
}
