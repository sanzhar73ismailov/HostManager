package kz.biostat.lishostmanager.comport.instrument.sysmexXS500i;

import kz.biostat.lishostmanager.comport.instrument.astm.Frame;
import kz.biostat.lishostmanager.comport.instrument.astm.Record;
import kz.biostat.lishostmanager.comport.instrument.astm.MessageAstm;
import kz.biostat.lishostmanager.comport.instrument.immulite2000.FrameImmulite2000;

import java.util.ArrayList;
import java.util.List;

import static kz.biostat.lishostmanager.comport.instrument.ASCII.*;
import static kz.biostat.lishostmanager.comport.instrument.ASCII.LF_STRING;

public class MessageSysmexXS500i extends MessageAstm {

    public MessageSysmexXS500i(List<Record> records) {
        super(records);
    }

    @Override
    public String getSidFromQueryRecord() {
        return null;
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
}
