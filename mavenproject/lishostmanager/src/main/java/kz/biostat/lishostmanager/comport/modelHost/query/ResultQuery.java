package kz.biostat.lishostmanager.comport.modelHost.query;

import kz.biostat.lishostmanager.comport.entity.Result;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import kz.biostat.lishostmanager.comport.modelHost.DaoException;

public class ResultQuery extends Query<Result> {

    public ResultQuery(int id) {
        super(id, "result");
    }

    @Override
    public Result getObjectFromResultSet(ResultSet rs) throws DaoException {
        try {
            Result obj = new Result();
            obj.setId(rs.getInt("id"));
            obj.setWorkOrderId(rs.getInt("workorder_id"));
            obj.setTestCode(rs.getString("test_code"));
            obj.setParameterId(rs.getInt("parameter_id"));
            obj.setValue(rs.getString("value"));
            obj.setUnits(rs.getString("units"));
            obj.setReferenseRanges(rs.getString("referense_ranges"));
            obj.setAbnormalFlags(rs.getString("abnormal_flags"));
            obj.setInitialRerun(rs.getString("initial_rerun"));
            obj.setComment(rs.getString("comment"));
            obj.setStatus(rs.getString("status"));
            obj.setRawText(rs.getString("raw_text"));
            obj.setAddParams(rs.getString("add_params"));
            obj.setInstrument(rs.getString("kz/biostat/lishostmanager/comport/instrument"));
            obj.setSid(rs.getString("sid"));
            obj.setVersion(rs.getInt("version"));
            obj.setInsertDatetime(new java.util.Date(rs.getTimestamp("insert_datetime").getTime()));
            obj.setSidVersion(obj.getSid() + "^" + obj.getVersion());
            return obj;
        } catch (SQLException ex) {
            throw new DaoException(ex);
        }
    }

    @Override
    public String getInsertQuery() {
        String query = "INSERT INTO "
                + "  result"
                + "("
                + "  workorder_id,"
                + "  test_code,"
                + "  parameter_id,"
                + "  value,"
                + "  units,"
                + "  referense_ranges,"
                + "  abnormal_flags,"
                + "  initial_rerun,"
                + "  comment,"
                + "  status,"
                + "  raw_text,"
                + "  add_params,"
                + "  instrument,"
                + "  sid,"
                + "  version"
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
                + "  ?"
                + ");";
        return query;
    }

    @Override
    public void setParamsToPreparedStatement(PreparedStatement ps, Result object, QueryOperation operation) throws DaoException {
        try {
            if (object.getWorkOrderId() == 0) {
                ps.setNull(1, java.sql.Types.INTEGER);
            } else {
                ps.setInt(1, object.getWorkOrderId());
            }
            ps.setString(2, object.getTestCode());
            ps.setInt(3, object.getParameterId());
            ps.setString(4, object.getValue());
            ps.setString(5, object.getUnits());
            ps.setString(6, object.getReferenseRanges());
            ps.setString(7, object.getAbnormalFlags());
            ps.setString(8, object.getInitialRerun());
            ps.setString(9, object.getComment());
            ps.setString(10, object.getStatus());
            ps.setString(11, object.getRawText());
            ps.setString(12, object.getAddParams());
            ps.setString(13, object.getInstrument());
            ps.setString(14, object.getSid());
            ps.setInt(15, object.getVersion());
        } catch (SQLException ex) {
            throw new DaoException(ex);
        }
    }

    @Override
    public String getUpdateQuery() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
