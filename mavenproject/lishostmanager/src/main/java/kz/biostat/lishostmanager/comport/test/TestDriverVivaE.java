package kz.biostat.lishostmanager.comport.test;

import kz.biostat.lishostmanager.comport.entity.Instrument;
import kz.biostat.lishostmanager.comport.instrument.DriverInstrument;
import kz.biostat.lishostmanager.comport.instrument.DriverVivaE;
import kz.biostat.lishostmanager.comport.instrument.InstrumentException;
import kz.biostat.lishostmanager.comport.instrument.InstrumentIndicator;
import kz.biostat.lishostmanager.comport.modelHost.Model;
import kz.biostat.lishostmanager.comport.modelHost.ModelException;
import kz.biostat.lishostmanager.comport.modelHost.ModelImpl;

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
