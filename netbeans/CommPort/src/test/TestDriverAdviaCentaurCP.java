package test;

import entity.Instrument;
import instrument.ASCII;
import static instrument.ASCII.*;
import instrument.DriverAdviaCentaurCP;
import instrument.DriverInstrument;
import instrument.InstrumentException;
import instrument.InstrumentIndicator;
import instrument.Message;
import instrument.adviaCentaurCP.AdviaCentaurException;
import instrument.adviaCentaurCP.FrameAdviaCentaurCp;
import instrument.adviaCentaurCP.MessageAdviaCentaurCp;
import instrument.adviaCentaurCP.RecordProducerAdviaCentaurCp;
import instrument.astm.Frame;
import instrument.astm.MessageAstm;
import instrument.astm.Record;
import instrument.cobas411.Cobas411Exception;
import instrument.immulite2000.RecordProducerImmulite2000;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelHost.Model;
import modelHost.ModelException;
import modelHost.ModelImpl;

public class TestDriverAdviaCentaurCP {

    public static void main(String[] args) {
        try {
            testMainRun();
//            testChk();
            // splitByCR();

            //testGetRecordsFromFrames();
        } catch (Throwable ex) {
            Throwable th = ex;
            int counter = 0;
            do {
                System.err.println("<<< Stack trace " + ++counter);
                th.printStackTrace();
                ex = th;
                th = ex.getCause();
            } while (!th.getMessage().equals(ex.getMessage()));
        }
    }

    public static void testMainRun() throws ModelException, InstrumentException {
        int istrId = 5;
        Model model = new ModelImpl();
        InstrumentIndicator instrInd = InstrumentIndicator.getInstance();
        instrInd.startInstrument(istrId);

        Instrument instrument = model.getObject(istrId, new Instrument());
        //   instrument.setIp("localhost");
        instrument.setIp("192.168.1.208");
        DriverInstrument driver = new DriverAdviaCentaurCP(instrument, model);
        driver.mainRun();

    }

    private static void testChk() throws Cobas411Exception {
        //<STX>1H|\^&|||ACCP1|||||Host||P|1|20141217090209<CR><ETX>E2<CR><LF><EOT>
        String str = STX_STRING + "1H|\\^&|||ACCP1|||||Host||P|1|20141217090209" + CR_STRING + ETX_STRING;
        // Frame frame = new Frame(str.getBytes());
        byte[] arr = calculateChk(str.getBytes());
        System.out.println(Arrays.toString(arr));
        for (byte b : arr) {
            System.out.println(ASCII.getASCIICodeAsString(b));
        }

    }

    public static byte[] calculateChk(byte[] bytes) {
        int sum = 0;
        sum += getSumOfByteArray(bytes);
        return lastTwoBytesFromInt(sum);
    }

    private static byte[] lastTwoBytesFromInt(int value) {
        String strHex = Integer.toHexString(value).toUpperCase();
        String lastTwoSymbols = strHex.substring(strHex.length() - 2, strHex.length());
        byte[] bytesReturn = lastTwoSymbols.getBytes();
        return bytesReturn;
    }

    private static int getSumOfByteArray(byte[] array) {
        int sum = 0;
        for (byte b : array) {
            if (b == STX) {
                continue;
            }
            sum += b;
        }
        return sum;
    }

    private static void splitByCR() {
        String str = "123\r456\r789";

        System.out.println("str = " + Arrays.toString(str.getBytes()));
        String[] arr = str.split("\r");
        System.out.println(Arrays.toString(arr));
    }

    private static void testGetRecordsFromFrames() throws AdviaCentaurException, InstrumentException {
        List<Frame> listFrames = null;
        //listFrames = getListFramesAllEtx();
        listFrames = getListFramesAllEtxAndOnEtb();
        for (Frame listFrame : listFrames) {
            System.out.println("listFrame = " + listFrame);
        }
        List<Record> records = new RecordProducerAdviaCentaurCp().getRecordsFromFrames(listFrames);
        for (Record record : records) {
            System.out.println(record);
        }
    }

    private static List<Frame> getListFramesAllEtx() {
        Frame fr1 = new FrameAdviaCentaurCp(STX_STRING + 1 + "H|\\^&|||ACCP1|||||Host||P|1|20141217173214" + "\r" + ETX_STRING + "EO" + "\r\n");
        Frame fr2 = new FrameAdviaCentaurCp(STX_STRING + 2 + "P|1||||^^||||||||||||||||||||" + CR_STRING + ETX_STRING + "9B" + "\r\n");
        Frame fr3 = new FrameAdviaCentaurCp(STX_STRING + 3 + "O|1|1486||^^^eE2^^|R||||||||||||||||||||P" + CR_STRING + ETX_STRING + "06" + "\r\n");
        Frame fr4 = new FrameAdviaCentaurCp(STX_STRING + 4 + "R|1|^^^eE2^^^^DOSE||||||P||||" + CR_STRING + ETX_STRING + "80" + "\r\n");
        Frame fr5 = new FrameAdviaCentaurCp(STX_STRING + 5 + "R|2|^^^eE2^^^^RLU||||||P||||" + CR_STRING + ETX_STRING + "4A" + "\r\n");
        Frame fr6 = new FrameAdviaCentaurCp(STX_STRING + 6 + "L|1|N<CR><ETX>09" + CR_STRING + ETX_STRING + "4A" + "\r\n");
        List<Frame> listFrames = new ArrayList<>();
        listFrames.add(fr1);
        listFrames.add(fr2);
        listFrames.add(fr3);
        listFrames.add(fr4);
        listFrames.add(fr5);
        listFrames.add(fr6);
        return listFrames;
    }

    private static List<Frame> getListFramesAllEtxAndOnEtb() throws InstrumentException {
        Record rec1 = new Record("H|\\^&|||ACCP1|||||Host||P|1|20141217173214\r");
        Record rec2 = new Record("P|1||||^^||||||||||||||||||||\r");
        Record rec3 = new Record("O|1|1486||^^^eE2^^|R||||||||||||||||||||P\r");
        Record rec4 = new Record("R|1|^^^eE2^^^^DOSE||||||P||||qR|1|^^^eE2^^^^DOSE||||||P||||qR|1|^^^eE2^^^^DOSE||||||P||||qR|1|^^^eE2^^^^DOSE||||||P||||qR|1|^^^eE2^^^^DOSE||||||P||||qR|1|^^^eE2^^^^DOSE||||||P||||qR|1|^^^eE2^^^^DOSE||||||P||||qR|1|^^^eE2^^^^DOSE^^^eE3^^^^MOSE||||||P||||qR|1|^^^eE2^^^^DOSE||||||P||||\r");
        Record rec5 = new Record("L|1|N<CR><ETX>09\r");
        List<Record> listRecords = new ArrayList<>();
        listRecords.add(rec1);
        listRecords.add(rec2);
        listRecords.add(rec3);
        listRecords.add(rec4);
        listRecords.add(rec5);
        MessageAstm mes = new MessageAdviaCentaurCp(listRecords);
        List<Frame> listFrames = mes.getFrames();
        return listFrames;
    }

}
