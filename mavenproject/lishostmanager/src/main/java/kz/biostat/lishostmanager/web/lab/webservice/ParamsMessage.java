package kz.biostat.lishostmanager.web.lab.webservice;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ParamsMessage {

    String id;
    String sender;
    String parameters;
    String sid;
    String rack;
    String position;
    String sampleType;
    String patientName;
    String patientNumber;
    String dateBirth;
    String sex;
    String dateCollection;
    String laborantName;
    Properties addParams = new Properties();

    @Override
    public String toString() {
        return "ParamsMessage{" + "id=" + id + ", sender=" + sender + ", parameters=" + parameters + ", sid=" + sid + ", rack=" + rack + ", position=" + position + ", sampleType=" + sampleType + ", patientName=" + patientName + ", patientNumber=" + patientNumber + ", dateBirth=" + dateBirth + ", sex=" + sex + ", dateCollection=" + dateCollection + ", laborantName=" + laborantName + ", addParams=" + addParams + '}';
    }
    
}
