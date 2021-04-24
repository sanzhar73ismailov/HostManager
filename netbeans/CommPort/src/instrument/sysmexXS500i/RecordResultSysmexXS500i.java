package instrument.sysmexXS500i;

import instrument.InstrumentException;

public class RecordResultSysmexXS500i extends instrument.astm.RecordResult {

    String testCode;
    String value;
    String units;
    String refRanges;
    String abnormalFlags;
    String natureAbnormalityTesting;
    String resultStatus;
    String dateTimeAsStr;

    public RecordResultSysmexXS500i(String textRecord) throws InstrumentException {
        super(textRecord);
        parseResultRecord();
    }

    public void parseResultRecord() {
        //<STX>1R|35|^^^^Fragments?|0|||||||||20210415133249<CR><ETX>1E<CR><LF>
        //<STX>7R|1|^^^^WBC^1|6.21|10*3/uL||N||||||20210415133249<CR><ETX>05<CR><LF>

        //R|1|^^^^WBC^1|6.21|10*3/uL||N||||||20210415133249
        /*
        0) R
        1) 1
        2) ^^^^WBC^1
        3) 6.21
        4) 10*3/uL
        5)
        6) N
        7)
        8)
        9)
        10)
        11)
        12) 20210415133249
*/

        String[] fields = super.getTextRecord().split("\\|");
        //examples of testCode values:
        // "^^^^MCV^1" or
        // "^^^^Blasts?"
        this.testCode = fields[2].replace("^^^^", "");
        this.testCode = testCode.replace("^1", "");
        this.testCode = testCode.replace("?", "");
        this.value = fields[3];
        this.units = fields[4];
        this.refRanges = fields[5];
        this.abnormalFlags = fields[6];
        this.natureAbnormalityTesting = fields[7];
        this.resultStatus = fields[8];
        this.dateTimeAsStr = fields[12];
    }

    public String getTestCode() {
        return testCode;
    }

    public String getValue() {
        return value;
    }

    public String getUnits() {
        return units;
    }

    public String getRefRanges() {
        return refRanges;
    }

    public String getAbnormalFlags() {
        return abnormalFlags;
    }

    public String getNatureAbnormalityTesting() {
        return natureAbnormalityTesting;
    }

    public String getResultStatus() {
        return resultStatus;
    }

    public String getDateTimeAsStr() {
        return dateTimeAsStr;
    }

    public void setDateTimeAsStr(String dateTimeAsStr) {
        this.dateTimeAsStr = dateTimeAsStr;
    }

    @Override
    public String getDateTestCompleted() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
