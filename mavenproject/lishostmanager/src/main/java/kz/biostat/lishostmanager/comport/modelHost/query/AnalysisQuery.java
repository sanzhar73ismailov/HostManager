package kz.biostat.lishostmanager.comport.modelHost.query;

import kz.biostat.lishostmanager.comport.entity.Analysis;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import kz.biostat.lishostmanager.comport.modelHost.DaoException;

public class AnalysisQuery extends Query<Analysis> {

    public AnalysisQuery(String tableName) {
        super(tableName);
    }
    
    public AnalysisQuery(int id) {
        super(id, "analysis");
    }

    @Override
    public Analysis getObjectFromResultSet(ResultSet rs) throws DaoException {
         try {
            Analysis obj = new Analysis();
            obj.setId(rs.getInt("id"));
            obj.setName(rs.getString("name"));
            return obj;
        } catch (SQLException ex) {
            throw new DaoException(ex);
        }
    }

    @Override
    public String getInsertQuery() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getUpdateQuery() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setParamsToPreparedStatement(PreparedStatement ps, Analysis object, QueryOperation operation) throws DaoException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
