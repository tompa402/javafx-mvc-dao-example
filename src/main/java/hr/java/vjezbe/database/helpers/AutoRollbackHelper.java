package hr.java.vjezbe.database.helpers;

import java.sql.Connection;
import java.sql.SQLException;

public class AutoRollbackHelper implements AutoCloseable {

    private Connection connection;
    private boolean isCommitted;

    public AutoRollbackHelper(Connection connection) throws SQLException {
        connection.setAutoCommit(false);
        this.connection = connection;
    }

    public void commit() throws SQLException {
        connection.commit();
        this.isCommitted = true;
    }

    @Override
    public void close() throws SQLException {
        if(!isCommitted){
            connection.rollback();
        }
    }
}
