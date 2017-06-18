package modelHost;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import util.DatabaseConnectionPool;

public class SqlNative {

    private SqlResult result;
    private static boolean DEBUG = true;
    private static SqlNative instance;

    private SqlNative() {
    }

    public static SqlNative getInstance() {
        if (instance == null) {
            instance = new SqlNative();
        }
        return instance;
    }

    public SqlResult executeQuery(String query) {
        SqlAction action = null;
        if (query == null) {
            throw new IllegalArgumentException("query should not be null");
        }
        if (query.trim().isEmpty()) {
            throw new IllegalArgumentException("query should not be empty");
        }
        query = query.trim();
        query = query.replaceAll("  ", " ");

        String typeOfQuery = query.split(" ")[0].toLowerCase();
        switch (typeOfQuery) {
            case "create":
                action = SqlAction.CREATE;
                break;
            case "select":
            case "show":
                action = SqlAction.SELECT;
                break;
            case "delete":
                action = SqlAction.DELETE;
                break;
            case "update":
                action = SqlAction.UPDATE;
                break;
            case "drop":
                action = SqlAction.DROP;
                break;
            case "insert":
                action = SqlAction.INSERT;
                break;
            case "alter":
                action = SqlAction.ALTER;
                break;
            default:
                action = SqlAction.OTHER;
            //throw new IllegalArgumentException("Unknown type of query: " + query);
        }
        SqlResult sqlResult = executeSql(query, action);
        return sqlResult;
    }

    public void createTable(String sql) {
        executeSql(sql, SqlAction.CREATE);
    }

    public void dropTable(String name) {
        executeSql("drop table " + name, SqlAction.DROP);
    }

    public void selectRowsFromTable(String table, String columns, String condition) {
        String sql = String.format("select %s from %s where 1=1 %s ",
                columns, table, condition == null || condition.trim().isEmpty() ? "" : " and " + condition.trim());
        executeSql(sql, SqlAction.SELECT);
    }

    public void insertRowIntoTable(String table, String columns, String values) {
        String sql = String.format("insert into %s (%s) values (%s) ", table, columns, values);
        executeSql(sql, SqlAction.INSERT);
    }

    public void deleteRowsInTable(String table, String condition) {
        String sql = String.format("delete from %s where 1=1 %s ",
                table, condition == null || condition.trim().isEmpty() ? "" : " and " + condition.trim());
        executeSql(sql, SqlAction.DELETE);
    }

    public void updateRowsInTable(String table, String updateSet, String condition) {
        String sql = String.format("update %s set %s where 1=1 %s ",
                table, updateSet, condition == null || condition.trim().isEmpty() ? "" : " and " + condition.trim());
        executeSql(sql, SqlAction.UPDATE);
    }

    private SqlResult executeSql(String sql, SqlAction action) {
        // List<String[]> bigList = new ArrayList<>();
        SqlResult sqlResult = new SqlResult();
        Connection conn = null;
        Statement stmt = null;
        int records = 0;
        StringBuffer strForConsole = new StringBuffer();
        ResultSet rs = null;
        try {
            conn = DatabaseConnectionPool.getInstance().getConnection();
            switch (action) {
                case CREATE:
                    strForConsole.append("Creating table in given database...");
                    break;
                case DROP:
                    strForConsole.append("Droping table in given database...");
                    break;
                case SELECT:
                    strForConsole.append("Selecting rows in given table...");
                    break;
                case INSERT:
                    strForConsole.append("Inserting row in given table...");
                    break;
                case UPDATE:
                    strForConsole.append("Updating row in given table...");
                    break;
                case DELETE:
                    strForConsole.append("Deleting row in given table...");
                    break;
                case ALTER:
                    strForConsole.append("Alter schema...");
                    break;
                case OTHER:
                    strForConsole.append("Other command...");
                    break;

            }
            stmt = conn.createStatement();
            if (action == action.SELECT) {
                rs = stmt.executeQuery(sql);
                sqlResult = getSqlResultFromResultSet(rs);
                sqlResult.setTableName(rs.getMetaData().getTableName(1));
                //records = printSelectToConsole(rs);
                records = sqlResult.getDataTable().length;
            } else {
                records = stmt.executeUpdate(sql);
            }
            strForConsole.append(sqlResult.getTableName());
            strForConsole.append("<br/>");
            strForConsole.append("<br/>" + "Задействовано " + records + " строк.");
            //System.out.println("Задействовано " + records + " строк. Result positive :)");
            //System.out.println("Created table in given database...");
        } catch (SQLException se) {
            strForConsole.append("<br/>" + "Result negative (: " + se.getMessage());
            printException(se);
        } catch (Exception e) {
            strForConsole.append("<br/>" + "Result negative (: " + e.getMessage());
            printException(e);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se) {
                printException(se);
            }
            try {
                DatabaseConnectionPool.getInstance().freeConnection(conn);
            } catch (Exception se) {
                printException(se);
            }
        }
        sqlResult.setResultMessage(strForConsole.toString());
        return sqlResult;
    }

    void fillTable(String[] insertSql) {
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = DatabaseConnectionPool.getInstance().getConnection();
            System.out.println("Insert Rows in given tabale");
            stmt = conn.createStatement();
            for (String string : insertSql) {
                System.out.println("Workin with insert: " + string);
                stmt.addBatch(string);
            }
            stmt.executeBatch();
            System.out.println("Insetion finished ...");
        } catch (SQLException e) {
            printException(e);
        } catch (Exception e) {
            printException(e);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                printException(e);
            }
            try {
                DatabaseConnectionPool.getInstance().freeConnection(conn);
            } catch (Exception e) {
                printException(e);
            }
        }
        System.out.println("Goodbye!");
    }

    private int printSelectToConsole(ResultSet rs) throws SQLException {
        int records = 0;
        int columnCount = rs.getMetaData().getColumnCount();
        for (int colNum = 1; colNum < columnCount + 1; colNum++) {
            if (colNum == 1) {
                System.out.print("\t");
            }
            System.out.print("" + rs.getMetaData().getColumnName(colNum) + "\t");
            if (colNum == columnCount) {
                System.out.println("");
            }
        }
        while (rs.next()) {
            records++;
            for (int colNum = 1; colNum < columnCount + 1; colNum++) {
                if (colNum == 1) {
                    System.out.print("row_" + records + "\t" + rs.getString(colNum) + "\t");
                } else {
                    System.out.print(rs.getString(colNum) + "\t");
                }
            }
            System.out.println("");
        }
        return records;
    }

    private SqlResult getSqlResultFromResultSet(ResultSet rs) throws SQLException {
        SqlResult sqlResult = new SqlResult();
        //int records = 0;
        int columnCount = rs.getMetaData().getColumnCount();

        String[] colNames = new String[columnCount];
        List<String[]> list = new ArrayList<>();

        for (int colNum = 0; colNum < columnCount; colNum++) {
            colNames[colNum] = rs.getMetaData().getColumnName(colNum + 1);
        }
        sqlResult.setColumns(colNames);
        while (rs.next()) {
            //records++;
            String[] row = new String[columnCount];
            for (int colNum = 0; colNum < columnCount; colNum++) {
                row[colNum] = rs.getString(colNum + 1);
            }
            list.add(row);
        }
        String[][] arrTwoDimen = list.toArray(new String[list.size()][]);
        sqlResult.setDataTable(arrTwoDimen);
        //return records;
        return sqlResult;
    }

    private int getSelect(ResultSet rs) throws SQLException {
        int records = 0;
        int columnCount = rs.getMetaData().getColumnCount();
        for (int colNum = 1; colNum < columnCount + 1; colNum++) {
            if (colNum == 1) {
                System.out.print("\t");
            }
            System.out.print("" + rs.getMetaData().getColumnName(colNum) + "\t");
            if (colNum == columnCount) {
                System.out.println("");
            }
        }
        while (rs.next()) {
            records++;
            for (int colNum = 1; colNum < columnCount + 1; colNum++) {
                if (colNum == 1) {
                    System.out.print("row_" + records + "\t" + rs.getString(colNum) + "\t");
                } else {
                    System.out.print(rs.getString(colNum) + "\t");
                }
            }
            System.out.println("");
        }
        return records;
    }

    private void printException(Exception ex) {
        System.err.println(ex.getMessage());
        if (DEBUG) {
            ex.printStackTrace();
        }
    }

}
