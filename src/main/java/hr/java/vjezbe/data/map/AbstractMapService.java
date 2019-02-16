package hr.java.vjezbe.data.map;

import hr.java.vjezbe.javafx.model.BazniEntitet;

import java.util.*;

public abstract class AbstractMapService<T extends BazniEntitet, ID extends Integer> {

    protected Map<Integer, T> map = new HashMap<>();

    Set<T> findAll() {
        return new HashSet<>(map.values());
    }

    T findById(ID id) {
        return map.get(id);
    }

    T save(T object) {
        if (object != null) {
            if (object.getId() == null || object.getId().equals(0)) {
                object.setId(getNextId());
            }

            map.put(object.getId(), object);
        } else {
            throw new RuntimeException("Object cannot be null.");
        }
        return object;
    }

    void deleteById(ID id) {
        map.remove(id);
    }

    void delete(T object) {
        map.entrySet().removeIf(entry -> entry.getValue().equals(object));
    }

    private Integer getNextId() {
        Integer nextId = null;
        try {
            nextId = Collections.max(map.keySet()) + 1;
        } catch (NoSuchElementException ex) {
            nextId = 1;
        }
        return nextId;
    }
}
