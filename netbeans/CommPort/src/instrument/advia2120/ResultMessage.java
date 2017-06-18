package instrument.advia2120;

import instrument.ASCII;
import java.util.ArrayList;
import java.util.List;

public class ResultMessage extends Advia2120Message {

    public ResultMessage(byte[] rawMessage) throws MtIsNotInDiapazonException {
        super(rawMessage);
        parsingResultMessage();

    }
    private String idCode;
    private String sid;
    private String rackPosition;
    private String probeClogIndicator;
    private String aspirationDate; // MM/DD/YY
    private String aspirationTime; // HH:MM:SS
    private List<ResultAdvia2120> results = new ArrayList<>();
    private String lrc;

    public void parsingResultMessage() {
        try {
            idCode = "" + (char) rawMessage[2];
            sid = new String(rawMessage, 4, Advia2120Message.SID_LENGTH);
            sid = util.Util.removeZeroesFromeHead(sid);
            rackPosition = new String(rawMessage, 19, 6);
            probeClogIndicator = new String(rawMessage, 26, 1);
            aspirationDate = new String(rawMessage, 36, 8);
            aspirationTime = new String(rawMessage, 45, 8);
            fillResults();
            this.lrc = ((char) rawMessage[rawMessage.length - 2]) + "";
        } catch (Exception e) {
            System.out.println("ResulMessage in catch = " + this.toString());
            System.out.println("e = " + e);
        }

    }

    private void fillResults() {
        String[] strArray = new String(rawMessage).split("\r\n");
        String strResults = strArray[1];
        String[] arr = util.Util.splitByLength(strResults, 9);
        for (String testValue : arr) {
            ResultAdvia2120 res = new ResultAdvia2120();
            res.setTest(testValue.substring(0, 3));
            res.setValue(testValue.substring(3, 9));
            this.results.add(res);
        }
    }

    @Override
    public String toString() {
        return "ResultMessage{" + "idCode=" + idCode + ", sid=" + sid + ", rackPosition=" + rackPosition + ", probeClogIndicator=" + probeClogIndicator + ", aspirationDate=" + aspirationDate + ", aspirationTime=" + aspirationTime + ", results=" + results + ", lrc=" + lrc + '}';
    }

    public String getIdCode() {
        return idCode;
    }

    public String getSid() {
        return sid;
    }

    public String getRackPosition() {
        return rackPosition;
    }

    public String getProbeClogIndicator() {
        return probeClogIndicator;
    }

    public String getAspirationDate() {
        return aspirationDate;
    }

    public String getAspirationTime() {
        return aspirationTime;
    }

    public List<ResultAdvia2120> getResults() {
        return results;
    }

    public String getLrc() {
        return lrc;
    }
}
