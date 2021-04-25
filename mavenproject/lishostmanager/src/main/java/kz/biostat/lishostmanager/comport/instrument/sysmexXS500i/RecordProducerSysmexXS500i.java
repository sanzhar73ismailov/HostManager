package kz.biostat.lishostmanager.comport.instrument.sysmexXS500i;

import kz.biostat.lishostmanager.comport.entity.WorkOrder;
import kz.biostat.lishostmanager.comport.instrument.InstrumentException;
import kz.biostat.lishostmanager.comport.instrument.astm.Frame;
import kz.biostat.lishostmanager.comport.instrument.astm.MessageAstm;
import kz.biostat.lishostmanager.comport.instrument.astm.Record;
import kz.biostat.lishostmanager.comport.instrument.astm.RecordResult;
import kz.biostat.lishostmanager.comport.instrument.astm.RecordProducer;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RecordProducerSysmexXS500i implements RecordProducer {
    @Override
    public Record createHeaderRecord() throws InstrumentException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Record createPatientRecord(WorkOrder wo) throws InstrumentException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Record> createOrderRecords(WorkOrder wo) throws InstrumentException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Record createTerminatorRecord() throws InstrumentException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public RecordResult getResultRecord(Record record) throws InstrumentException {
        try {
            return new RecordResultSysmexXS500i(record.getTextRecord());
        } catch (InstrumentException ex) {
            Logger.getLogger(RecordProducerSysmexXS500i.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public String getSidFromOrderRecord(Record record) throws InstrumentException {
        //4O|1||^^          85098^M|^^^^WBC\^^^^RBC\^^^^HGB\^^^^HCT\^^^^MCV\^^^^MCH\^^^^MCHC\^^^^PLT\^^^^NEUT%\^^^^LYMPH%\^^^^MONO%\^^^^EO%\^^^^BASO%\^^^^NEUT#\^^^^LYMPH#\^^^^MONO#\^^^^EO#\^^^^BASO#\^^^^RDW-SD\^^^^RDW-CV\^^^^PDW\^^^^MPV\^^^^P-LCR\^^^^
        //sid: 85098
        String sid = "";
        String field4 = record.getTextRecord().split("\\|")[3]; //"^^          85098^M"
        String[] arr = field4.split("\\^");
        if (arr.length > 3) {
            sid = arr[2].trim();
        }
        if (sid.trim().isEmpty()) {
            throw new SysmexXS500iException("sid is empty." + " field4=" + field4);
        }
        if (!isNumeric(sid)) {
            throw new SysmexXS500iException("sid is not numeric. sid=" + sid + ", field4=" + field4);
        }
        return sid;
    }

    @Override
    public List<Record> getRecordsFromFrames(List<Frame> listFrames) throws InstrumentException {
        List<Record> records = new ArrayList<Record>();
        for (Frame frame : listFrames) {
            Record rec = ((FrameSysmexXS500i) frame).getRecord();
            records.add(rec);
        }
        return records;
    }

    @Override
    public MessageAstm createMessageFromWorkOrder(WorkOrder workOrder) throws InstrumentException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
