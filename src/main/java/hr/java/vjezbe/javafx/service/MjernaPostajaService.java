package hr.java.vjezbe.javafx.service;

import hr.java.vjezbe.javafx.model.MjernaPostaja;

import java.util.List;

public interface MjernaPostajaService extends CrudService<MjernaPostaja, Long> {
    List<MjernaPostaja> findByName(String name);
}
