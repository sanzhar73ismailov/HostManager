package kz.biostat.lishostmanager.comport.test;

import kz.biostat.lishostmanager.comport.entity.WorkOrder;
import kz.biostat.lishostmanager.comport.instrument.sysmexCA660.Parameters;
import kz.biostat.lishostmanager.comport.instrument.sysmexCA660.SysmexCA660Message;
import java.util.Arrays;
import kz.biostat.lishostmanager.comport.modelHost.Model;
import kz.biostat.lishostmanager.comport.modelHost.ModelException;
import kz.biostat.lishostmanager.comport.modelHost.ModelImpl;

public class TestSysmexCA660Message {
    public static void main(String[] args) throws ModelException {
//        testParameters();
        testCreateOrderFromWorkrder();
    }
    
    public static void testParameters(){
        String strResults = "D1210101U9605012359000110123-456-78901M****NAME***041  150 042   75 043   75 044   75 051  150 052    1 ";
        String strInQuary = "R1210101 060515163600010123-234567-001B           040      050      060      510      ";
        SysmexCA660Message messageRes = new SysmexCA660Message(strResults.getBytes());
        SysmexCA660Message messageQuary = new SysmexCA660Message(strInQuary.getBytes());
        Parameters par1 = null;//messageRes.getParameters();
        Parameters par2 = null;//messageQuary.getParameters();
        //System.out.println("par.text");
        System.out.println("par Results= " + par1);
        System.out.println("===============================");
        System.out.println("par Quary = " + par2);
        System.out.println("===============================");
        String [] strArr = {"aaa","bbb","ccc"};
        System.out.println(Arrays.toString(strArr));
    }

    private static void testCreateOrderFromWorkrder() throws ModelException {
        Model model = new ModelImpl();
        WorkOrder wo = model.getObject(186, new WorkOrder());
        //String str = SysmexCA660Message.createOrderFromWorkOrder(wo);
        //System.out.println("str = " + ASCII.getStringWithAsciiCodes(str));
    }
    
    
}
