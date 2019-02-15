package hr.java.vjezbe.database.exceptions;

public class DAOConfigurationException extends RuntimeException {

    public DAOConfigurationException() {
        super("Failed to execute SQL statement, something unexpected happen.");
    }

    public DAOConfigurationException(String message) {
        super(message);
    }

    public DAOConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public DAOConfigurationException(Throwable cause) {
        super(cause);
    }
}
