package kz.biostat.lishostmanager.comport.test;

import kz.biostat.lishostmanager.comport.entity.Instrument;
import kz.biostat.lishostmanager.comport.instrument.DriverInstrument;
import kz.biostat.lishostmanager.comport.instrument.DriverSysmexCa660;
import kz.biostat.lishostmanager.comport.instrument.InstrumentException;
import kz.biostat.lishostmanager.comport.instrument.InstrumentIndicator;
import kz.biostat.lishostmanager.comport.modelHost.Model;
import kz.biostat.lishostmanager.comport.modelHost.ModelException;
import kz.biostat.lishostmanager.comport.modelHost.ModelImpl;

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
