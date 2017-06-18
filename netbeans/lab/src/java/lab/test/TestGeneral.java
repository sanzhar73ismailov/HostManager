package lab.test;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TestGeneral {
    public static void main(String[] args) {
        testSimpleDateFormat();
    }

    private static void testSimpleDateFormat() {
        String dateForSid = new SimpleDateFormat("yyyyMMdd").format(new Date());
        System.out.println("dateForSid = " + dateForSid);
    }
}
