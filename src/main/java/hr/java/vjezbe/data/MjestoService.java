package hr.java.vjezbe.data;

import hr.java.vjezbe.javafx.model.Mjesto;

import java.util.Set;

public interface MjestoService extends CrudService<Mjesto, Integer> {
    Set<Mjesto> findByName(String name);
}