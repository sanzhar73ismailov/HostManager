package modelHost.query;

import entity.Instrument;
import entity.Parameter;
import entity.Test;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import modelHost.DaoException;

public class TestQuery extends Query<Test> {

    public TestQuery(int id) {
        super(id, "test");
    }

    @Override
    public Test getObjectFromResultSet(ResultSet rs) throws DaoException {
        try {
            Test obj = new Test();
            obj.setId(rs.getInt("id"));
            Instrument instr = new Instrument(rs.getInt("instrument_id"), null);
            obj.setInstrument(instr);
            obj.setCode(rs.getString("code"));
            obj.setName(rs.getString("name"));
            obj.setDescription(rs.getString("description"));
            obj.setUnits(rs.getString("units"));
            obj.setOrder(rs.getInt("test_order"));
            //obj.setUseDefault(rs.getBoolean("use_default"));
            obj.setParameter(new Parameter(rs.getInt("parameter_id"), ""));
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
    public void setParamsToPreparedStatement(PreparedStatement ps, Test object, QueryOperation opeartion) throws DaoException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
