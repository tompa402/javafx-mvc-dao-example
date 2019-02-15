package hr.java.vjezbe.javafx.service.implDB;

import hr.java.vjezbe.database.DAOFactory;
import hr.java.vjezbe.database.MjernaPostajaDAO;
import hr.java.vjezbe.database.MjestoDAO;
import hr.java.vjezbe.database.ZupanijaDAO;
import hr.java.vjezbe.database.exceptions.DAOException;
import hr.java.vjezbe.javafx.model.MjernaPostaja;
import hr.java.vjezbe.javafx.model.Mjesto;
import hr.java.vjezbe.javafx.model.Zupanija;
import hr.java.vjezbe.javafx.service.MjestoService;
import hr.java.vjezbe.javafx.validator.InputValidator;
import javafx.collections.FXCollections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MjestoServiceImpl implements MjestoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZupanijaServiceImpl.class);
    private final MjestoDAO mjestoDAO;
    private final ZupanijaDAO zupanijaDAO;
    private final MjernaPostajaDAO mjernaPostajaDAO;

    public MjestoServiceImpl() {
        this.mjestoDAO = Objects.requireNonNull(DAOFactory.getDaoFactory(DAOFactory.H2)).getMjestoDAO();
        this.zupanijaDAO = Objects.requireNonNull(DAOFactory.getDaoFactory(DAOFactory.H2)).getZupanijaDAO();
        this.mjernaPostajaDAO = Objects.requireNonNull(DAOFactory.getDaoFactory(DAOFactory.H2)).getMjernaPostajaDAO();
    }

    @Override
    public Mjesto get(Integer id) {
        Mjesto mjesto = null;
        try {
            mjesto = mjestoDAO.get(id);
            mjesto.setZupanija(zupanijaDAO.get(mjesto.getZupanija().getId()));
            mjesto.setMjernePostaje(FXCollections
                    .observableArrayList(mjernaPostajaDAO.getAllByMjestoId(mjesto.getId())));
        } catch (DAOException ex) {
            LOGGER.error(ex.getMessage());
        }
        return mjesto;
    }

    @Override
    public List<Mjesto> getAll() {
        List<Mjesto> mjesta = null;
        try {
            mjesta = mjestoDAO.getAll();
            mapReferences(mjesta);
        } catch (DAOException ex) {
            LOGGER.error(ex.getMessage());
        }
        return mjesta;
    }

    @Override
    public int save(Mjesto mjesto) {
        int insertedRow = -1;
        try {
            insertedRow = mjestoDAO.save(mjesto);
        } catch (DAOException ex) {
            LOGGER.error(ex.getMessage());
        }
        return insertedRow;
    }

    @Override
    public boolean update(Mjesto mjesto) {
        boolean updated = false;
        try {
            updated = mjestoDAO.update(mjesto);
        } catch (DAOException ex) {
            LOGGER.error(ex.getMessage());
        }
        return updated;
    }

    @Override
    public boolean delete(Mjesto mjesto) {
        boolean deleted = false;
        try {
            deleted = mjestoDAO.delete(mjesto);
        } catch (DAOException ex) {
            LOGGER.error(ex.getMessage());
        }
        return deleted;
    }

    @Override
    public List<Mjesto> findByName(String name) {
        if (InputValidator.isNullOrEmpty(name)) {
            return getAll();
        }

        List<Mjesto> mjesta = null;
        try {
            mjesta = mjestoDAO.findByName(name);
            mapReferences(mjesta);
        } catch (DAOException ex) {
            LOGGER.error(ex.getMessage());
        }
        return mjesta;
    }

    private void mapReferences(List<Mjesto> mjesta) {
        List<Zupanija> zupanije = zupanijaDAO.getAll();
        List<MjernaPostaja> postaje = mjernaPostajaDAO.getAll();
        mjesta.forEach(mjesto -> {
            zupanije.stream().filter(zup -> zup.getId().equals(mjesto.getZupanija().getId())).findFirst()
                    .ifPresent(mjesto::setZupanija);
            mjesto.setMjernePostaje(FXCollections.observableArrayList(postaje.stream()
                    .filter(p -> p.getMjesto().getId().equals(mjesto.getId())).collect(Collectors.toList())));
        });
    }
}
