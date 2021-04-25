package kz.biostat.lishostmanager.comport.instrument;

import kz.biostat.lishostmanager.comport.entity.Instrument;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import kz.biostat.lishostmanager.comport.modelHost.Model;
import kz.biostat.lishostmanager.comport.modelHost.ModelException;
import kz.biostat.lishostmanager.comport.modelHost.ModelImpl;

public class InstrumentIndicator {

    private static InstrumentIndicator instanse;
    private Map<Integer, Boolean> mapInstruments;
    private Map<Integer, Thread> mapThreads;
    private Map<String, String> mapSettings;
    Model model;
    List<Instrument> listInstruments;
    private boolean allInstrumentsStarted;

    public boolean isAllInstrumentsStarted() {
        return allInstrumentsStarted;
    }

    public void addThreadToMapInstruments(Integer instrId, Thread thread) {
        if (!mapThreads.containsKey(instrId)) {
            mapThreads.put(instrId, thread);
        }
    }

    private InstrumentIndicator() {
        readApparatusAndSettingsFromDb();
    }

    private void readApparatusAndSettingsFromDb() {
        try {
            model = new ModelImpl();
            readingApparatusInfoFromDB();
            readingSettingsFromDB();
        } catch (ModelException ex) {
            Logger.getLogger(InstrumentIndicator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void reReadApparatusAndSettingsFromDb() {
        readApparatusAndSettingsFromDb();
    }

    private final void readingApparatusInfoFromDB() throws ModelException {
        System.out.println("    *** * Reading apparatus info from DB * ***");
        mapInstruments = new HashMap<>();
        mapThreads = new HashMap<>();
        listInstruments = model.getInstruments();
        for (Instrument instrument : listInstruments) {
            putInstrument(instrument.getId());
        }

    }

    private final void readingSettingsFromDB() throws ModelException {
        System.out.println("    *** * Reading settings from DB * ***");
        mapSettings = model.getSettings();
    }

    public void runAllActive() {
        InstrumentIndicator instrIndicator = InstrumentIndicator.getInstance();
        Map m = instrIndicator.getMapInstruments();
        //System.out.println("m = " + m);
        for (Instrument instrObj : listInstruments) {
            if (instrObj.isActive()) {
                DriverInstrument driver = null;
                if (instrObj.getModel().getName().equals(Configurator.COBASE411)) {
                    driver = new DriverCobas411(instrObj, model);
                } else if (instrObj.getModel().getName().equals(Configurator.ADVIA2120)) {
                    driver = new DriverAdvia2120(instrObj, model);
                } else if (instrObj.getModel().getName().equals(Configurator.DIMENSION_XPAND)) {
                    driver = new DriverDimensionXpand(instrObj, model);
                } else if (instrObj.getModel().getName().equals(Configurator.IMMULITE2000)) {
                    driver = new DriverImmulite2000(instrObj, model);
                } else if (instrObj.getModel().getName().equals(Configurator.ADVIA_CENTAUR_CP)) {
                    driver = new DriverAdviaCentaurCP(instrObj, model);
                } else if (instrObj.getModel().getName().equals(Configurator.SYSMEX_CA_660)) {
                    driver = new DriverSysmexCa660(instrObj, model);
                } else if (instrObj.getModel().getName().equals(Configurator.VIVA_E)) {
                    driver = new DriverVivaE(instrObj, model);
                } else if (instrObj.getModel().getName().equals(Configurator.SYSMEX_CA_1500)) {
                    driver = new DriverSysmexCa1500(instrObj, model);
                } else if (instrObj.getModel().getName().equals(Configurator.MINDRAY_BC_3000)) {
                    driver = new DriverMindrayBC3000(instrObj, model);
                } else {
                    throw new UnsupportedOperationException("Unknown model of driver: instrObj.getModel().getName()");
                }

                try {
                    if (instrIndicator.isStopped(instrObj.getId())) {
                        InstrumentIndicator.getInstance().startInstrument(instrObj.getId());
                        driver.mainRunInNewThread();
                        startInstrument(instrObj.getId());
                    } else {
                        driver.logIsAlredyRun();
                    }
                } catch (InstrumentException ex) {
                    Logger.getLogger(InstrumentIndicator.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ModelException ex) {
                    Logger.getLogger(InstrumentIndicator.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
        allInstrumentsStarted = true;

    }

    public static InstrumentIndicator getInstance() {
        if (instanse == null) {
            instanse = new InstrumentIndicator();
        }
        return instanse;
    }

    public Map<Integer, Boolean> getMapInstruments() {
        return mapInstruments;
    }

    public void stopInstrument(int id) {
        if (mapInstruments.containsKey(Integer.valueOf(id))) {
            mapInstruments.put(Integer.valueOf(id), Boolean.FALSE);
        }
        // прерываем поток, в случае если останавливаем драйвер с прибором
        if (mapThreads.containsKey(Integer.valueOf(id))) {
            Thread thread = mapThreads.get(Integer.valueOf(id));
            if (thread.isAlive() && !thread.isInterrupted()) {
                thread.interrupt();
                mapThreads.remove(Integer.valueOf(id));
            }
        }

    }

    public void startInstrument(int id) {
        if (mapInstruments.containsKey(Integer.valueOf(id))) {
            mapInstruments.put(Integer.valueOf(id), Boolean.TRUE);
        }
    }

    public void putInstrument(int id) {
        mapInstruments.put(Integer.valueOf(id), Boolean.FALSE);
    }

    public boolean isStopped(int id) {
        return !mapInstruments.get(id);
    }

    public boolean isRun(int id) {
        return mapInstruments.get(id);
    }

    public void stopAll() {
        Set keys = mapInstruments.keySet();
        Iterator iter = keys.iterator();
        while (iter.hasNext()) {
            Integer key = (Integer) iter.next();
            mapInstruments.put(key, Boolean.FALSE);
        }
    }

    public Map<String, String> getMapSettings() {
        return mapSettings;
    }

}
