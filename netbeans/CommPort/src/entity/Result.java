package entity;

import java.util.Date;

public class Result  implements HostDictionary {

    private int id;
    private int workOrderId;
    private String testCode;
    private int parameterId;
    private String value;
    private String units;
    private String referenseRanges;
    private String abnormalFlags;
    private String initialRerun;
    private String comment;
    private String status;
    private String rawText;
    private String addParams;
    private String instrument;
    private String sid;
    private int version;
    private Date insertDatetime;
    private String sidVersion; // сочетание баркода и версии - для отчетности

    /*
     `units` VARCHAR(20) COLLATE utf8_general_ci DEFAULT NULL,
     `referense_ranges` VARCHAR(30) COLLATE utf8_general_ci DEFAULT NULL,
     `abnormal_flags` VARCHAR(20) COLLATE utf8_general_ci DEFAULT NULL,
     `initial_rerun` VARCHAR(20) COLLATE utf8_general_ci DEFAULT NULL,
     `comment` VARCHAR(100) COLLATE utf8_general_ci DEFAULT NULL,
     `status` VARCHAR(20) COLLATE utf8_general_ci DEFAULT NULL,
     `raw_text` INTEGER(250) DEFAULT NULL,
     */
    public Result() {
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTestCode() {
        return testCode;
    }

    public void setTestCode(String testCode) {
        this.testCode = testCode;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(int workOrderId) {
        this.workOrderId = workOrderId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getReferenseRanges() {
        return referenseRanges;
    }

    public void setReferenseRanges(String referenseRanges) {
        this.referenseRanges = referenseRanges;
    }

    public String getAbnormalFlags() {
        return abnormalFlags;
    }

    public void setAbnormalFlags(String abnormalFlags) {
        this.abnormalFlags = abnormalFlags;
    }

    public String getInitialRerun() {
        return initialRerun;
    }

    public void setInitialRerun(String initialRerun) {
        this.initialRerun = initialRerun;
    }

    public String getRawText() {
        return rawText;
    }

    public void setRawText(String rawText) {
        this.rawText = rawText;
    }

    public String getAddParams() {
        return addParams;
    }

    public void setAddParams(String addParams) {
        this.addParams = addParams;
    }

    public int getParameterId() {
        return parameterId;
    }

    public void setParameterId(int parameterId) {
        this.parameterId = parameterId;
    }

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }
    
    @Override
    public String toString() {
        return "Result{" + "id=" + id + ", workOrderId=" + workOrderId + ", testCode=" + testCode + ", parameterId=" + parameterId + ", value=" + value + ", units=" + units + ", referenseRanges=" + referenseRanges + ", abnormalFlags=" + abnormalFlags + ", initialRerun=" + initialRerun + ", rawText=" + rawText + ", comment=" + comment + ", status=" + status + ", addParams=" + addParams + '}';
    }

    @Override
    public String getName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Date getInsertDatetime() {
        return insertDatetime;
    }

    public void setInsertDatetime(Date insertDatetime) {
        this.insertDatetime = insertDatetime;
    }

    public String getSidVersion() {
        return sidVersion;
    }

    public void setSidVersion(String sidVersion) {
        this.sidVersion = sidVersion;
    }

}
