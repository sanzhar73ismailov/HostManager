package modelHost.query;

import entity.Instrument;
import entity.LogInstrument;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelHost.DaoException;

public class LogInstrumentQuery extends Query<LogInstrument> {

    public LogInstrumentQuery(int id) {
        super(id, "logs");
    }

    @Override
    public LogInstrument getObjectFromResultSet(ResultSet rs) throws DaoException {
        try {
            LogInstrument obj = new LogInstrument();
            obj.setId(rs.getInt("id"));
            obj.setInstrument(new Instrument(rs.getInt("instrument_id"), null));
            obj.setDirection(rs.getString("direction"));
            obj.setMessage(rs.getString("message"));
            obj.setTemp(rs.getBoolean("temp"));
            obj.setInsertDate(new java.util.Date(rs.getTimestamp("insert_datetime").getTime()));
            return obj;
        } catch (SQLException ex) {
            throw new DaoException(ex);
        }
    }

    @Override
    public String getInsertQuery() {
        String query = "INSERT INTO logs (instrument_id, direction,  message, temp) VALUES (?,?,?,?)";
        return query;
    }

    @Override
    public void setParamsToPreparedStatement(PreparedStatement ps, LogInstrument object, QueryOperation operation) throws DaoException {
        try {
            ps.setInt(1, object.getInstrument().getId());
            ps.setString(2, object.getDirection());
            ps.setString(3, object.getMessage());
            ps.setBoolean(4, object.isTemp());
            if(operation == QueryOperation.UPDATE){
                ps.setInt(5, object.getId());
            }
        } catch (SQLException ex) {
            throw new DaoException(ex);
        }
    }

    @Override
    public String getUpdateQuery() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
