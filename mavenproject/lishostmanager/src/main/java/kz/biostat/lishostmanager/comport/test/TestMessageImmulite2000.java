/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.biostat.lishostmanager.comport.test;

import kz.biostat.lishostmanager.comport.entity.WorkOrder;
import kz.biostat.lishostmanager.comport.instrument.immulite2000.Immulite2000Exception;

import java.util.Date;

/**
 *
 * @author sanzhar.ismailov
 */
public class TestMessageImmulite2000 {
    public static void main(String[] args) throws Immulite2000Exception {
        testGetFrames();
    }
    
    public static void testGetFrames() throws Immulite2000Exception{
        WorkOrder wo = new WorkOrder();
        wo.setPatientName("Ivanov Sidor Ivanovich1234567890");
        wo.setPatientNumber("1234567890qwertyuiop[]asdfghkjlzxcvbnm");
        wo.setDateBirth(new Date());
        wo.setSid("1023");
        wo.setTests("qwert,THR,UTE");
        wo.setSex(2);
        //MessageImmulite2000 messageToSent = MessageImmulite2000.createMessageFromWorkOrder(wo);
        //List<Frame>listFrames = messageToSent.getFrames();
        //for (Frame frame : listFrames) {
          //  System.out.println("frame = " + frame.getFrameAsString());
        //}
        
    }
}
