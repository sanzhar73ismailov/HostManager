package instrument.adviaCentaurCP;

import static instrument.ASCII.*;

public class FrameAdviaCentaurCp extends instrument.astm.Frame {

    

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
