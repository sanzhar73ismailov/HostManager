package cobas;

import instrument.ASCII;

public class Main {

    public static void main(String[] args) {
        String header = ASCII.SOH_STRING+ ASCII.LF_STRING
                + "09 INTEGRA 30-1051  00"
                + ASCII.LF_STRING
                + ASCII.STX_STRING + ASCII.LF_STRING
                + ASCII.ETX_STRING + ASCII.LF_STRING
                + "1"
                + ASCII.LF_STRING;
        byte[] arr = header.getBytes();
        int sum = 0;
        for (byte b : arr) {
            sum += b;
            System.out.println("b = " + b);
        }
        System.out.println("sum = " + sum);
        System.out.println("mod of sum = " + sum % 1000);
        


    }
}
