package kz.biostat.lishostmanager.comport.modelHost.query;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import kz.biostat.lishostmanager.comport.modelHost.Dao;
import kz.biostat.lishostmanager.comport.modelHost.DaoMysqlImpl;
import kz.biostat.lishostmanager.comport.modelHost.DaoException;

public abstract class Query<T> {

    protected int id;
    protected String tableName;
    Dao dao = null;

    public Query(String tableName) {
        this.tableName = tableName;
        try {
            dao = new DaoMysqlImpl();
        } catch (DaoException ex) {
            Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Query(int id, String tableName) {
        this.id = id;
        this.tableName = tableName;
        try {
            dao = new DaoMysqlImpl();
        } catch (DaoException ex) {
            Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public  String getSelectQuery(){
        return "SELECT * FROM " + tableName + " WHERE 1=1 ";
    }
    public String getQueryById() {
        return getSelectQuery() + " AND id='" + id + "'";
    }

    public abstract T getObjectFromResultSet(ResultSet rs) throws DaoException;

    public abstract String getInsertQuery();
    
    public String getDeleteQuery(){
        String query = "DELETE FROM " + tableName + " WHERE id=" + id;
        return query;
    }
    
    public abstract String getUpdateQuery();

     public abstract void setParamsToPreparedStatement(PreparedStatement ps, T object, QueryOperation operation)  throws DaoException;

    protected static java.sql.Timestamp getCurrentTimeStamp() {
        java.util.Date today = new java.util.Date();
        return new java.sql.Timestamp(today.getTime());
    }

    public int getId() {
        return id;
    }

    public String getTableName() {
        return tableName;
    }
    
}
