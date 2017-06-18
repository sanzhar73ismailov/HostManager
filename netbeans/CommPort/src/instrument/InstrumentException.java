package instrument;

public class InstrumentException extends Exception{

    public InstrumentException() {
    }

    public InstrumentException(String message) {
        super(message);
    }

    public InstrumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public InstrumentException(Throwable cause) {
        super(cause);
    }

    public InstrumentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
