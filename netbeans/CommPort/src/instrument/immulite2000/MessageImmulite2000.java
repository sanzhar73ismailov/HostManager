/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package instrument.immulite2000;

import entity.WorkOrder;
import static instrument.ASCII.*;
import instrument.InstrumentException;
import instrument.astm.Frame;
import instrument.astm.Record;
import instrument.astm.RecordProducer;
import instrument.astm.TypeMessage;
import instrument.astm.TypeRecord;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sanzhar.ismailov
 */
public class MessageImmulite2000 extends instrument.astm.MessageAstm {

//    List<RecordImmulite2000> records;
//    private TypeMessage typeMessage;
    public MessageImmulite2000() {
    }

    public MessageImmulite2000(List<Record> records) {
        super(records);
        recordProducer = new RecordProducerImmulite2000();
    }

    @Override
    public void defineTypeMessage() {
        for (Record record : records) {
            if (record.getTypeRecord() == TypeRecord.QUERY) {
                typeMessage = TypeMessage.QUERY_FROM_INSTRUMENT;
                break;
            } else if (record.getTypeRecord() == TypeRecord.RESULT) {
                typeMessage = TypeMessage.RESULT_FROM_INSTRUMENT;
                break;
            }
        }
    }

    @Override
    public List<Frame> getFrames() {
        List<Frame> frames = new ArrayList<>();
        int frameNumber = 1;
        for (int i = 0; i < records.size(); i++) {
            Record record = records.get(i);
            String text = STX_STRING + frameNumber + record.getTextRecord() + ETX_STRING;
            String chk = FrameImmulite2000.calculateChk(text.getBytes());
            text += chk + CR_STRING + LF_STRING;
            FrameImmulite2000 frame = new FrameImmulite2000(text);
            frames.add(frame);
            frameNumber++;
            if ((i + 1) % 7 == 0) {
                frameNumber = 0;
            }
        }
        return frames;
    }

    public String getSidFromQueryRecord() {
        Record queryRecord = null;
        for (Record record : records) {
            if (record.getTypeRecord() == TypeRecord.QUERY) {
                queryRecord = record;
                break;
            }
        }
        String sid = queryRecord.getTextRecord().split("\\|")[2];
        sid = sid.replaceAll("\\^", "");
        return sid;
    }

    @Deprecated
    public static MessageImmulite2000 createMessageFromWorkOrder(WorkOrder wo) throws Immulite2000Exception {
        MessageImmulite2000 message = null;
        List<Record> listRecords = new ArrayList<>();
        try {
            RecordProducer recordProducer = new RecordProducerImmulite2000();
            Record headerRecord = recordProducer.createHeaderRecord();
            listRecords.add(headerRecord);
            Record patientRecord = recordProducer.createPatientRecord(wo);
            listRecords.add(patientRecord);
            List<Record> orderRecords = recordProducer.createOrderRecords(wo);
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
