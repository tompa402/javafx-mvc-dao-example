package hr.java.vjezbe.javafx.service.implDB;

import hr.java.vjezbe.database.DAOFactory;
import hr.java.vjezbe.database.DrzavaDAO;
import hr.java.vjezbe.database.ZupanijaDAO;
import hr.java.vjezbe.database.exceptions.DAOException;
import hr.java.vjezbe.javafx.model.Drzava;
import hr.java.vjezbe.javafx.model.Zupanija;
import hr.java.vjezbe.javafx.service.DrzavaService;
import hr.java.vjezbe.javafx.validator.InputValidator;
import javafx.collections.FXCollections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DrzavaServiceImpl implements DrzavaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DrzavaServiceImpl.class);
    private final DrzavaDAO drzavaDAO;
    private final ZupanijaDAO zupanijaDAO;

    public DrzavaServiceImpl() {
        this.drzavaDAO = Objects.requireNonNull(DAOFactory.getDaoFactory(DAOFactory.H2)).getDrzavaDAO();
        this.zupanijaDAO = Objects.requireNonNull(DAOFactory.getDaoFactory(DAOFactory.H2)).getZupanijaDAO();
    }

    @Override
    public Drzava get(Integer id) {
        Drzava drzava = null;
        try {
            drzava = drzavaDAO.get(id);
            drzava.setZupanije(FXCollections.observableArrayList(zupanijaDAO.getAllByDrzavaId(drzava.getId())));
        } catch (DAOException ex) {
            LOGGER.error(ex.getMessage());
        }
        return drzava;
    }

    @Override
    public List<Drzava> getAll() {
        List<Drzava> drzave = null;
        try {
            drzave = drzavaDAO.getAll();
            mapReferences(drzave);
        } catch (DAOException ex) {
            LOGGER.error(ex.getMessage());
        }
        return drzave;
    }

    @Override
    public int save(Drzava drzava) {
        int insertedRow = -1;
        try {
            insertedRow = drzavaDAO.save(drzava);
        } catch (DAOException ex) {
            LOGGER.error(ex.getMessage());
        }
        return insertedRow;
    }

    @Override
    public boolean update(Drzava drzava) {
        boolean updated = false;
        try {
            updated = drzavaDAO.update(drzava);
        } catch (DAOException ex) {
            LOGGER.error(ex.getMessage());
        }
        return updated;
    }

    @Override
    public boolean delete(Drzava drzava) {
        boolean deleted = false;
        try {
            deleted = drzavaDAO.delete(drzava);
        } catch (DAOException ex) {
            LOGGER.error(ex.getMessage());
        }
        return deleted;
    }

    @Override
    public List<Drzava> findByName(String name) {
        if (InputValidator.isNullOrEmpty(name)) {
            return getAll();
        }

        List<Drzava> drzave = null;
        try {
            drzave = drzavaDAO.findByName(name);
            mapReferences(drzave);
        } catch (DAOException ex) {
            LOGGER.error(ex.getMessage());
        }
        return drzave;
    }

    private void mapReferences(List<Drzava> drzave) {
        List<Zupanija> zupanije = zupanijaDAO.getAll();
        drzave.forEach(drzava -> drzava.setZupanije(FXCollections.observableArrayList(zupanije
                .stream().filter(zup -> zup.getDrzava().getId().equals(drzava.getId()))
                .collect(Collectors.toList()))));
    }
}
