package hr.java.vjezbe.database;

import hr.java.vjezbe.database.exceptions.DAOException;
import hr.java.vjezbe.javafx.model.MjernaPostaja;

import java.util.List;

public interface MjernaPostajaDAO extends DAO<MjernaPostaja> {
    List<MjernaPostaja> findByName(String name) throws DAOException;

    List<MjernaPostaja> getAllByMjestoId(Integer mjestoId) throws DAOException;
}
