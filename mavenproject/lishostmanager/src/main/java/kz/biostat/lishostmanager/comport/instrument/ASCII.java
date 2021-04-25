package kz.biostat.lishostmanager.comport.instrument;

public class ASCII {

    /**
     * NULL
     */
    public final static byte NULL = 0x0;

    /**
     * START OF HEADING
     */
    public final static byte SOH = 0x1;
    /**
     * START OF TEXT
     */
    public final static byte STX = 0x2;
    /**
     * END OF TEXT
     */
    public final static byte ETX = 0x3;
    /**
     * END OF TRANSMISSION
     */
    public final static byte EOT = 0x4;
    /**
     * ENQUIRY
     */
    public final static byte ENQ = 0x5;
    /**
     * ACKNOWLEDGE
     */
    public final static byte ACK = 0x6;
    /**
     * LINE FEED
     */
    public final static byte LF = 0xA; //10
    /**
     * CARRIAGE RETURN
     */
    public final static byte CR = 0xD; //13
    /**
     * NEGATIVE ACKNOWLEDGE
     */
    public final static byte NACK = 0x15; //21
    /**
     * END OF TRANS. BLOCK
     */
    public final static byte ETB = 0x17; //23

    /**
     * END OF FILE
     */
    public final static byte EOF = 0x1A; //23

    /**
     * INFORMATION SEPARATOR FOUR (file separator)
     */
    public final static byte FS = 0x1C; //28
    public final static String NUL_STRING = new String(new byte[]{NULL});
    public final static String SOH_STRING = new String(new byte[]{SOH});
    public final static String STX_STRING = new String(new byte[]{STX});
    public final static String ETX_STRING = new String(new byte[]{ETX});
    public final static String EOT_STRING = new String(new byte[]{EOT});
    public final static String ENQ_STRING = new String(new byte[]{ENQ});
    public final static String ACK_STRING = new String(new byte[]{ACK});
    public final static String LF_STRING = new String(new byte[]{LF});
    public final static String CR_STRING = new String(new byte[]{CR});
    public final static String NACK_STRING = new String(new byte[]{NACK});
    public final static String FS_STRING = new String(new byte[]{FS});
    public final static String ETB_STRING = new String(new byte[]{ETB});
    public final static String EOF_STRING = new String(new byte[]{EOF});

    public static String getASCIICodeAsString(byte value) {
        String result = null;
        switch (value) {
            case ASCII.NULL:
                result = "<NULL>";
                break;
            case ASCII.SOH:
                result = "<SOH>";
                break;
            case ASCII.STX:
                result = "<STX>";
                break;
            case ASCII.ETX:
                result = "<ETX>";
                break;
            case ASCII.EOT:
                result = "<EOT>";
                break;
            case ASCII.ENQ:
                result = "<ENQ>";
                break;
            case ASCII.ACK:
                result = "<ACK>";
                break;
            case ASCII.LF:
                result = "<LF>";
                break;
            case ASCII.CR:
                result = "<CR>";
                break;
            case ASCII.NACK:
                result = "<NACK>";
                break;
            case ASCII.ETB:
                result = "<ETB>";
                break;
            case ASCII.FS:
                result = "<FS>";
                break;
            case ASCII.EOF:
                result = "<EOF>";
                break;
            default:
                result = String.valueOf((char) value);
        }

        return result;
    }

    public static String getStringWithAsciiCodes(String text) {
        return getStringFromByteArray(text.getBytes());
    }

    public static String getStringFromByteArray(byte[] bytes) {
        StringBuilder stb = new StringBuilder();
        for (byte aByte : bytes) {
            stb.append(getASCIICodeAsString(aByte));
        }
        return stb.toString();
    }

}
