package kz.biostat.lishostmanager.comport.test;

import kz.biostat.lishostmanager.comport.entity.Instrument;
import kz.biostat.lishostmanager.comport.instrument.DriverInstrument;
import kz.biostat.lishostmanager.comport.instrument.DriverMindrayBC3000;
import kz.biostat.lishostmanager.comport.instrument.InstrumentException;
import kz.biostat.lishostmanager.comport.instrument.InstrumentIndicator;
import kz.biostat.lishostmanager.comport.modelHost.Model;
import kz.biostat.lishostmanager.comport.modelHost.ModelException;
import kz.biostat.lishostmanager.comport.modelHost.ModelImpl;

public class TestDriverMindrayBC3000 {

    public static void main(String[] args) throws ModelException, InstrumentException {
        testMainRun();
    }

    public static void testMainRun() throws ModelException, InstrumentException {
        Model model = new ModelImpl();
        InstrumentIndicator instrInd = InstrumentIndicator.getInstance();
        int instrId = 9;
        instrInd.startInstrument(instrId);
        Instrument instrument = model.getObject(instrId, new Instrument());
//        instrument.setIp("127.0.0.1");
        instrument.setIp("192.168.104.25");
        DriverInstrument driver = new DriverMindrayBC3000(instrument, model);
        driver.mainRun();
    }
}
