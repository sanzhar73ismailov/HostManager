package kz.biostat.lishostmanager.comport.instrument.sysmexXS500i;

import kz.biostat.lishostmanager.comport.instrument.InstrumentException;

public class SysmexXS500iException extends InstrumentException {
    public SysmexXS500iException() {
    }

    public SysmexXS500iException(String message) {
        super(message);
    }

    public SysmexXS500iException(String message, Throwable cause) {
        super(message, cause);
    }

    public SysmexXS500iException(Throwable cause) {
        super(cause);
    }

    public SysmexXS500iException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
