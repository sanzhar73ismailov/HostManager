package kz.biostat.lishostmanager.comport.modelHost.query;

import kz.biostat.lishostmanager.comport.entity.InstrumentModel;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import kz.biostat.lishostmanager.comport.modelHost.DaoException;

public class InstrumentModelQuery extends Query <InstrumentModel>{

    public InstrumentModelQuery(int id) {
        super(id, "instrument_model");
    }

    @Override
    public InstrumentModel getObjectFromResultSet(ResultSet rs) throws DaoException {
        try {
            InstrumentModel obj = new InstrumentModel();
            obj.setId(rs.getInt("id"));
            obj.setName(rs.getString("name"));
            obj.setBrand(rs.getString("brand"));
            obj.setType(rs.getString("type"));
            obj.setComProtocol(rs.getString("com_protocol"));
            obj.setSidSize(rs.getInt("sid_size"));
            obj.setTestCodeInt(rs.getBoolean("test_code_int"));
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
    public void setParamsToPreparedStatement(PreparedStatement ps, InstrumentModel object, QueryOperation opeartion) throws DaoException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

   

  

}
