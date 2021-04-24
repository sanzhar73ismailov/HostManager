package test;

import entity.Instrument;
import instrument.*;
import modelHost.Model;
import modelHost.ModelException;
import modelHost.ModelImpl;

public class TestDriverSysmexXS500i {
    public static void main(String[] args) throws ModelException, InstrumentException {
        testMainRun();
    }

    public static void testMainRun() throws ModelException, InstrumentException {
        int istrId = 8;
        Model model = new ModelImpl();
        InstrumentIndicator instrInd = InstrumentIndicator.getInstance();
        instrInd.startInstrument(istrId);
        Instrument instrument = model.getObject(istrId, new Instrument());
           instrument.setIp("localhost");
//        instrument.setIp("192.168.1.205");
        DriverInstrument driver = new DriverSysmexXS500i(instrument, model);
        driver.mainRun();
    }
}
