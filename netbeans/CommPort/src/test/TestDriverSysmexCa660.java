package test;

import entity.Instrument;
import instrument.DriverAdviaCentaurCP;
import instrument.DriverInstrument;
import instrument.DriverSysmexCa660;
import instrument.InstrumentException;
import instrument.InstrumentIndicator;
import modelHost.Model;
import modelHost.ModelException;
import modelHost.ModelImpl;

public class TestDriverSysmexCa660 {
    public static void main(String[] args) throws ModelException, InstrumentException {
        testMainRun();
        
    }
    public static void testMainRun() throws ModelException, InstrumentException {
        int istrId = 6;
        Model model = new ModelImpl();
        InstrumentIndicator instrInd = InstrumentIndicator.getInstance();
        instrInd.startInstrument(istrId);

        Instrument instrument = model.getObject(istrId, new Instrument());
        //   instrument.setIp("localhost");
        instrument.setIp("192.168.1.209");
        //instrument.setIp("192.168.104.23");
        DriverInstrument driver = new DriverSysmexCa660(instrument, model);
        driver.mainRun();
    }
}
