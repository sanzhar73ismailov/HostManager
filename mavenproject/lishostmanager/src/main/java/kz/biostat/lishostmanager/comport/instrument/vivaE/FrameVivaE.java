package kz.biostat.lishostmanager.comport.instrument.vivaE;

import kz.biostat.lishostmanager.comport.instrument.astm.Frame;

import static kz.biostat.lishostmanager.comport.instrument.ASCII.ETX;

public class FrameVivaE extends Frame {

    public static final int FRAME_SIZE_VIVAE = 64000;

    public FrameVivaE(String text) {
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
    
    public String getCalculatedCheckSum(){
      //  String checkSumFromText = getCheckSum();
        String fromStxToETX = text.substring(0, text.length() - 4);
        String checkSumCalculated = calculateChk(fromStxToETX.getBytes());
        return checkSumCalculated;
    }

}
