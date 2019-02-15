package hr.java.vjezbe.database.H2;

import hr.java.vjezbe.database.DrzavaDAO;
import hr.java.vjezbe.database.H2DAOFactory;
import hr.java.vjezbe.database.exceptions.DAOException;
import hr.java.vjezbe.database.util.DAOUtil;
import hr.java.vjezbe.javafx.model.Drzava;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class H2DrzavaDAO implements DrzavaDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(H2DrzavaDAO.class);

    private static final String TABLE = "POSTAJE.DRZAVA";

    private static final String COLUMNS = "naziv, povrsina";

    private static final String RETURN_COLUMNS = "id, " + COLUMNS;

    private static final String SQL_FIND_BY_ID =
            "SELECT " + RETURN_COLUMNS + " FROM " + TABLE + " WHERE id = ?";

    private static final String SQL_FIND_BY_NAME =
            "SELECT " + RETURN_COLUMNS + " FROM " + TABLE + " WHERE naziv ILIKE ?";

    private static final String SQL_LIST_ORDER_BY_ID =
            "SELECT " + RETURN_COLUMNS + " FROM " + TABLE + " ORDER BY id";

    private static final String SQL_INSERT =
            "INSERT INTO " + TABLE + " (" + COLUMNS + ") VALUES (?, ?)";

    private static final String SQL_UPDATE =
            "UPDATE " + TABLE + " SET naziv = ?, povrsina = ? WHERE id = ?";

    private static final String SQL_DELETE =
            "DELETE FROM " + TABLE + " WHERE id = ?";

    @Override
    public Drzava get(Integer id) throws DAOException {
        Drzava drzava = null;
        try (Connection conn = H2DAOFactory.createConnection();
             PreparedStatement stmt = DAOUtil.prepareStatement(conn, SQL_FIND_BY_ID, false, id);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                drzava = map(rs);
            }
        } catch (SQLException ex) {
            LOGGER.error("Error while executing SQL_FIND_BY_ID: " + ex);
            throw new DAOException();
        }
        return drzava;
    }

    @Override
    public List<Drzava> getAll() throws DAOException {
        List<Drzava> drzave = new ArrayList<>();
        try (Connection conn = H2DAOFactory.createConnection();
             PreparedStatement stmt = DAOUtil.prepareStatement(conn, SQL_LIST_ORDER_BY_ID, false);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                drzave.add(map(rs));
            }
        } catch (SQLException ex) {
            LOGGER.error("Error while executing SQL_LIST_ORDER_BY_ID: " + ex);
            throw new DAOException();
        }
        return drzave;
    }

    @Override
    public int save(Drzava drzava) throws IllegalArgumentException, DAOException {
        if (drzava.getId() != 0) {
            LOGGER.error("Object is already created, the Object ID is not null or 0.");
            throw new IllegalArgumentException("Object is already created.");
        }
        Object[] values = {
                drzava.getNaziv(),
                drzava.getPovrsina()
        };

        try (Connection conn = H2DAOFactory.createConnection();
             PreparedStatement stmt = DAOUtil.prepareStatement(conn, SQL_INSERT, true, values)) {

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted == 0) {
                LOGGER.error("Error while executing SQL_INSERT, no rows affected/inserted.");
                throw new DAOException("Object creation failed, no rows affected.");
            }

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    Integer newId = rs.getInt(1);
                    drzava.setId(newId);
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
    public boolean update(Drzava drzava) throws IllegalArgumentException, DAOException {
        if (drzava.getId() == null) {
            LOGGER.error("Update denied. Object is not created yet, the Object ID is null.");
            throw new IllegalArgumentException("Unable to perform update. Object is not created yet.");
        }

        Object[] values = {
                drzava.getNaziv(),
                drzava.getPovrsina(),
                drzava.getId()
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
    public boolean delete(Drzava drzava) throws DAOException {
        Object[] values = {
                drzava.getId()
        };

        try (Connection conn = H2DAOFactory.createConnection();
             PreparedStatement stmt = DAOUtil.prepareStatement(conn, SQL_DELETE, false, values)) {

            int rowsDeleted = stmt.executeUpdate();

            if (rowsDeleted == 0) {
                LOGGER.error("Error while executing SQL_DELETE, no rows affected/inserted.");
                throw new DAOException("Unable to perform delete, no rows affected.");
            } else {
                drzava.setId(null);
            }
        } catch (SQLException ex) {
            LOGGER.error("Error while executing SQL_DELETE: " + ex);
            throw new DAOException();
        }
        return true;
    }

    @Override
    public List<Drzava> findByName(String name) throws DAOException {
        List<Drzava> drzave = new ArrayList<>();
        Object[] values = {
                "%" + name + "%"
        };
        try (Connection conn = H2DAOFactory.createConnection();
             PreparedStatement stmt = DAOUtil.prepareStatement(conn, SQL_FIND_BY_NAME, false, values);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                drzave.add(map(rs));
            }
        } catch (Exception ex) {
            LOGGER.error("Error while executing SQL_FIND_BY_NAME: " + ex);
            throw new DAOException();
        }
        return drzave;
    }

    private Drzava map(ResultSet resultSet) throws SQLException {
        Drzava drzava = new Drzava();
        drzava.setId(resultSet.getInt("id"));
        drzava.setNaziv(resultSet.getString("naziv"));
        drzava.setPovrsina(resultSet.getBigDecimal("povrsina"));
        return drzava;
    }
}
