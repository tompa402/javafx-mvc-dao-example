package hr.java.vjezbe.database;

import hr.java.vjezbe.database.exceptions.DAOException;
import hr.java.vjezbe.javafx.model.Zupanija;

import java.util.List;

public interface ZupanijaDAO extends DAO<Zupanija> {
    List<Zupanija> findByName(String name) throws DAOException;

    List<Zupanija> getAllByDrzavaId(Integer drzavaId) throws DAOException;
}
