package kz.biostat.lishostmanager.comport.modelHost.query;

import kz.biostat.lishostmanager.comport.entity.Message;
import kz.biostat.lishostmanager.comport.entity.MessageHistory;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import kz.biostat.lishostmanager.comport.modelHost.DaoException;

public class MessageHistoryQuery extends Query <MessageHistory>{

    public MessageHistoryQuery(int id) {
        super(id, "message_history");
    }

    @Override
    public MessageHistory getObjectFromResultSet(ResultSet rs) throws DaoException {
        try {
            MessageHistory obj = new MessageHistory();
            obj.setId(rs.getInt("id"));
            Message message = new Message();
            message.setId(rs.getInt("message_id"));
            obj.setMessage(message);
            obj.setText(rs.getString("text"));
            obj.setInsertDatetime(new java.util.Date(rs.getTimestamp("insert_datetime").getTime()));
            return obj;
        } catch (SQLException ex) {
            throw new DaoException(ex);
        }
    }

    @Override
    public String getInsertQuery() {
        return "INSERT INTO message_history (message_id, text) VALUE (?,?)";
    }


    @Override
    public String getUpdateQuery() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setParamsToPreparedStatement(PreparedStatement ps, MessageHistory object, QueryOperation opeartion) throws DaoException {
        try {
            ps.setInt(1, object.getMessage().getId());
            ps.setString(2, object.getText());
        } catch (SQLException ex) {
            throw new DaoException(ex);
        }
    }

   
}
