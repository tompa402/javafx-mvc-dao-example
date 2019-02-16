package hr.java.vjezbe.data;

import hr.java.vjezbe.javafx.model.Zupanija;

import java.util.Set;

public interface ZupanijaService extends CrudService<Zupanija, Integer> {
    Set<Zupanija> findByName(String name);
}
