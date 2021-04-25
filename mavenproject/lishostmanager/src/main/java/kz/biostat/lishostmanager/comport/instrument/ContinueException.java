package kz.biostat.lishostmanager.comport.instrument;

public class ContinueException extends InstrumentException {

//    public ContinueException() {
//    }

    public ContinueException(String message) {
        super(message);
    }

    public ContinueException(String message, Throwable cause) {
        super(message, cause);
    }

    public ContinueException(Throwable cause) {
        super(cause);
    }

}
