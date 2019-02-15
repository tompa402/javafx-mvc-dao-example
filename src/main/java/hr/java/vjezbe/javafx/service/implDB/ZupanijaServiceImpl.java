package hr.java.vjezbe.javafx.service.implDB;

import hr.java.vjezbe.database.DAOFactory;
import hr.java.vjezbe.database.DrzavaDAO;
import hr.java.vjezbe.database.MjestoDAO;
import hr.java.vjezbe.database.ZupanijaDAO;
import hr.java.vjezbe.database.exceptions.DAOException;
import hr.java.vjezbe.javafx.model.Drzava;
import hr.java.vjezbe.javafx.model.Mjesto;
import hr.java.vjezbe.javafx.model.Zupanija;
import hr.java.vjezbe.javafx.service.ZupanijaService;
import hr.java.vjezbe.javafx.validator.InputValidator;
import javafx.collections.FXCollections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ZupanijaServiceImpl implements ZupanijaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZupanijaServiceImpl.class);
    private final ZupanijaDAO zupanijaDAO;
    private final DrzavaDAO drzavaDAO;
    private final MjestoDAO mjestoDAO;

    public ZupanijaServiceImpl() {
        this.zupanijaDAO = Objects.requireNonNull(DAOFactory.getDaoFactory(DAOFactory.H2)).getZupanijaDAO();
        this.drzavaDAO = Objects.requireNonNull(DAOFactory.getDaoFactory(DAOFactory.H2)).getDrzavaDAO();
        this.mjestoDAO = Objects.requireNonNull(DAOFactory.getDaoFactory(DAOFactory.H2)).getMjestoDAO();
    }

    @Override
    public Zupanija get(Integer id) {
        Zupanija zupanija = null;
        try {
            zupanija = zupanijaDAO.get(id);
            zupanija.setDrzava(drzavaDAO.get(zupanija.getDrzava().getId()));
            zupanija.setMjesta(FXCollections.observableArrayList(mjestoDAO.getAllByZupanijaId(zupanija.getId())));
        } catch (DAOException ex) {
            LOGGER.error(ex.getMessage());
        }
        return zupanija;
    }

    @Override
    public List<Zupanija> getAll() {
        List<Zupanija> zupanije = null;
        try {
            zupanije = zupanijaDAO.getAll();
            mapReferences(zupanije);
        } catch (DAOException ex) {
            LOGGER.error(ex.getMessage());
        }
        return zupanije;
    }

    @Override
    public int save(Zupanija zupanija) {
        int insertedRow = -1;
        try {
            insertedRow = zupanijaDAO.save(zupanija);
        } catch (DAOException ex) {
            LOGGER.error(ex.getMessage());
        }
        return insertedRow;
    }

    @Override
    public boolean update(Zupanija zupanija) {
        boolean updated = false;
        try {
            updated = zupanijaDAO.update(zupanija);
        } catch (DAOException ex) {
            LOGGER.error(ex.getMessage());
        }
        return updated;
    }

    @Override
    public boolean delete(Zupanija zupanija) {
        boolean deleted = false;
        try {
            deleted = zupanijaDAO.delete(zupanija);
        } catch (DAOException ex) {
            LOGGER.error(ex.getMessage());
        }
        return deleted;
    }

    @Override
    public List<Zupanija> findByName(String name) {
        if (InputValidator.isNullOrEmpty(name)) {
            return getAll();
        }

        List<Zupanija> zupanije = null;
        try {
            zupanije = zupanijaDAO.findByName(name);
            mapReferences(zupanije);
        } catch (DAOException ex) {
            LOGGER.error(ex.getMessage());
        }
        return zupanije;
    }

    @Override
    public List<Zupanija> getByDrzavaId(Integer id) {
        List<Zupanija> zupanije = null;

        try {
            zupanije = zupanijaDAO.getAllByDrzavaId(id);
            mapReferences(zupanije);
        } catch (DAOException ex) {
            LOGGER.error(ex.getMessage());
        }
        return zupanije;
    }

    private void mapReferences(List<Zupanija> zupanije) {
        List<Drzava> drzave = drzavaDAO.getAll();
        List<Mjesto> mjesta = mjestoDAO.getAll();
        zupanije.forEach(zupanija -> {
            drzave.stream().filter(drz -> drz.getId().equals(zupanija.getDrzava().getId())).findFirst()
                    .ifPresent(zupanija::setDrzava);
            zupanija.setMjesta(FXCollections.observableArrayList(mjesta.stream()
                    .filter(m -> m.getZupanija().getId().equals(zupanija.getId())).collect(Collectors.toList())));
        });
    }
}
