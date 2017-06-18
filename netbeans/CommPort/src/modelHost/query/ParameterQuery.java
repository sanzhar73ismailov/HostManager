package modelHost.query;

import entity.Analysis;
import entity.Parameter;
import entity.Test;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import modelHost.Dao;
import modelHost.DaoException;

public class ParameterQuery extends Query<Parameter> {

    public ParameterQuery(int id) {
        super(id, "parameter");
    }

    @Override
    public Parameter getObjectFromResultSet(ResultSet rs) throws DaoException {
        try {
            Parameter obj = new Parameter();
            obj.setId(rs.getInt("id"));
            obj.setName(rs.getString("name"));
            Test test = dao.getObject(rs.getInt("test_id"), new Test());
            Analysis analysis = dao.getObject(rs.getInt("analysis_id"), new Analysis());
            // if (test != null) {
            Properties propForTests = new Properties();
            propForTests.setProperty("parameter_id", "=" + obj.getId() + "");
            List<Test> tests = dao.getObjects(propForTests, new Test());
            Set<Test> testsSet = new HashSet<Test>(tests);
            obj.setTestDefault(test);
            obj.setTests(testsSet);
            obj.setAnalysis(analysis);
            // }
            return obj;
        } catch (SQLException ex) {
            throw new DaoException(ex);
        }
    }

    @Override
    public String getInsertQuery() {
        String query = "INSERT INTO "
                + "  parameter"
                + "("
                + "  name,"
                + "  test_id,"
                + "  analysis_id"
                + ") "
                + "VALUE ("
                + "  ?,"
                + "  ?,"
                + "  ?"
                + ")";
        return query;
    }

    @Override
    public String getUpdateQuery() {
        String query = "UPDATE "
                + "  parameter  "
                + "SET "
                + "  name = ?,"
                + "  test_id = ?,"
                + "  analysis_id = ?"
                + " "
                + "WHERE "
                + "  id = ?";
        return query;
    }

    @Override
    public void setParamsToPreparedStatement(PreparedStatement ps, Parameter object, QueryOperation operation) throws DaoException {
        try {
            ps.setString(1, object.getName());
            ps.setInt(2, object.getTestDefault().getId());
            ps.setInt(3, object.getAnalysis().getId());
            if (operation == QueryOperation.UPDATE) {
                ps.setInt(4, object.getId());
            }
        } catch (SQLException ex) {
            throw new DaoException(ex);
        }
    }
}
