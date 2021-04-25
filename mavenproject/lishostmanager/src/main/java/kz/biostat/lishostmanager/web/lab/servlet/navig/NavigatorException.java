package kz.biostat.lishostmanager.web.lab.servlet.navig;

public class NavigatorException extends Exception{

    public NavigatorException() {
    }

    public NavigatorException(String message) {
        super(message);
    }

    public NavigatorException(String message, Throwable cause) {
        super(message, cause);
    }

    public NavigatorException(Throwable cause) {
        super(cause);
    }

}
