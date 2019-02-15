package hr.java.vjezbe.javafx.service.implDB;

import hr.java.vjezbe.database.DAOFactory;
import hr.java.vjezbe.database.MjernaPostajaDAO;
import hr.java.vjezbe.database.SenzorDAO;
import hr.java.vjezbe.database.exceptions.DAOException;
import hr.java.vjezbe.javafx.model.MjernaPostaja;
import hr.java.vjezbe.javafx.model.Senzor;
import hr.java.vjezbe.javafx.service.SenzorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

public class SenzorServiceImpl implements SenzorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SenzorServiceImpl.class);

    private final SenzorDAO senzorDAO;
    private final MjernaPostajaDAO mjernaPostajaDAO;

    public SenzorServiceImpl() {
        this.senzorDAO = Objects.requireNonNull(DAOFactory.getDaoFactory(DAOFactory.H2)).getSenzorDAO();
        this.mjernaPostajaDAO = Objects.requireNonNull(DAOFactory.getDaoFactory(DAOFactory.H2)).getMjernaPostajaDAO();
    }

    @Override
    public Senzor get(Integer id) {
        Senzor senzor = null;
        try {
            senzor = senzorDAO.get(id);
            senzor.setPostaja(mjernaPostajaDAO.get(senzor.getPostaja().getId()));
        } catch (DAOException ex) {
            LOGGER.error(ex.getMessage());
        }
        return senzor;
    }

    @Override
    public List<Senzor> getAll() {
        List<Senzor> senzori = null;
        try {
            senzori = senzorDAO.getAll();
            mapReferences(senzori);
        } catch (DAOException ex) {
            LOGGER.error(ex.getMessage());
        }
        return senzori;
    }

    @Override
    public int save(Senzor senzor) {
        int insertedRow = -1;
        try {
            insertedRow = senzorDAO.save(senzor);
        } catch (DAOException ex) {
            LOGGER.error(ex.getMessage());
        }
        return insertedRow;
    }

    @Override
    public boolean update(Senzor senzor) {
        boolean updated = false;
        try {
            updated = senzorDAO.update(senzor);
        } catch (DAOException ex) {
            LOGGER.error(ex.getMessage());
        }
        return updated;
    }

    @Override
    public boolean delete(Senzor senzor) {
        boolean deleted = false;
        try {
            deleted = senzorDAO.delete(senzor);
        } catch (DAOException ex) {
            LOGGER.error(ex.getMessage());
        }
        return deleted;
    }

    private void mapReferences(List<Senzor> senzori) {
        List<MjernaPostaja> postaje = mjernaPostajaDAO.getAll();
        senzori.forEach(senzor ->
                postaje.stream().filter(postaja -> postaja.getId().equals(senzor.getPostaja().getId())).findFirst()
                        .ifPresent(senzor::setPostaja));
    }
}
