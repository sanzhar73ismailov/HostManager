package kz.biostat.lishostmanager.comport.instrument.mindrayBC3000;

import java.util.List;

public class Parameter {

    public String textIdentifier;
    public String version;
    public String idLength;
    public String numberOfParameters;
    public String numberOfParametersWithDescriptions;
    public String id;
    public String sampleMode;
    public String month;
    public String day;
    public String year;
    public String hour;
    public String minutes;
    public String seconds;
    public List<Data> dataList;
    private String text;

    private int positioRead = 0;

    public Parameter(String text) {
        this.text = text;
        textIdentifier = getPartString(1);
        version = getPartString(2);
        idLength = getPartString(3);
        numberOfParameters = getPartString(3);
        numberOfParametersWithDescriptions = getPartString(2);
        id = getPartString(10);
        sampleMode = getPartString(1);
        month = getPartString(2);
        day = getPartString(2);
        year = getPartString(4);
        hour = getPartString(2);
        minutes = getPartString(2);
        seconds = getPartString(1);
        seconds="00";
        dataList = Data.getListOfData();
        for (int i = 0; i < dataList.size(); i++) {
            Data data = dataList.get(i);
            data.setValue(getPartString(data.getLengthDataInMessage()), data.isFloatNumber());
            //data.setValueAfterDevision();
        }
    }

    private String getPartString(int number) {
        int startRead = positioRead;
        int beforeRead = positioRead + number;
        positioRead += number;
        return this.text.substring(startRead, beforeRead);
    }

    @Override
    public String toString() {
        String str = "Parameter{" + "textIdentifier=" + textIdentifier + ", version=" + version + ", idLength=" + idLength + ", numberOfParameters=" + numberOfParameters + ", numberOfParametersWithDescriptions=" + numberOfParametersWithDescriptions + ", id=" + id + ", sampleMode=" + sampleMode + ", month=" + month + ", day=" + day + ", year=" + year + ", hour=" + hour + ", minutes=" + minutes + ", seconds=" + seconds + ", positioRead=" + positioRead + '}';
        for (int i = 0; i < dataList.size(); i++) {
            str += "\n" + dataList.get(i).toString();
        }
        return str;
    }

}
