/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package instrument.dimensionXpand;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sanzhar.ismailov
 */
public class ResultMessageDimensionXpand extends MessageDimensionXpand{

    private String patientID;
    private String sid;
    private String sampleType;
    private String location;
    private String priority;
    private Date dateTime;
    private int dilution;
    private int numberOfTests;
    private List<ResultDimensionXpand> listResults = new ArrayList<>();
    
    
    public ResultMessageDimensionXpand(byte[] rawMessage) {
        super(rawMessage);
        this.patientID = fields[2];
        this.sid = fields[3];
        this.sampleType = fields[4];
        this.location = fields[5];
        this.priority = fields[6];
        SimpleDateFormat sdf = new SimpleDateFormat("ssmmHHddMMyy");
        try {
            this.dateTime = sdf.parse(fields[7]);
        } catch (ParseException ex) {
            Logger.getLogger(ResultMessageDimensionXpand.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.dilution = Integer.parseInt(fields[9]);
        this.numberOfTests = Integer.parseInt(fields[10]);
        int fieldNumber = 11;
        for (int i = 0; i < numberOfTests; i++) {
            ResultDimensionXpand resultDimensionXpand = new ResultDimensionXpand();
            resultDimensionXpand.setTestName(fields[fieldNumber++]);
            resultDimensionXpand.setValue(fields[fieldNumber++]);
            resultDimensionXpand.setUnits(fields[fieldNumber++]);
            resultDimensionXpand.setErrorCode(fields[fieldNumber++]);
            this.listResults.add(resultDimensionXpand);
        }
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getSampleType() {
        return sampleType;
    }

    public void setSampleType(String sampleType) {
        this.sampleType = sampleType;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public int getDilution() {
        return dilution;
    }

    public void setDilution(int dilution) {
        this.dilution = dilution;
    }

    public int getNumberOfTests() {
        return numberOfTests;
    }

    public void setNumberOfTests(int numberOfTests) {
        this.numberOfTests = numberOfTests;
    }

    public List<ResultDimensionXpand> getListResults() {
        return listResults;
    }

    public void setListResults(List<ResultDimensionXpand> listResults) {
        this.listResults = listResults;
    }

    @Override
    public String toString() {
        return "ResultMessageDimensionXpand{" + "patientID=" + patientID + ", sid=" + sid + ", sampleType=" + sampleType + ", location=" + location + ", priority=" + priority + ", dateTime=" + dateTime + ", dilution=" + dilution + ", numberOfTests=" + numberOfTests + ", listResults=" + listResults + '}';
    }
    
}
