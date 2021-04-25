/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.biostat.lishostmanager.comport.instrument.vivaE;

import static kz.biostat.lishostmanager.comport.instrument.ASCII.*;
import kz.biostat.lishostmanager.comport.instrument.astm.Frame;
import kz.biostat.lishostmanager.comport.instrument.astm.Record;
import kz.biostat.lishostmanager.comport.instrument.astm.TypeRecord;
import kz.biostat.lishostmanager.comport.instrument.astm.MessageAstm;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sanzhar.ismailov
 */
public class MessageVivaE extends MessageAstm {

    public MessageVivaE(List<Record> records) {
        super(records);
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
        String sid = queryRecord.getTextRecord().split("\\|")[2];
        sid = sid.replaceAll("\\^", "");
        return sid;
    }

    @Override
    public List<Frame> getFrames() {
        List<Frame> frames = new ArrayList<>();
        int frameNumber = 1;
        StringBuilder stb = new StringBuilder();
        for (int i = 0; i < records.size(); i++) {
            String recordText = records.get(i).getTextRecord();
            stb.append(recordText);
        }
        String text = STX_STRING + frameNumber + stb.toString() + ETX_STRING;
        String chk = Frame.calculateChk(text.getBytes());
        text += chk + CR_STRING + LF_STRING;
        System.out.println("chk = " + chk);
        Frame frame = new FrameVivaE(text);
        frames.add(frame);
        return frames;
    }

}
