package kz.biostat.lishostmanager.comport.test;

import kz.biostat.lishostmanager.comport.instrument.InstrumentException;

import static kz.biostat.lishostmanager.comport.instrument.emulator.ApparatusEmulator.COMPORT;

import kz.biostat.lishostmanager.comport.instrument.emulator.ApparEmulatorPanel;
import kz.biostat.lishostmanager.comport.instrument.emulator.ApparEmulatorPanelAdvia2120;
import kz.biostat.lishostmanager.comport.instrument.emulator.ApparatusEmulator;
import kz.biostat.lishostmanager.comport.modelHost.ModelException;
import kz.biostat.lishostmanager.comport.modelHost.*;

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
