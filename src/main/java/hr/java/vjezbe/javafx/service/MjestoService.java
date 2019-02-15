package hr.java.vjezbe.javafx.service;

import hr.java.vjezbe.javafx.model.Mjesto;

import java.util.List;

public interface MjestoService extends CrudService<Mjesto, Integer> {
    List<Mjesto> findByName(String name);
}
