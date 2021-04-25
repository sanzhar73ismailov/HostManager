/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.biostat.lishostmanager.comport.test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import static kz.biostat.lishostmanager.comport.util.DbConnection.getConnection;

/**
 *
 * @author sanzhar.ismailov
 */
public class TestDbConnection {
    public static void main(String[] args) throws SQLException {
        Connection con = getConnection();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("select * from sender");
        while(rs.next()){
            System.out.println("rs = " + rs.getString(3));
        }
        
    }
}
