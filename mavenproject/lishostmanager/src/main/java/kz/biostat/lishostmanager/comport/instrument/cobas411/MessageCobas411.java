package kz.biostat.lishostmanager.comport.instrument.cobas411;

import kz.biostat.lishostmanager.comport.instrument.InstrumentException;
import kz.biostat.lishostmanager.comport.instrument.astm.Record;
import java.util.ArrayList;
import java.util.List;
import kz.biostat.lishostmanager.comport.instrument.astm.*;

public class MessageCobas411 extends MessageAstm {

   

    public MessageCobas411(List<RecordCobas411> records) throws Cobas411Exception {
        for (RecordCobas411 record : records) {
            super.records.add(record);
        }
       
    }

   

    @Override
    public void defineTypeMessage() throws Cobas411Exception {
        RecordCobas411 headerRecord = getHeaderRecord();
        String headTextRecord = headerRecord.getTextRecord();
        //System.out.println("in defineTypeMessage textRecord = " + textRecord);
        String[] fields = headTextRecord.split("\\|");
        //System.out.println(java.util.Arrays.toString(fields));
        String commentOrSpecialInstractions = fields[10];
        String[] meaningOfMessageArray = commentOrSpecialInstractions.split("\\^");
        String meaningOfMessage = "ERROR";
        if (meaningOfMessageArray.length > 0) {
            meaningOfMessage = commentOrSpecialInstractions.split("\\^")[0];
        }
        switch (meaningOfMessage) {
            case "TSREQ":
                this.typeMessage = TypeMessage.QUERY_FROM_INSTRUMENT;
                break;
            case "RSUPL":
                this.typeMessage = TypeMessage.RESULT_FROM_INSTRUMENT;
                break;
            case "TSDWN":
                this.typeMessage = TypeMessage.ORDER_TO_INSTRUMENT;
                break;
            case "ERROR":
                this.typeMessage = TypeMessage.ERROR_FROM_INSTRUMENT;
                break;
            default:
                throw new Cobas411Exception("Unknown meaning of Message: " + meaningOfMessage);
        }
    }

    public RecordCobas411 getRecordByType(TypeRecord typeRecord) {
        RecordCobas411 returnRecord = null;
        for (Record record : records) {
            if (record.getTypeRecord() == typeRecord) {
                returnRecord = (RecordCobas411) record;
                break;
            }
        }
        return returnRecord;
    }

    public RecordCobas411 getHeaderRecord() {
        return getRecordByType(TypeRecord.HEAD);
    }

    public RecordCobas411 getOrderRecord() {
        return getRecordByType(TypeRecord.ORDER);
    }

    public RecordCobas411 getQueryRecord() {
        return getRecordByType(TypeRecord.QUERY);
    }

    public List<RecordCobas411> getResultsRecords() {
        List<RecordCobas411> returnRecords = new ArrayList<>();
        for (Record record : records) {
            if (record.getTypeRecord() == TypeRecord.RESULT) {
                returnRecords.add((RecordCobas411) record);
            }
        }
        return returnRecords;
    }

    public TypeMessage getTypeMessage() {
        return typeMessage;
    }

    @Override
    public String toString() {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("MessageCobas411{" + "records=");
        for (Record record : records) {
            strBuilder.append("\n").append(record.getTextRecord().replace("\r", "<CR>\r"));
        }
        strBuilder.append(", typeMessage=" + typeMessage + '}');
        return strBuilder.toString();
    }

    public static MessageCobas411 createOrderMessage(ParamsCobas411OrderRecord params, boolean batchOrder) throws Cobas411Exception {
        List<RecordCobas411> recordsForMessage = new ArrayList<>();
        RecordCobas411 recordHeader;
        if (batchOrder) {
            recordHeader = RecordCobas411.createHeaderRecordFromHostOrderBatch();
        } else {
            recordHeader = RecordCobas411.createHeaderRecordFromHostOrderReply();
        }
        RecordCobas411 recordPatient = RecordCobas411.createPatientRecordFromHost();
        RecordCobas411 recordOrder = RecordCobas411.createOrderRecordFromHost(params);
        RecordCobas411 recordTerminal = RecordCobas411.createTerminalRecordFromHost();
        recordsForMessage.add(recordHeader);
        recordsForMessage.add(recordPatient);
        recordsForMessage.add(recordOrder);
        recordsForMessage.add(recordTerminal);
        MessageCobas411 message = new MessageCobas411(recordsForMessage);
        return message;
    }

    public static MessageCobas411 createQueryMessageFromHost(int[] tests, ParamsCobas411OrderRecord params) throws Cobas411Exception {
        List<RecordCobas411> recordsForMessage = new ArrayList<>();
        RecordCobas411 recordHeader = RecordCobas411.createHeaderRecordFromHostOrderBatch();
        //Record recordPatient = Record.createPatientRecordFromHost();
        RecordCobas411 recordOrder = null;
        RecordCobas411 recordTerminal = RecordCobas411.createTerminalRecordFromHost();
        recordsForMessage.add(recordHeader);
        // recordsForMessage.add(recordPatient);
        recordsForMessage.add(recordOrder);
        recordsForMessage.add(recordTerminal);
        MessageCobas411 message = new MessageCobas411(recordsForMessage);
        return message;
    }

    public static MessageCobas411 createQueryMessageFromInstrumentForTesting() throws Cobas411Exception {
        String sid = "000004", seqNum = "40", position = "5"; /*SID=000004, seqNo=40, Disk=0 and Position=5 */
        // String   sid = "@40", seqNum="40", position="5"; /*SID=@40, seqNo=40, Disk=0 and Position=5 */
        //String sid = "@40", seqNum="40", position="7"; /*SID=@40, seqNo=40, Disk=0 and Position=7 */
        ParamsCobas411OrderRecord params = new ParamsCobas411OrderRecord();
        params.setSid(sid);
        params.setSequenceNumber(seqNum);
        params.setRack("0");
        params.setPosition(position);

        List<RecordCobas411> recordsForMessage = new ArrayList<>();
        RecordCobas411 recordHeader = RecordCobas411.getHeaderRecordFromInstrumentForQuery();
        RecordCobas411 recordQuery = RecordCobas411.createQueryRecordFromInstrument(params);
        RecordCobas411 recordTerminal = RecordCobas411.createTerminalRecordFromHost();
        recordsForMessage.add(recordHeader);
        recordsForMessage.add(recordQuery);
        recordsForMessage.add(recordTerminal);
        MessageCobas411 message = new MessageCobas411(recordsForMessage);
        return message;
    }
    
     public static MessageCobas411 createResultsMessageFromInstrumentForTesting() throws Cobas411Exception {
        try {
            String sid = "000004", seqNum = "40", position = "5"; /*SID=000004, seqNo=40, Disk=0 and Position=5 */
            // String   sid = "@40", seqNum="40", position="5"; /*SID=@40, seqNo=40, Disk=0 and Position=5 */
            //String sid = "@40", seqNum="40", position="7"; /*SID=@40, seqNo=40, Disk=0 and Position=7 */
            ParamsCobas411OrderRecord params = new ParamsCobas411OrderRecord();
            params.setSid(sid);
            params.setSequenceNumber(seqNum);
            params.setRack("0");
            params.setPosition(position);
            List<RecordCobas411> recordsForMessage = new ArrayList<>();
            RecordCobas411 recordHeader = RecordCobas411.getHeaderRecordFromInstrumentWithResults();
            RecordCobas411 recordOrder = new RecordCobas411("O|1|100|1^0^1^^S1^SC|^^^4^1\\^^^12^1|R||20150212101744||||N||||1|||||||20150212104839|||F\r");
            RecordCobas411 resultRecord1 = new RecordCobas411("R|1|^^^4/1/not|0.484|nmol/l||N||F|||||E1\r");
            RecordCobas411 resultRecord2 = new RecordCobas411("R|2|^^^12/1/not|-1^0.101|COI||N||F|||||E1\r");
            RecordCobas411 recordTerminal = RecordCobas411.createTerminalRecordFromHost();
            recordsForMessage.add(recordHeader);
            recordsForMessage.add(recordOrder);
            recordsForMessage.add(resultRecord1);
            recordsForMessage.add(resultRecord2);
            recordsForMessage.add(recordTerminal);
            MessageCobas411 message = new MessageCobas411(recordsForMessage);
            return message;
        } catch (InstrumentException ex) {
             throw new Cobas411Exception(ex);
        }
    }

    public static int[] getTests() {
        int[] tests = {111, 750, 140};
        return tests;
    }

    public List<FrameCobas411> getFramesFromMessage() throws Cobas411Exception {
        List<FrameCobas411> frames = new ArrayList<>();
        List<Record> listRecords = this.getRecords();
        StringBuilder strBuilder = new StringBuilder();
        for (Record record : listRecords) {
            strBuilder.append(record.getTextRecord());
        }
        String textAllRecords = strBuilder.toString();
        String[] arrTokensForFrames = textAllRecords.split(String.format("(?<=\\G.{%s})", FrameCobas411.SIZE_FRAME_MAX));
        for (int i = 0; i < arrTokensForFrames.length; i++) {
            String string = arrTokensForFrames[i];
            boolean lastFrame = (i + 1) == arrTokensForFrames.length;
            FrameCobas411 frame = new FrameCobas411(i + 1, string, lastFrame);
            //Frame.forminOutcomeFrame(string, i + 1, lastFrame);
            frames.add(frame);
        }
        return frames;
    }

    @Override
    public String getSidFromQueryRecord() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Frame> getFrames() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
}
