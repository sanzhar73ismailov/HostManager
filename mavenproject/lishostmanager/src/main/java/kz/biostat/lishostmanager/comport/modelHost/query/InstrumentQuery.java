package kz.biostat.lishostmanager.comport.modelHost.query;

import kz.biostat.lishostmanager.comport.entity.Instrument;
import kz.biostat.lishostmanager.comport.entity.InstrumentModel;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import kz.biostat.lishostmanager.comport.modelHost.DaoException;

public class InstrumentQuery extends Query<Instrument> {

    public InstrumentQuery(int id) {
        super(id, "instrument");
    }

    @Override
    public String getSelectQuery() {
        String query = "SELECT i.*, m.name m_name, m.brand m_brand, m.type m_type, "
                + " m.com_protocol m_com_protocol, m.sid_size m_sid_size, m.test_code_int m_test_code_int FROM "
                + tableName + " i "
                + " INNER JOIN instrument_model m ON (i.model_id=m.id)  WHERE 1=1  ";
        return query;
    }

    @Override
    public String getQueryById() {
        return getSelectQuery() + " AND i.id=" + id;
    }

    @Override
    public Instrument getObjectFromResultSet(ResultSet rs) throws DaoException {
        try {
            Instrument obj = null;
            obj = new Instrument();
            obj.setId(rs.getInt("id"));
            obj.setName(rs.getString("name"));

            InstrumentModel instrModel = new InstrumentModel(rs.getInt("model_id"), null);
            instrModel.setName(rs.getString("m_name"));
            instrModel.setBrand(rs.getString("m_brand"));
            instrModel.setComProtocol(rs.getString("m_com_protocol"));
            instrModel.setSidSize(rs.getInt("m_sid_size"));
            instrModel.setTestCodeInt(rs.getBoolean("m_test_code_int"));
            instrModel.setType(rs.getString("m_type"));

            obj.setModel(instrModel);
            obj.setIp(rs.getString("ip"));
            obj.setPort(rs.getInt("port"));
            String mode = rs.getString("mode");
            if (mode.equals("batch")) {
                obj.setMode(Instrument.ModeWorking.BATCH);
            } else if (mode.equals("query")) {
                obj.setMode(Instrument.ModeWorking.QUERY);
            } else {
                obj.setMode(Instrument.ModeWorking.UNKNOWN);
            }
            obj.setActive(rs.getBoolean("active"));
            obj.setTestMode(rs.getBoolean("test_mode"));
            return obj;
        } catch (SQLException ex) {
            throw new DaoException(ex);
        }
    }

    @Override
    public String getInsertQuery() {
        String query = "INSERT INTO "
                + "instrument"
                + "("
                + "  name,"
                + "  model_id,"
                + "  ip,"
                + "  port,"
                + "  mode,"
                + "  active,"
                + "  test_mode"
                + ") "
                + "VALUE ("
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
    public void setParamsToPreparedStatement(PreparedStatement ps, Instrument obj, QueryOperation operation) throws DaoException {
        try {
            ps.setString(1, obj.getName());
            ps.setInt(2, obj.getModel().getId());
            ps.setString(3, obj.getIp());
            ps.setInt(4, obj.getPort());
            ps.setString(5, obj.getMode().toString().toLowerCase());
            ps.setBoolean(6, obj.isActive());
            ps.setBoolean(7, obj.isTestMode());
            if (operation == QueryOperation.UPDATE) {
                ps.setInt(8, obj.getId());
            }
        } catch (SQLException ex) {
            throw new DaoException(ex);
        }
    }

    @Override
    public String getUpdateQuery() {
        String query = "UPDATE "
                + "instrument  "
                + "SET "
                + "  name = ?,"
                + "  model_id = ?,"
                + "  ip = ?,"
                + "  port = ?,"
                + "  mode = ?,"
                + "  active = ?,"
                + "  test_mode = ?"
                + " "
                + "WHERE "
                + "  id = ?";
        return query;
    }
}
