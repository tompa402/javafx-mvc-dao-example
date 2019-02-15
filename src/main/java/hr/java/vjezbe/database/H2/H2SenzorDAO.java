package hr.java.vjezbe.database.H2;

import hr.java.vjezbe.database.H2DAOFactory;
import hr.java.vjezbe.database.SenzorDAO;
import hr.java.vjezbe.database.exceptions.DAOException;
import hr.java.vjezbe.database.helpers.AutoRollbackHelper;
import hr.java.vjezbe.database.util.DAOUtil;
import hr.java.vjezbe.javafx.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class H2SenzorDAO implements SenzorDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(H2SenzorDAO.class);

    private static final int SENZOR_TYPE_TEMP = 1;
    private static final int SENZOR_TYPE_VLAGA = 2;
    private static final int SENZOR_TYPE_TLAK = 3;

    private static final String TABLE = "POSTAJE.SENZOR";
    private static final String TABLE_TEMP = "POSTAJE.SENZOR_TEMPERATURE";
    private static final String TABLE_VLAGA = "POSTAJE.SENZOR_VLAGE";
    private static final String TABLE_TLAK = "POSTAJE.SENZOR_TLAKA";

    private static final String COLUMNS = "MJERNA_JEDINICA, PRECIZNOST, VRIJEDNOST, RAD_SENZORA, AKTIVAN, MJERNA_POSTAJA_ID";
    private static final String COLUMNS_TEMP = "ID, NAZIV";
    private static final String COLUMNS_VLAGA = "ID";
    private static final String COLUMNS_TLAK = "ID";

    private static final String RETURN_COLUMNS = "s.*, stemp.NAZIV, COALESCE(stemp.SENZOR_TYPE_ID, sv.SENZOR_TYPE_ID, stl.SENZOR_TYPE_ID) AS SENZOR_TYPE_ID";

    private static final String SELECT_JOIN =
            "SELECT " + RETURN_COLUMNS
                    + " FROM " + TABLE + " as s"
                    + " LEFT JOIN " + TABLE_TEMP + " AS stemp ON s.ID = stemp.ID"
                    + " LEFT JOIN " + TABLE_VLAGA + " AS sv ON s.ID = sv.ID"
                    + " LEFT JOIN " + TABLE_TLAK + " AS stl ON s.ID = stl.ID";

    private static final String SQL_FIND_BY_ID = SELECT_JOIN + " WHERE s.ID = ?";

    private static final String SQL_FIND_BY_POSTAJA = SELECT_JOIN + " WHERE MJERNA_POSTAJA_ID = ?";

    private static final String SQL_LIST_ORDER_BY_ID = SELECT_JOIN + " ORDER BY s.ID";

    private static final String SQL_INSERT =
            "INSERT INTO " + TABLE + " (" + COLUMNS + ") VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_INSERT_TEMP =
            "INSERT INTO " + TABLE_TEMP + " (" + COLUMNS_TEMP + ") VALUES (?, ?)";
    private static final String SQL_INSERT_VLAGA =
            "INSERT INTO " + TABLE_VLAGA + " (" + COLUMNS_VLAGA + ") VALUES (?)";
    private static final String SQL_INSERT_TLAK =
            "INSERT INTO " + TABLE_TLAK + " (" + COLUMNS_TLAK + ") VALUES (?)";

    private static final String SQL_UPDATE =
            "UPDATE " + TABLE + " SET MJERNA_JEDINICA = ?, PRECIZNOST = ?, VRIJEDNOST = ?, RAD_SENZORA = ?, AKTIVAN = ?, MJERNA_POSTAJA_ID = ? WHERE ID = ?";
    private static final String SQL_UPDATE_TEMP =
            "UPDATE " + TABLE_TEMP + " SET NAZIV = ? WHERE ID = ?";

    private static final String SQL_DELETE =
            "DELETE FROM " + TABLE + " WHERE ID = ?";

    @Override
    public Senzor get(Integer id) throws DAOException {
        Senzor senzor = null;
        try (Connection conn = H2DAOFactory.createConnection();
             PreparedStatement stmt = DAOUtil.prepareStatement(conn, SQL_FIND_BY_ID, false, id);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                senzor = map(rs);
            }
        } catch (SQLException ex) {
            LOGGER.error("Error while executing SQL_FIND_BY_ID: " + ex);
            throw new DAOException();
        }
        return senzor;
    }

    @Override
    public List<Senzor> getAll() throws DAOException {
        List<Senzor> senzori = new ArrayList<>();
        try (Connection conn = H2DAOFactory.createConnection();
             PreparedStatement stmt = DAOUtil.prepareStatement(conn, SQL_LIST_ORDER_BY_ID, false);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                senzori.add(map(rs));
            }
        } catch (SQLException ex) {
            LOGGER.error("Error while executing SQL_LIST_ORDER_BY_ID: " + ex);
            throw new DAOException();
        }
        return senzori;
    }

    @Override
    public int save(Senzor senzor) throws IllegalArgumentException, DAOException {
        if (senzor.getId() != 0) {
            LOGGER.error("Object is already created, the Object ID is not null or 0.");
            throw new IllegalArgumentException("Object is already created.");
        }

        Object[] values = {
                senzor.getMjernaJedinica(),
                senzor.getPreciznost(),
                senzor.getVrijednost(),
                senzor.getRadSenzora().toString(),
                senzor.getActive(),
                senzor.getPostaja().getId()
        };

        try (Connection conn = H2DAOFactory.createConnection();
             PreparedStatement stmt = DAOUtil.prepareStatement(conn, SQL_INSERT, true, values);
             AutoRollbackHelper ar = new AutoRollbackHelper(conn)) {

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted == 0) {
                LOGGER.error("Error while executing SQL_INSERT, no rows affected/inserted.");
                throw new DAOException("Object creation failed, no rows affected/inserted.");
            }

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    Integer newId = rs.getInt(1);
                    senzor.setId(newId);
                } else {
                    LOGGER.error("Creating failed, no generated key obtained.");
                    throw new DAOException();
                    // or return -1; (which mean insert failed)
                }
            }

            Map<String, Object> data = prepareChildSaveData(senzor);

            try (PreparedStatement ps = DAOUtil.prepareStatement(conn, (String) data.get("sql"), false, (Object[]) data.get("values"))) {
                int rowsInsertedSonda = ps.executeUpdate();
                if (rowsInsertedSonda == 0) {
                    LOGGER.error("Error while executing SQL_INSERT_SONDA, no rows affected/inserted.");
                    throw new DAOException("Object creation failed, no rows affected/inserted.");
                }
            }
            ar.commit();
        } catch (SQLException ex) {
            LOGGER.error("Error while executing SQL_INSERT: " + ex);
            throw new DAOException();
        }
        return 1;
    }

    @Override
    public boolean update(Senzor senzor) throws IllegalArgumentException, DAOException {
        if (senzor.getId() == null) {
            LOGGER.error("Update denied. Object is not created yet, the Object ID is null.");
            throw new IllegalArgumentException("Unable to perform update. Object is not created yet.");
        }

        Object[] values = {
                senzor.getMjernaJedinica(),
                senzor.getPreciznost(),
                senzor.getVrijednost(),
                senzor.getRadSenzora().toString(),
                senzor.getActive(),
                senzor.getPostaja().getId(),
                senzor.getId()
        };

        try (Connection conn = H2DAOFactory.createConnection();
             PreparedStatement stmt = DAOUtil.prepareStatement(conn, SQL_UPDATE, false, values);
             AutoRollbackHelper ar = new AutoRollbackHelper(conn)) {

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated == 0) {
                LOGGER.error("Error while executing SQL_UPDATE, no rows affected/inserted.");
                throw new DAOException("Object update failed, no rows affected.");
            }

            if (senzor instanceof SenzorTemperature) {
                Object[] valuesST = {
                        ((SenzorTemperature) senzor).getNaziv(),
                        senzor.getId()
                };

                try (PreparedStatement ps = DAOUtil.prepareStatement(conn, SQL_UPDATE_TEMP, false, valuesST)) {
                    int rowsInsertedSonda = ps.executeUpdate();
                    if (rowsInsertedSonda == 0) {
                        LOGGER.error("Error while executing SQL_UPDATE_SONDA, no rows affected/inserted.");
                        throw new DAOException("Object creation failed, no rows affected/inserted.");
                    }
                }
            }
            ar.commit();
        } catch (SQLException ex) {
            LOGGER.error("Error while executing SQL_UPDATE: " + ex);
            throw new DAOException();
        }
        return true;
    }


    @Override
    public boolean delete(Senzor senzor) throws DAOException {
        Object[] values = {
                senzor.getId()
        };

        try (Connection conn = H2DAOFactory.createConnection();
             PreparedStatement stmt = DAOUtil.prepareStatement(conn, SQL_DELETE, false, values)) {

            int rowsDeleted = stmt.executeUpdate();

            if (rowsDeleted == 0) {
                LOGGER.error("Error while executing SQL_DELETE, no rows affected/inserted.");
                throw new DAOException("Unable to perform delete, no rows affected.");
            } else {
                senzor.setId(null);
            }

        } catch (SQLException ex) {
            LOGGER.error("Error while executing SQL_DELETE: " + ex);
            throw new DAOException();
        }
        return true;
    }

    @Override
    public List<Senzor> getAllByPostajaId(Integer postajaId) throws DAOException {
        List<Senzor> senzori = new ArrayList<>();

        Object[] values = {
                postajaId
        };
        try (Connection conn = H2DAOFactory.createConnection();
             PreparedStatement stmt = DAOUtil.prepareStatement(conn, SQL_FIND_BY_POSTAJA, false, values);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                senzori.add(map(rs));
            }
        } catch (SQLException ex) {
            LOGGER.error("Error while executing SQL_FIND_BY_DRZAVA: " + ex);
            throw new DAOException();
        }
        return senzori;
    }

    private Map<String, Object> prepareChildSaveData(Senzor senzor) {
        Map<String, Object> items = new HashMap<>();

        if (senzor instanceof SenzorTemperature) {
            Object[] values = {
                    senzor.getId(),
                    ((SenzorTemperature) senzor).getNaziv()
            };
            items.put("sql", SQL_INSERT_TEMP);
            items.put("values", values);
            return items;
        } else if (senzor instanceof SenzorTlaka) {
            Object[] values = {
                    senzor.getId()
            };
            items.put("sql", SQL_INSERT_TLAK);
            items.put("values", values);
            return items;
        } else {
            Object[] values = {
                    senzor.getId()
            };
            items.put("sql", SQL_INSERT_VLAGA);
            items.put("values", values);
            return items;
        }
    }

    /**
     * Should prepare data for updating child Senzor elements. But since only SenzorTemperature have additional value
     * this method isn't needed for now.
     *
     * @param senzor
     * @return
     */
    private Map<String, Object> prepareChildUpdateData(Senzor senzor) {
        throw new UnsupportedOperationException();
    }


    private Senzor map(ResultSet resultSet) throws SQLException {
        Senzor senzor;
        if (resultSet.getInt("SENZOR_TYPE_ID") == SENZOR_TYPE_TEMP) {
            senzor = new SenzorTemperature();
            ((SenzorTemperature) senzor).setNaziv(resultSet.getString("NAZIV"));
        } else if (resultSet.getInt("SENZOR_TYPE_ID") == SENZOR_TYPE_VLAGA) {
            senzor = new SenzorVlage();
        } else {
            senzor = new SenzorTlaka();
        }
        senzor.setId(resultSet.getInt("ID"));
        senzor.setMjernaJedinica(resultSet.getString("MJERNA_JEDINICA"));
        senzor.setPreciznost(resultSet.getDouble("PRECIZNOST"));
        senzor.setVrijednost(resultSet.getBigDecimal("VRIJEDNOST"));
        senzor.setRadSenzora(RadSenzora.valueOf(resultSet.getString("RAD_SENZORA")));
        //senzor.setActive(resultSet.getBoolean("AKTIVAN"));
        MjernaPostaja postaja = new MjernaPostaja();
        postaja.setId(resultSet.getInt("MJERNA_POSTAJA_ID"));
        senzor.setPostaja(postaja);
        return senzor;
    }
}
