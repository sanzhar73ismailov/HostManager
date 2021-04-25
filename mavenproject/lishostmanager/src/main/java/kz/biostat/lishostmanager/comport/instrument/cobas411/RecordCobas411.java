package kz.biostat.lishostmanager.comport.instrument.cobas411;

import kz.biostat.lishostmanager.comport.instrument.InstrumentException;
import kz.biostat.lishostmanager.comport.instrument.astm.Record;

public class RecordCobas411 extends Record {

    private static final String BATCH = "BATCH";
    private static final String REPLY = "REPLY";

    public RecordCobas411() {
    }

    public RecordCobas411(String textRecord) throws InstrumentException {
        super(textRecord);
    }

    @Override
    public String toString() {
        return "Record{" + "textRecord=" + textRecord + '}';
    }

    public static RecordCobas411 createHeaderRecordFromHostOrderBatch() throws Cobas411Exception {
        return getHeaderRecordFromHostForOrder(BATCH);
    }

    public static RecordCobas411 createHeaderRecordFromHostOrderReply() throws Cobas411Exception {
        return getHeaderRecordFromHostForOrder(REPLY);
    }

    private static RecordCobas411 getHeaderRecordFromHostForOrder(String replyOrBatch) throws Cobas411Exception {
        try {
            String textForRecord = "H|\\^&|||elsi-host^1|||||cobas-e411|TSDWN^" + replyOrBatch + "|P|1\r";
            RecordCobas411 record = new RecordCobas411(textForRecord);
            return record;
        } catch (InstrumentException ex) {
            throw new Cobas411Exception(ex);
        }
    }

    public static RecordCobas411 getHeaderRecordFromInstrumentForQuery() throws Cobas411Exception {
        try {
            String textForRecord = "H|\\^&|||cobas-e411^1|||||host|TSREQ^REAL|P|1\r";
            RecordCobas411 record = new RecordCobas411(textForRecord);
            return record;
        } catch (InstrumentException ex) {
            throw new Cobas411Exception(ex);
        }
    }

    public static RecordCobas411 getHeaderRecordFromInstrumentWithResults() throws Cobas411Exception {
        try {
            String textForRecord = "H|\\^&|||cobas-e411^1|||||host|RSUPL^REAL|P|1\r";
            RecordCobas411 record = new RecordCobas411(textForRecord);
            return record;
        } catch (InstrumentException ex) {
            throw new Cobas411Exception(ex);
        }
    }

    public static RecordCobas411 createTerminalRecordFromHost() throws Cobas411Exception {
        try {
            return new RecordCobas411("L|1|N\r");
        } catch (InstrumentException ex) {
            throw new Cobas411Exception(ex);
        }
    }

    public static RecordCobas411 createPatientRecordFromHost() throws Cobas411Exception {
        try {
            return new RecordCobas411("P|1\r");
        } catch (InstrumentException ex) {
            throw new Cobas411Exception(ex);
        }
    }

    public static RecordCobas411 createOrderRecordFromHost(ParamsCobas411OrderRecord params) throws Cobas411Exception {
        StringBuilder stringBuilderForOrder = new StringBuilder();
        String sid = params.getSid();
        String sequenceNumber = params.getSequenceNumber();
        String diskNumber = params.getRack();
        String position = params.getPosition();
        String sampleType = params.getSampleType();
        String containerType = params.getContainerType();
        String routineOrStatSample = params.getRoutineOrStatSample();
        String[] tests = params.getTests();
        if (tests == null) {
            tests = new String[0];
        }
        //sampleType could be "S1" - blood serum, S2 - urine, S5 - others, QC - control sample. Usually S1
        if (sampleType == null) {
            sampleType = "S1";
        } else {
            if (!sampleType.equals("S1") && !sampleType.equals("S2") && !sampleType.equals("S5") && !sampleType.equals("QC")) {
                throw new Cobas411Exception("wrong sample type: " + sampleType);
            }
        }
        //containerType coud be SC - test tube or sample cup, MC - reduced sample volume. Usually SC
        if (containerType == null) {
            containerType = "SC";
        } else {
            if (!containerType.equals("SC") && !containerType.equals("MC")) {
                throw new Cobas411Exception("wrong containerType: " + containerType);
            }
        }
        stringBuilderForOrder.append(String.format("O|1|%s|%s^%s^%s^^%s^%s|", sid, sequenceNumber, diskNumber, position, sampleType, containerType));

        for (int i = 0; i < tests.length; i++) {
            String test = tests[i];
            if (i > 0) {
                stringBuilderForOrder.append("\\");
            }
            stringBuilderForOrder.append("^^^" + test + "^" + params.getDilution());
        }
        // routineSampleOrStatSample "R" (routine - рутинный) or "S" (stat - срочный). Usually R
        if (routineOrStatSample == null) {
            routineOrStatSample = "R";
        } else {
            if (!routineOrStatSample.equals("R") && !routineOrStatSample.equals("S")) {
                routineOrStatSample = "R";
            }
        }
        String specimenDescriptor = "";
        if (sampleType.equals("S1")) {
            specimenDescriptor = "1";
        } else if (sampleType.equals("S2")) {
            specimenDescriptor = "2";
        } else {
            specimenDescriptor = "5";
        }

        stringBuilderForOrder.append(String.format("|%s||||||A||||%s||||||||||O\r", routineOrStatSample, specimenDescriptor));
        RecordCobas411 orderRecord;
        try {
            orderRecord = new RecordCobas411(stringBuilderForOrder.toString());
        } catch (InstrumentException ex) {
            throw new Cobas411Exception(ex);
        }
        return orderRecord;
    }

    public static RecordCobas411 createQueryRecordFromInstrument(ParamsCobas411OrderRecord params) throws Cobas411Exception {

        try {
            String str = String.format("Q|1|^^%s^%s^%s^%s^^S1^SC||ALL||||||||O\r",
                    params.getSid(), params.getSequenceNumber(), params.getRack(), params.getPosition());
            return new RecordCobas411(str);
        } catch (InstrumentException ex) {
            throw new Cobas411Exception(ex);
        }
    }

    public ParamsCobas411OrderRecord getParamsFromQueryRecord() throws Cobas411Exception {
        ParamsCobas411OrderRecord params = null;
        if (this.typeRecord != typeRecord.QUERY) {
            throw new Cobas411Exception("attempt get SID from not Query record");
        }
        String[] fields = this.textRecord.split("\\|");
        String[] sidAndNumbers = fields[2].split("\\^");
        String sid = sidAndNumbers[2];
        String seqNumber = sidAndNumbers[3];
        String rack = sidAndNumbers[4];
        String position = sidAndNumbers[5];
        String sampleType = sidAndNumbers[7];
        String containerType = sidAndNumbers[8];
        params = new ParamsCobas411OrderRecord(sid, position, null);
        params.setSequenceNumber(seqNumber);
        params.setRack(rack);
        params.setSampleType(sampleType);
        params.setContainerType(containerType);
        return params;
    }

    public ParamsCobas411OrderRecord getParamsFromOrderResultRecord() throws Cobas411Exception {
        ParamsCobas411OrderRecord params = null;
        if (this.typeRecord != typeRecord.ORDER) {
            throw new Cobas411Exception("attempt get SID from not Query record");
        }
        //
// O|1|601|4^0^4^^S1^SC|^^^6^1^^^7^1^^^17^1|R||20150224101454||||N||||1|||||||20150224104158|||F<CR>
        String[] fields = this.textRecord.split("\\|");
        String sid = fields[2];
        String[] seqAndNumbers = fields[3].split("\\^");
        String seqNumber = seqAndNumbers[0];
        String rack = seqAndNumbers[1];
        String position = seqAndNumbers[2];
        String sampleType = seqAndNumbers[4];
        String containerType = seqAndNumbers[5];
        params = new ParamsCobas411OrderRecord(sid, position, null);
        params.setSequenceNumber(seqNumber);
        params.setRack(rack);
        params.setSampleType(sampleType);
        params.setContainerType(containerType);
        return params;
    }
}
