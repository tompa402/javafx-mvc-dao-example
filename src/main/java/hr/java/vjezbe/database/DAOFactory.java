package hr.java.vjezbe.database;

public abstract class DAOFactory {

    // List of DAO types supported by the factory
    public static final int H2 = 1;
    public static final int ORACLE = 2;
    public static final int SQLITE = 3;

    public abstract DrzavaDAO getDrzavaDAO();

    public abstract ZupanijaDAO getZupanijaDAO();

    public abstract MjestoDAO getMjestoDAO();

    public abstract MjernaPostajaDAO getMjernaPostajaDAO();

    public abstract SenzorDAO getSenzorDAO();

    public static DAOFactory getDaoFactory(int whichFactory) {
        switch (whichFactory) {
            case H2:
                return new H2DAOFactory();
            default:
                return null;
        }
    }
}
