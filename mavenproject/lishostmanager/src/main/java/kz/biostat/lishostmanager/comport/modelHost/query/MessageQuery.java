package kz.biostat.lishostmanager.comport.modelHost.query;

import kz.biostat.lishostmanager.comport.entity.Message;
import kz.biostat.lishostmanager.comport.entity.Sender;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import kz.biostat.lishostmanager.comport.modelHost.DaoException;

public class MessageQuery extends Query<Message> {

    public MessageQuery(int id) {
        super(id, "message");
    }

//    @Override
//    public String getQueryById() {
//        String query = "SELECT m.*, s.id s_id, s.name s_name, s.description s_description "
//                + " FROM message m "
//                + " INNER JOIN sender s ON (m.sender_id = s.id)"
//                + " WHERE m.id='" + id + "'";
//        return query; //To change body of generated methods, choose Tools | Templates.
//    }
    @Override
    public Message getObjectFromResultSet(ResultSet rs) throws DaoException {
        try {
            Message obj = new Message();
            obj.setId(rs.getInt("id"));
            Sender sender = new Sender();
            sender.setId(rs.getInt("sender_id"));
            //sender.setName(rs.getString("s_name"));
            //sender.setDescription(rs.getString("s_description"));
            obj.setSender(sender);
            obj.setIncomeNumber(rs.getString("income_number"));
            obj.setClose(rs.getBoolean("close"));
            obj.setInsertDatetime(new java.util.Date(rs.getTimestamp("insert_datetime", Calendar.getInstance()).getTime()));
            return obj;
        } catch (SQLException ex) {
            throw new DaoException(ex);
        }
    }

    @Override
    public String getInsertQuery() {
        String query = "INSERT INTO message (sender_id, income_number, close) "
                + " VALUE (?,?,?);";
        return query;
    }

    @Override
    public String getUpdateQuery() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setParamsToPreparedStatement(PreparedStatement ps, Message object, QueryOperation opeartion) throws DaoException {
        try {
            ps.setInt(1, object.getSender().getId()); 
            ps.setString(2, object.getIncomeNumber()); 
            ps.setBoolean(3, object.isClose());
        } catch (SQLException ex) {
            throw new DaoException(ex);
        }
    }
}
