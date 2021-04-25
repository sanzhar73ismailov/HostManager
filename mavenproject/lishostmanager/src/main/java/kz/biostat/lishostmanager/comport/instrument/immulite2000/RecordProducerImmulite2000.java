/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.biostat.lishostmanager.comport.instrument.immulite2000;

import kz.biostat.lishostmanager.comport.entity.WorkOrder;
import static kz.biostat.lishostmanager.comport.instrument.ASCII.*;
import kz.biostat.lishostmanager.comport.instrument.DriverImmulite2000;
import kz.biostat.lishostmanager.comport.instrument.InstrumentException;
import kz.biostat.lishostmanager.comport.instrument.astm.Frame;
import kz.biostat.lishostmanager.comport.instrument.astm.MessageAstm;
import kz.biostat.lishostmanager.comport.instrument.astm.Record;
import kz.biostat.lishostmanager.comport.instrument.astm.RecordProducer;
import kz.biostat.lishostmanager.comport.instrument.astm.RecordResult;
import kz.biostat.lishostmanager.comport.instrument.astm.TypeMessage;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sanzhar.ismailov
 */
public class RecordProducerImmulite2000 implements RecordProducer {

    @Override
    public Record createHeaderRecord() throws Immulite2000Exception {
        try {
            String dateTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

            if (DriverImmulite2000.PASSWORD_INSTR.isEmpty()) {
                throw new Immulite2000Exception("password not defined in DriverImmulite2000.PASSWORD_INSTR");
            }
            if (DriverImmulite2000.SENDER_ID.isEmpty()) {
                throw new Immulite2000Exception("senderID not defined in DriverImmulite2000.SENDER_ID");
            }
            if (DriverImmulite2000.RECEIVER_ID.isEmpty()) {
                throw new Immulite2000Exception("receiverID not defined in DriverImmulite2000.RECEIVER_ID");
            }
            String text = "H|\\^&||"
                    + DriverImmulite2000.PASSWORD_INSTR + "|"
                    + DriverImmulite2000.RECEIVER_ID + "|Markova|||8N1|"
                    + DriverImmulite2000.SENDER_ID + "||P|1|" + dateTime + CR_STRING;
            Record record = new Record(text);
            return record;
        } catch (InstrumentException ex) {
            throw new Immulite2000Exception(ex);
        }
    }

    @Override
    public Record createPatientRecord(WorkOrder wo) throws InstrumentException {
        try {
            String dateTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            String patId = "";
            String patName = "";
            if (wo.getPatientNumber() != null) {
                patId = wo.getPatientNumber().length() > 20 ? wo.getPatientNumber().substring(0, 20) : wo.getPatientNumber();
                patId = replaceAllSpecialCharacters(patId);
            }
            if (wo.getPatientName() != null) {
                patName = replaceAllSpecialCharacters(wo.getPatientName());
                patName = patName.replaceAll("  ", " ");
                patName = patName.length() > 30 ? patName.substring(0, 30) : patName;
                patName = patName.replaceAll(" ", "^");
            }
            String patDateBirth = wo.getDateBirth() != null ? new SimpleDateFormat("yyyyMMdd").format(wo.getDateBirth()) : "";
            String patSex = "";
            switch (wo.getSex()) {
                case 1:
                    patSex = "M";
                    break;
                case 2:
                    patSex = "F";
                    break;
            }
            String text = "P|1|"
                    + patId + "|"
                    + "|"
                    + "|"
                    + patName + "|"
                    + "|"
                    + patDateBirth + "|"
                    + patSex + "|"
                    + "|" + "|" + "|" + "|" + CR_STRING;
            Record record = new Record(text);
            return record;
        } catch (InstrumentException ex) {
            throw new Immulite2000Exception(ex);
        }
    }

    @Override
    public List<Record> createOrderRecords(WorkOrder wo) throws InstrumentException {
        int maxSidLength = 20;
        List<Record> orderRecords = new ArrayList<>();
        String sid = wo.getSid();
        if (sid.length() > maxSidLength) {
            throw new Immulite2000Exception("length of sid more than 20 symbols: " + sid);
        }
        String dateTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String[] tests = wo.getTests().split(",");
        for (int i = 0; i < tests.length; i++) {
            try {
                String text = "O|1|"
                        + sid + "|"
                        + "|"
                        + "^^^" + tests[i] + "|"
                        + "R" + "|"
                        + dateTime + "|"
                        + dateTime + "|"
                        + "|||||" + CR_STRING;

                Record orderRecord = new Record(text);
                orderRecords.add(orderRecord);
            } catch (InstrumentException ex) {
                throw new Immulite2000Exception(ex);
            }
        }
        return orderRecords;
    }

    @Override
    public Record createTerminatorRecord() throws InstrumentException {
        try {
            String text = "L|1|N" + CR_STRING;
            Record record = new Record(text);
            return record;
        } catch (InstrumentException ex) {
            throw new Immulite2000Exception(ex);
        }
    }

    @Override
    public RecordResult getResultRecord(Record record) throws InstrumentException {
        try {
            return new RecordResultImmulite2000(record.getTextRecord());
        } catch (InstrumentException ex) {
            Logger.getLogger(Record.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static String replaceAllSpecialCharacters(String str) {
        str = str.replaceAll("\\|", "");
        str = str.replaceAll("\\\\", "");
        str = str.replaceAll("\\^", "");
        str = str.replaceAll("&", "");
        return str;
    }

    @Override
    public String getSidFromOrderRecord(Record record) throws InstrumentException {
        String sid = record.getTextRecord().split("\\|")[2];
        return sid;
    }

    @Override
    public List<Record> getRecordsFromFrames(List<Frame> listFrames) throws InstrumentException {
        List<Record> records = new ArrayList<Record>();
        for (Frame frame : listFrames) {
            Record rec = ((FrameImmulite2000) frame).getRecord();
            records.add(rec);
        }
        return records;
    }

    @Override
    public MessageAstm createMessageFromWorkOrder(WorkOrder workOrder) throws InstrumentException {
        MessageImmulite2000 message = null;
        List<Record> listRecords = new ArrayList<>();
        try {
            RecordProducer recordProducer = new RecordProducerImmulite2000();
            Record headerRecord = recordProducer.createHeaderRecord();
            listRecords.add(headerRecord);
            Record patientRecord = recordProducer.createPatientRecord(workOrder);
            listRecords.add(patientRecord);
            List<Record> orderRecords = recordProducer.createOrderRecords(workOrder);
            for (Record orderRecord : orderRecords) {
                listRecords.add(orderRecord);
            }
            Record terminatorRecord = recordProducer.createTerminatorRecord();
            listRecords.add(terminatorRecord);
            message = new MessageImmulite2000(listRecords);
            message.setTypeMessage(TypeMessage.ORDER_TO_INSTRUMENT);
        } catch (InstrumentException ex) {
            throw new Immulite2000Exception(ex);
        }
        return message;
    }

}
