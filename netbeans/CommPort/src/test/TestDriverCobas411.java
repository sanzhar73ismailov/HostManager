package test;

import entity.Instrument;
import instrument.DriverCobas411;
import instrument.InstrumentException;
import modelHost.Model;
import modelHost.ModelException;
import modelHost.ModelImpl;

public class TestDriverCobas411 {

    public static void main(String[] args) throws InstrumentException, ModelException {
        Model model = new ModelImpl();
        Instrument instrument = model.getObject(2, new Instrument());
        DriverCobas411 driver = new DriverCobas411(instrument, model);
        driver.mainRun();
        // System.out.println(new Date().getTime()/1000);
    }
}
