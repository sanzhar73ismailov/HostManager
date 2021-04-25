package kz.biostat.lishostmanager.comport.test;

import kz.biostat.lishostmanager.comport.entity.Instrument;
import kz.biostat.lishostmanager.comport.instrument.DriverCobas411;
import kz.biostat.lishostmanager.comport.instrument.InstrumentException;
import kz.biostat.lishostmanager.comport.modelHost.Model;
import kz.biostat.lishostmanager.comport.modelHost.ModelException;
import kz.biostat.lishostmanager.comport.modelHost.ModelImpl;

public class TestDriverCobas411 {

    public static void main(String[] args) throws InstrumentException, ModelException {
        Model model = new ModelImpl();
        Instrument instrument = model.getObject(2, new Instrument());
        DriverCobas411 driver = new DriverCobas411(instrument, model);
        driver.mainRun();
        // System.out.println(new Date().getTime()/1000);
    }
}
