package test;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import util.DatabaseConnectionPool;

public class TestDbConnectionPool {

    public static void main(String[] args) throws Exception {
        DatabaseConnectionPool pool = DatabaseConnectionPool.getInstance();
//        Connection con1 = pool.getConnection();
//        Connection con2 = pool.getConnection();
//        Connection con3 = pool.getConnection();
//        Connection con4 = pool.getConnection();
//        Connection con5 = pool.getConnection();
//        Connection con6 = pool.getConnection();
//        Connection con7 = pool.getConnection();
//        Statement st = con7.createStatement();
//        pool.freeConnection(con5);
//        pool.freeConnection(con6);
//        pool.freeConnection(con7);
//        pool.freeConnection(con4);
//        Connection con8 = pool.getConnection();
//        Connection con9 = pool.getConnection();
//        Connection con10 = pool.getConnection();
        
        List <Connection> connLis = new ArrayList<>();
         Connection con1 = null;
         for (int i = 0; i < 30; i++) {
            con1 = pool.getConnection();
            connLis.add(con1);
        }
         
         for (int i = 0; i < connLis.size(); i++) {
            Connection connection = connLis.get(i);
            pool.freeConnection(connection);
            
        }
         
         for (int i = 0; i < 30; i++) {
            con1 = pool.getConnection();
            connLis.add(con1);
        }
         

    }
}
