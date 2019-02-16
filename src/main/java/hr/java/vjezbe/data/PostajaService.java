package hr.java.vjezbe.data;

import hr.java.vjezbe.javafx.model.MjernaPostaja;

import java.util.Set;

public interface PostajaService extends CrudService<MjernaPostaja, Integer> {
    Set<MjernaPostaja> findByName(String name);
}
