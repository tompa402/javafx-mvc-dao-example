package hr.java.vjezbe.database;

import hr.java.vjezbe.database.exceptions.DAOException;
import hr.java.vjezbe.javafx.model.Senzor;

import java.util.List;

public interface SenzorDAO extends DAO<Senzor> {

    List<Senzor> getAllByPostajaId(Integer postajaId) throws DAOException;
}
