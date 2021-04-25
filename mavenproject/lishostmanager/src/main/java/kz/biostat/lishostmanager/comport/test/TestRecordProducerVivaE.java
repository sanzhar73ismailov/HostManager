package kz.biostat.lishostmanager.comport.test;

import kz.biostat.lishostmanager.comport.entity.WorkOrder;
import kz.biostat.lishostmanager.comport.instrument.InstrumentException;
import kz.biostat.lishostmanager.comport.instrument.astm.Record;
import kz.biostat.lishostmanager.comport.instrument.astm.RecordProducer;
import kz.biostat.lishostmanager.comport.instrument.vivaE.RecordProducerVivaE;
import java.util.logging.Level;
import java.util.logging.Logger;
import kz.biostat.lishostmanager.comport.modelHost.Model;
import kz.biostat.lishostmanager.comport.modelHost.ModelException;
import kz.biostat.lishostmanager.comport.modelHost.ModelImpl;

public class TestRecordProducerVivaE {

    public static void main(String[] args) throws InstrumentException {

//        testCreateHeaderRecord();
        testCreatePatientRecord();
    }

    private static void testCreateHeaderRecord() throws InstrumentException {
        RecordProducer recProd = new RecordProducerVivaE();
        Record record = recProd.createHeaderRecord();
        System.out.println("record = " + record);

    }

    private static void testCreatePatientRecord() throws InstrumentException {
        RecordProducer recProd = new RecordProducerVivaE();
        WorkOrder wo = getTestWorkOrder();
        System.out.println("wo = " + wo);
        Record record = recProd.createPatientRecord(wo);
        System.out.println("record = " + record);
    }

    private static WorkOrder getTestWorkOrder() {
        try {
            WorkOrder wo = null;
            Model model = new ModelImpl();
            wo = model.getObject(177, new WorkOrder());
            return wo;
        } catch (ModelException ex) {
            Logger.getLogger(TestRecordProducerVivaE.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
