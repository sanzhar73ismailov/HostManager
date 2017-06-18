package test;

import entity.Instrument;
import entity.WorkOrder;
import static instrument.ASCII.*;
import instrument.*;
import instrument.dimensionXpand.DimensionXpandException;
import instrument.dimensionXpand.MessageDimensionXpand;
import instrument.dimensionXpand.ProduceMessage;
import java.util.Arrays;
import modelHost.Model;
import modelHost.ModelException;
import modelHost.ModelImpl;

public class TestDriverDimensionXpand {

    public static void main(String[] args) throws ModelException, InstrumentException {
        //testCheckSum();
        // testExtractCheckSumFromMessage();
        //testCalculateCheckSum();
        //testValidateCheckSum();
        //testSplit();
        //testMessagePoll();
        testMainRun();
       // testGetSampleRequestMessage();
    }
    
     public static void testMainRun() throws ModelException, InstrumentException {
        Model model = new ModelImpl();
        InstrumentIndicator instrInd = InstrumentIndicator.getInstance();
        instrInd.startInstrument(3);

        Instrument instrument = model.getObject(3, new Instrument());
     //   instrument.setIp("localhost");
        instrument.setIp("192.168.1.206");
       // instrument.setIp("192.168.127.254");
//        instrument.setIp("192.168.104.21");
        DriverInstrument driver = new DriverDimensionXpand(instrument, model);
        driver.mainRun();

    }

    public static void testCheckSum() {
        String str = "P" + FS_STRING + "92300" + FS_STRING + "1" + FS_STRING
                + 1 + FS_STRING + "0" + FS_STRING;
        String str2 = "P" + FS_STRING + "92300" + FS_STRING + "0" + FS_STRING
                + 1 + FS_STRING + "0" + FS_STRING;
        String resultExpected = "6C";
        byte[] arr = str2.getBytes();
        byte sum = 0;
        for (byte b : arr) {
            sum += b;
        }
        System.out.println("sum = " + sum);
        System.out.println("toHexString(sum) = " + Integer.toHexString(sum).toUpperCase());

    }

    public static void testExtractCheckSumFromMessage() {
        String str = getPollMessageString1();
        String str2 = getPollMessageString2();
        Message mess = new MessageDimensionXpand(str2.getBytes());
        System.out.println("mess = " + mess.getMessageAsString());
        System.out.println("extractCheckSumFromMessage() = " + mess.extractCheckSumFromMessage());
    }

    private static void testCalculateCheckSum() {
        String str = getPollMessageString1();
        System.out.println("str = " + str);
        int excpectResult = 108;
        Message mess = new MessageDimensionXpand(str.getBytes());
        int result = mess.calculateCheckSum();
        System.out.println("excpectResult=" + excpectResult + ", result=" + result);
        System.out.println("test: " + (excpectResult == result));

    }

    private static void testValidateCheckSum() {
        String str1 = getPollMessageString1();
        String str2 = getPollMessageString2();
        Message mess1 = new MessageDimensionXpand(str1.getBytes());
        Message mess2 = new MessageDimensionXpand(str2.getBytes());
        System.out.println("mess1.validateCheckSum() = " + mess1.validateCheckSum());
        System.out.println("mess2.validateCheckSum() = " + mess2.validateCheckSum());

    }

    public static String getPollMessageString1() {
        String str = STX_STRING + "P" + FS_STRING + "92300" + FS_STRING + "1" + FS_STRING
                + 1 + FS_STRING + "0" + FS_STRING + "6C" + ETX_STRING;
        return str;
    }

    public static String getPollMessageString2() {
        String str = STX_STRING + "P" + FS_STRING + "92300" + FS_STRING + "0" + FS_STRING
                + 1 + FS_STRING + "0" + FS_STRING + "6B" + ETX_STRING;
        return str;
    }

    public static MessageDimensionXpand getPollMessageAsMess1() {
        return new MessageDimensionXpand(getPollMessageString1().getBytes());
    }

    public static MessageDimensionXpand getPollMessageAsMess2() {
        return new MessageDimensionXpand(getPollMessageString2().getBytes());
    }

    public static void testSplit() {
        String str = STX_STRING + "P" + FS_STRING + "92300" + FS_STRING + "0" + FS_STRING
                + 1 + FS_STRING + "0" + FS_STRING + "6B" + ETX_STRING;
        Message mess = new MessageDimensionXpand(str.getBytes());
        System.out.println("str = " + str);
        System.out.println("str = " + mess.getMessageAsString());
        String[] arr = str.split(FS_STRING);
        System.out.println(Arrays.toString(arr));
        for (String arr1 : arr) {
            System.out.println("arr1 ={" + arr1 + "}");
        }

    }

    public static void testMessagePoll() {
        MessageDimensionXpand mPoll = getPollMessageAsMess1();
        System.out.println(mPoll.getMessageAsString());
        System.out.println("isFirstPoll: " + mPoll.isFirstPoll());
        System.out.println("isBusy: " + mPoll.isBusy());
        
        MessageDimensionXpand mPoll2 = getPollMessageAsMess2();
        System.out.println(mPoll2.getMessageAsString());
        System.out.println("isFirstPoll: " + mPoll2.isFirstPoll());
        System.out.println("isBusy: " + mPoll2.isBusy());
    }
    
    public static void testGetSampleRequestMessage() throws DimensionXpandException{
        WorkOrder wo = new WorkOrder();
        wo.setSid("test01");;
        wo.setSampleType("ser");;
        wo.setPatientName("Petrov Nikolay");;
        wo.setTests("TP,AST");
        ProduceMessage pm = new ProduceMessage();
        MessageDimensionXpand mess = pm.sampleRequestMessage(wo);
        System.out.println(mess.getMessageAsString());
       
    }
}
