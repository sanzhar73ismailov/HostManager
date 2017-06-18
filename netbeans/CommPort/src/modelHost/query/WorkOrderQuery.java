package modelHost.query;

import entity.Instrument;
import entity.WorkOrder;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelHost.DaoException;

public class WorkOrderQuery extends Query<WorkOrder> {

    public WorkOrderQuery(int id) {
        super(id, "workorder");
    }

    @Override
    public WorkOrder getObjectFromResultSet(ResultSet rs) throws DaoException {
        try {
            WorkOrder obj = new WorkOrder();
            obj.setId(rs.getInt("id"));
            Instrument instr = new Instrument(rs.getInt("instrument_id"), rs.getString("i_name"));
            obj.setInstrument(instr);
            obj.setMid(rs.getInt("mid"));
            obj.setSid(rs.getString("sid"));
            obj.setRack(rs.getString("rack"));
            obj.setPosition(rs.getInt("position"));
            obj.setSampleType(rs.getString("sample_type"));
            obj.setPatientName(rs.getString("patient_name"));
            obj.setPatientNumber(rs.getString("patient_number"));
            obj.setDateBirth(util.Util.getUtilDateFromSqlDate(rs.getDate("date_birth")));
            obj.setSex(rs.getInt("sex"));
            obj.setDateCollection(util.Util.getUtilDateFromSqlDate(rs.getDate("date_collection")));
            obj.setLaborantName(rs.getString("laborant_name"));
            obj.setStatus(rs.getInt("status"));
            obj.setTests(rs.getString("tests"));
            obj.setAddParamsFromString(rs.getString("add_params"));
            obj.setInsertDatetime(new java.util.Date(rs.getTimestamp("insert_datetime").getTime()));
            return obj;
        } catch (SQLException ex) {
            throw new DaoException(ex);
        }
    }

    
        @Override
    public String getSelectQuery() {
        /*
         `id` INTEGER(11) NOT NULL AUTO_INCREMENT,
         `name` VARCHAR(50) COLLATE utf8_general_ci NOT NULL DEFAULT '',
         `brand` VARCHAR(30) COLLATE utf8_general_ci NOT NULL DEFAULT '',
         `type` VARCHAR(30) COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'Тип анализатора (биохимический, гематологический)',
         `com_protocol` VARCHAR(20) COLLATE utf8_general_ci NOT NULL DEFAULT '',
         `sid_size` INTEGER(11) NOT NULL COMMENT 'макс. кол-во знаков в SID',
         `test_code_int` TINYINT(1) NOT NULL DEFAULT '0' COMMENT 'тип тестовых кодов числовые'
         */
        String query = "SELECT w.*, i.name i_name FROM "
                + tableName + " w "
                + " INNER JOIN instrument i ON (w.instrument_id=i.id)  WHERE 1=1  ";

        return query;
    }

    @Override
    public String getQueryById() {
        return getSelectQuery() + " AND w.id=" + id;
    }
    
    @Override
    public String getInsertQuery() {
        String query = "INSERT INTO "
                + "  workorder"
                + "("
                + "  instrument_id,"
                + "  mid,"
                + "  sid,"
                + "  rack,"
                + "  position,"
                + "  sample_type,"
                + "  patient_name,"
                + "  patient_number,"
                + "  date_birth,"
                + "  sex,"
                + "  date_collection,"
                + "  laborant_name,"
                + "  status,"
                + "  tests,"
                + "  add_params,"
                + "  insert_datetime"
                + ") "
                + "VALUE ("
                + "  ?,"
                + "  ?,"
                + "  ?,"
                + "  ?,"
                + "  ?,"
                + "  ?,"
                + "  ?,"
                + "  ?,"
                + "  ?,"
                + "  ?,"
                + "  ?,"
                + "  ?,"
                + "  ?,"
                + "  ?,"
                + "  ?,"
                + "  ?"
                + ")";
        return query;
    }

    @Override
    public void setParamsToPreparedStatement(PreparedStatement ps, WorkOrder obj, QueryOperation operation) throws DaoException {
        try {
            ps.setInt(1, obj.getInstrument().getId());
            ps.setInt(2, obj.getMid());
            ps.setString(3, obj.getSid());
            ps.setString(4, obj.getRack());
            ps.setInt(5, obj.getPosition());
            ps.setString(6, obj.getSampleType());
            ps.setString(7, obj.getPatientName());
            ps.setString(8, obj.getPatientNumber());
            ps.setDate(9, util.Util.getSqlDateFromUtilDate(obj.getDateBirth()));
            ps.setInt(10, obj.getSex());
            ps.setDate(11, util.Util.getSqlDateFromUtilDate(obj.getDateCollection()));
            ps.setString(12, obj.getLaborantName());
            ps.setInt(13, obj.getStatus());
            ps.setString(14, obj.getTests());
            ps.setString(15, obj.getAddParamsAsString());
            if (operation == QueryOperation.INSERT) {
                ps.setTimestamp(16, getCurrentTimeStamp());
            } else if (operation == QueryOperation.UPDATE) {
                ps.setInt(16, obj.getId());
            } else {
                throw new DaoException("Unsupported operation: " + operation);

            }
        } catch (SQLException ex) {
            throw new DaoException(ex);
        }
    }

    @Override
    public String getUpdateQuery() {
        String query = "UPDATE "
                + "  workorder SET"
                + "  instrument_id=?,"
                + "  mid=?,"
                + "  sid=?,"
                + "  rack=?,"
                + "  position=?,"
                + "  sample_type=?,"
                + "  patient_name=?,"
                + "  patient_number=?,"
                + "  date_birth=?,"
                + "  sex=?,"
                + "  date_collection=?,"
                + "  laborant_name=?,"
                + "  status=?,"
                + "  tests=?,"
                + "  add_params=? "
                + " WHERE id=?";
        return query;
    }
}
