package test;

import entity.Instrument;
import instrument.DriverAdvia2120;
import instrument.DriverInstrument;
import instrument.InstrumentException;
import instrument.InstrumentIndicator;
import modelHost.Model;
import modelHost.ModelException;
import modelHost.ModelImpl;

public class TestDriverAdvia2120 {

    public static void main(String[] args) {
        try {
            //testReceiveResults();
            testMainRun();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void testMainRun() throws ModelException, InstrumentException {
        int istrId = 1;
        Model model = new ModelImpl();
        InstrumentIndicator instrInd = InstrumentIndicator.getInstance();
        instrInd.startInstrument(istrId);

        Instrument instrument = model.getObject(istrId, new Instrument());
//        instrument.setIp("localhost");
        instrument.setIp("192.168.104.24");
//        instrument.setIp("192.168.1.205");
        DriverInstrument driver = new DriverAdvia2120(instrument, model);
        driver.mainRun();

    }

    public static void testReceiveResults() throws InstrumentException, ModelException {
        Model model = new ModelImpl();
        Instrument instrument = model.getObject(1, new Instrument());
        DriverAdvia2120 driver = new DriverAdvia2120(instrument, model);
       // driver.receiveResults();
    }
}
