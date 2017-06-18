package instrument.vivaE;

import instrument.InstrumentException;

public class RecordResultVivaE extends instrument.astm.RecordResult {

    private String testCode;
    private String testFullName;
    
    //String dilutionProtocol;
    //String dilutionRatio;
    //String replicateNumber;
    //String resultAspects;
    private String value;
    private String units;
    private String refRanges;
    private String abnormalFlags;
    private String natureAbnormalityTesting;
    private String resultStatus;
    private String dateTestCompleted;
    private String instrumentIdentification;

    public RecordResultVivaE(String textRecord) throws InstrumentException {
        super(textRecord);
        parseResultRecord();
    }

    @Override
    public void parseResultRecord() {
        String[] fields = super.getTextRecord().split("\\|");
        String[] testIdFields = fields[2].split("\\^");
        this.testCode = getFromArraIndexIfExist(testIdFields, 3);
        this.testFullName = getFromArraIndexIfExist(testIdFields, 4);
        //this.dilutionRatio = getFromArraIndexIfExist(testIdFields, 5);
        //this.replicateNumber = getFromArraIndexIfExist(testIdFields, 6);
        //this.resultAspects = getFromArraIndexIfExist(testIdFields, 7);
        this.value = getFromArraIndexIfExist(fields, 3);
        this.units = getFromArraIndexIfExist(fields, 4);
        this.refRanges = getFromArraIndexIfExist(fields, 5);
        this.abnormalFlags = getFromArraIndexIfExist(fields, 6);
        //this.natureAbnormalityTesting = fields, 7];
        this.resultStatus = getFromArraIndexIfExist(fields, 8);
        this.dateTestCompleted = getFromArraIndexIfExist(fields, 12);
        this.instrumentIdentification = getFromArraIndexIfExist(fields, 13);
    }

    public String getTestCode() {
        return testCode;
    }

    public String getTestFullName() {
        return testFullName;
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

    public String getDateTestCompleted() {
        return dateTestCompleted;
    }

    public String getInstrumentIdentification() {
        return instrumentIdentification;
    }

    
}
