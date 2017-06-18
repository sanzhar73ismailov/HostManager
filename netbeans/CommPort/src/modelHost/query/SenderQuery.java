package modelHost.query;

import entity.Sender;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import modelHost.DaoException;

public class SenderQuery extends Query<Sender> {

    public SenderQuery(int id) {
        super(id, "sender");
    }

    @Override
    public Sender getObjectFromResultSet(ResultSet rs) throws DaoException {
        try {
            Sender obj = new Sender();
            obj.setId(rs.getInt("id"));
            obj.setName(rs.getString("name"));
            obj.setDescription(rs.getString("description"));
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
    public void setParamsToPreparedStatement(PreparedStatement ps, Sender object, QueryOperation opeartion) throws DaoException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
