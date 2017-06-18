package instrument.astm;

import instrument.InstrumentException;

public abstract class RecordResult extends Record {

    public RecordResult(String textRecord) throws InstrumentException {
        super(textRecord);
    }
    
    public abstract void parseResultRecord();
    
    public abstract String getTestCode();
    public abstract String getValue();
    public abstract String getUnits();
    public abstract String getRefRanges();
    public abstract String getAbnormalFlags();
    public abstract String getResultStatus();
    public abstract String getDateTestCompleted();
    /*
     result.setValue(recordResult.getValue());
                    result.setUnits(recordResult.getUnits());
                    result.setReferenseRanges(recordResult.getRefRanges());
                    result.setAbnormalFlags(recordResult.getAbnormalFlags());
                    addParams = "natureAbnormalityTesting" + "=" + recordResult.getNatureAbnormalityTesting();
                    addParams += ",resultStatus" + "=" + recordResult.getResultStatus();
                    addParams += ",dilutionProtocol" + "=" + recordResult.getDilutionProtocol();
                    addParams += ",dilutionRatio" + "=" + recordResult.getDilutionRatio();
                    addParams += ",replicateNumber" + "=" + recordResult.getReplicateNumber();
                    addParams += ",resultAspects" + "=" + recordResult.getResultAspects();
                    addParams += ",dateTestCompleted" + "=" + recordResult.getDateTestCompleted();
    */
}
