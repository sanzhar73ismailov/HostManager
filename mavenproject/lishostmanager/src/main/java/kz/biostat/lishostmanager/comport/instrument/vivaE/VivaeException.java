/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.biostat.lishostmanager.comport.instrument.vivaE;

import kz.biostat.lishostmanager.comport.instrument.InstrumentException;

/**
 *
 * @author sanzhar.ismailov
 */
public class VivaeException  extends InstrumentException {

    public VivaeException() {
    }

    public VivaeException(String message) {
        super(message);
    }

    public VivaeException(String message, Throwable cause) {
        super(message, cause);
    }

    public VivaeException(Throwable cause) {
        super(cause);
    }
    
}
