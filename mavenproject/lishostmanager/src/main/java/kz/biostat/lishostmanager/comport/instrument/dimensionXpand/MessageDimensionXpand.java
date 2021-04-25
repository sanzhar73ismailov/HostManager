package kz.biostat.lishostmanager.comport.instrument.dimensionXpand;

import kz.biostat.lishostmanager.comport.instrument.Message;

import static kz.biostat.lishostmanager.comport.instrument.ASCII.*;

import java.util.Arrays;

public class MessageDimensionXpand extends Message {

    TypeReceivedMessage typeReceivedMessage;
    String[] fields;

    public MessageDimensionXpand(byte[] rawMessage) {
        super(rawMessage);
        super.type = (char) rawMessage[1] + "";
        //System.out.println("super.type = " + super.type);
        switch (super.type) {
            case "P":
                typeReceivedMessage = TypeReceivedMessage.POLL_MESSAGE;
                break;
            case "M":
                typeReceivedMessage = TypeReceivedMessage.REQUUEST_ACCEPTANCE_MESSAGE;
                break;
            case "I":
                typeReceivedMessage = TypeReceivedMessage.QUERY_MESSAGE;
                break;
            case "R":
                typeReceivedMessage = TypeReceivedMessage.RESULT_MESSAGE;
                break;
            case "C":
                typeReceivedMessage = TypeReceivedMessage.CALIBRATION_RESULT_MESSAGE;
                break;

        }
        //System.out.println("typeReceivedMessage: " + typeReceivedMessage);
        byte[] byteArrayWithoutStxEtx = Arrays.copyOfRange(rawMessage, 1, rawMessage.length - 1);
        fields = new String(byteArrayWithoutStxEtx).split(FS_STRING);
        //System.out.println(Arrays.toString(fields));

    }

    public TypeReceivedMessage getTypeReceivedMessage() {
        return typeReceivedMessage;
    }

    @Override
    public boolean validateCheckSum() {
        int calculatedBySum = this.calculateCheckSum();
        int checkSumFromMessage = this.extractCheckSumFromMessage();
        return calculatedBySum == checkSumFromMessage;
    }

    @Override
    public int calculateCheckSum() {
        byte[] bArrayWithout_STX_CHK_ETX = java.util.Arrays.copyOfRange(rawMessage, 1, rawMessage.length - 3);
        //System.out.println("bArrayWithout_STX_CHK_ETX = " + new String(bArrayWithout_STX_CHK_ETX));
        int sum = getSumOfByteArray(bArrayWithout_STX_CHK_ETX);
        return sum;
    }

    @Override
    public int extractCheckSumFromMessage() {
        String lastTwoSymbols = (char) rawMessage[rawMessage.length - 3] + "" + (char) rawMessage[rawMessage.length - 2];
        return Integer.parseInt(lastTwoSymbols, 16);
    }

    private int getSumOfByteArray(byte[] array) {
        int sum = 0;
        for (byte b : array) {
            sum += b;
        }
        return get8BitFromInt(sum);

    }

    public static String getCheckSumAsString(byte[] array) {
        int sum = 0;
        for (byte b : array) {
            sum += b;
        }
        return get8BitFromIntAsHexStr(sum);
    }

    private static int get8BitFromInt(int number) {
        String binaryString = Integer.toBinaryString(number);
        int startBinary = 0;
        if (binaryString.length() > 8) {
            startBinary = binaryString.length() - 8;
        }
        String binaryStringTo8Bit = binaryString.substring(startBinary, binaryString.length());
        return Integer.parseInt(binaryStringTo8Bit, 2);
    }

    private static String get8BitFromIntAsHexStr(int number) {
        String binaryString = Integer.toHexString(number);
        String retStr = binaryString.substring(binaryString.length() - 2, binaryString.length()).toUpperCase();
        return retStr;
    }

    public String[] getFields() {
        return fields;
    }

    public boolean isFirstPoll() {
        return fields[2].equals("1");

    }

    public boolean isBusy() {
        return fields[3].equals("0");
    }

    public static MessageDimensionXpand noRequestMessage() {
        String str = STX_STRING + "N" + FS_STRING + "6A" + ETX_STRING;
        return new MessageDimensionXpand(str.getBytes());
    }


    @Override
    public String getSidFromQueryRecord() {
        return fields[1];
    }

    public boolean isWorkorderAccepted() {
        return fields[1].equals("A");
    }

    public String getDiscriptionOfReasonOfNotAccepted() {
        int reasonCode = 0;
        String str = fields[2];
        if (str == null || str.isEmpty()) {
            return "";
        }
        reasonCode = Integer.parseInt(str);
        switch (reasonCode) {
            case 1:
                str = "Request in process";
                break;
            case 2:
                str = "Result no longer available";
                break;
            case 3:
                str = "Sample carrier in use";
                break;
            case 4:
                str = "No memory to store reques";
                break;
            case 5:
                str = "Error in test request";
                break;
            case 6:
                str = "Reserved";
                break;
            case 7:
                str = "Sample Carrier full";
                break;
            case 8:
                str = "No known carriers";
                break;
            case 9:
                str = "Incorrect Fluid Type";
                break;
            default:
                str = "Unknown Reason Of Not Accepted, code: " + reasonCode;
                break;
        }
        return str;
    }

}
