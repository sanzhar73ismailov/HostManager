package kz.biostat.lishostmanager.comport.modelHost;

import kz.biostat.lishostmanager.comport.entity.HostDictionary;
import kz.biostat.lishostmanager.comport.modelHost.query.Query;
import kz.biostat.lishostmanager.comport.modelHost.query.QueryCreator;
import kz.biostat.lishostmanager.comport.modelHost.query.QueryOperation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import kz.biostat.lishostmanager.comport.util.DatabaseConnectionPool;

public class DaoMysqlImpl implements Dao {

    DatabaseConnectionPool conPool;

    public DaoMysqlImpl() throws DaoException {
        try {
            conPool = DatabaseConnectionPool.getInstance();
        } catch (SQLException ex) {
            throw new DaoException(ex);
        }

    }

    @Override
    public <T extends HostDictionary> T getObject(int id, T obj) throws DaoException {
        T objectToReturn = null;
        Query<T> queryObj = QueryCreator.createQueryToGetOneInstance(obj, id);
        String query = queryObj.getQueryById();
        Connection con = conPool.getConnection();
        try (
                 Statement st = con.createStatement();  
                 ResultSet rs = st.executeQuery(query)) {
            if (rs.next()) {
                objectToReturn = queryObj.getObjectFromResultSet(rs);
            }
        } catch (SQLException ex) {
            throw new DaoException(ex.getMessage(), ex);
        } finally {
            conPool.freeConnection(con);
        }
        return objectToReturn;
    }

    @Override
    public <T extends HostDictionary> int insertObject(T obj) throws DaoException {
        int returnId = 0;
        Query<T> queryObj = QueryCreator.createQueryToGetOneInstance(obj, 0);
        String insertTableSQL = queryObj.getInsertQuery();
        Connection con = conPool.getConnection();
        try (
                 PreparedStatement preparedStatement = con.prepareStatement(insertTableSQL, Statement.RETURN_GENERATED_KEYS);
            ) {
            queryObj.setParamsToPreparedStatement(preparedStatement, obj, QueryOperation.INSERT);
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                returnId = (int) generatedKeys.getLong(1);
            } else {
                throw new DaoException("not created workorder, ID not generated!");
            }
        } catch (SQLException e) {
            throw new DaoException("not created workorder: " + e.getMessage(), e);
        } finally {
            conPool.freeConnection(con);
        }
        return returnId;
    }

    @Override
    public <T extends HostDictionary> int removeObject(int id, T obj) throws DaoException {
        Connection con = conPool.getConnection();
        Query<T> queryObj = QueryCreator.createQueryToGetOneInstance(obj, id);
        int rows = 0;
        try ( Statement st = con.createStatement()) {
            rows = st.executeUpdate(queryObj.getDeleteQuery());
        } catch (SQLException ex) {
            throw new DaoException(ex.getMessage(), ex);
        } finally {
            conPool.freeConnection(con);
        }
        return rows;
    }

    @Override
    public <T extends HostDictionary> int updateObject(T object) throws DaoException {
        Connection con = conPool.getConnection();
        int rowCount = 0;
        Query<T> queryObj = QueryCreator.createQueryToGetOneInstance(object, 0);
        try ( PreparedStatement preparedStatement = con.prepareStatement(queryObj.getUpdateQuery())) {
            queryObj.setParamsToPreparedStatement(preparedStatement, object, QueryOperation.UPDATE);
            System.out.println("getUpdateQuery = " + queryObj.getUpdateQuery());
            rowCount = preparedStatement.executeUpdate();
            if (rowCount < 1) {
                throw new DaoException("not updated object with id " + object.getId());
            }
        } catch (SQLException e) {
            throw new DaoException("not updated workorder: " + e.getMessage(), e);
        } finally {
            conPool.freeConnection(con);
        }
        return rowCount;
    }

    @Override
    public <T extends HostDictionary> List<T> getObjects(Properties properties, T obj) throws DaoException {
        return getObjects(properties, obj, " order by id");
    }

    @Override
    public <T extends HostDictionary> List<T> getObjects(Properties properties, T obj, String orderBy) throws DaoException {
        return getObjects(properties, obj, orderBy, 0);
    }

    @Override
    public <T extends HostDictionary> List<T> getObjects(Properties properties, T obj, String orderBy, int limit) throws DaoException {
        List<T> list = new ArrayList<>();
        Query<T> queryObj = QueryCreator.createQueryToGetOneInstance(obj, 0);
        StringBuilder where = new StringBuilder();
        String limitQuery = "";
        if (properties != null) {
            Set keys = properties.keySet();
            Iterator iterator = keys.iterator();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                String value = properties.getProperty(key);
                where.append(" AND ").append(key).append(value);
            }
        }
        if (orderBy == null) {
            orderBy = "";
        }
        if (limit > 0) {
            limitQuery = " LIMIT " + limit;
        }
        String query = queryObj.getSelectQuery() + where.toString() + " " + orderBy + limitQuery;
        System.out.println("          Query: ");
        System.out.println(query);
        System.out.println("          >>>>>>>>>");
        Connection con = conPool.getConnection();
        try (
                 Statement st = con.createStatement();  
                 ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                T objElement = queryObj.getObjectFromResultSet(rs);
                list.add(objElement);
            }
        } catch (SQLException ex) {
            throw new DaoException(ex.getMessage(), ex);
        } finally {
            conPool.freeConnection(con);
        }
        System.out.println("list.size()=" + list.size());
        return list;
    }

    @Override
    public <T extends HostDictionary> int removeObjects(Properties properties, T obj) throws DaoException {
        int rows = 0;
        Query<T> queryObj = QueryCreator.createQueryToGetOneInstance(obj, 0);
        StringBuilder where = new StringBuilder();
        Set keys = properties.keySet();
        Iterator iterator = keys.iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            String value = properties.getProperty(key);
            where.append(" AND ").append(key).append(value);
        }
        String query = "DELETE FROM " + queryObj.getTableName() + " WHERE 1=1 " + where.toString();
        Connection con = conPool.getConnection();
        try ( Statement st = con.createStatement()) {
            rows = st.executeUpdate(query);
        } catch (SQLException ex) {
            throw new DaoException(ex.getMessage(), ex);
        } finally {
            conPool.freeConnection(con);
        }
        return rows;
    }

    @Override
    public <T extends HostDictionary> int updatePropertyOfObject(int id, T object, String setString) throws DaoException {
        Connection con = conPool.getConnection();
        int rowCount = 0;
        Query<T> queryObj = QueryCreator.createQueryToGetOneInstance(object, id);
        String query = "UPDATE " + queryObj.getTableName() + " " + setString + " WHERE id=" + id;
        try ( Statement st = con.createStatement()) {
            rowCount = st.executeUpdate(query);
            if (rowCount < 1) {
                throw new DaoException("not updated object with id " + object.getId());
            }
        } catch (SQLException e) {
            throw new DaoException("not updated workorder: " + e.getMessage(), e);
        } finally {
            conPool.freeConnection(con);
        }
        return rowCount;
    }

    @Override
    public int getMaxResultVersion(String instrument, String sid, String testCode) throws DaoException {
        Connection con = conPool.getConnection();
        int value = 0;
        String query = String.format("SELECT max(version) FROM result WHERE instrument='%s' AND sid='%s' AND test_code='%s'",
                instrument, sid, testCode);
        try ( Statement st = con.createStatement();  ResultSet rs = st.executeQuery(query)) {
            if (rs.next()) {
                value = rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            conPool.freeConnection(con);
        }
        return value;
    }

    @Override
    public int getMaxResultVersion(String instrument, String sid) throws DaoException {
        Connection con = conPool.getConnection();
        int value = 0;
        String query = String.format("SELECT max(version) FROM result WHERE instrument='%s' AND sid='%s'",
                instrument, sid);
        try ( Statement st = con.createStatement();  
              ResultSet rs = st.executeQuery(query)) {
            if (rs.next()) {
                value = rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            conPool.freeConnection(con);
        }
        return value;
    }

    @Override
    public int delete(String tableName, Properties properties) throws DaoException {
        Connection con = conPool.getConnection();
        int rows = 0;
        StringBuilder where = new StringBuilder("");
        if (properties != null) {
            Set keys = properties.keySet();
            Iterator iterator = keys.iterator();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                String value = properties.getProperty(key);
                where.append(" AND ").append(key).append(value);
            }
        }
        String query = String.format("DELETE FROM %s WHERE 1=1 %s", tableName, where.toString());
        System.out.println("query = " + query);
        try ( Statement st = con.createStatement()) {
            rows = st.executeUpdate(query);
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            conPool.freeConnection(con);
        }
        return rows;
    }
}
