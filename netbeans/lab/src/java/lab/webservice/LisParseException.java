package lab.webservice;

public class LisParseException extends Exception {

    public LisParseException() {
    }

    public LisParseException(String message) {
        super(message);
    }

    public LisParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public LisParseException(Throwable cause) {
        super(cause);
    }

}
