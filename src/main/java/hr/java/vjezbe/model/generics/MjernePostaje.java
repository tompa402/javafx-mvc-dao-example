package hr.java.vjezbe.model.generics;

import hr.java.vjezbe.model.MjernaPostaja;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MjernePostaje<T extends MjernaPostaja> {
    private List<T> mjernePostaje;

    public MjernePostaje() {
        this.mjernePostaje = new ArrayList<>();
    }

    public T get(Integer i) {
        return this.mjernePostaje.get(i);
    }

    public void add(T postaja) {
        mjernePostaje.add(postaja);
    }

    public List<T> getSortedList() {
        mjernePostaje.sort(Comparator.comparing(MjernaPostaja::getNaziv));
        return this.mjernePostaje;
    }

    @Override
    public String toString() {
        return "MjernePostaje{" +
                "mjernePostaje=" + mjernePostaje +
                '}';
    }
}
