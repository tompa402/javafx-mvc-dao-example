package hr.java.vjezbe.javafx.service.implDB;

import hr.java.vjezbe.database.DAOFactory;
import hr.java.vjezbe.database.MjernaPostajaDAO;
import hr.java.vjezbe.database.MjestoDAO;
import hr.java.vjezbe.database.SenzorDAO;
import hr.java.vjezbe.database.exceptions.DAOException;
import hr.java.vjezbe.javafx.model.MjernaPostaja;
import hr.java.vjezbe.javafx.model.Mjesto;
import hr.java.vjezbe.javafx.model.Senzor;
import hr.java.vjezbe.javafx.service.MjernaPostajaService;
import hr.java.vjezbe.javafx.validator.InputValidator;
import javafx.collections.FXCollections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MjernaPostajaServiceImpl implements MjernaPostajaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MjernaPostajaServiceImpl.class);

    private final MjernaPostajaDAO mjernaPostajaDAO;
    private final MjestoDAO mjestoDAO;
    private final SenzorDAO senzorDAO;


    public MjernaPostajaServiceImpl() {
        this.mjernaPostajaDAO = Objects.requireNonNull(DAOFactory.getDaoFactory(DAOFactory.H2)).getMjernaPostajaDAO();
        this.mjestoDAO = Objects.requireNonNull(DAOFactory.getDaoFactory(DAOFactory.H2)).getMjestoDAO();
        this.senzorDAO = Objects.requireNonNull(DAOFactory.getDaoFactory(DAOFactory.H2)).getSenzorDAO();
    }

    @Override
    public MjernaPostaja get(Integer id) {
        MjernaPostaja postaja = null;
        try {
            postaja = mjernaPostajaDAO.get(id);
            postaja.setMjesto(mjestoDAO.get(postaja.getMjesto().getId()));
            // TODO: set list of senzors
        } catch (DAOException ex) {
            LOGGER.error(ex.getMessage());
        }
        return postaja;
    }

    @Override
    public List<MjernaPostaja> getAll() {
        List<MjernaPostaja> postaje = null;
        try {
            postaje = mjernaPostajaDAO.getAll();
            mapReferences(postaje);
        } catch (DAOException ex) {
            LOGGER.error(ex.getMessage());
        }
        return postaje;
    }

    @Override
    public int save(MjernaPostaja mjernaPostaja) {
        int insertedRow = -1;
        try {
            insertedRow = mjernaPostajaDAO.save(mjernaPostaja);
        } catch (DAOException ex) {
            LOGGER.error(ex.getMessage());
        }
        return insertedRow;
    }

    @Override
    public boolean update(MjernaPostaja mjernaPostaja) {
        boolean updated = false;
        try {
            updated = mjernaPostajaDAO.update(mjernaPostaja);
        } catch (DAOException ex) {
            LOGGER.error(ex.getMessage());
        }
        return updated;
    }

    @Override
    public boolean delete(MjernaPostaja mjernaPostaja) {
        boolean deleted = false;
        try {
            deleted = mjernaPostajaDAO.delete(mjernaPostaja);
        } catch (DAOException ex) {
            LOGGER.error(ex.getMessage());
        }
        return deleted;
    }

    @Override
    public List<MjernaPostaja> findByName(String name) {
        if (InputValidator.isNullOrEmpty(name)) {
            return getAll();
        }

        List<MjernaPostaja> postaje = null;
        try {
            postaje = mjernaPostajaDAO.findByName(name);
            mapReferences(postaje);
        } catch (DAOException ex) {
            LOGGER.error(ex.getMessage());
        }
        return postaje;
    }

    private void mapReferences(List<MjernaPostaja> postaje) {
        List<Mjesto> mjesta = mjestoDAO.getAll();
        List<Senzor> senzori = senzorDAO.getAll();
        postaje.forEach(postaja -> {
            mjesta.stream().filter(mjesto -> mjesto.getId().equals(postaja.getMjesto().getId())).findFirst()
                    .ifPresent(postaja::setMjesto);
            postaja.setSenzori(FXCollections.observableArrayList(senzori.stream()
                  .filter(p -> p.getPostaja().getId().equals(postaja.getId())).collect(Collectors.toList())));
        });

    }
}
