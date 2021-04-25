package kz.biostat.lishostmanager.comport.instrument.adviaCentaurCP;

import kz.biostat.lishostmanager.comport.instrument.InstrumentException;
import kz.biostat.lishostmanager.comport.instrument.astm.RecordResult;

public class RecordResultAdviaCentaurCP extends RecordResult {

    String testCode;
    String dilutionProtocol;
    String dilutionRatio;
    String replicateNumber;
    String resultAspects;
    String value;
    String units;
    String refRanges;
    String abnormalFlags;
    String natureAbnormalityTesting;
    String resultStatus;
    String dateTestCompleted;

    public RecordResultAdviaCentaurCP(String textRecord) throws InstrumentException {
        super(textRecord);
        parseResultRecord();
    }

    public void parseResultRecord() {
        String[] fields = super.getTextRecord().split("\\|");
        String[] testIdFields = fields[2].split("\\^");
        this.testCode = getFromArraIndexIfExist(testIdFields, 3);
        this.dilutionProtocol = getFromArraIndexIfExist(testIdFields, 4);
        this.dilutionRatio = getFromArraIndexIfExist(testIdFields, 5);
        this.replicateNumber = getFromArraIndexIfExist(testIdFields, 6);
        this.resultAspects = getFromArraIndexIfExist(testIdFields, 7);

        this.value = getFromArraIndexIfExist(fields, 3);
        this.units = getFromArraIndexIfExist(fields, 4);
        this.refRanges = getFromArraIndexIfExist(fields, 5);
        this.abnormalFlags = getFromArraIndexIfExist(fields, 6);
        //this.natureAbnormalityTesting = fields, 7];
        this.resultStatus = getFromArraIndexIfExist(fields, 8);
        this.dateTestCompleted = getFromArraIndexIfExist(fields, 12);
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

    public String getDilutionProtocol() {
        return dilutionProtocol;
    }

    public String getDilutionRatio() {
        return dilutionRatio;
    }

    public String getReplicateNumber() {
        return replicateNumber;
    }

    public String getResultAspects() {
        return resultAspects;
    }

    public String getDateTestCompleted() {
        return dateTestCompleted;
    }
    
    

    public boolean isDose() {
        return this.resultAspects.equalsIgnoreCase("DOSE");
    }
    
    public boolean isIndex() {
        return this.resultAspects.equalsIgnoreCase("INDX");
    }
}
