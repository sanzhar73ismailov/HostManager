package instrument;

public abstract class Message {

    protected byte[] rawMessage;
    protected String type;

    public Message(byte[] rawMessage) {
        this.rawMessage = rawMessage;
    }

    public static byte[] charArrayToByteArray(char[] charArray) {
        byte[] byteArray = new byte[charArray.length];
        for (int i = 0; i < charArray.length; i++) {
            byteArray[i] = (byte) charArray[i];
        }
        return byteArray;
    }

    public abstract boolean validateCheckSum();

    public abstract int calculateCheckSum();

    public String getMessageAsString() {
        StringBuilder strBuilder = new StringBuilder();
        for (int i = 0; i < rawMessage.length; i++) {
            byte c = rawMessage[i];
            strBuilder.append(getASCIICodeAsString(c));
        }
        return strBuilder.toString();
    }

    public void printMessageAsString() {
        System.out.println(getMessageAsString());
    }

    public static String getASCIICodeAsString(byte value) {
        return ASCII.getASCIICodeAsString(value);
    }

    public byte[] getRawMessage() {
        return rawMessage;
    }

    public String getType() {
        return type;
    }

    public abstract int extractCheckSumFromMessage();

   public abstract   String getSidFromQueryRecord();
}
