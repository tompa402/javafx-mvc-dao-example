package hr.java.vjezbe.model.generics;

import hr.java.vjezbe.model.Senzor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Senzori<T extends Senzor> {

    private List<T> senzori;

    public Senzori() {
        this.senzori = new ArrayList<>();
    }

    public T getById(Integer id) {
        return senzori.stream().filter(i -> i.getId() == id).findFirst().orElse(null);
    }

    public List<T> getAll() {
        return this.senzori;
    }

    public List<T> getAllSortedSensors() {
        senzori.sort(Comparator.comparing(Senzor::getMjernaJedinica));
        return this.senzori;
    }

    public void add(T postaja) {
        senzori.add(postaja);
    }
}
