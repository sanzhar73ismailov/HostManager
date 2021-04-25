/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.biostat.lishostmanager.comport.instrument;

/**
 *
 * @author sanzhar.ismailov
 */
public class WorkOrderNotSendException extends InstrumentException{

    public WorkOrderNotSendException() {
    }

    public WorkOrderNotSendException(String message) {
        super(message);
    }

    public WorkOrderNotSendException(String message, Throwable cause) {
        super(message, cause);
    }

    public WorkOrderNotSendException(Throwable cause) {
        super(cause);
    }
    
}
