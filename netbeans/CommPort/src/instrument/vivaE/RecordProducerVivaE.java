package instrument.vivaE;

import entity.WorkOrder;
import static instrument.ASCII.*;
import instrument.InstrumentException;
import instrument.adviaCentaurCP.AdviaCentaurException;
import instrument.astm.Frame;
import instrument.astm.MessageAstm;
import instrument.astm.Record;
import instrument.astm.RecordProducer;
import instrument.astm.RecordResult;
import instrument.astm.TypeMessage;
import static instrument.immulite2000.RecordProducerImmulite2000.replaceAllSpecialCharacters;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecordProducerVivaE implements RecordProducer {

    @Override
    public Record createHeaderRecord() throws InstrumentException {
        try {
            //String senderId = "NG_LIS"; //default
            //String senderId = "vivae^"; //default
            String senderId = ""; //default
            //String receiverId = "ACCP1";
            String receiverId = "";
            String text = "H" //1
                    + "|\\^&" + "|"//2
                    + "" + "|"//3
                    + "" + "|"//4
                    + senderId + "|"//5
                    + "" + "|"//6
                    + "" + "|"//7
                    + "" + "|"//8
                    + "4.0" + "|"//9
                    + receiverId + "|"//10
                    + "" + "|"//11
                    + "P" + "|"//12
                    + "" + "|" //13
                    + "" + "" //14
                    + CR_STRING;//
            Record record = new Record(text);
            return record;
        } catch (InstrumentException ex) {
            throw new VivaeException(ex);
        }
    }

    @Override
    public Record createPatientRecord(WorkOrder wo) throws InstrumentException {
        try {
            String dateTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            String patId = "";
            String patName = "";
            if (wo.getPatientNumber() != null) {
                patId = wo.getPatientNumber().length() > 13 ? wo.getPatientNumber().substring(0, 13) : wo.getPatientNumber();
                patId = replaceAllSpecialCharacters(patId);
            }
            if (wo.getPatientName() != null) {
                patName = replaceAllSpecialCharacters(wo.getPatientName());
                patName = patName.replaceAll("  ", " ").trim();
                patName = patName.length() > 20 ? patName.substring(0, 20) : patName;
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
            String text = "P|" //1
                    + "1|" //2
                    + patId + "|" //3
                    + "|" //4
                    + "|" //5
                    + patName + "|" //6
                    + "|" //7
                    + patDateBirth + "|" //8
                    + patSex //9
                    + CR_STRING;
            Record record = new Record(text);
            return record;
        } catch (InstrumentException ex) {
            throw new VivaeException(ex);
        }
    }

    @Override
    public List<Record> createOrderRecords(WorkOrder wo) throws InstrumentException {
        int maxSidLength = 12;
        List<Record> orderRecords = new ArrayList<>();
        String sid = wo.getSid();
        if (sid.length() > maxSidLength) {
            throw new VivaeException("length of sid more than " + maxSidLength + " symbols: " + sid);
        }
        String[] tests = wo.getTests().split(",");
        String testText = "";
        for (int i = 0; i < tests.length; i++) {
            testText += "^^^" + tests[i] + "\\";
        }
        testText = testText.substring(0, testText.length() - 1);
        try {
            String text = "O|" //1
                    + "1|" //2
                    + sid + "|" //3
                    + "|" //4
                    + testText + "|" //5
                    + "R" + "|" //6
                    + "|" // 7
                    + "|" // 8
                    + "|" // 9
                    + "|" // 10
                    + "|" // 11
                    + "|" // 12
                    + "|" // 13
                    + "|" // 14
                    + "|" // 15
                    + "|" // 16
                    + "|" // 17
                    + "|" // 18
                    + "|" // 19
                    + "|" // 20
                    + "|" // 21
                    + "|" // 22
                    + "|" // 23
                    + "|" // 24
                    + "|" // 25
                    + "|" // 26
                    + CR_STRING;

            Record orderRecord = new Record(text);
            orderRecords.add(orderRecord);
        } catch (InstrumentException ex) {
            throw new VivaeException(ex);
        }
        return orderRecords;
    }

    @Override
    public Record createTerminatorRecord() throws InstrumentException {
        try {
            String text = "L|1|" + CR_STRING;
            Record record = new Record(text);
            return record;
        } catch (InstrumentException ex) {
            throw new VivaeException(ex);
        }
    }

    @Override
    public RecordResult getResultRecord(Record record) throws InstrumentException {
        try {
            return new RecordResultVivaE(record.getTextRecord());
        } catch (InstrumentException ex) {
            throw new VivaeException(ex);
        }
    }

    @Override
    public String getSidFromOrderRecord(Record record) throws InstrumentException {
        String sid = record.getTextRecord().split("\\|")[2];
        return sid;
    }

    @Override
    public List<Record> getRecordsFromFrames(List<Frame> listFrames) throws InstrumentException {
        List<Record> records = new ArrayList<>();
        try {
            StringBuilder stb = new StringBuilder();
            for (Frame frame : listFrames) {
                String frameText = frame.getText();
                //Сзади убираем 5 символов <ETX>CHK1CHK2<CR><LF> (вместо <ETX> может быть <ETB>) <CR> перед ETX не убираем, он должен входить в record
                String str = frameText.substring(2, frameText.length() - 5);
                String [] strArray = str.split("\r");
                
                for (String recordText : strArray) {
                    Record record = new Record(recordText + "\r");
                    records.add(record);
                    recordText = "";
                }
            }
        } catch (Exception ex) {
            throw new AdviaCentaurException(ex);
        }
        return records;
    }

    @Override
    public MessageAstm createMessageFromWorkOrder(WorkOrder workOrder) throws InstrumentException {
        MessageVivaE message = null;
        List<Record> listRecords = new ArrayList<>();
        try {
            RecordProducer recordProducer = new RecordProducerVivaE();
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
            message = new MessageVivaE(listRecords);
            message.setTypeMessage(TypeMessage.ORDER_TO_INSTRUMENT);
        } catch (InstrumentException ex) {
            throw new AdviaCentaurException(ex);
        }
        return message;
    }

}
