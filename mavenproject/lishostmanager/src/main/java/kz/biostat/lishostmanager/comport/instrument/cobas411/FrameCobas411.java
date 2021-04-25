package kz.biostat.lishostmanager.comport.instrument.cobas411;

import kz.biostat.lishostmanager.comport.util.Util;

import static kz.biostat.lishostmanager.comport.instrument.ASCII.*;
import java.util.*;

public class FrameCobas411 {

    private byte[] rawBytes;
    private int numberFrame;
    private String text;
    private boolean lastFrame;
    private byte[] chk = new byte[2];
    public static final int SIZE_FRAME_MAX = 240;

    public FrameCobas411(int numberFrame, String text, boolean lastFrame) throws Cobas411Exception {
        List<Byte> bytes = new ArrayList();
        if (numberFrame > 7) {
            throw new Cobas411Exception("number frame more than 7!");
        }
        this.numberFrame = numberFrame;
        this.text = text;
        this.lastFrame = lastFrame;
        this.chk = calculateChk(numberFrame, text, lastFrame);
        bytes.add(STX);
        addStringToByteList(bytes, numberFrame + "");
        addStringToByteList(bytes, text);
        bytes.add(lastFrame ? ETX : ETB);
        addArrayByteToByteList(bytes, chk);
        bytes.add(CR);
        bytes.add(LF);
        this.rawBytes = Util.convertBytes(bytes);
    }

    public FrameCobas411(byte[] rawBytes) throws Cobas411Exception {
        this.rawBytes = rawBytes;
        this.formingIncomeFrame(rawBytes);
    }

    public byte[] calculateChk(int myNumberFrame, String textMessage, boolean myLastFrame) {
        List<Byte> listBytes = new ArrayList<>();
        listBytes.add((myNumberFrame + "").getBytes()[0]);
        addStringToByteList(listBytes, textMessage);
        listBytes.add(myLastFrame ? ETX : ETB);
        return calculateChk(Util.convertBytes(listBytes));
    }

    public byte[] calculateChk(byte[] bytes) {
        int sum = 0;
        sum += getSumOfByteArray(bytes);
        return lastTwoBytesFromInt(sum);
    }

    private byte[] lastTwoBytesFromInt(int value) {
        String strHex = Integer.toHexString(value).toUpperCase();
        String lastTwoSymbols = strHex.substring(strHex.length() - 2, strHex.length());
        byte[] bytesReturn = lastTwoSymbols.getBytes();
        return bytesReturn;
    }

    private int getSumOfByteArray(byte[] array) {
        int sum = 0;
        for (byte b : array) {
            if (b == STX) {
                continue;
            }
            sum += b;
        }
        return sum;
    }

    public void setNumberFrame(int numberFrame) {
        this.numberFrame = numberFrame;
    }

    public byte[] calculateChk() {
        try {
            if (this.numberFrame == 0) {
                throw new Cobas411Exception("number frame не установлен");
            }
            if (this.text == null || this.text.isEmpty()) {
                throw new Cobas411Exception("text of Frame is null or empty");
            }
        } catch (Exception ex) {
            System.err.println("ex = " + ex);
        }
        return calculateChk(this.numberFrame, this.text, this.lastFrame);
    }

    public boolean checkChk() {
        byte[] chkCalculated = this.calculateChk();
        //System.out.println("chkCalculated = " + java.util.Arrays.toString(chkCalculated));
        //System.out.println("chk = " + java.util.Arrays.toString(chk));
        return Arrays.equals(chkCalculated, chk);
    }

    public byte[] getRawBytes() {
        return rawBytes;
    }

    public void setRawBytes(byte[] rawBytes) {
        this.rawBytes = rawBytes;
    }

    private void formingIncomeFrame(byte[] bytesArray) throws Cobas411Exception {
        this.rawBytes = bytesArray;
        if (bytesArray == null || bytesArray.length == 0) {
            throw new Cobas411Exception("Not possible form frame, bytesArray is null or length is 0");
        } else {
            String numberFrameAsText = new String(new byte[]{bytesArray[1]});
            //System.out.println("numberFrameAsText = " + numberFrameAsText);
            this.numberFrame = Integer.parseInt(numberFrameAsText);
            this.chk[0] = bytesArray[bytesArray.length - 4];
            this.chk[1] = bytesArray[bytesArray.length - 3];
            if (bytesArray[bytesArray.length - 5] == ETX) {
                lastFrame = true;
            } else if (bytesArray[bytesArray.length - 5] == ETB) {
                lastFrame = false;
            } else {
                throw new Cobas411Exception("Wrong end symbol, not ETB and not ETX: " + bytesArray[bytesArray.length - 5]);
            }
            byte[] textBytes = Arrays.copyOfRange(bytesArray, 2, bytesArray.length - 5);
            this.text = new String(textBytes);
        }
    }

    public int getNumberFrame() {
        return numberFrame;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public byte[] getChk() {
        return chk;
    }

    public void setChk(byte[] chk) {
        this.chk = chk;
    }

    public boolean isLastFrame() {
        return lastFrame;
    }

    public void setLastFrame(boolean lastFrame) {
        this.lastFrame = lastFrame;
    }

    public String getFrameAsString() {
        StringBuilder strBuilder = new StringBuilder();
        for (int i = 0; i < rawBytes.length; i++) {
            byte c = rawBytes[i];
            strBuilder.append(getASCIICodeAsString(c));
        }
        return strBuilder.toString();
    }

    public String getBytesAsString(byte[] bytes) {
        StringBuilder strBuilder = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            byte c = bytes[i];
            strBuilder.append(getASCIICodeAsString(c));
        }
        return strBuilder.toString();
    }

    public void printFrameAsString() {
        System.out.println(getFrameAsString());
    }

    private static void addArrayByteToByteList(List<Byte> listByRef, byte[] array) {
        Util.addArrayByteToByteList(listByRef, array);
    }

    public static void addStringToByteList(List<Byte> listByRef, String text) {
        Util.addStringToByteList(listByRef, text);
    }

    public static void testCalculateCheck() throws Cobas411Exception {
        FrameCobas411 frame = new FrameCobas411(1, "Test", true);
        frame.calculateChk();
        //System.out.println("frame:" + frame);
        frame.printFrameAsString();
        System.out.println("frame.validate() = " + frame.validate());
        System.out.println("-----------------");
    }

    public static void testCalculateCheck2() throws Cobas411Exception {
        List<Byte> bytes = new ArrayList();
        int numberFrame1 = 1;
        String text1 = "Test";
        boolean lastFrame1 = false;
        byte[] chk = new FrameCobas411(1, "Test", true).calculateChk(numberFrame1, text1, lastFrame1);
        bytes.add(STX);
        addStringToByteList(bytes, numberFrame1 + "");
        addStringToByteList(bytes, text1);
        bytes.add(lastFrame1 ? ETX : ETB);
        addArrayByteToByteList(bytes, chk);
        bytes.add(CR);
        bytes.add(LF);
        //System.out.println("bytes = " + bytes);
        byte[] rawBytes = Util.convertBytes(bytes);
        //System.out.println("rawBytes ={" + new String(rawBytes) + "}");
        FrameCobas411 frame = new FrameCobas411(rawBytes);
        //frame.calculateChk();
        // System.out.println("frame:" + frame);
        System.out.println("frame.validate() = " + frame.validate());
        frame.printFrameAsString();
    }

    @Override
    public String toString() {
        return "Frame{" + "rawBytes=" + Arrays.toString(rawBytes) + ", numberFrame=" + numberFrame + ", text=" + text + ", lastFrame=" + lastFrame + ", chk=" + Arrays.toString(chk) + '}';
    }

    public boolean validate() {
        boolean returnResult = true;
        StringBuilder sb = new StringBuilder();
        if (this.rawBytes == null) {
            sb.append("rawbytes is null");
            returnResult = false;
        }
        if (this.rawBytes != null && this.rawBytes.length == 0) {
            sb.append("\nrawbytes length is 0");
            returnResult = false;
        }
        if (this.numberFrame < 1 || this.numberFrame > 7) {
            sb.append("\nnumberFrame is out of diapazon (1-7): ").append(this.numberFrame);
            returnResult = false;
        }
        if (this.chk == null || this.chk.length != 2) {
            sb.append("\nchk is null or length is not 2: ").append((this.chk != null ? this.chk.length : "null"));
            returnResult = false;
        }
        if (this.text == null || this.text.length() == 0) {
            sb.append("\ntext is null or empty : ").append(text);
            returnResult = false;
        }
        if (this.rawBytes != null && this.rawBytes.length > 0) {
            if (this.rawBytes[0] != STX) {
                sb.append("\nthe first byte is not STX");
                returnResult = false;
            }
            if (this.rawBytes.length > 1 && (this.rawBytes[1] < (byte) '1' || this.rawBytes[1] > (byte) '7')) {
                sb.append("\nrawBytes[1] (numberFrame) is out of diapazon (1-7): ").
                        append(rawBytes[1]);
                returnResult = false;
            }
            if (this.rawBytes.length > 7 && (this.rawBytes[this.rawBytes.length - 5] != ETB && this.rawBytes[this.rawBytes.length - 5] != ETX)) {
                sb.append("\nrawBytes[rawBytes.length-5] is not ETX and not ETB").
                        append(this.rawBytes[this.rawBytes.length - 5]);
                returnResult = false;
            }
            if (this.rawBytes.length > 7 && (this.rawBytes[this.rawBytes.length - 5] == ETB && lastFrame == true)) {
                sb.append("\nrawBytes[rawBytes.length-5] is ETB but lastFrame is true");
                returnResult = false;
            }
            if (this.rawBytes.length > 7 && (this.rawBytes[this.rawBytes.length - 5] == ETX && lastFrame == false)) {
                sb.append("\nrawBytes[rawBytes.length-5] is ETX but lastFrame is false");
                returnResult = false;
            }
            if (this.rawBytes.length > 7 && (this.rawBytes[this.rawBytes.length - 2] != CR)) {
                sb.append("\nrawBytes[rawBytes.length-2] is not CR");
                returnResult = false;
            }
            if (this.rawBytes.length > 7 && (this.rawBytes[this.rawBytes.length - 1] != LF)) {
                sb.append("\nrawBytes[rawBytes.length-1] is not LF");
                returnResult = false;
            }
        }
        if (!checkChk()) {
            sb.append("\ncheckChk is not passed");
            returnResult = false;
        }
        if (!returnResult) {
            System.out.println("validation is not passed: " + sb.toString());
        }
        return returnResult;
    }
}
