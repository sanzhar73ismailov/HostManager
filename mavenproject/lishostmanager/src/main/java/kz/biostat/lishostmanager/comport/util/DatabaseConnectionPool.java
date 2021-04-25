package kz.biostat.lishostmanager.comport.util;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Vector;

import kz.biostat.lishostmanager.comport.modelHost.DaoException;

public class DatabaseConnectionPool {

    private String driverName;
    private String password;
    private String url;
    private String user;
    private Driver driver;
    private Vector freeConnections;
    private final static int MAX_CONN_NUMBER = 10;
    private int count;
    public final static String dburlDefault = String.format("jdbc:mysql://%s:%s/%s", DbConnection.HOST, DbConnection.PORT, DbConnection.DB_NAME);
    public final static String driverDefault = DbConnection.DRIVER;
    public final static String sUserDefault = DbConnection.USER;
    public final static String sPwdDefault = DbConnection.PASSWORD;
    private static DatabaseConnectionPool instance;

    public static DatabaseConnectionPool getInstance() throws SQLException {
        if (instance == null) {
            instance = new DatabaseConnectionPool();
        }
        return instance;
    }

    private DatabaseConnectionPool() throws SQLException {
        this(driverDefault, dburlDefault, sUserDefault, sPwdDefault);
    }

    private DatabaseConnectionPool(String drivername, String conUrl,
            String conuser, String conpassword) throws SQLException {
        freeConnections = new Vector();
        driverName = drivername;
        url = conUrl;
        user = conuser;
        password = conpassword;
        try {
            driver = (Driver) Class.forName(driverName).newInstance();
            DriverManager.registerDriver(driver);
        } catch (Exception ex) {
            throw new SQLException();
        }
        count = 0;
    }

    /**
     * Method to destroy all connections.
     */
    public void destroy() {
        closeAll();
        try {
            DriverManager.deregisterDriver(driver);
            return;
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    /**
     * Method to add free connections in to pool.
     *
     * @param connection
     */
    public synchronized void freeConnection(Connection connection) throws DaoException {
        if (count > MAX_CONN_NUMBER) {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException ex) {
                throw new DaoException(ex);
            }
        } else {
            freeConnections.addElement(connection);
            notifyAll();
        }
        count--;
    }

    /**
     * Method to get connections.
     *
     * @return Connection
     */
    public synchronized Connection getConnection() {
        Connection connection = null;
        if (freeConnections.size() > 0) {
            connection = (Connection) freeConnections.elementAt(0);
            freeConnections.removeElementAt(0);
            try {
                if (connection.isClosed()) {
                    connection = getConnection();
                }
            } catch (Exception e) {
                print(e.getMessage());
                connection = getConnection();
            }
            return connection;
        } else {
            connection = newConnection();
            if (connection != null) {
                count++;
                //print("NEW CONNECTION N " + count + " CREATED");
            }
        }
        return connection;
    }

    /**
     * Method to close all resources
     */
    private synchronized void closeAll() {
        for (Enumeration enumeration = freeConnections.elements(); enumeration
                .hasMoreElements();) {
            Connection connection = (Connection) enumeration.nextElement();
            try {
                connection.close();
            } catch (Exception e) {
                print(e.getMessage());
            }
        }
        freeConnections.removeAllElements();
    }

    /**
     * Method to create new connection object.
     *
     * @return Connection.
     */
    private Connection newConnection() {
        return DbConnection.getConnection();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize(); //To change body of generated methods, choose Tools | Templates.
        DriverManager.deregisterDriver(driver);
    }

    private void print(String print) {
        System.out.println(print);
    }
}
