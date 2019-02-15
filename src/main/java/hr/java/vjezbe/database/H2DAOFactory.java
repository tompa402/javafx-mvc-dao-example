package hr.java.vjezbe.database;

import hr.java.vjezbe.database.H2.*;
import hr.java.vjezbe.database.exceptions.DAOConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class H2DAOFactory extends DAOFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(H2DAOFactory.class);
    private static final String DATABASE_FILE = "src/main/resources/database.properties";

    public static Connection createConnection() throws SQLException {
        LOGGER.info("Creating connection");
        Properties properties = new Properties();
        try (InputStream in = new FileInputStream(DATABASE_FILE)) {
            properties.load(in);
        } catch (IOException ex) {
            LOGGER.info("Failed to load database properties: " + ex);
            throw new DAOConfigurationException(ex);
        }
        String urlBazePodataka = properties.getProperty("bazaPodatakaUrl");
        String korisnickoIme = properties.getProperty("korisnickoIme");
        String lozinka = properties.getProperty("lozinka");
        return DriverManager.getConnection(urlBazePodataka,
                korisnickoIme, lozinka);
    }

    @Override
    public DrzavaDAO getDrzavaDAO() {
        return new H2DrzavaDAO();
    }

    @Override
    public ZupanijaDAO getZupanijaDAO() {
        return new H2ZupanijaDAO();
    }

    @Override
    public MjestoDAO getMjestoDAO() {
        return new H2MjestoDAO();
    }

    @Override
    public MjernaPostajaDAO getMjernaPostajaDAO() {
        return new H2MjernaPostajaDAO();
    }

    @Override
    public SenzorDAO getSenzorDAO() {
        return new H2SenzorDAO();
    }
}
