package kz.biostat.lishostmanager.comport.instrument.sysmexCA660;

public class Data {

    public enum unitsEnum {

        Time, ActiviyPercentConcentration, Ratio, INR, dFbg
    }
    private String testCode;
    private String testCodeForResult;
    private unitsEnum units;
    private String value;
    private String flag;

    public String getTestCode() {
        return testCode;
    }

    public void setTestCode(String testCode) {
        this.testCode = testCode;
    }

    public void setData(String data) {
        String result = data;
        try {
            switch (units) {
                case Time:
                case ActiviyPercentConcentration:
                case dFbg:
                    result = Double.parseDouble(data) / 10 + "";
                    break;
                case Ratio:
                case INR:
                    result = Double.parseDouble(data) / 100 + "";
                    break;
            }
        } catch (Exception ex) {
            System.out.println("ex = " + ex.getMessage());
        }
        this.value = result;

    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getUnits() {
        return units.toString();
    }

    public void setUnitsByNumber(String number) {
        switch (number) {
            case "1":
                units = unitsEnum.Time;
                break;
            case "2":
                units = unitsEnum.ActiviyPercentConcentration;
                break;
            case "3":
                units = unitsEnum.Ratio;
                break;
            case "4":
                units = unitsEnum.INR;
                break;
            case "5":
                units = unitsEnum.dFbg;
                break;
            default:
                units = null;
        }

    }

    public String getTestCodeForResult() {
        return testCodeForResult;
    }

    public void setTestCodeForResult(String testCodeForResult) {
        this.testCodeForResult = testCodeForResult;
    }

    public String getValue() {
        return value;
    }
    

    @Override
    public String toString() {
        return "<br><b>Data</b>{" + "testCode=" + testCode + ", testCodeForResult=" + testCodeForResult + 
                ", units=" + units + ", value=" + value + ", flag=" + flag + '}';
    }

}
