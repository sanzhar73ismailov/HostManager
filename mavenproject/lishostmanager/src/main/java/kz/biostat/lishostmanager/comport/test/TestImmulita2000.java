/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.biostat.lishostmanager.comport.test;

import kz.biostat.lishostmanager.comport.entity.WorkOrder;
import kz.biostat.lishostmanager.comport.instrument.ASCII;
import kz.biostat.lishostmanager.comport.instrument.immulite2000.Immulite2000Exception;
import kz.biostat.lishostmanager.comport.modelHost.Model;
import kz.biostat.lishostmanager.comport.modelHost.ModelException;
import kz.biostat.lishostmanager.comport.modelHost.ModelImpl;

/**
 *
 * @author sanzhar.ismailov
 */
public class TestImmulita2000 {
    public static void main(String[] args) throws Immulite2000Exception, ModelException {
//        testRecordClassCreateHeaderRecord();
//        testRecordClassCreatePatientRecord();
//        testRecordClassCreateOrderRecord();
        testSubstr();

        
    }

    public static void main1(String[] args) {
        String str1 = "2P|1||||||||||||";// + ASCII.CR_STRING;//+ ASCII.ETX_STRING;
        String str2 = "2P|1|101|||Riker^Al||19611102|F|||||Bashere" + ASCII.CR_STRING+ ASCII.ETX_STRING;
        String str3 = "6O|1|130000724||^^^E2|||19950118122000" + ASCII.CR_STRING+ ASCII.ETX_STRING;
        String str4 = "7R|1|^^^E2|25.3|pg/mL|12\\12^2000\\2000|N|N|F||test|19950119084815|19950119100049|SenderID" + ASCII.CR_STRING+ ASCII.ETX_STRING;
        String str="1H|\\^&||PASSWORD|DPC CIRRUS||Randolph^New^Jersey^07869||(201)927-2828|N81|Your System||P|1|19940407120613"  + ASCII.CR_STRING+ ASCII.ETX_STRING;
        byte[] arr = str.getBytes();
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            byte b = arr[i];
            sum += b;
        }
        System.out.println("sum = " + sum);
        System.out.println("sum = " + Integer.toHexString(sum));
        System.out.println("sum = " + Integer.toBinaryString(sum));
        
    }
    
    public static void testRecordClassCreateHeaderRecord() throws Immulite2000Exception{
       // RecordImmulite2000 rec = RecordImmulite2000.createHeaderRecord();
        //System.out.println("rec = " + rec);
    }
    
    public static void testRecordClassCreatePatientRecord() throws Immulite2000Exception, ModelException{
        Model model = new ModelImpl();
        WorkOrder wo = model.getObject(152, new WorkOrder());
//        WorkOrder wo = new WorkOrder();
//        wo.setPatientName("Ivanov Sidor Ivanovich1234567890");
//        wo.setPatientNumber("1234567890qwertyuiop[]asdfghkjlzxcvbnm");
//        wo.setDateBirth(new Date());
//        wo.setSex(2);
//        RecordImmulite2000 rec = RecordImmulite2000.createPatientRecord(wo);
    //    System.out.println("rec = " + rec);
    }
    public static void testRecordClassCreateOrderRecord() throws Immulite2000Exception, ModelException{
        Model model = new ModelImpl();
        WorkOrder wo = model.getObject(152, new WorkOrder());
//        WorkOrder wo = new WorkOrder();
//        wo.setPatientName("Ivanov Sidor Ivanovich1234567890");
//        wo.setPatientNumber("1234567890qwertyuiop[]asdfghkjlzxcvbnm");
//        wo.setDateBirth(new Date());
//        wo.setSid("1023");
//        wo.setTests("qwert,THR,UTE");
//        wo.setSex(2);
        //Record rec = new RecordProducerImmulite2000().createOrderRecords(wo);
        //System.out.println("rec = " + rec);
    }
    public static void testSubstr(){
        String testText = "123123213/";
        testText = testText.substring(0, testText.length() - 1);
        System.out.println("testText = " + testText);
    }

}
