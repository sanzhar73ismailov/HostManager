/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import entity.Instrument;
import instrument.DriverDimensionXpand;
import instrument.DriverImmulite2000;
import instrument.DriverInstrument;
import instrument.InstrumentException;
import instrument.InstrumentIndicator;
import modelHost.Model;
import modelHost.ModelException;
import modelHost.ModelImpl;

/**
 *
 * @author sanzhar.ismailov
 */
public class TestDriverImmulite2000 {

    public static void main(String[] args) throws ModelException, InstrumentException {
        testMainRun();
    }

    public static void testMainRun() throws ModelException, InstrumentException {
        int istrId = 4;
        Model model = new ModelImpl();
        InstrumentIndicator instrInd = InstrumentIndicator.getInstance();
        instrInd.startInstrument(istrId);

        Instrument instrument = model.getObject(istrId, new Instrument());
//           instrument.setIp("localhost");
//        instrument.setIp("192.168.1.207");
        instrument.setIp("192.168.104.23");
        DriverInstrument driver = new DriverImmulite2000(instrument, model);
        driver.mainRun();

    }
}
