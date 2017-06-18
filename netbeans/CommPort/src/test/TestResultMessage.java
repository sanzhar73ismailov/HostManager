/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

/**
 *
 * @author sanzhar.ismailov
 */
public class TestResultMessage {

    public static String str = "001123.2 1200.544 345123.4";

    public static void main(String[] args) {
        //cut();
//        cut2();
        removeZeroesFromeHead();
    }

    public static void main2(String[] args) {
        String str = "123\r\n456789\r\nqqq";
        System.out.println("str = " + str);
        String[] arr = str.split("\r\n");
        String s2 = java.util.Arrays.toString(arr);
        System.out.println("s2 = " + s2);
    }

    public static void cut() {
        int i = 0;
        int i2 = 8;
        int len = 9;
        boolean stop = false;
        while (!stop) {
            if (i2 > str.length()) {
                i2 = str.length();
            }
            if (i2 == str.length()) {
                stop = true;
            }
            String str2 = str.substring(i, i2);
            System.out.println("str2 = " + str2);
            i += len;
            i2 += len;
        }

    }

    public static void cut2() {
        String str = "001123.2 1200.544 345123.4";
//        String[] arr = str.split("(?<=\\G.{9})");
        String[] arr = util.Util.splitByLength(str, 9);
        for (String arr1 : arr) {
            System.out.println("arr1 = " + arr1);
        }

    }
    public static void removeZeroesFromeHead(){
        String str = "00000000000000000000001qwerty";
        System.out.println("str = " + str);
        int positionOfLastZero = 0;
        char [] arr = str.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            char b = arr[i];
            if(b == '0'){
                positionOfLastZero = i;
            }else{
                break;
            }
        }
        str  = str.substring(positionOfLastZero + 1, str.length());
        System.out.println("str = " + str);
        
    }

}
