package test;

import entity.Instrument;
import instrument.DriverAdvia2120;
import instrument.DriverInstrument;
import instrument.InstrumentException;
import instrument.InstrumentIndicator;
import instrument.emulator.*;
import static instrument.emulator.ApparatusEmulator.COMPORT;
import modelHost.*;

public class TestEmulator {

    public static void main(String[] args) throws ModelException, InstrumentException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ApparEmulatorPanel apparEmulatorPanel = new ApparEmulatorPanelAdvia2120();
                new ApparatusEmulator(apparEmulatorPanel).myConnect(COMPORT);
            }
        });
        thread.run();
    }
}
