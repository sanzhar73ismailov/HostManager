/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package instrument.sysmexCA1500;

import java.util.Arrays;

/**
 *
 * @author sanzhar.ismailov
 */
public class Parameters {

    public String textDistinctionCode1;
    public String textDistinctionCode2;
    public String textDistinctionCode3;
    public String blockNumber;
    public String totalNumberBlocks;
    public String sampleDistinctionCode;
    public String date;
    public String time;
    public String rackNumber;
    public String tubePositionNumber;
    public String sampleIdNumber;
    public String idInformation;
    public String patientName;
    public String[] data;
    private String text;

    private int positioRead = 0;

    public Parameters() {
    }

    public Parameters(String str) {
        this.text = str.substring(1,str.length()-1);

        textDistinctionCode1 = getPartString(1);
        textDistinctionCode2 = getPartString(1);
        textDistinctionCode3 = getPartString(2);
        blockNumber = getPartString(2);
        totalNumberBlocks = getPartString(2);
        sampleDistinctionCode = getPartString(1);
        date = getPartString(6);
        time = getPartString(4);
        rackNumber = getPartString(4);
        tubePositionNumber = getPartString(2);
        sampleIdNumber = getPartString(13);
        idInformation = getPartString(1);
        patientName = getPartString(11);
        data = getDataString();
    }

    private String getPartString(int number) {
        int startRead = positioRead;
        int beforeRead = positioRead + number;
        positioRead += number;
        return this.text.substring(startRead, beforeRead);
    }

    private String[] getDataString() {
        int startRead = positioRead;
        int beforeRead = text.length();
        String str = this.text.substring(startRead, beforeRead);
        String[] array = str.split("(?<=\\G.{9})");
        return array;
    }

    @Override
    public String toString() {
        return "Parameters{" + "textDistinctionCode1=" + textDistinctionCode1
                + ", \ntextDistinctionCode2=" + textDistinctionCode2
                + ", \ntextDistinctionCode3=" + textDistinctionCode3
                + ", \nblockNumber=" + blockNumber + ", \ntotalNumberBlocks="
                + totalNumberBlocks + ", \nsampleDistinctionCode=" + sampleDistinctionCode
                + ", \ndate=" + date + ", \ntime=" + time + ", \nrackNumber=" + rackNumber
                + ", \ntubePositionNumber=" + tubePositionNumber + ", \nsampleIdNumber="
                + sampleIdNumber + ", \nidInformation=" + idInformation
                + ", \npatientName=" + patientName + ", \ndata=" + Arrays.toString(data) + '}';
    }

}
