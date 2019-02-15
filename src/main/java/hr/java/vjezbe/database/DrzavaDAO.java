package hr.java.vjezbe.database;

import hr.java.vjezbe.database.exceptions.DAOException;
import hr.java.vjezbe.javafx.model.Drzava;

import java.util.List;

public interface DrzavaDAO extends DAO<Drzava> {
    List<Drzava> findByName(String name) throws DAOException;
}
