package instrument.sysmexXS500i;

import instrument.InstrumentException;
import instrument.astm.Record;

public class FrameSysmexXS500i extends instrument.astm.Frame {
    public FrameSysmexXS500i(String text) {
        super(text);
    }

    @Override
    public boolean validateCheckSum() {
        String checkSumFromText = getCheckSum();
        String fromStxToETX = text.substring(0, text.length() - 4);
        String checkSumCalculated = calculateChk(fromStxToETX.getBytes());
        return checkSumFromText.equals(checkSumCalculated);
    }

    public Record getRecord() throws InstrumentException {
        //убираем спереди STX и номер фрейма. Сзади убираем 5 символов <ETX>CHK1CHK2<CR><LF> (<CR> перед ETX оставляем, должен входить в запись)
        String textForRecord = this.text.substring(2, this.text.length() - 5);
        if(textForRecord.startsWith("O|")) {
            textForRecord += "\r";
        }
        return new Record(textForRecord);
    }
}
