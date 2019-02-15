package hr.java.vjezbe.database.H2;

import hr.java.vjezbe.database.H2DAOFactory;
import hr.java.vjezbe.database.MjernaPostajaDAO;
import hr.java.vjezbe.database.exceptions.DAOException;
import hr.java.vjezbe.database.helpers.AutoRollbackHelper;
import hr.java.vjezbe.database.util.DAOUtil;
import hr.java.vjezbe.javafx.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class H2MjernaPostajaDAO implements MjernaPostajaDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(H2MjernaPostajaDAO.class);

    private static final int POSTAJA_TYPE_SONDA = 1;

    private static final String TABLE = "POSTAJE.MJERNA_POSTAJA";

    private static final String TABLE_SONDA = "POSTAJE.MJERNA_POSTAJA_SONDAZNA";

    private static final String COLUMNS = "NAZIV, LAT, LNG, MJESTO_ID";

    private static final String COLUMNS_SONDA = "ID, VISINA";

    private static final String RETURN_COLUMNS = "p.ID, p.naziv, p.lat, p.lng, p.mjesto_id, s.visina, s.postaja_type_id";

    private static final String SELECT_JOIN =
            "SELECT " + RETURN_COLUMNS
                    + " FROM " + TABLE + " as p"
                    + " LEFT JOIN " + TABLE_SONDA + " AS s ON p.ID = s.ID";

    private static final String SQL_FIND_BY_ID = SELECT_JOIN + " WHERE p.ID = ?";

    private static final String SQL_FIND_BY_NAME = SELECT_JOIN + " WHERE p.naziv ILIKE ?";

    private static final String SQL_FIND_BY_MJESTO = SELECT_JOIN + " WHERE mjesto_id = ?";

    private static final String SQL_LIST_ORDER_BY_ID = SELECT_JOIN + " ORDER BY p.ID";

    private static final String SQL_INSERT =
            "INSERT INTO " + TABLE + " (" + COLUMNS + ") VALUES (?, ?, ?, ?)";
    private static final String SQL_INSERT_SONDA =
            "INSERT INTO " + TABLE_SONDA + " (" + COLUMNS_SONDA + ") VALUES (?, ?)";

    private static final String SQL_UPDATE =
            "UPDATE " + TABLE + " SET naziv = ?, lat = ?, lng = ?, mjesto_id = ? WHERE id = ?";
    private static final String SQL_UPDATE_SONDA =
            "UPDATE " + TABLE_SONDA + " SET visina = ? WHERE id = ?";

    private static final String SQL_DELETE =
            "DELETE FROM " + TABLE + " WHERE id = ?";


    @Override
    public MjernaPostaja get(Integer id) throws DAOException {
        MjernaPostaja postaja = null;
        try (Connection conn = H2DAOFactory.createConnection();
             PreparedStatement stmt = DAOUtil.prepareStatement(conn, SQL_FIND_BY_ID, false, id);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                postaja = map(rs);
            }
        } catch (SQLException ex) {
            LOGGER.error("Error while executing SQL_FIND_BY_ID: " + ex);
            throw new DAOException();
        }
        return postaja;
    }

    @Override
    public List<MjernaPostaja> getAll() throws DAOException {
        List<MjernaPostaja> postaje = new ArrayList<>();
        try (Connection conn = H2DAOFactory.createConnection();
             PreparedStatement stmt = DAOUtil.prepareStatement(conn, SQL_LIST_ORDER_BY_ID, false);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                postaje.add(map(rs));
            }
        } catch (SQLException ex) {
            LOGGER.error("Error while executing SQL_LIST_ORDER_BY_ID: " + ex);
            throw new DAOException();
        }
        return postaje;
    }

    @Override
    public int save(MjernaPostaja mjernaPostaja) throws IllegalArgumentException, DAOException {
        if (mjernaPostaja.getId() != 0) {
            LOGGER.error("Object is already created, the Object ID is not null or 0.");
            throw new IllegalArgumentException("Object is already created.");
        }

        Object[] values = {
                mjernaPostaja.getNaziv(),
                mjernaPostaja.getGeografskaTocka().getGeoX(),
                mjernaPostaja.getGeografskaTocka().getGeoY(),
                mjernaPostaja.getMjesto().getId()
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
                    mjernaPostaja.setId(newId);
                } else {
                    LOGGER.error("Creating failed, no generated key obtained.");
                    throw new DAOException();
                    // or return -1; (which mean insert failed)
                }
            }

            if (mjernaPostaja instanceof RadioSondaznaMjernaPostaja) {
                Object[] valuesSnd = {
                        mjernaPostaja.getId(),
                        ((RadioSondaznaMjernaPostaja) mjernaPostaja).dohvatiVisinuPostaje()
                };

                try (PreparedStatement ps = DAOUtil.prepareStatement(conn, SQL_INSERT_SONDA, false, valuesSnd)) {
                    int rowsInsertedSonda = ps.executeUpdate();
                    if (rowsInsertedSonda == 0) {
                        LOGGER.error("Error while executing SQL_INSERT_SONDA, no rows affected/inserted.");
                        throw new DAOException("Object creation failed, no rows affected/inserted.");
                    }
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
    public boolean update(MjernaPostaja mjernaPostaja) throws IllegalArgumentException, DAOException {
        if (mjernaPostaja.getId() == null) {
            LOGGER.error("Update denied. Object is not created yet, the Object ID is null.");
            throw new IllegalArgumentException("Unable to perform update. Object is not created yet.");
        }

        Object[] values = {
                mjernaPostaja.getNaziv(),
                mjernaPostaja.getGeografskaTocka().getGeoX(),
                mjernaPostaja.getGeografskaTocka().getGeoY(),
                mjernaPostaja.getMjesto().getId(),
                mjernaPostaja.getId()
        };
        try (Connection conn = H2DAOFactory.createConnection();
             PreparedStatement stmt = DAOUtil.prepareStatement(conn, SQL_UPDATE, false, values);
             AutoRollbackHelper ar = new AutoRollbackHelper(conn)) {

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated == 0) {
                LOGGER.error("Error while executing SQL_UPDATE, no rows affected/inserted.");
                throw new DAOException("Object update failed, no rows affected.");
            }

            if (mjernaPostaja instanceof RadioSondaznaMjernaPostaja) {
                Object[] valuesSnd = {
                        ((RadioSondaznaMjernaPostaja) mjernaPostaja).dohvatiVisinuPostaje(),
                        mjernaPostaja.getId()
                };

                try (PreparedStatement ps = DAOUtil.prepareStatement(conn, SQL_UPDATE_SONDA, false, valuesSnd)) {
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
    public boolean delete(MjernaPostaja mjernaPostaja) throws DAOException {
        Object[] values = {
                mjernaPostaja.getId()
        };

        try (Connection conn = H2DAOFactory.createConnection();
             PreparedStatement stmt = DAOUtil.prepareStatement(conn, SQL_DELETE, false, values)) {

            int rowsDeleted = stmt.executeUpdate();

            if (rowsDeleted == 0) {
                LOGGER.error("Error while executing SQL_DELETE, no rows affected/inserted.");
                throw new DAOException("Unable to perform delete, no rows affected.");
            } else {
                mjernaPostaja.setId(null);
            }

        } catch (SQLException ex) {
            LOGGER.error("Error while executing SQL_DELETE: " + ex);
            throw new DAOException();
        }
        return true;
    }

    @Override
    public List<MjernaPostaja> findByName(String name) throws DAOException {
        List<MjernaPostaja> postaje = new ArrayList<>();
        Object[] values = {
                "%" + name + "%"
        };
        try (Connection conn = H2DAOFactory.createConnection();
             PreparedStatement stmt = DAOUtil.prepareStatement(conn, SQL_FIND_BY_NAME, false, values);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                postaje.add(map(rs));
            }
        } catch (Exception ex) {
            LOGGER.error("Error while executing SQL_FIND_BY_NAME: " + ex);
            throw new DAOException();
        }
        return postaje;
    }

    @Override
    public List<MjernaPostaja> getAllByMjestoId(Integer mjestoId) throws DAOException {
        List<MjernaPostaja> postaje = new ArrayList<>();

        Object[] values = {
                mjestoId
        };
        try (Connection conn = H2DAOFactory.createConnection();
             PreparedStatement stmt = DAOUtil.prepareStatement(conn, SQL_FIND_BY_MJESTO, false, values);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                postaje.add(map(rs));
            }
        } catch (SQLException ex) {
            LOGGER.error("Error while executing SQL_FIND_BY_DRZAVA: " + ex);
            throw new DAOException();
        }
        return postaje;
    }

    private MjernaPostaja map(ResultSet resultSet) throws SQLException {
        MjernaPostaja postaja;
        if (resultSet.getInt("postaja_type_id") == POSTAJA_TYPE_SONDA) {
            postaja = new RadioSondaznaMjernaPostaja();
            ((RadioSondaznaMjernaPostaja) postaja).podesiVisinuPostaje(resultSet.getInt("visina"));
        } else {
            postaja = new MjernaPostaja();
        }
        postaja.setId(resultSet.getInt("id"));
        postaja.setNaziv(resultSet.getString("naziv"));
        BigDecimal geoX = resultSet.getBigDecimal("lat");
        BigDecimal geoY = resultSet.getBigDecimal("lng");
        postaja.setGeografskaTocka(new GeografskaTocka(geoX, geoY));
        Mjesto mjesto = new Mjesto();
        mjesto.setId(resultSet.getInt("mjesto_id"));
        postaja.setMjesto(mjesto);
        return postaja;
    }
}
