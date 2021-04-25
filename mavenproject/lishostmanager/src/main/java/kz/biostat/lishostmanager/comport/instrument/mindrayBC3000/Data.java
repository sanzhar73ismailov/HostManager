package kz.biostat.lishostmanager.comport.instrument.mindrayBC3000;

import java.util.ArrayList;
import java.util.List;

public class Data {

    private String testCode;
    int lengthDataInMessage;
    private String valueBeforeDevision;
    private String value;
    private String unit;
    private boolean reserved; // если это не параметр, а пустое место
    private int devideTo;
    private boolean floatNumber = true; // если дробное то true (ставим в конце ноль), если не дробное то в конце 0 не ставим

    public Data(String testCode, int lengthDataInMessage, String unit, String value, int devideTo) {
        this.testCode = testCode;
        this.lengthDataInMessage = lengthDataInMessage;
        this.unit = unit;
        this.valueBeforeDevision = value;
        this.value = value;
        this.devideTo = devideTo;
    }

    public Data(String testCode, int lengthDataInMessage, String unit, String value, boolean emptySpace) {
        this(testCode, lengthDataInMessage, unit, value, 0);
        this.reserved = emptySpace;
    }

    public boolean isReserved() {
        return reserved;
    }

    public void setReserved(boolean reserved) {
        this.reserved = reserved;
    }

    @Override
    public String toString() {
        return "Data{" + "testCode=" + testCode + ", lengthDataInMessage=" + lengthDataInMessage + ", valueBeforeDevision=" + valueBeforeDevision
                + ", value=" + value + ", unit=" + unit + '}';
        //return "Data{" + "testCode=" + testCode +  ", value=" + value + ", unit=" + unit + '}';
    }

    public static List<Data> getListOfData() {
        List<Data> listData = new ArrayList<>();
        listData.add(new Data("wbc", 5, "10^9/L", "", 1000));
        listData.add(new Data("lymph_abs", 4, "10^9/L", "", 1000));
        listData.add(new Data("mid_abs", 4, "10^9/L", "", 1000));
        listData.add(new Data("gran_abs", 2, "10^9/L", "", 10));
        listData.add(new Data("lymph_proc", 3, "%", "", 10));
        listData.add(new Data("mid_proc", 3, "%", "", 10));
        listData.add(new Data("gran_proc", 3, "%", "", 10));
        listData.add(new Data("rbc", 3, "10^12/L", "", 100));

        Data hgb = new Data("hgb", 4, "g/L", "", 10);
        hgb.floatNumber = false;
        listData.add(hgb);

        Data mchc = new Data("mchc", 4, "g/L", "", 10);
        mchc.floatNumber = false;
        listData.add(mchc);

        listData.add(new Data("mcv", 4, "fL", "", 100));
        listData.add(new Data("mch", 3, "pg", "", 10));
        listData.add(new Data("rdwcv_proc", 3, "%", "", 10));
        listData.add(new Data("hct_proc", 4, "%", "", 100));

        Data plt = new Data("plt", 4, "10^9/L", "", 10);
        plt.floatNumber = false;
        listData.add(plt);

        listData.add(new Data("mpv", 2, "fL", "", 10));
        listData.add(new Data("pdw", 3, "", "", 10));
        listData.add(new Data("pct", 4, "%", "", 10000));
        listData.add(new Data("rdwsd", 5, "fL", "", 1000));
        // listData.add(new Data("rdwsd", 5, "fL", ""));
        listData.add(new Data("Reserved", 12, "", "", true));
        listData.add(new Data("rm", 1, "", "", 0));
        listData.add(new Data("r1", 1, "", "", 0));
        listData.add(new Data("r2", 1, "", "", 0));
        listData.add(new Data("r3", 1, "", "", 0));
        listData.add(new Data("r4", 1, "", "", 0));
        listData.add(new Data("pm", 1, "", "", 0));
        listData.add(new Data("ps", 1, "", "", 0));
        listData.add(new Data("pi", 1, "", "", 0));
        listData.add(new Data("L1Region", 3, "", "", 0));
        listData.add(new Data("L2Region", 3, "", "", 0));
        listData.add(new Data("L3Region", 3, "", "", 0));
        listData.add(new Data("L4Region", 3, "", "", 0));
        listData.add(new Data("L5Region", 3, "", "", 0));
        listData.add(new Data("L6Region", 3, "", "", 0));
        listData.add(new Data("L7Region", 3, "", "", 0));
        listData.add(new Data("L8Region", 3, "", "", 0));
        listData.add(new Data("Reserved", 16, "", "", true));
        listData.add(new Data("wbcHistio", 3, "", "", true));
        listData.add(new Data("rbcHistio", 3, "", "", true));
        listData.add(new Data("pltHistio", 3, "", "", true));
        return listData;
    }

    public static void main(String[] args) {
        List<Data> listData = getListOfData();
        //for (int i = 0; i < listData.size(); i++) {
        //Data element = listData.get(i);
        //System.out.println("get = " + get);
        //System.out.println(element.getTestCode() + "\t" + element.unit);
        //}

        float f = Float.valueOf("1a23");
        System.out.println("f = " + f);

    }

    public String getTestCode() {
        return testCode;
    }

    public void setTestCode(String testCode) {
        this.testCode = testCode;
    }

    public int getLengthDataInMessage() {
        return lengthDataInMessage;
    }

    public void setLengthDataInMessage(int lengthDataInMessage) {
        this.lengthDataInMessage = lengthDataInMessage;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value, boolean isFloat) {
        this.valueBeforeDevision = value;
        if (devideTo != 0 && !reserved) {
            try {
                float tempVal = Float.valueOf(value);
                tempVal /= devideTo;
                if ((tempVal == (int) tempVal) && (!isFloat)) { //если после точки 0, то его убираем
                    value = ((int) tempVal) + "";
                } else {
                    value = tempVal + "";
                }
            } catch (NumberFormatException e) {
                //System.out.println("e = " + e.getMessage());
            }
        }
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public boolean isFloatNumber() {
        return floatNumber;
    }

}
