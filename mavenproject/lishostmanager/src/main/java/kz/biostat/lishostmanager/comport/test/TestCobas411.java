package kz.biostat.lishostmanager.comport.test;

import kz.biostat.lishostmanager.comport.instrument.cobas411.Cobas411Exception;
import kz.biostat.lishostmanager.comport.instrument.cobas411.ParamsCobas411OrderRecord;

public class TestCobas411 {
    public static void main(String[] args) throws Cobas411Exception {
        //int[] testDil = MessageCobas411.getTests();
        String[] testDil = new String[0];
        ParamsCobas411OrderRecord params = new ParamsCobas411OrderRecord("12345", "7", testDil);
       // MessageCobas411 messageOrder = MessageCobas411.createOrderMessage(params);
        //System.out.println("messageOrder = " + messageOrder);
      //  List <Frame> list =  MessageCobas411.getFramesFromMessage(messageOrder);
      //  for (Frame frame : list) {
//            System.out.println(frame);
          //  frame.printFrameAsString();
      //  }
    }
}
