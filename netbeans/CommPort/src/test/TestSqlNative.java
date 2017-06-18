package test;

import java.util.Arrays;
import modelHost.SqlAction;
import modelHost.SqlNative;
import modelHost.SqlResult;

public class TestSqlNative {

    public static void main(String[] args) {
       // insertRows();
//        selectRows();
       //deleteRows();
        alterTable();
    }

    public static void deleteRows() {
        SqlNative sqlNative = SqlNative.getInstance();
        SqlResult sqlResult = sqlNative.executeQuery("delete from table1 where id > 44");
        System.out.println(sqlResult.getResultMessage());
        for (String colName : sqlResult.getColumns()) {
            System.out.print("\t" + colName);
        }
        for (String[] row : sqlResult.getDataTable()) {
            System.out.println("\t" + Arrays.toString(row));
        }
    }

    public static void insertRows() {
        SqlNative sqlNative = SqlNative.getInstance();
        //SqlResult sqlResult = sqlNative.executeQuery("select * from result limit 10");
        for (int i = 2; i < 12; i++) {
            SqlResult sqlResult = sqlNative.executeQuery(String.format("insert into table1 values(%s,'%s','%s')", "null", "val" + i, "val2_" + i));
            System.out.println(sqlResult.getResultMessage());
            for (String colName : sqlResult.getColumns()) {
                System.out.print("\t" + colName);
            }
            for (String[] row : sqlResult.getDataTable()) {
                System.out.println("\t" + Arrays.toString(row));
            }
        }
    }

    public static void selectRows() {
        SqlNative sqlNative = SqlNative.getInstance();
        SqlResult sqlResult = sqlNative.executeQuery("select * from table1 limit 100");
        System.out.println(sqlResult.getResultMessage());
        for (String colName : sqlResult.getColumns()) {
            System.out.print("\t" + colName);
        }
        for (String[] row : sqlResult.getDataTable()) {
            System.out.println("\t" + Arrays.toString(row));
        }
    }
    
    public static void alterTable() {
       SqlNative sqlNative = SqlNative.getInstance();
        SqlResult sqlResult = sqlNative.executeQuery("ALTER TABLE `logs` MODIFY COLUMN `message` TEXT COLLATE utf8_general_ci");
        System.out.println(sqlResult.getResultMessage());
        for (String colName : sqlResult.getColumns()) {
            System.out.print("\t" + colName);
        }
        for (String[] row : sqlResult.getDataTable()) {
            System.out.println("\t" + Arrays.toString(row));
        }
    }
}
