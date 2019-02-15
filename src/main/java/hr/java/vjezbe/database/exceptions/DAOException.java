package hr.java.vjezbe.database.exceptions;

/**
 * Represents a generic DAO exception. It should wrap any exception of the underlying code, such as SQLException
 *
 * @author Tomislav
 */
public class DAOException extends RuntimeException {

    public DAOException() {
        super("Failed to execute SQL statement, something unexpected happen.");
    }

    public DAOException(String message) {
        super(message);
    }

    public DAOException(String message, Throwable cause) {
        super(message, cause);
    }

    public DAOException(Throwable cause) {
        super(cause);
    }
}
