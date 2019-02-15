package hr.java.vjezbe.database;

import hr.java.vjezbe.database.exceptions.DAOException;

import java.util.List;
import java.util.Optional;

public interface DAO<T> {
    //return object or null or error or if not found
    T get(Integer id) throws DAOException;

    // return list list of objects
    List<T> getAll() throws DAOException;

    // return newly created object number, or a -1 on error
    int save(T t) throws IllegalArgumentException, DAOException;

    // return true on success, false on failure
    boolean update(T t) throws IllegalArgumentException, DAOException;

    // return true on success, false on failure
    boolean delete(T t) throws DAOException;

}
