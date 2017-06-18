package instrument.astm;

import instrument.ASCII;
import static instrument.ASCII.STX;
import static instrument.ASCII.getASCIICodeAsString;

public abstract class Frame {

    protected int frameNumber;
    protected String checkSum;
    protected String text;
    /**
     * если фрейм содержит в конце ETX, а не ETB - для тех протоколов где, 
     * запись делится на две и более если сообщение превышает 240 символов
     */
    protected boolean etxFrame;
    public static final int SIZE_FRAME_MAX = 240;

    public Frame(String text) {
        this.text = text;
        frameNumber = text.toCharArray()[1];
    }
    
    public static String calculateChk(byte[] bytes) {
        int sum = 0;
        sum += getSumOfByteArray(bytes);
        return lastTwoBytesFromInt(sum);
    }

    private static String lastTwoBytesFromInt(int value) {
        String strHex = Integer.toHexString(value).toUpperCase();
        String lastTwoSymbols = strHex.substring(strHex.length() - 2, strHex.length());
        return lastTwoSymbols;
    }

    private static int getSumOfByteArray(byte[] array) {
        int sum = 0;
        for (byte b : array) {
            if (b == STX) {
                continue;
            }
            sum += b;
        }
        return sum;
    }

    public abstract boolean validateCheckSum();

    public String getFrameAsString() {
        StringBuilder strBuilder = new StringBuilder();
        byte[] rawBytes = text.getBytes();
        for (int i = 0; i < rawBytes.length; i++) {
            byte c = rawBytes[i];
            strBuilder.append(getASCIICodeAsString(c));
        }
        return strBuilder.toString();
    }

    public String getText() {
        return text;
    }

    public byte[] getRawBytes() {
        return text.getBytes();
    }
    
    public String getCheckSum() {
        checkSum = text.substring(text.length() - 4, text.length() - 2);
        return checkSum;
    }

    public int getFrameNumber() {
        return frameNumber;
    }

    public void setFrameNumber(int frameNumber) {
        this.frameNumber = frameNumber;
    }

    public boolean isEtxFrame() {
        return etxFrame;
    }

    public void setEtxFrame(boolean etxFrame) {
        this.etxFrame = etxFrame;
    }

    @Override
    public String toString() {
        return "Frame{" + "text=" + ASCII.getStringWithAsciiCodes(text) + '}';
    }
    
    

    
    
    
}
