package instrument.immulite2000;

import instrument.InstrumentException;

public class RecordResultImmulite2000 extends instrument.astm.RecordResult {

    String testCode;
    String value;
    String units;
    String refRanges;
    String abnormalFlags;
    String natureAbnormalityTesting;
    String resultStatus;

    public RecordResultImmulite2000(String textRecord) throws InstrumentException {
        super(textRecord);
        parseResultRecord();
    }

    public void parseResultRecord() {
        String [] fields = super.getTextRecord().split("\\|");
        this.testCode = fields[2].replaceAll("^", "");
        this.value = fields[3];
        this.units = fields[4];
        this.refRanges = fields[5];
        this.abnormalFlags = fields[6];
        this.natureAbnormalityTesting = fields[7];
        this.resultStatus = fields[8];
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

    @Override
    public String getDateTestCompleted() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
