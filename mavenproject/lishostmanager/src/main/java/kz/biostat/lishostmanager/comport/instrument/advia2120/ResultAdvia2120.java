package kz.biostat.lishostmanager.comport.instrument.advia2120;

public class ResultAdvia2120 {

    private String test;
    private String value;

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "ResultAdvia2120{" + "test=" + test + ", value=" + value + '}';
    }
    
    
    
}
