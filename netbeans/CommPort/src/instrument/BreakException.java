package instrument;

public class BreakException extends InstrumentException{

    public BreakException() {
    }

    public BreakException(String message) {
        super(message);
    }

    public BreakException(String message, Throwable cause) {
        super(message, cause);
    }

    public BreakException(Throwable cause) {
        super(cause);
    }

}
