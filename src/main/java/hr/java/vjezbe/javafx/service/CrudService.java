package hr.java.vjezbe.javafx.service;

import java.util.List;
import java.util.Optional;

public interface CrudService <T, ID> {
    //return object or null or error or if not found
    T get(Integer id);

    // return list list of objects
    List<T> getAll();

    // return newly created object number, or a -1 on error
    int save(T t);

    // return true on success, false on failure
    boolean update(T t);

    // return true on success, false on failure
    boolean delete(T t);
}
