package entity;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;
import java.util.Properties;
import java.util.Set;

public class WorkOrder  implements HostDictionary {

    private int id;
    private Instrument instrument;
    private int mid;
    private String sid;
    private String rack;
    private int position;
    private String sampleType;
    private String patientName;
    private String patientNumber;
    private Date dateBirth;
    private int sex = 3; //1-m 2-f 3-unknown
    private Date dateCollection;
    private String laborantName;
    private int status;
    private String tests = "";
    private Properties addParams = new Properties();
    private Date insertDatetime;

    public WorkOrder() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public void setInstrument(Instrument instrument) {
        this.instrument = instrument;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientNumber() {
        return patientNumber;
    }

    public void setPatientNumber(String patientNumber) {
        this.patientNumber = patientNumber;
    }

    public Date getDateBirth() {
        return dateBirth;
    }

    public void setDateBirth(Date dateBirth) {
        this.dateBirth = dateBirth;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public Date getDateCollection() {
        return dateCollection;
    }

    public void setDateCollection(Date dateCollection) {
        this.dateCollection = dateCollection;
    }

    public String getLaborantName() {
        return laborantName;
    }

    public void setLaborantName(String laborantName) {
        this.laborantName = laborantName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getInsertDatetime() {
        return insertDatetime;
    }

    public void setInsertDatetime(Date insertDatetime) {
        this.insertDatetime = insertDatetime;
    }

    @Override
    public String toString() {
        return "WorkOrder{" + "id=" + id + ", instrument=" + instrument + ", mid=" + mid + ", sid=" + sid + ", rack=" + rack + ", position=" + position + ", sampleType=" + sampleType + ", patientName=" + patientName + ", patientNumber=" + patientNumber + ", dateBirth=" + dateBirth + ", sex=" + sex + ", dateCollection=" + dateCollection + ", laborantName=" + laborantName + ", status=" + status + ", tests=" + tests + ", addParams=" + addParams + ", insertDatetime=" + insertDatetime + '}';
    }

 
    public String getTests() {
        return tests;
    }

    public void setTests(String tests) {
        this.tests = tests;
    }

    public Properties getAddParams() {
        return addParams;
    }

    public void setAddParams(Properties addParams) {
        this.addParams = addParams;
    }

    public void setAddParamsFromString(String arg) {
        if (arg != null && !arg.isEmpty()) {
            String[] tokens = arg.split(",");
            this.addParams.clear();
            for (String keyVal : tokens) {
                String[] keyValAsArray = keyVal.split("=");
                this.addParams.setProperty(keyValAsArray[0], keyValAsArray[1]);
            }
        }
    }
    //public static  Properties get

    public String getParamValue(String key) {
        return this.addParams.getProperty(key);
    }

    public String getRack() {
        return rack;
    }

    public void setRack(String rack) {
        this.rack = rack;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getSampleType() {
        return sampleType;
    }

    public void setSampleType(String sampleType) {
        this.sampleType = sampleType;
    }

    public String getAddParamsAsString() {
        StringBuilder sb = new StringBuilder();
        if(addParams == null){
            return "";
        }
        if (this.addParams.size() > 0) {
            Set<Object> keys = addParams.keySet();
            for (Object key : keys) {
                String keyAsStr = (String) key;
                sb.append(",").append(keyAsStr).append("=").append(addParams.getProperty(keyAsStr));
            }
            return sb.substring(1).toString();
        }
        return "";
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    @Override
    public String getName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
