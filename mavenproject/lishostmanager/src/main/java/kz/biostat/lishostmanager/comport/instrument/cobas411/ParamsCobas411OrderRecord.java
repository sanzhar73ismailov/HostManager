package kz.biostat.lishostmanager.comport.instrument.cobas411;

import kz.biostat.lishostmanager.comport.entity.WorkOrder;

public class ParamsCobas411OrderRecord {

    private String sid;
    private String sequenceNumber;
    private String rack;
    private String position;
    private String sampleType; 
    private String containerType; 
    private String routineOrStatSample;
    private int dilution;
    private String [] tests;
    

    public ParamsCobas411OrderRecord() {
    }
    public ParamsCobas411OrderRecord(WorkOrder workOrder){
        this(workOrder.getSid(), workOrder.getPosition() + "", workOrder.getTests().split(","));
    }

    public ParamsCobas411OrderRecord(String sid, String position, String [] tests) {
        this.sid = sid;
        this.sequenceNumber = "";
        this.rack = "0";
        this.position = position;
        this.sampleType = "S1"; //S1
        this.containerType = "SC"; //"SC"
        this.routineOrStatSample = "R";//
        this.dilution = 1;
        this.tests = tests;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getRack() {
        return rack;
    }

    public void setRack(String rack) {
        this.rack = rack;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getSampleType() {
        return sampleType;
    }

    public void setSampleType(String sampleType) {
        this.sampleType = sampleType;
    }

    public String getContainerType() {
        return containerType;
    }

    public void setContainerType(String containerType) {
        this.containerType = containerType;
    }

    public String getRoutineOrStatSample() {
        return routineOrStatSample;
    }

    public void setRoutineOrStatSample(String routineOrStatSample) {
        this.routineOrStatSample = routineOrStatSample;
    }

    public String getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(String sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public int getDilution() {
        return dilution;
    }

    public void setDilution(int dilution) {
        this.dilution = dilution;
    }

    public String[] getTests() {
        return tests;
    }

    public void setTests(String[] tests) {
        this.tests = tests;
    }
    
    
    
    
}
