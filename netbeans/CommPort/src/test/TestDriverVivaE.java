package test;

import entity.Instrument;
import instrument.DriverImmulite2000;
import instrument.DriverInstrument;
import instrument.DriverVivaE;
import instrument.InstrumentException;
import instrument.InstrumentIndicator;
import modelHost.Model;
import modelHost.ModelException;
import modelHost.ModelImpl;

public class TestDriverVivaE {

    public static void main(String[] args) throws ModelException, InstrumentException {
        testMainRun();
    }

    public static void testMainRun() throws ModelException, InstrumentException {
        int istrId = 7;
        Model model = new ModelImpl();
        InstrumentIndicator instrInd = InstrumentIndicator.getInstance();
        instrInd.startInstrument(istrId);
        Instrument instrument = model.getObject(istrId, new Instrument());
//           instrument.setIp("localhost");
        instrument.setIp("192.168.104.22");
        DriverInstrument driver = new DriverVivaE(instrument, model);
        driver.mainRun();
    }
}
