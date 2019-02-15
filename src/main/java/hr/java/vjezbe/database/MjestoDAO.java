package hr.java.vjezbe.database;

import hr.java.vjezbe.database.exceptions.DAOException;
import hr.java.vjezbe.javafx.model.Mjesto;

import java.util.List;

public interface MjestoDAO extends DAO<Mjesto> {
    List<Mjesto> findByName(String name) throws DAOException;

    List<Mjesto> getAllByZupanijaId(Integer zupanijaId) throws DAOException;
}
