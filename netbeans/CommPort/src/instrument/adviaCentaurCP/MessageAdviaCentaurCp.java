/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package instrument.adviaCentaurCP;

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
public class MessageAdviaCentaurCp extends instrument.astm.MessageAstm {

    public MessageAdviaCentaurCp() {
    }

    public MessageAdviaCentaurCp(List<Record> records) throws InstrumentException {
        super(records);
        recordProducer = new RecordProducerAdviaCentaurCp();
    }

    @Override
    public String getSidFromQueryRecord() {
         Record queryRecord = null;
        for (Record record : records) {
            if (record.getTypeRecord() == TypeRecord.QUERY) {
                queryRecord = record;
                break;
            }
        }
        if(queryRecord.getTextRecord().replaceAll("^", "").equals("ALL")){
            return "ALL";
        }
        String sid = queryRecord.getTextRecord().split("\\|")[2];
        sid = sid.split("\\^")[1];
        return sid;
    }

    @Override
    public List<Frame> getFrames() {
        List<Frame> frames = new ArrayList<>();
        int frameNumber = 1;
        for (int i = 0; i < records.size(); i++) {
            String recordText = records.get(i).getTextRecord();

            List<String> textsForFrames = new ArrayList<>();
            if (recordText.length() <= Frame.SIZE_FRAME_MAX) {
                String text = STX_STRING + frameNumber++ + recordText + ETX_STRING;
                textsForFrames.add(text);
            } else {
                String[] arrTokensForFrames = recordText.split(String.format("(?<=\\G.{%s})", Frame.SIZE_FRAME_MAX));
                for (int j = 0; j < arrTokensForFrames.length; j++) {
                    String str = arrTokensForFrames[j];
                    boolean lastFrame = (j + 1) == arrTokensForFrames.length;
                    String text = STX_STRING + frameNumber++ + str + (lastFrame ? ETX_STRING : ETB_STRING);
                    textsForFrames.add(text);
                }
            }

            for (String text : textsForFrames) {
                String chk = Frame.calculateChk(text.getBytes());
                text += chk + CR_STRING + LF_STRING;
                Frame frame = new FrameAdviaCentaurCp(text);
                frames.add(frame);
            }

            // if ((i + 1) % 7 == 0) {
            //    frameNumber = 0;
            //}
        }
        return frames;
    }

}
