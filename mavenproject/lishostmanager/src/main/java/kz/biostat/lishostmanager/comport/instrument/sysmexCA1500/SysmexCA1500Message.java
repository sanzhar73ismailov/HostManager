package kz.biostat.lishostmanager.comport.instrument.sysmexCA1500;

import kz.biostat.lishostmanager.comport.entity.WorkOrder;
import static kz.biostat.lishostmanager.comport.instrument.ASCII.*;
import kz.biostat.lishostmanager.comport.instrument.InstrumentException;
import kz.biostat.lishostmanager.comport.instrument.Message;
import kz.biostat.lishostmanager.comport.util.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SysmexCA1500Message extends Message {

    private TypeMessageSysmexCa1500 typeOfMessage;
    private String textDistinctionCode;
    private String sampleDistinctCode;
    private Date date;
    private int rackNumber;
    private int positionTubeNumber;
    private String sid;
    private String idInfo;
    private String patientName;
    private List<Data> data = new ArrayList<>();
    private Parameters params;
private static final int SID_LENGTH = 13;
    private static SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyhhmm");

    public SysmexCA1500Message(byte[] rawMessage) {
        super(rawMessage);

    }

    public SysmexCA1500Message(List<BlockSysmexCA1500> listBlocks) throws InstrumentException {
        super(null);
        for (int i = 0; i < listBlocks.size(); i++) {
            BlockSysmexCA1500 block = listBlocks.get(i);
            this.params = block.getParameters();
            if (i == 0) {
                switch (params.textDistinctionCode1) {
                    case "D":
                        typeOfMessage = TypeMessageSysmexCa1500.RESULT;
                        break;
                    case "R":
                        typeOfMessage = TypeMessageSysmexCa1500.QUERY;
                        break;
                    case "S":
                        typeOfMessage = TypeMessageSysmexCa1500.ORDER;
                        break;
                    default:
                        throw new InstrumentException("Unknown type of Message: " + params.textDistinctionCode1);

                }
                this.textDistinctionCode = params.textDistinctionCode1;
                //this.sampleDistinctCode = params.sampleDistinctionCode;
                this.sampleDistinctCode = params.sampleDistinctionCode;
                rackNumber = Integer.parseInt(params.rackNumber);
                positionTubeNumber = Integer.parseInt(params.tubePositionNumber);
                this.idInfo = params.idInformation;
                this.sid = params.sampleIdNumber.trim();
                this.patientName = params.patientName.trim();
                //String dateTime = "0605151636";
                String dateTimeStr = params.date + params.time;

                try {
                    this.date = sdf.parse(dateTimeStr);
                } catch (ParseException ex) {
                    Logger.getLogger(SysmexCA1500Message.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            this.setDataFromParams(params);
        }
    }

    public String createOrderEmptyFromQueryMessage() {
        String parameterCode = null;
        //System.out.println("textDistinctionCode:" + textDistinctionCode);
        // когда посылаем 999, то аппарат перестает запрашивать все остальные пробирки
        // когда посылаем 000, он понимает что для данного образца заданий нет, 
        // но остальные опрашивает до конца
        parameterCode = "000";
        /*
         if (params.textDistinctionCode2.equals("1")) {
         //если запрос по Rack N и Tube Post 
         //parameterCode = "999";
         parameterCode = "000";
         } else {
         //если запрос по SID (когда params.textDistinctionCode2 = 2)
         parameterCode = "000";
            
         }
         */
        String str = STX_STRING
                + "S"
                + params.textDistinctionCode2
                //+ "2210101U"
                + params.textDistinctionCode3
                + params.blockNumber
                + params.totalNumberBlocks
                + params.sampleDistinctionCode
                + params.date
                + params.time
                + params.rackNumber
                + params.tubePositionNumber
                + params.sampleIdNumber
                + params.idInformation
                + Util.getStringFillBySpace(11)
                + parameterCode + Util.getStringFillBySpace(6)
                + ETX_STRING;
        return str;
    }

    public String createOrderFromWorkOrderFromThis(WorkOrder wo) {
        //String dateTimeStr = wo.getDateCollection() != null ? sdf.format(wo.getDateCollection()) : sdf.format(new Date());
        //String sidFromWo = wo.getSid();
        //String sidForMess = util.Util.getStringFillBySpace(13 - sidFromWo.length()) + sidFromWo;
        String patName = getPatientName(wo);
        String testsStr = getTestsForSysmex600(wo);

        String str = STX_STRING + "S"
                + params.textDistinctionCode2
                + "210101"
                + params.sampleDistinctionCode
                + params.date + params.time//params.date + params.time
                + params.rackNumber // params.rackNumber
                + params.tubePositionNumber // params.tubePositionNumber
                //                + params.sampleIdNumber //params.sampleIdNumber
                + Util.getStringFillBySpace(SID_LENGTH - wo.getSid().length()) + wo.getSid() //params.sampleIdNumber
                + params.idInformation//params.idInformation
                + patName
                + testsStr
                + ETX_STRING;
        return str;
    }

    /**
     * берем у заказа список тестов в формате
     * "051,052,054,061,062,073,074,501,504"
     *
     * @return получаем список тестов в формате "050 060 070 500 "
     */
    private static String getTestsForSysmex600(WorkOrder workOrder) {
        StringBuilder stb = new StringBuilder();
        //String str = "051,052,054,061,062,073,074,501,504";
        String[] tests = workOrder.getTests().split(",");
        Set<String> set = new LinkedHashSet<>();
        for (String el : tests) {
            String elStr = el.substring(0, 2) + "0";
            set.add(elStr);
        }
        for (String el : set) {
            stb.append(el + "      ");
        }
        return stb.toString();
    }

    private static String getPatientName(WorkOrder wo) {
        String patientName = Util.getStringFillBySpace(11);
        int numberOfChars = 11;
        String pName = wo.getPatientName();
        if (pName != null && !pName.trim().isEmpty()) {
            if (pName.trim().length() > numberOfChars) {
                pName = pName.substring(0, numberOfChars);
            }
            patientName = Util.getStringFillBySpace(numberOfChars - pName.length()) + pName;
        }
        return patientName;
    }

    private void setDataFromParams(Parameters parameters) {
        for (String atrArrayData2 : parameters.data) {
            Data data = new Data();
            String atrArrayData1 = atrArrayData2;
            String testCode = atrArrayData1.substring(0, 3);
            String result = atrArrayData1.substring(3, 8);
            String flag = atrArrayData1.substring(8, 9);
            if (this.typeOfMessage == TypeMessageSysmexCa1500.RESULT) {
                data.setTestCodeForResult(testCode);
                data.setTestCode(testCode.substring(0, 2) + "0");
                data.setUnitsByNumber(testCode.substring(2, 3));
                data.setData(result.trim());
                data.setFlag(flag);
            } else {
                data.setTestCode(testCode);
            }
            this.data.add(data);
        }
    }

    @Override
    public boolean validateCheckSum() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int calculateCheckSum() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int extractCheckSumFromMessage() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getSidFromQueryRecord() {
        return sid;
    }

    public TypeMessageSysmexCa1500 getTypeOfMessage() {
        return typeOfMessage;
    }

    public String getTextDistinctionCode() {
        return textDistinctionCode;
    }

    public String getSampleDistinctCode() {
        return sampleDistinctCode;
    }

    public Date getDate() {
        return date;
    }

    public int getRackNumber() {
        return rackNumber;
    }

    public int getPositionTubeNumber() {
        return positionTubeNumber;
    }

    public String getSid() {
        return sid;
    }

    public String getIdInfo() {
        return idInfo;
    }

    public String getPatientName() {
        return patientName;
    }

    public List<Data> getData() {
        return data;
    }

    @Override
    public String toString() {
        return "SysmexCA660Message{" + "typeOfMessage=<b>" + typeOfMessage + "</b>, textDistinctionCode=" + textDistinctionCode + ", sampleDistinctCode=" + sampleDistinctCode + ", date=" + date + ", rackNumber=" + rackNumber + ", positionTubeNumber=" + positionTubeNumber + ", sid=" + sid + ", idInfo=" + idInfo + ", patientName=" + patientName + ", data=" + data + '}';
    }

}
