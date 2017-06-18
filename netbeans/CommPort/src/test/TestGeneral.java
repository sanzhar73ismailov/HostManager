package test;

import instrument.InstrumentException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class TestGeneral {

    public static void main(String[] args) {
        try {
            //testException(0);
//            parseDate();
//            testParseDouble();
//            testIntToByte();
//            testChars();
            //testSplit();
//            testSplit2();
//            testRelaceAll();
            //modifyMidToSid();
            //createTestVariants();
//            splitTest();
            //checkStr();
            //usingSet();
//            arrayToList();
            checkLetters();

        } catch (Exception ex) {
            ex.printStackTrace();
            //Logger.getLogger(TestGeneral.class.getName()).log(Level.SEVERE, null, ex);

            //System.out.println(ex.getCause() instanceof ArithmeticException);
        }
    }

    public static void checkLetters() {
        String str = "CPK";
//        String str = "СРК";
        char[] arrChars = str.toCharArray();
        for (int i = 0; i < arrChars.length; i++) {
            char arrChar = arrChars[i];
            System.out.println("arrChar = " + arrChar + "=" + (int) arrChar);
        }

    }

    public static void arrayToList() {
        List<String[]> list = new ArrayList<>();
        String[][] arr = new String[2][];
        arr[0] = new String[]{"1", "5", "7"};
        arr[1] = new String[]{"4", "8", "10"};
        for (String[] arr1 : arr) {
            System.out.println(Arrays.toString(arr1));
        }

        System.out.println("------------");
        list.add(arr[0]);
        list.add(arr[1]);
        for (String[] arr1 : list) {
            System.out.println(Arrays.toString(arr1));
        }

        System.out.println("=============");
        String[][] newStrArr = list.toArray(new String[list.size()][]);
        for (String[] arr1 : newStrArr) {
            System.out.println(Arrays.toString(arr1));
        }
    }

    public static void splitTest() {
        String str = "   1 7.26   2 5.02   3   94   4 29.7   5 59.2   6 18.7   7  316  51  327  72 19.2   8 19.5   9 39.4  10  193  11  9.7  20 57.6  21 28.5  22  7.8  23  1.7  24  0.4  25  4.0  28    0  14 4.18  15 2.07  16 0.57  17 0.12  18 0.03  19 0.29  29    0  50 6.98  52 7.26 910 1.09 911 54.6 916 4.84 921104.6 914 67.6 922  270 915  267 912 28.1 919 18.0 19163.57 19281.84  36   ++  37  +++  39    +  40   ++  12 43.3  13 0.19  26 2.28  27  2.4  73 3.78 917 18.6 918 32.3 920 3.39 923 12.7 924 24.0 913 3.56 925 17.8 926 68.4 927  331 928 24.0 929  116 930  7.6 931   37 932 7.64 93337.33 934    3 935 10.3 936 -8.3 937 0.35 943 -5.0 941 98.9 94244019 945 67.2 946 17.7 947 32.3 948  267 949 3.21  83 51.4  84 29.3  53 7.26  54 57.6  55 28.5  56  7.8  57  1.7  58  0.4  59  4.0  60 4.18  61 2.07  62 0.57  63 0.12  64 0.03  65 0.29";
        char[] arr = str.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            char b = arr[i];
            System.out.print(b);
            if (i % 9 == 0) {
                System.out.println();
            }

        }
    }

    public static void usingSet() {
        Set<Integer> usedIndexes = new HashSet<>();
        usedIndexes.add(1);
        usedIndexes.add(2);
        usedIndexes.add(3);
        usedIndexes.add(1);
        usedIndexes.add(2);
        usedIndexes.add(4);
        System.out.println("usedIndexes = " + usedIndexes);
        for (Iterator<Integer> iterator = usedIndexes.iterator(); iterator.hasNext();) {
            Integer next = iterator.next();

            System.out.println("next = " + next);

        }

    }

    public static void createTestVariants() {
        List<String> list = new ArrayList<>();
        Set<Integer> usedIndexes = new HashSet<>();

        String[] arr = {"001", "002", "003", "004"};
        //String[] arr = {"aaa", "bbb", "ccc", "ddd"};
        //for (int i = 0; i < arr.length; i++) {
        //   list.add(arr[i]);
        //   usedIndexes.add(i);
        //}
        String str = getVariant(list, usedIndexes);
        System.out.println("str = " + str);
        //System.out.println("list = " + list);
    }

    public static String getVariant(List<String> list, Set<Integer> usedIndexes) {
        for (int i = 0; i < list.size(); i++) {
            String str = list.get(i);
            if (!usedIndexes.contains(i)) {
                usedIndexes.add(i);
                return str;
            }
        }
        return "";
    }

    public static void createTestVariantsNew() {
        String[] arr = {"001", "002", "003", "004"};
        for (int i = 0; i < arr.length; i++) {
            String arr1 = arr[i];

        }

    }

    private static void checkStr() {
        String messageId = "123 : 2015-07-01";
        String sid = "";
        sid = messageId.substring(0, messageId.indexOf(" :"));
        System.out.println("sid = <<<" + sid + ">>>");
    }

    public String premzhVar(String[] arr) {
        for (int i = 0; i < arr.length; i++) {
            if ((i + 1) < arr.length) {
                String str1 = arr[i];
                String str2 = arr[i + 1];
            }
        }
        return null;
    }

    public String getString(String str1, String str2) {
        return str1 + str2;
    }

    public static void fillList(List list) {
        //createTestVariants();
        usingSet();
    }

    public static void modifyMidToSid() {
        String mid = "5 : 2015-07-18";
        String sid = mid.replaceAll(" ", "").replaceAll("-", "").replaceAll(":", "-");
        System.out.println("sid = " + sid);
    }

    public static void testSplit() {
        String str = "123456\r789\rqwerty\r";
        String[] arr = str.split("\r");
        for (int i = 0; i < arr.length; i++) {
            String arr1 = arr[i];
            System.out.println("arr1 = " + arr1);
        }

    }

    private static void testException(int x) throws InstrumentException {
        try {
            int r = 12 / x;
        } catch (Exception ex) {
            // throw new instrument.InstrumentException(ex);
            //ex.printStackTrace();
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            String str = errors.toString();
            System.out.println("str = " + str);
        }

    }

    public static void parseDate() throws ParseException {
        String str = "0605151636";
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyhhmm");
        Date date = sdf.parse(str);

        System.out.println("date = " + date);
    }

    public static void testParseDouble() throws ParseException {
        String str = "170";
        double d = Integer.parseInt(str);
        System.out.println("d = " + d / 1000);

    }

    public static void testIntToByte() throws ParseException {
        int i = 181;
        byte d = (byte) i;
        System.out.println("d = " + d);
        System.out.println("as char = " + (char) 181);

    }

    public static void testChars() {
        for (int i = 0; i < 256; i++) {
            System.out.println(i + ": " + (char) i);
        }
    }

    private static void testSplit2() {
        String str = "051,052,054,061,062,073,074,501,504";
        String[] arr = str.split(",");
        Set<String> set = new LinkedHashSet<>();
        System.out.println("<<<<<<<<<<<<");
        for (String el : arr) {
            String elStr = el.substring(0, 2) + "0";
            System.out.println("elStr = " + elStr);
            set.add(elStr);
        }

        System.out.println("**********");

        for (String set1 : set) {
            System.out.println("set1 = " + set1);
        }
    }

    private static void testRelaceAll() {
        String str = "asdsa 111 22222";
        str = str.replaceAll(" ", "");
        System.out.println("str = " + str);

    }
}
