package kz.biostat.lishostmanager.comport.util;

import java.sql.*;

public class DbConnection {

//    public final static String DRIVER = "com.mysql.jdbc.Driver";
    public final static String DRIVER = "com.mysql.cj.jdbc.Driver";
    //public final static String HOST = "localhost";
    public final static String HOST = "127.0.0.1"; // использовать этот для localhost (в SUSE надежнее)
    public final static String PORT = "3306";


    /* for assuta working */
    public final static String USER = "lisadmin";
    public final static String PASSWORD = "1qaz_2wsx";

    /* for local working */
    //    public final static String USER = "root";
    //    public final static String PASSWORD = "";

    public final static String DB_NAME = "lis";

    public static Connection getConnection() {
        return getConnection(DB_NAME);
    }

    public static Connection getConnection(String host, String port, String dbName, String user, String password) {
        Connection connection = null;

        try {
            Class.forName(DRIVER).newInstance();
            connection = DriverManager.getConnection(String.format("jdbc:mysql://%s:%s/",
                    host, port) + dbName + "?useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC", user, password);
            if (!connection.isClosed()) {
                //System.out.println("Successfully connected to " + "MySQL server using TCP/IP...");
            }
        } catch (SQLException ex) {
            System.out.println("------***------- SQLException in getConnection method ------***-------");
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        } catch (Exception ex) {
            System.out.println("------------- Other Ecxeption getConnection method -------------");
        }
        return connection;
    }

    public static Connection getConnection(String dbName) {
        return getConnection(HOST, PORT, dbName, USER, PASSWORD);
    }
}
