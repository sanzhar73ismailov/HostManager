package kz.biostat.lishostmanager.comport.instrument.adviaCentaurCP;

import kz.biostat.lishostmanager.comport.instrument.astm.Frame;

import static kz.biostat.lishostmanager.comport.instrument.ASCII.*;

public class FrameAdviaCentaurCp extends Frame {

    

    public FrameAdviaCentaurCp(String text) {
        super(text);
        if (text.getBytes()[text.getBytes().length - 5] == ETX) {
            etxFrame = true;
        }
    }

    @Override
    public boolean validateCheckSum() {
        String checkSumFromText = getCheckSum();
        String fromStxToETX = text.substring(0, text.length() - 4);
        String checkSumCalculated = calculateChk(fromStxToETX.getBytes());
        return checkSumFromText.equals(checkSumCalculated);
    }

}
