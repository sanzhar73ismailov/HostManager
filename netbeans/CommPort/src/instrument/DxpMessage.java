package instrument;

import java.util.List;

public class DxpMessage  {

//    List<String> datas;
//
//    public DxpMessage(byte[] rawMessage) {
//        super(rawMessage);
//    }
//
//    public DxpMessage(char[] charArray) {
//        super(charArray);
//    }
//
//    public DxpMessage(String value) {
//        super(value);
//    }
//
//    @Override
//    public boolean validateCheckSum() {
//        int calculatedSum = 0;
//        int checkSumFromMessage = getCheckSumFromMessage();
//        for (int i = 0; i < rawMessage.length - 3; i++) {
//            byte c = rawMessage[i];
//            if (c != ASCII.STX) {
//                //System.out.println("<" + (char) c + ">" + " HEX" + Integer.toHexString(c) + "---" + c);
//                System.out.println(c);
//                calculatedSum += (int) c;
//            }
//        }
//        String binaryString = Integer.toBinaryString(calculatedSum);
//        System.out.println("binaryString = " + binaryString);
//        //оставляет только последние 8 бит из двоичного представления суммы
//        String checkSumAsBinaryString = binaryString.substring(binaryString.length() - 8);
//        calculatedSum = Integer.parseInt(checkSumAsBinaryString, 2);
//        //System.out.println("checkSumAsInt = " + checkSumAsInt);
//        //String checkSumAsHex = Integer.toHexString(checkSumAsInt);
//        // System.out.println("checkSumAsHex = " + checkSumAsHex);
//        return calculatedSum == checkSumFromMessage;
//    }
//
//    public int getCheckSumFromMessage() {
//        int result = 0;
//        int messageLength = this.rawMessage.length;
//        String checkSumFromMessage = (char) rawMessage[messageLength - 3] + "" + (char) rawMessage[messageLength - 2];
//        //System.out.println("checkSumFromMessage = " + checkSumFromMessage);
//        result = Integer.parseInt(checkSumFromMessage, 16);
//        return result;
//    }
//
//    public static void main(String[] args) {
//        //byte[] messageArg = test.Test.messageArg;
//        byte[] messageArg = test.Test.messageRequest;
//        DxpMessage messageObj = new DxpMessage(messageArg);
//        int chSum = messageObj.getCheckSumFromMessage();
//        System.out.println("chSum = " + chSum);
//        System.out.println(messageObj.validateCheckSum());
//        //System.out.println("messageObj = " + messageObj.getASCIICodeAsString((byte) 3));
//        System.out.println(messageObj.getMessageAsString());
//
//    }
//
//    @Override
//    public boolean calculateCheckSum() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public int extractCheckSumFromMessage() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
}
