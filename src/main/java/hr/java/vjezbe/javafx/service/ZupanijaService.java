package hr.java.vjezbe.javafx.service;

import hr.java.vjezbe.javafx.model.Zupanija;

import java.util.List;

public interface ZupanijaService extends CrudService<Zupanija, Integer> {
    List<Zupanija> findByName(String name);
    List<Zupanija> getByDrzavaId(Integer id);
}
