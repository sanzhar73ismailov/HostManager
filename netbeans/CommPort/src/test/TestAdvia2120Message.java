package test;

import instrument.advia2120.Advia2120Message;

public class TestAdvia2120Message {

    public static void main(String[] args) {
        //testGetStringBySize();
        testGetStringBySizeFilledWithZerosOnTheLeft();
    }

    public static void testGetStringBySize() {
        String str = "qwerty";
        String res = Advia2120Message.getStringBySizeFilledWithSpacesOnTheRight(str, 3);
        System.out.println("{" + res + "}, length=" + res.length());
    }

    private static void testGetStringBySizeFilledWithZerosOnTheLeft() {
        String str = "123";
        String res = Advia2120Message.getStringBySizeFilledWithZerosOnTheLeft(str, 14);
        System.out.println("{" + res + "}, length=" + res.length());
    }
}
