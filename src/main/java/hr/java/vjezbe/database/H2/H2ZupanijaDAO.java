package hr.java.vjezbe.database.H2;

import hr.java.vjezbe.database.H2DAOFactory;
import hr.java.vjezbe.database.ZupanijaDAO;
import hr.java.vjezbe.database.exceptions.DAOException;
import hr.java.vjezbe.database.util.DAOUtil;
import hr.java.vjezbe.javafx.model.Drzava;
import hr.java.vjezbe.javafx.model.Zupanija;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class H2ZupanijaDAO implements ZupanijaDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(H2ZupanijaDAO.class);

    private static final String TABLE = "POSTAJE.ZUPANIJA";

    private static final String COLUMNS = "NAZIV, DRZAVA_ID";

    private static final String RETURN_COLUMNS = "ID, " + COLUMNS;

    private static final String SQL_FIND_BY_ID =
            "SELECT " + RETURN_COLUMNS + " FROM " + TABLE + " WHERE ID = ?";

    private static final String SQL_FIND_BY_NAME =
            "SELECT " + RETURN_COLUMNS + " FROM " + TABLE + " WHERE NAZIV ILIKE ?";

    private static final String SQL_FIND_BY_DRZAVA =
            "SELECT " + RETURN_COLUMNS + " FROM " + TABLE + " WHERE DRZAVA_ID = ?";

    private static final String SQL_LIST_ORDER_BY_ID =
            "SELECT " + RETURN_COLUMNS + " FROM " + TABLE + " ORDER BY ID";

    private static final String SQL_INSERT =
            "INSERT INTO " + TABLE + " (" + COLUMNS + ") VALUES (?, ?)";

    private static final String SQL_UPDATE =
            "UPDATE " + TABLE + " SET naziv = ?, drzava_id = ? WHERE id = ?";

    private static final String SQL_DELETE =
            "DELETE FROM " + TABLE + " WHERE id = ?";

    @Override
    public Zupanija get(Integer id) throws DAOException {
        Zupanija zupanija = null;
        try (Connection conn = H2DAOFactory.createConnection();
             PreparedStatement stmt = DAOUtil.prepareStatement(conn, SQL_FIND_BY_ID, false, id);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                zupanija = map(rs);
            }
        } catch (SQLException ex) {
            LOGGER.error("Error while executing SQL_FIND_BY_ID: " + ex);
            throw new DAOException();
        }
        return zupanija;
    }

    @Override
    public List<Zupanija> getAll() throws DAOException {
        List<Zupanija> zupanije = new ArrayList<>();
        try (Connection conn = H2DAOFactory.createConnection();
             PreparedStatement stmt = DAOUtil.prepareStatement(conn, SQL_LIST_ORDER_BY_ID, false);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                zupanije.add(map(rs));
            }
        } catch (SQLException ex) {
            LOGGER.error("Error while executing SQL_LIST_ORDER_BY_ID: " + ex);
            throw new DAOException();
        }
        return zupanije;
    }

    @Override
    public int save(Zupanija zupanija) throws IllegalArgumentException, DAOException {
        if (zupanija.getId() != 0) {
            LOGGER.error("Object is already created, the Object ID is not null or 0.");
            throw new IllegalArgumentException("Object is already created.");
        }

        Object[] values = {
                zupanija.getNaziv(),
                zupanija.getDrzava().getId()
        };

        try (Connection conn = H2DAOFactory.createConnection();
             PreparedStatement stmt = DAOUtil.prepareStatement(conn, SQL_INSERT, true, values)) {

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted == 0) {
                LOGGER.error("Error while executing SQL_INSERT, no rows affected/inserted.");
                throw new DAOException("Object creation failed, no rows affected/inserted.");
            }

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    Integer newId = rs.getInt(1);
                    zupanija.setId(newId);
                } else {
                    LOGGER.error("Creating failed, no generated key obtained.");
                    throw new DAOException();
                    // or return -1; (which mean insert failed)
                }
            }
        } catch (SQLException ex) {
            LOGGER.error("Error while executing SQL_INSERT: " + ex);
            throw new DAOException();
        }
        return 1;
    }

    @Override
    public boolean update(Zupanija zupanija) throws IllegalArgumentException, DAOException {
        if (zupanija.getId() == null) {
            LOGGER.error("Update denied. Object is not created yet, the Object ID is null.");
            throw new IllegalArgumentException("Unable to perform update. Object is not created yet.");
        }

        Object[] values = {
                zupanija.getNaziv(),
                zupanija.getDrzava().getId(),
                zupanija.getId()
        };
        try (Connection conn = H2DAOFactory.createConnection();
             PreparedStatement stmt = DAOUtil.prepareStatement(conn, SQL_UPDATE, false, values)) {

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated == 0) {
                LOGGER.error("Error while executing SQL_UPDATE, no rows affected/inserted.");
                throw new DAOException("Object update failed, no rows affected.");
            }
        } catch (SQLException ex) {
            LOGGER.error("Error while executing SQL_UPDATE: " + ex);
            throw new DAOException();
        }
        return true;
    }

    @Override
    public boolean delete(Zupanija zupanija) throws DAOException {
        Object[] values = {
                zupanija.getId()
        };

        try (Connection conn = H2DAOFactory.createConnection();
             PreparedStatement stmt = DAOUtil.prepareStatement(conn, SQL_DELETE, false, values)) {

            int rowsDeleted = stmt.executeUpdate();

            if (rowsDeleted == 0) {
                LOGGER.error("Error while executing SQL_DELETE, no rows affected/inserted.");
                throw new DAOException("Unable to perform delete, no rows affected.");
            } else {
                zupanija.setId(null);
            }

        } catch (SQLException ex) {
            LOGGER.error("Error while executing SQL_DELETE: " + ex);
            throw new DAOException();
        }
        return true;
    }

    @Override
    public List<Zupanija> findByName(String name) throws DAOException {
        List<Zupanija> zupanije = new ArrayList<>();
        Object[] values = {
                "%" + name + "%"
        };
        try (Connection conn = H2DAOFactory.createConnection();
             PreparedStatement stmt = DAOUtil.prepareStatement(conn, SQL_FIND_BY_NAME, false, values);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                zupanije.add(map(rs));
            }
        } catch (Exception ex) {
            LOGGER.error("Error while executing SQL_FIND_BY_NAME: " + ex);
            throw new DAOException();
        }
        return zupanije;
    }

    @Override
    public List<Zupanija> getAllByDrzavaId(Integer drzavaId) throws DAOException {
        List<Zupanija> zupanije = new ArrayList<>();

        Object[] values = {
                drzavaId
        };
        try (Connection conn = H2DAOFactory.createConnection();
             PreparedStatement stmt = DAOUtil.prepareStatement(conn, SQL_FIND_BY_DRZAVA, false, values);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                zupanije.add(map(rs));
            }
        } catch (SQLException ex) {
            LOGGER.error("Error while executing SQL_FIND_BY_DRZAVA: " + ex);
            throw new DAOException();
        }
        return zupanije;
    }

    private Zupanija map(ResultSet resultSet) throws SQLException {
        Zupanija zupanija = new Zupanija();
        zupanija.setId(resultSet.getInt("id"));
        zupanija.setNaziv(resultSet.getString("naziv"));
        Drzava drzava = new Drzava();
        drzava.setId(resultSet.getInt("drzava_id"));
        zupanija.setDrzava(drzava);
        return zupanija;
    }
}
