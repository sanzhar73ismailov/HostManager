package util;

import entity.Test;
import java.util.List;
import java.util.Properties;
import java.util.*;
import modelHost.Model;
import modelHost.ModelException;
import modelHost.ModelImpl;

public class Util {

    public static String getStringFillByCharacter(int n, char ch) {
        return new String(new char[n]).replace('\0', ch);
    }

    public static String getStringFillBySpace(int n) {
        return getStringFillByCharacter(n, ' ');
    }

    public static java.sql.Date getSqlDateFromUtilDate(java.util.Date paramDate) {
        java.sql.Date returnDate = null;
        if (paramDate != null) {
            returnDate = new java.sql.Date(paramDate.getTime());
        }
        return returnDate;
    }

    public static java.util.Date getUtilDateFromSqlDate(java.sql.Date paramDate) {
        java.util.Date returnDate = null;
        if (paramDate != null) {
            returnDate = new java.util.Date(paramDate.getTime());
        }
        return returnDate;
    }

    public static String getAddParamsAsString(Properties prop) {
        StringBuilder sb = new StringBuilder();
        Set<Object> keys = prop.keySet();
        for (Object key : keys) {
            String keyAsStr = (String) key;
            sb.append(",").append(keyAsStr).append("=").append(prop.getProperty(keyAsStr));
        }
        return sb.substring(1).toString();
    }

    public static int[] convertIntegers(List<Integer> list) {
        int[] array = new int[list.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = list.get(i).intValue();
        }
        return array;
    }

    public static byte[] convertBytes(List<Byte> list) {
        byte[] array = new byte[list.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = list.get(i).byteValue();
        }
        return array;
    }

    public static void addArrayByteToByteList(List<Byte> listByRef, byte[] array) {
        for (int i = 0; i < array.length; i++) {
            listByRef.add(new Byte(array[i]));
        }
    }

    public static void addStringToByteList(List<Byte> listByRef, String text) {
        addArrayByteToByteList(listByRef, text.getBytes());
    }

    public static boolean isAllDigits(String[] strArray) {
        for (String string : strArray) {
            if (!isInteger(string)) {
                return false;
            }
        }
        return true;
    }

    public static String[] getSortedAsDigits(String[] strArray) {
        int[] intArray = new int[strArray.length];
        for (int i = 0; i < intArray.length; i++) {
            intArray[i] = Integer.parseInt(strArray[i]);
        }

        Arrays.sort(intArray);
        for (int i = 0; i < strArray.length; i++) {
            strArray[i] = String.valueOf(intArray[i]);

        }
        return strArray;
    }

    public static boolean isInteger(String s) {
        if (s.isEmpty()) {
            return false;
        }
        for (int i = 0; i < s.length(); i++) {
            if (i == 0) {
                if (s.length() > 1 && (s.charAt(i) == '0')) {
                    return false;
                }
                if ((s.charAt(i) == '-')) {
                    if (s.length() == 1) {
                        return false;
                    } else {
                        continue;
                    }
                }
            }
            if (!Character.isDigit(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static int[] getIntArrayFromCSVString(String str) {
        return getIntArrayFromStringArray(str.split(","));
    }

    public static int[] getIntArrayFromStringArray(String[] strArray) {
        int[] intArray = new int[strArray.length];
        for (int i = 0; i < intArray.length; i++) {
            intArray[i] = Integer.parseInt(strArray[i].trim());
        }
        return intArray;
    }

    public static String[] splitByLength(String str, int length) {
        return str.split(String.format("(?<=\\G.{%s})", length));
    }

    public static String removeZeroesFromeHead(String str) {
        int positionOfLastZero = 0;
        char[] arr = str.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            char b = arr[i];
            if (b == '0') {
                positionOfLastZero = i;
            } else {
                break;
            }
        }
        return str.substring(positionOfLastZero + 1, str.length());
    }

    public static boolean isAsciiPrintable(String str) {
        if (str == null) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (isAsciiPrintable(str.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    public static String getAsciiPrintable(String str) {
        if (str == null || str.trim().isEmpty()) {
            return "";
        }
        char[] retCharArray;
        int sz = str.length();
        retCharArray = new char[sz];
        for (int i = 0; i < str.length(); i++) {
            retCharArray[i] = getAsciiFromNonAScii(str.charAt(i));
        }
        return new String(retCharArray);
    }

    public static void printAllSymbolsSanTem0(String str) {
        if (str != null) {
            int sz = str.length();
            for (int i = 0; i < sz; i++) {
                System.out.println(str.charAt(i) + " " + ((int) str.charAt(i)));
            }
        }

    }

    /**
     * <p>
     * Checks whether the character is ASCII 7 bit printable.</p>
     *
     * <pre>
     *   CharUtils.isAsciiPrintable('a')  = true
     *   CharUtils.isAsciiPrintable('A')  = true
     *   CharUtils.isAsciiPrintable('3')  = true
     *   CharUtils.isAsciiPrintable('-')  = true
     *   CharUtils.isAsciiPrintable('\n') = false
     *   CharUtils.isAsciiPrintable('&copy;') = false
     * </pre>
     *
     * @param ch the character to check
     * @return true if between 32 and 126 inclusive
     */
    public static boolean isAsciiPrintable(char ch) {
        return ch >= 32 && ch < 127;
    }
/**
 * 
 * @param ch
 * @return возвращает вопрос если символ не ASCII и неизвестено на что его менять 
 */
    public static char getAsciiFromNonAScii(char ch) {
        char retChar = ch;
        if (ch < 32 || ch >= 127) {
            if (ch == 181) { // if ch is 'µ'
                retChar = 'u';
            } else {
                retChar = '?';
            }
        }
        return retChar;
    }
}
