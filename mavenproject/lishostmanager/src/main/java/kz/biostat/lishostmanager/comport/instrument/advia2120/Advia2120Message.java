package kz.biostat.lishostmanager.comport.instrument.advia2120;

import kz.biostat.lishostmanager.comport.entity.WorkOrder;
import kz.biostat.lishostmanager.comport.instrument.ASCII;
import kz.biostat.lishostmanager.comport.instrument.Message;
import kz.biostat.lishostmanager.comport.util.Util;

import static kz.biostat.lishostmanager.comport.instrument.ASCII.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;

public class Advia2120Message extends Message {

    private byte messageToggle = 0x30;
    public static byte[] MESSAGE_INIT_NO_IMAGES = getInitMessageWithoutImages();
    public static byte[] MESSAGE_RESUL_EXAMPLE = getMessageResultExample();
    public static final int SID_LENGTH = 14;

    public Advia2120Message(byte[] rawMessage) throws MtIsNotInDiapazonException {
        super(rawMessage);
        extractMT();
        this.type = String.valueOf((char) rawMessage[2]);
    }

    public Advia2120Message(char[] charArray) throws MtIsNotInDiapazonException {
        this(Message.charArrayToByteArray(charArray));
    }

    public Advia2120Message(String value) throws MtIsNotInDiapazonException {
        this(value.toCharArray());
    }

    public static byte[] getInitMessageWithoutImages() {
        byte chkForMeassage = (byte) calculateCheckSumFromAllBytes(new byte[]{0x30, 'I', ' ', CR, LF});
        byte[] byteArray = {STX, 0x30, 'I', ' ', CR, LF, chkForMeassage, ETX};
        return byteArray;
    }

    public static byte[] getMessageResultExample() {
        byte chkForMeassage = (byte) calculateCheckSumFromAllBytes(new byte[]{0x30, 'R', ' ', '1', CR, LF});
        byte[] byteArray = {STX, 0x30, 'R', ' ', '1', CR, LF, chkForMeassage, ETX};
        return byteArray;
    }

    public Advia2120Message getMessageNoWorkOrder(String sid) throws MtIsNotInDiapazonException {
        String mtValue = String.valueOf((char) calculateNextMT(messageToggle));
        String idCode = "N";
        //String sid = "00000000000123";
        sid = getStringBySizeFilledWithZerosOnTheLeft(sid, SID_LENGTH);
        String strMessage = mtValue + idCode + " " + "W" + " " + sid + CR_STRING + LF_STRING;
        byte chkForMeassage = (byte) calculateCheckSumFromAllBytes(strMessage.getBytes());
        String strForByteArray = STX_STRING + strMessage + String.valueOf((char) chkForMeassage) + ETX_STRING;
        byte[] byteArray = strForByteArray.getBytes();
        return new Advia2120Message(byteArray);
    }

    public Advia2120Message getMessageWorkOrder(String sid) throws MtIsNotInDiapazonException {
        String mtValue = String.valueOf((char) calculateNextMT(messageToggle));
        String idCode = "Y";
        String patNumber = "00000000000111";
        String strMessage
                = mtValue
                + idCode
                + " "
                + " "
                + " "
                + "A"
                + " "
                + sid
                + " "
                + "000-00"
                + " "
                + "N"
                + " "
                + " "
                + getStringFillBySpace(6)
                + " "
                + " "
                + getStringFillBySpace(5)
                + " "
                + patNumber
                + " "
                + " "
                + " "
                + getStringFillBySymbol(30, 'p')
                + " "
                + "01/20/1990"
                + " "
                + "M"
                + " "
                + "01/20/2015"
                + " "
                + "1020"
                + " "
                + "Almaty"
                + " "
                + "Ivanov"
                + " "
                + CR_STRING + LF_STRING
                + "001"
                + CR_STRING + LF_STRING;
        byte chkForMeassage = (byte) calculateCheckSumFromAllBytes(strMessage.getBytes());
        String strForByteArray = STX_STRING + strMessage + String.valueOf((char) chkForMeassage) + ETX_STRING;
        byte[] byteArray = strForByteArray.getBytes();
        return new Advia2120Message(byteArray);
    }

    public static String getStringBySizeFilledWithSpacesOnTheRight(String str, int size) {
        if (str == null) {
            str = "";
        }
        if (str.length() > size) {
            return str.substring(0, size);
        }
        return str + getStringFillBySpace(size - str.length());
    }

    public static String getStringBySizeFilledWithZerosOnTheLeft(String str, int size) {
        if (str == null) {
            str = "";
        }
        if (str.length() > size) {
            return str.substring(0, size);
        }
        return getStringFillByZeros(size - str.length()) + str;
    }

    public Advia2120Message getMessageWorkOrder(WorkOrder workOrder) throws MtIsNotInDiapazonException, Advia2120Exception {
        ParamsAdvia2120OrderRecord params = convertWorkOrderToParams(workOrder);
        StringBuilder stb = new StringBuilder();
        stb.append(params.mtValue).
                append(params.idCode).
                append(" ").
                append(" ").
                append(params.statIndicator).
                append(params.updateIndicator).
                append(" ").
                //append(params.sid);

                append(getStringBySizeFilledWithZerosOnTheLeft("t" + System.currentTimeMillis() + "", SID_LENGTH)).
                 //append(getStringBySizeFilledWithZerosOnTheLeft("t100-6", SID_LENGTH)).
                //append(getStringBySizeFilledWithZerosOnTheLeft("sidtemp03", SID_LENGTH)).
                append(" ").
                append(params.rackPosition).
                append(" ").
                append(params.slideNotification).
                append(" ").
                append(" ").
                append(getStringFillBySpace(6)).
                append(" ").
                append(" ").
                append(getStringFillBySpace(5)).
                append(" ").
                append(params.patientNumber).
                append(" ").
                append(" ").
                append(" ").
                append(params.patientName).
                append(" ").
                append(params.patientDob).
                append(" ").
                append(params.patientSex).
                append(" ").
                append(params.dateCollection).
                append(" ").
                append(params.timeCollection).
                append(" ").
                append(params.location).
                append(" ").
                append(params.doctor).
                append(" ").
                append(CR_STRING).
                append(LF_STRING);
        //for (String test : params.tests) {
        //    stb.append(test).append(getStringFillBySpace(13));
        //}

        //stb.append("001002003004005006007008009010011012013");
        
        //001,002,003,004,005,006,007,008,009,010,011,036,037,038,039,040,041,051,072,076,077,078
        //stb.append("001002003004005006007008009010011036037038039040041051072076077078");
        //stb.append("001");
        stb.append("001002003004005010011");
       // stb.append("001002");
        //stb.append("     ");
        //stb.append("001002003004005006007008009010011012013014015016017018019020021022023024025026027028029042050051052053054055056057058059060061062063064065072073083084191192");
        //stb.append("001002003036051004039009041040038006007005037011013012010077078008076915922920913919912918924914921917923911910");
        stb.append(CR_STRING).append(LF_STRING);

        byte chkForMeassage = (byte) calculateCheckSumFromAllBytes(stb.toString().getBytes());
        String strForByteArray = STX_STRING + stb.toString() + String.valueOf((char) chkForMeassage) + ETX_STRING;
        byte[] byteArray = strForByteArray.getBytes();
        return new Advia2120Message(byteArray);
    }

    private ParamsAdvia2120OrderRecord convertWorkOrderToParams(WorkOrder workOrder) throws Advia2120Exception {
        ParamsAdvia2120OrderRecord param = new ParamsAdvia2120OrderRecord();
        param.mtValue = String.valueOf((char) calculateNextMT(messageToggle));
        param.idCode = "Y";
        param.statIndicator = formatPriority(workOrder);
        param.updateIndicator = formatUpdateIndicator(workOrder);
        param.sid = formatSid(workOrder);
        param.rackPosition = formatRackPosition(workOrder);
        param.slideNotification = "N";
        param.patientNumber = getStringBySizeFilledWithSpacesOnTheRight(workOrder.getPatientNumber(), 14);
        param.patientName = getStringBySizeFilledWithSpacesOnTheRight(workOrder.getPatientName(), 30);
        param.patientDob = formatDateToInstrVariant(workOrder.getDateBirth());
        param.patientSex = formatSex(workOrder.getSex());
        param.dateCollection = formatDateToInstrVariant(workOrder.getDateCollection());
        param.timeCollection = formatTimeToInstrVariant(workOrder.getDateCollection());
        param.location = getStringBySizeFilledWithSpacesOnTheRight(workOrder.getParamValue("location"), 6);
        param.doctor = getStringBySizeFilledWithSpacesOnTheRight(workOrder.getParamValue("doctor"), 6);;
        param.tests = converTests(workOrder.getTests());
        return param;
    }

    private static String getStringFillBySpace(int n) {
        return Util.getStringFillBySpace(n);
    }

    private static String getStringFillByZeros(int n) {
        return Util.getStringFillByCharacter(n, '0');
    }

    private String getStringFillBySymbol(int n, char ch) {
        return Util.getStringFillByCharacter(n, ch);
    }

    public Advia2120Message getTokenTransferMessage() throws MtIsNotInDiapazonException {
        byte mtValue = calculateNextMT(messageToggle);
        byte chkForMeassage = (byte) calculateCheckSumFromAllBytes(
                new byte[]{mtValue, 'S', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', CR, LF});
        byte[] byteArray = {STX, mtValue, 'S', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', CR, LF, chkForMeassage, ETX};
        return new Advia2120Message(byteArray);
    }

    public Advia2120Message getResultValidationMessage() throws MtIsNotInDiapazonException {
        byte mtValue = calculateNextMT(messageToggle);
        byte chkForMeassage = (byte) calculateCheckSumFromAllBytes(new byte[]{mtValue, 'Z',
            ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
            '0', CR, LF});
        byte[] byteArray = {STX, mtValue, 'Z',
            ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
            '0', CR, LF, chkForMeassage, ETX};
        return new Advia2120Message(byteArray);
    }

    private byte calculateNextMT(byte val) {
        if ((val + 1) > 0x5A) {
            return 0x30;
        } else {
            return (byte) (val + 1);
        }
    }

    @Override
    public boolean validateCheckSum() {
        int calculatedByXOR = this.calculateCheckSum();
        int checkSumFromMessage = this.extractCheckSumFromMessage();
        return calculatedByXOR == checkSumFromMessage;
    }

    @Override
    public int calculateCheckSum() {
        int calculatedByXOR = 0;
        byte[] bArrayWithout_STX_CHK_ETX = java.util.Arrays.copyOfRange(rawMessage, 1, rawMessage.length - 2);
        calculatedByXOR = calculateCheckSumFromAllBytes(bArrayWithout_STX_CHK_ETX);
        // ЕСЛИ CHK = 3, то должен менятся на 127
        if (calculatedByXOR == ASCII.ETX) {
            calculatedByXOR = 0x7F; //127
        }
        return calculatedByXOR;
    }

    public static int calculateCheckSumFromAllBytes(byte[] byteArray) {
        int calculatedByXOR = 0;
        for (int i = 0; i < byteArray.length; i++) {
            byte c = byteArray[i];
            calculatedByXOR ^= c;
        }
        if (calculatedByXOR == ASCII.ETX) {
            calculatedByXOR = 0x7F; //127
        }
        return calculatedByXOR;
    }

    @Override
    public int extractCheckSumFromMessage() {
        return rawMessage[rawMessage.length - 2];
    }

    public byte getMessageToggle() {
        return messageToggle;
    }

    private void extractMT() throws MtIsNotInDiapazonException {
        if (rawMessage.length > 0) {
            messageToggle = rawMessage[1];
        }
        if (messageToggle < 0x30 && messageToggle > 0x5A) {
            throw new MtIsNotInDiapazonException("MT не входит в правильный диапазон 30h-5Ah (48-90), "
                    + "составляет:" + messageToggle);
        }

    }

    public static void main(String[] args) throws MtIsNotInDiapazonException {
        byte mtValue = 0x31;
        byte chkForMeassage = (byte) calculateCheckSumFromAllBytes(
                new byte[]{mtValue, 'S', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', CR, LF});
        byte[] byteArray = {STX, mtValue, 'S', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', CR, LF, chkForMeassage, ETX};
        Advia2120Message mes = new Advia2120Message(byteArray);
        Advia2120Message mes2 = mes.getMessageWorkOrder("12345678901234");
        mes2.printMessageAsString();
    }

    String formatDateToInstrVariant(java.util.Date date) {
        String strAsDate = getStringFillBySpace(10);
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            strAsDate = sdf.format(date);
        }
        return strAsDate;
    }

    String formatTimeToInstrVariant(java.util.Date date) {
        String strAsDate = getStringFillBySpace(4);
        //String strAsDate = "0800";
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
            strAsDate = sdf.format(date);
        }
        return strAsDate;
    }

    //workOrder.getSex()
    String formatSex(int sex) {
        String str = " ";
        if (sex == 1) {
            str = "M";
        } else if (sex == 2) {
            str = "F";
        }
        return str;
    }

    String[] converTests(String tests) {
        if (tests == null || tests.trim().isEmpty()) {
            return new String[0];
        }
        String[] testsArray = tests.split(",");
        for (int i = 0; i < testsArray.length; i++) {
            String string = testsArray[i];
            testsArray[i] = getStringBySizeFilledWithZerosOnTheLeft(string, 3);
        }
        return testsArray;
    }

    String formatRackPosition(WorkOrder workOrder) {
        String retStr = getStringFillBySpace(6);
        if (workOrder.getPosition() != 0) {
            if (workOrder.getRack() != null && !workOrder.getRack().trim().isEmpty()) {
                String rack = "";
                String position = "";
                //String rack = getStringFillBySpace(3);
                //String position = getStringFillBySpace(2);
                rack = getStringBySizeFilledWithZerosOnTheLeft(workOrder.getRack(), 3);
                position = getStringBySizeFilledWithZerosOnTheLeft(workOrder.getPosition() + "", 2);
                retStr = rack + "-" + position;
            }
        }
        return retStr;
    }

    String formatSid(WorkOrder workOrder) throws Advia2120Exception {
        if (workOrder.getSid().length() > 14) {
            throw new Advia2120Exception(String.format("the length of sid %s is more of 14 and = %s",
                    workOrder.getSid(), workOrder.getSid().length()));
        }
        return getStringBySizeFilledWithZerosOnTheLeft(workOrder.getSid(), 14);
    }

    String formatPriority(WorkOrder workOrder) {
        String param = "routineSampleOrStatSample";
        if (workOrder.getParamValue(param) != null && workOrder.getParamValue(param).equals("S")) {
            return "U";
        } else {
            return " ";
        }
    }

    String formatUpdateIndicator(WorkOrder workOrder) {
        String param = "updateIndicator";
        if (workOrder.getParamValue(param) != null && workOrder.getParamValue(param).equals("update")) {
            return "A";
        } else {
            return " ";
        }
    }

    public static Advia2120Message getTestResultMessage() throws MtIsNotInDiapazonException {
        //byte mtValue = calculateNextMT(messageToggle);
        String messTogel = ((char) 50 + "");
        StringBuilder stb = new StringBuilder();
        //<STX>2R 00000009000000                  10/18/14 09:43:00   <CR><LF>  6 31.4   7 34.8 <CR><LF>B<ETX>
        //stb.append(STX_STRING);
        //stb.append((char) mtValue + "");
        stb.append(messTogel);
        stb.append("R");
        stb.append(" ");
        stb.append(getStringBySizeFilledWithZerosOnTheLeft("sid_123123", 14));
        stb.append(" ");
        stb.append("001-01");
        stb.append(" ");
        stb.append(" ");
        stb.append(getStringFillBySpace(8));
        stb.append(" ");
        stb.append("10/18/14");
        stb.append(" ");
        stb.append("09:43:00");
        stb.append("   ");
        stb.append(CR_STRING);
        stb.append(LF_STRING);
        stb.append("  1 30.4");
        stb.append(" ");
        stb.append(" 10 35.4");
        stb.append(" ");
        //stb.append("  3 37.4");
        //stb.append(" ");
        stb.append(CR_STRING);
        stb.append(LF_STRING);

        byte chkForMeassage = (byte) calculateCheckSumFromAllBytes(stb.toString().getBytes());
        byte[] byteArray = (STX_STRING + stb.toString() + (char) chkForMeassage + ETX_STRING).getBytes();
        return new ResultMessage(byteArray);
    }

    public String getSidFromQueryMessage() throws Advia2120Exception {
        if (!type.equals("Q")) {
            throw new Advia2120Exception("Type is not query (Q). Value of type: " + this.type);
        }
        String sid = new String(Arrays.copyOfRange(rawMessage, 4, (4 + SID_LENGTH)));
        return sid;
    }

    @Override
    public String getSidFromQueryRecord() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
