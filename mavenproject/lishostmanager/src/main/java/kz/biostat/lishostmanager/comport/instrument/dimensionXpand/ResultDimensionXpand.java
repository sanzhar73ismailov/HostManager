/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.biostat.lishostmanager.comport.instrument.dimensionXpand;

/**
 *
 * @author sanzhar.ismailov
 */
public class ResultDimensionXpand {
    private String testName;
    private String value;
    private String units;
    private String errorCode;

    public ResultDimensionXpand() {
    }

    public ResultDimensionXpand(String testName, String value, String units, String errorCode) {
        this.testName = testName;
        this.value = value;
        this.units = units;
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    @Override
    public String toString() {
        return "ResultDimensionXpand{" + "testName=" + testName + ", value=" + value + ", units=" + units + ", errorCode=" + errorCode + '}';
    }
    
    
}
